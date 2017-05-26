package info.megadrum.managerfx;

import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.management.OperationsException;

import com.sun.javafx.scene.traversal.TopMostTraversalEngine;

import info.megadrum.managerfx.data.ConfigFull;
import info.megadrum.managerfx.data.ConfigOptions;
import info.megadrum.managerfx.midi.MidiController;
import info.megadrum.managerfx.midi.MidiEvent;
import info.megadrum.managerfx.midi.MidiEventListener;
import info.megadrum.managerfx.midi.MidiRescanEvent;
import info.megadrum.managerfx.midi.MidiRescanEventListener;
import info.megadrum.managerfx.ui.ControlChangeEvent;
import info.megadrum.managerfx.ui.ControlChangeEventListener;
import info.megadrum.managerfx.ui.UIGlobal;
import info.megadrum.managerfx.ui.UIGlobalMisc;
import info.megadrum.managerfx.ui.UIInput;
import info.megadrum.managerfx.ui.UIMisc;
import info.megadrum.managerfx.ui.UIOptions;
import info.megadrum.managerfx.ui.UIPad;
import info.megadrum.managerfx.ui.UIPedal;
import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller implements MidiRescanEventListener {
	private Stage window;
	private Scene scene1;
	private MenuBar mainMenuBar;
	private Menu mainMenu, viewMenu, aboutMenu;
	private Menu allSettingsMenu, miscMenu, hihatMenu, allPadMenu, selectedPadMenu,customCurvesMenu;
	private Menu loadFromMdSlotMenu, saveToMdSlotMenu;
	private MenuItem firmwareUpgradeMenu, optionsMenu, exitMenu;
	private UIOptions optionsWindow;
	private UIGlobal uiGlobal;
	private UIGlobalMisc uiGlobalMisc;
	private UIMisc uiMisc;
	private UIPedal uiPedal;
	private UIPad uiPad;
	//private ProgressBar tempProgressBar;
	
	private MidiController midiController;
	private ConfigOptions configOptions;
	private ConfigFull configFull;
	private ConfigFull moduleConfigFull;
	private int padPair = 0;
	private int comboBoxInputChangedFromSet = 0;

	private List<byte[]> sysexSendList;
	
	public Controller(Stage primaryStage) {
		window = primaryStage;
		window.setTitle("MegaDrumManagerFX");
		window.setOnCloseRequest(e-> {
			e.consume();
			closeProgram();
		});

		initMidi();
		initConfigs();
		createMainMenuBar();
		uiGlobal = new UIGlobal();
		uiGlobalMisc = new UIGlobalMisc();
		uiMisc = new UIMisc("Misc");
		uiMisc.getButtonSend().setOnAction(e-> sendSysexMisc());
		uiMisc.getButtonGet().setOnAction(e-> sendSysexMiscRequest());
		uiMisc.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				// TODO Auto-generated method stub
				if (configOptions.liveUpdates) {
					sendSysexMisc();
				}
			}
		});

		uiPedal = new UIPedal("HiHat Pedal");
		uiPedal.getButtonSend().setOnAction(e-> sendSysexPedal());
		uiPedal.getButtonGet().setOnAction(e-> sendSysexPedalRequest());
		uiPedal.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				// TODO Auto-generated method stub
				if (configOptions.liveUpdates) {
					sendSysexPedal();
				}
			}
		});
		uiPad = new UIPad("Pads");
		uiPad.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				// TODO Auto-generated method stub
				Integer inputNumber = 0;
				if (padPair > 0) {
					inputNumber = ((padPair - 1)*2) +1; 
				}				
				System.out.printf("Input %s control change\n", (parameter == Constants.CONTROL_CHANGE_EVENT_LEFT_INPUT) ? "left" : "right");
				switch (parameter) {
				case Constants.CONTROL_CHANGE_EVENT_LEFT_INPUT:
					uiPad.setConfigFromControlsPad(configFull.configPads[inputNumber], true);
					break;
				case Constants.CONTROL_CHANGE_EVENT_RIGHT_INPUT:
					uiPad.setConfigFromControlsPad(configFull.configPads[inputNumber + 1], false);					
					break;
				case Constants.CONTROL_CHANGE_EVENT_3RD_INPUT:
					
					break;

				default:
					break;
				}
				if (uiPad.isNameChanged()) {
					uiPad.resetNameChanged();
					updateComboBoxInput();
				}
			}
		});

		padPair = 0;
		uiPad.getButtonPrev().setOnAction(e-> {
			if (padPair > 0) {
				padPair--;
				switchToSelectedPair();
			}
		});
		uiPad.getButtonNext().setOnAction(e-> {
			if (padPair < ((configFull.configGlobalMisc.inputs_count/2) - 1)) {
				padPair++;
				switchToSelectedPair();
			}
		});
		uiPad.getButtonFirst().setOnAction(e-> {
			padPair = 0;
			switchToSelectedPair();
		});
		uiPad.getButtonLast().setOnAction(e-> {
			padPair = (configFull.configGlobalMisc.inputs_count/2) - 1;
			switchToSelectedPair();
		});
		uiPad.getComboBoxInput().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
		    	if (comboBoxInputChangedFromSet > 0) {
		    		comboBoxInputChangedFromSet--;
		        	//System.out.printf("changedFromSet reduced to %d for %s\n", changedFromSet, label.getText());
		    	} else {
		    		Integer newInValue = uiPad.getComboBoxInput().getSelectionModel().getSelectedIndex();
		        	//System.out.printf("Setting %s to %s\n", label.getText(), newValue);
		    		if (newInValue > -1) {
			    		padPair = newInValue;
			    		switchToSelectedPair();		    			
		    		}
		    	}				
			}
        });
		updateComboBoxInput();
		uiPad.setInputPair(0, configFull.configPads[0], configFull.configPos[0], null, null);
		//uiPad.setInputPair(1, configFull.configPads[1], configFull.configPos[1], configFull.configPads[2], configFull.configPos[2]);
		VBox layout1VBox = new VBox();

		mainMenuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		layout1VBox.getChildren().add(mainMenuBar);
		layout1VBox.getChildren().add(uiGlobal.getUI());
		layout1VBox.getChildren().add(uiGlobalMisc.getUI());
		
		HBox layout2HBox = new HBox(5);
		Button button = new Button("b");
		//layout2HBox.getChildren().add(button);
		layout2HBox.getChildren().add(uiMisc.getUI());
		layout2HBox.getChildren().add(uiPedal.getUI());
		layout2HBox.getChildren().add(uiPad.getUI());

		layout1VBox.getChildren().add(layout2HBox);
		//layout1VBox.setPadding(new Insets(5, 5, 5, 5));
		layout1VBox.setStyle("-fx-border-width: 2px; -fx-padding: 2.0 2.0 2.0 2.0; -fx-border-color: #2e8b57");
		//scene1 = new Scene(layout1, 300,500);
		scene1 = new Scene(layout1VBox);
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		optionsWindow = new UIOptions(this, configOptions);
		optionsWindow.addMidiRescanEventListener(this);
		
		window.setScene(scene1);
		window.setMinWidth(1000);
		window.sizeToScene();
		scene1.widthProperty().addListener((obs, oldVal, newVal) -> {
			respondToResize(scene1);
		});

		scene1.heightProperty().addListener((obs, oldVal, newVal) -> {
			respondToResize(scene1);
		});
		window.show();
	}

/*
	public void respondToResize(Scene sc) {
		Double height = sc.getHeight() - mainMenuBar.getHeight();
		Double width = height*2;
		Double controlH, controlW;
		controlH= height *0.05;
		//controlW= width *0.2;
		controlW= controlH *5;
		//System.out.println("Responding to scene resize in Controller");
		//uiMisc.respondToResize((height)*0.45, sc.getWidth()*0.17, height, controlH, controlW);
		uiMisc.respondToResize(height, width, height, controlH, controlW);
		//uiPedal.respondToResize((height)*0.65, sc.getWidth()*0.17, height, controlH, controlW);
		//uiPad.respondToResize((height)*1.33 - 200, sc.getWidth()*0.65, height, controlH, controlW);
		//uiPad.respondToResize(sc.getHeight() - mainMenuBar.getHeight() - 50, sc.getWidth()*0.6, height);
	}
*/	
	public void respondToResize(Scene sc) {
		Double mainMenuBarHeight = mainMenuBar.getHeight();
		Double globalBarHeight = uiGlobal.getUI().layoutBoundsProperty().getValue().getHeight();
		Double globalMiscBarHeight = uiGlobalMisc.getUI().layoutBoundsProperty().getValue().getHeight();
		//System.out.printf("menuBar = %f, global = %f, globalMisc = %f\n", mainMenuBarHeight,globalBarHeight,globalMiscBarHeight);
		//Double height = sc.getHeight();
		Double height = sc.getHeight() - mainMenuBarHeight - globalBarHeight - globalMiscBarHeight;
		Double width = height*2;
		Double controlH, controlW;
		controlH= height *0.039 *0.8;
		//controlW= width *0.2;
		controlW= controlH *8;
		//System.out.println("Responding to scene resize in Controller");
		//uiMisc.respondToResize((height)*0.45, sc.getWidth()*0.17, height, controlH, controlW);
		uiMisc.respondToResize(height, width, height, controlH, controlW);
		//uiPedal.respondToResize((height)*0.65, sc.getWidth()*0.17, height, controlH, controlW);
		uiPedal.respondToResize(height, width, height, controlH, controlW);
		//uiPad.respondToResize((height)*1.33 - 200, sc.getWidth()*0.65, height, controlH, controlW);
		uiPad.respondToResize(height, width, height, controlH, controlW);
		//uiPad.respondToResize(sc.getHeight() - mainMenuBar.getHeight() - 50, sc.getWidth()*0.6, height);
	}

	public void createMainMenuBar() {
		mainMenuBar = new MenuBar();
		mainMenuBar.setStyle("-fx-font-size: 10 pt");
		mainMenu = new Menu("Main");
		viewMenu = new Menu("View");
		aboutMenu = new Menu("About");
		
		mainMenuBar.getMenus().addAll(mainMenu,viewMenu,aboutMenu);
		allSettingsMenu = new Menu("All Settings");
		miscMenu = new Menu("Misc Settings");
		hihatMenu = new Menu("HiHat Pedal Settings");
		allPadMenu = new Menu("All Pads Settings");
		selectedPadMenu = new Menu("Selected Pad Settings");
		customCurvesMenu = new Menu("Custom Curves");
		firmwareUpgradeMenu = new MenuItem("Firmware Upgrade");
		optionsMenu = new MenuItem("Options");
		optionsMenu.setOnAction(e-> { 
			showOptionsWindow();
		});
		exitMenu = new MenuItem("Exit");
		exitMenu.setOnAction(e-> closeProgram());
		
		mainMenu.getItems().addAll(allSettingsMenu,miscMenu,
				hihatMenu,allPadMenu,selectedPadMenu,customCurvesMenu,
				new SeparatorMenuItem(), firmwareUpgradeMenu, new SeparatorMenuItem(), optionsMenu,
				new SeparatorMenuItem(),exitMenu
				);
	}
	
	private void closeProgram() {
		System.out.println("Exiting\n");
		window.close();
		System.exit(0);
	}
	
	private void sendSysex() {
		midiController.sendSysexConfigsTaskRecreate();
		midiController.addSendSysexConfigsTaskSucceedEventHandler(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				// TODO Auto-generated method stub
				//System.out.println("SendSysexConfigsTask succeeded");
				uiGlobal.getProgressBarSysex().progressProperty().unbind();
				uiGlobal.getProgressBarSysex().setProgress(1.0);
				uiGlobal.getProgressBarSysex().setVisible(false);
			}
		});
		midiController.sendSysexConfigs(sysexSendList, uiGlobal.getProgressBarSysex(), 10, 50);		
	}
	
	private void sendSysexRequest() {
		midiController.sendSysexRequestsTaskRecreate();
		uiGlobal.getProgressBarSysex().setVisible(true);
		midiController.addSendSysexRequestsTaskSucceedEventHandler(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				// TODO Auto-generated method stub
				//System.out.println("SendSysexRequestsTask succeeded");
				uiGlobal.getProgressBarSysex().progressProperty().unbind();
				uiGlobal.getProgressBarSysex().setProgress(1.0);
				uiGlobal.getProgressBarSysex().setVisible(false);
			}
		});
		midiController.sendSysexRequests(sysexSendList, uiGlobal.getProgressBarSysex(), 10, 50);		
	}

	private void sendSysexMisc() {
		uiMisc.setConfigFromControls(configFull.configMisc);
		byte [] sysex = new byte[Constants.MD_SYSEX_MISC_SIZE];
		Utils.copyConfigMiscToSysex(configFull.configMisc, sysex, configOptions.chainId);
		sysexSendList.clear();
		sysexSendList.add(sysex);
		sendSysex();
	}

	private void sendSysexMiscRequest() {
		byte [] typeAndId;
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_MISC;
		sysexSendList.clear();
		sysexSendList.add(typeAndId);
		sendSysexRequest();
	}

	private void sendSysexPedal() {
		uiPedal.setConfigFromControls(configFull.configPedal);
		byte [] sysex = new byte[Constants.MD_SYSEX_PEDAL_SIZE];
		Utils.copyConfigPedalToSysex(configFull.configPedal, sysex, configOptions.chainId);
		sysexSendList.clear();
		sysexSendList.add(sysex);
		sendSysex();
	}

	private void sendSysexPedalRequest() {
		byte [] typeAndId;
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_PEDAL;
		sysexSendList.clear();
		sysexSendList.add(typeAndId);
		sendSysexRequest();
	}
	
	private void sendAllSysexRequests() {
		byte [] typeAndId;
		byte i;
		sysexSendList.clear();
		for (i = 0; i < 32; i++) {
			typeAndId = new byte[2];
			typeAndId[0] = Constants.MD_SYSEX_PAD;
			typeAndId[1] = i;
			sysexSendList.add(typeAndId);
		}
		midiController.sendSysexRequestsTaskRecreate();
		midiController.addSendSysexRequestsTaskSucceedEventHandler(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				// TODO Auto-generated method stub
				//System.out.println("SendSysexRequestsTask succeeded");
				uiGlobal.getProgressBarSysex().progressProperty().unbind();
				uiGlobal.getProgressBarSysex().setProgress(1.0);
			}
		});
		uiGlobal.getProgressBarSysex().setProgress(0.0);
		midiController.sendSysexRequests(sysexSendList, uiGlobal.getProgressBarSysex(), 10, 50);

	}
	
	private void showOptionsWindow() {
		optionsUpdatePorts();
		optionsWindow.show();
		if (optionsWindow.getClosedWithOk()) {
			System.out.println("Closed with ok");
			if (configOptions.useThruPort) {
				midiController.openMidi(configOptions.MidiInName, configOptions.MidiOutName, configOptions.MidiThruName);				
			} else {
				midiController.openMidi(configOptions.MidiInName, configOptions.MidiOutName, "");
			}
		}
	}
	
	private void initMidi() {
		midiController = new MidiController();
		sysexSendList = new ArrayList<>();
		midiController.addMidiEventListener(new MidiEventListener() {
			@Override
			public void midiEventOccurred(MidiEvent evt) {
				// TODO Auto-generated method stub
/*				if (!upgradeDialog.isVisible()) {
					sendSysexEnabled = false;
					midiController.getMidi();
					if (midiController.sysexReceived) {
						midiController.sysexReceived = false;
						if (compareSysexToConfigIsOn) {
							compareSysexToConfig(midiController.bufferIn);
						} else {
							decodeSysex(midiController.bufferIn);						
						}
					} else if (midiController.bufferIn != null) {
						decodeShortMidi(midiController.bufferIn);
					}
					midiController.bufferIn = null;
				}
*/				
			}

			@Override
			public void midiEventOccurredWithBuffer(MidiEvent evt, byte[] buffer) {
				// TODO Auto-generated method stub
				//System.out.println("Received MidiEvent with buffer");
				processSysex(buffer);
			}
		});		
	}
	
	private void processSysex(byte [] sysex) {
    	switch (sysex[3]) {
		case Constants.MD_SYSEX_3RD:
			break;
		case Constants.MD_SYSEX_CONFIG_COUNT:
			break;
		case Constants.MD_SYSEX_CONFIG_CURRENT:
			break;
		case Constants.MD_SYSEX_CONFIG_NAME:
			break;
		case Constants.MD_SYSEX_CURVE:
			break;
		case Constants.MD_SYSEX_CUSTOM_NAME:
			break;
		case Constants.MD_SYSEX_GLOBAL_MISC:
			break;
		case Constants.MD_SYSEX_MCU_TYPE:
			break;
		case Constants.MD_SYSEX_MISC:
			Utils.copySysexToConfigMisc(sysex, configFull.configMisc);
			Utils.copySysexToConfigMisc(sysex, moduleConfigFull.configMisc);
			configFull.configMisc.syncState = Constants.SYNC_STATE_RECEIVED;
			configFull.configMisc.sysexReceived = true;
			uiMisc.setControlsFromConfig(configFull.configMisc, true);
			break;
		case Constants.MD_SYSEX_PAD:
			break;
		case Constants.MD_SYSEX_PEDAL:
			Utils.copySysexToConfigPedal(sysex, configFull.configPedal);
			Utils.copySysexToConfigPedal(sysex, moduleConfigFull.configPedal);
			configFull.configPedal.syncState = Constants.SYNC_STATE_RECEIVED;
			configFull.configPedal.sysexReceived = true;
			uiPedal.setControlsFromConfig(configFull.configPedal, true);
			break;
		case Constants.MD_SYSEX_POS:
			break;
		case Constants.MD_SYSEX_VERSION:
			break;
		default:
			break;
		}

	}
	private void initConfigs() {
		configOptions = new ConfigOptions();
		configFull = new ConfigFull(); 
		moduleConfigFull = new ConfigFull();
	}

	private void optionsUpdatePorts() {
		optionsWindow.setMidiInList(Arrays.asList(midiController.getMidiInList()));
		optionsWindow.setMidiOutList(Arrays.asList(midiController.getMidiOutList()));
		optionsWindow.setMidiThruList(Arrays.asList(midiController.getMidiOutList()));
		optionsWindow.updateControls();
	}
	
	@Override
	public void midiRescanEventOccurred(MidiRescanEvent evt) {
		// TODO Auto-generated method stub
		System.out.println("Midi Rescan Event occured");
		optionsUpdatePorts();
		
	}

	private String getInputName(int input) {
		String result = Integer.valueOf(input + 1).toString() + " ";
		int totalCustomNames = Constants.CUSTOM_PADS_NAMES_LIST.length;
		int namePointer;
		namePointer = configFull.configPads[input].name;
		if (namePointer == 0) {
			result = result + Constants.PADS_NAMES_LIST[input];
		} else {
			if (namePointer < (totalCustomNames + 1 )) {
				result = result + (Constants.CUSTOM_PADS_NAMES_LIST[namePointer - 1]);
			} else {
				result = result + configFull.configCustomNames[namePointer - totalCustomNames + 1];
			}
			if ((input & 1) > 0) {
				result += "h";
			} else {
				result += "r";
			}
		}
		return result;
	}
	
	private void updateComboBoxInput() {
		List<String> list;
		String name;
		list = new ArrayList<>();
		int inputPointer;
		for (int i = 0; i < ((configFull.configGlobalMisc.inputs_count/2)); i++) {
			if (i == 0) {
				list.add(getInputName(i));
			} else {
				inputPointer = (i*2) - 1;
				name = getInputName(inputPointer);
				name = name + "/";
				inputPointer++;
				name = name + getInputName(inputPointer);
				list.add(name);
			}
		}
		comboBoxInputChangedFromSet++;
		uiPad.getComboBoxInput().getItems().clear();
		uiPad.getComboBoxInput().getItems().addAll(list);
		uiPad.getComboBoxInput().getSelectionModel().select(padPair);
	}
	
	private void switchToSelectedPair() {
		if (padPair == 0) {
			uiPad.setInputPair(padPair, configFull.configPads[0], configFull.configPos[0], null, null);
		} else {
			uiPad.setInputPair(padPair, configFull.configPads[((padPair-1)*2) + 1], configFull.configPos[((padPair-1)*2) + 1], configFull.configPads[((padPair-1)*2) + 2], configFull.configPos[((padPair-1)*2) + 2]);
		}
		uiPad.getComboBoxInput().getSelectionModel().select(padPair);
	}
}
