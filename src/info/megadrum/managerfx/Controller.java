package info.megadrum.managerfx;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import info.megadrum.managerfx.data.ConfigFull;
import info.megadrum.managerfx.data.ConfigOptions;
import info.megadrum.managerfx.data.FileManager;
import info.megadrum.managerfx.midi.MidiController;
import info.megadrum.managerfx.midi.MidiEvent;
import info.megadrum.managerfx.midi.MidiEventListener;
import info.megadrum.managerfx.midi.MidiRescanEvent;
import info.megadrum.managerfx.midi.MidiRescanEventListener;
import info.megadrum.managerfx.ui.ControlChangeEvent;
import info.megadrum.managerfx.ui.ControlChangeEventListener;
import info.megadrum.managerfx.ui.MidiLevelBar;
import info.megadrum.managerfx.ui.MidiLevelBarsPanel;
import info.megadrum.managerfx.ui.SpinnerFast;
import info.megadrum.managerfx.ui.UIPadsExtra;
import info.megadrum.managerfx.ui.UIPanel;
import info.megadrum.managerfx.ui.UIGlobal;
import info.megadrum.managerfx.ui.UIGlobalMisc;
import info.megadrum.managerfx.ui.UIInput;
import info.megadrum.managerfx.ui.UIMidiLog;
import info.megadrum.managerfx.ui.UIMisc;
import info.megadrum.managerfx.ui.UIOptions;
import info.megadrum.managerfx.ui.UIPad;
import info.megadrum.managerfx.ui.UIPedal;
import info.megadrum.managerfx.ui.UIUpgrade;
import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Controller implements MidiRescanEventListener {
	private Stage window;
	private Scene scene1;
	private MenuBar mainMenuBar;
	private Menu mainMenu, viewMenu, helpMenu;
	private MenuItem aboutMenuItem;
	private Menu menuAllSettings, menuGlobalMisc, menuMisc, menuHiHat, menuAllPads,
				menuSelectedPad, menuCustomCurves, menuCustomNames;
	private MenuItem menuItemAllSettingsGet, menuItemAllSettingsSend, menuItemAllSettingsLoad,
				menuItemAllSettingsSave;
	private Menu menuLoadFromMdSlot, menuSaveToMdSlot;
	private ContextMenu contextMenuLoadFromMdSlot, contextMenuSaveToMdSlot;
	private ArrayList<MenuItem> allMenuItemsLoadFromSlot, allMenuItemsSaveSlot;
	private MenuItem menuItemGlobalMiscGet, menuItemGlobalMiscSend;
	private MenuItem menuItemMiscGet, menuItemMiscSend;
	private MenuItem menuItemHiHatGet, menuItemHiHatSend;
	private MenuItem menuItemAllPadsGet, menuItemAllPadsSend;
	private MenuItem menuItemSelectedPadGet, menuItemSelectedPadSend;
	private MenuItem menuItemCustomCurvesGet, menuItemCustomCurvesSend;
	private MenuItem menuItemCustomNamesGet, menuItemCustomNamesSend;
	private MenuItem firmwareUpgradeMenuItem, optionsMenuItem, exitMenuItem;
	private ContextMenu contextMenuCopyPad;
	private Menu menuCopyPad, menuCopyHead, menuCopyRim, menuCopy3rd;
	private ArrayList<MenuItem> menuItemsCopyPad, menuItemsCopyHead, menuItemsCopyRim, menuItemsCopy3rd;
	
	private UIOptions optionsWindow;
	private UIUpgrade upgradeWindow;
	private VBox topVBox;
	private HBox hBoxUIviews;
	private Double hBoxUIviewsHPadding = 2.0;
	private Double hBoxUIviewsVPadding = 1.0;	
	private Double hBoxUIviewsGap = 2.0;
	private UIGlobal uiGlobal;
	private UIGlobalMisc uiGlobalMisc;
	private UIMisc uiMisc;
	private UIPedal uiPedal;
	private UIPad uiPad;
	private UIPadsExtra uiPadsExtra;
	private UIMidiLog uiMidiLog;
	private ArrayList<UIPanel>	allPanels;
	private Double mainWindowMinWidth = 1000.0;
	//private ArrayList<Stage> allWindows;
	//private ProgressBar tempProgressBar;
	private Timer 		timerResize;
	private TimerTask	timerTaskResize;
	private int			timerResizeDelay = 200;
	
	private MidiController midiController;
	private ConfigOptions configOptions;
	private ConfigFull configFull;
	private ConfigFull moduleConfigFull;
	private ConfigFull [] fullConfigs;
	private String [] configFileNames;
	private FileManager fileManager;
	private File file;
	private int padPair = 0;
	private int comboBoxInputChangedFromSet = 0;
	private int comboBoxFileChangedFromSet = 0;
	private int toggleButtonMidiChangedFromSet = 0;
	private int oldInputsCounts = 0;
	private Boolean sendNextAllSysexRequestsFlag = false;
	private Boolean sendSysexReadOnlyRequestFlag = false;
	private Boolean loadConfigAfterLoadSlot = false;
	private Boolean saveToSlotAfterSendAll = false;
	private int saveToSlot = 0;
	
	private int curvePointer = 0;

	private List<byte[]> sysexSendList;
	private List<byte[]> sysexLastChanged;
	private int sysexThreadsStarted = 0;
	
	private Boolean versionWarningAlreadyShown = false;

	public Controller(Stage primaryStage) {
		window = primaryStage;
		window.setTitle(Constants.WINDOWS_TITLE);
		window.setOnCloseRequest(e-> {
			e.consume();
			closeProgram();
		});

		fileManager = new FileManager(window);
		uiGlobal = new UIGlobal();
		uiGlobalMisc = new UIGlobalMisc();
		uiMisc = new UIMisc("Misc");
		uiPedal = new UIPedal("HiHat Pedal");
		uiPad = new UIPad("Pads");
		uiPadsExtra = new UIPadsExtra("Pads Extra Settings");
		uiMidiLog = new UIMidiLog("MIDI Log");
		initMidi();
		initConfigs();
		
		allPanels = new ArrayList<UIPanel>();
		allPanels.add(uiMisc);
		allPanels.add(uiPedal);
		allPanels.add(uiPad);
		allPanels.add(uiPadsExtra);
		allPanels.add(uiMidiLog);
		
		createMainMenuBar();
		createContextMenuCopyPad();
		uiGlobal.getButtonGetAll().setOnAction(e-> sendAllSysexRequests());
		uiGlobal.getButtonSendAll().setOnAction(e-> sendAllSysex());
		uiGlobal.getButtonLoadAll().setOnAction(e-> load_all());
		uiGlobal.getButtonSaveAll().setOnAction(e-> save_all());
		uiGlobal.getButtonLoadFromSlot().setOnAction(e-> {
			contextMenuLoadFromMdSlot.show(uiGlobal.getButtonLoadFromSlot(), Side.RIGHT, 0, 0);
		});
		uiGlobal.getButtonSaveToSlot().setOnAction(e-> {
			contextMenuSaveToMdSlot.show(uiGlobal.getButtonSaveToSlot(), Side.RIGHT, 0, 0);
		});	
		uiGlobal.getComboBoxFile().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if (comboBoxFileChangedFromSet > 0) {
		    		comboBoxFileChangedFromSet--;
		    	} else {
		    		if (uiGlobal.getComboBoxFile().getItems().size() > 0) {
						configOptions.lastConfig = uiGlobal.getComboBoxFile().getSelectionModel().getSelectedIndex();
						fileManager.loadAllSilent(fullConfigs[configOptions.lastConfig], configOptions);
						loadAllFromConfigFull();
		    		}
		    	}				
			}
        });
		uiGlobal.getButtonPrevFile().setOnAction(e-> {
			if (configOptions.lastConfig>0) {
				uiGlobal.getComboBoxFile().getSelectionModel().select(configOptions.lastConfig - 1);
			}			
		});
		uiGlobal.getButtonNextFile().setOnAction(e-> {
			if (configOptions.lastConfig<(Constants.CONFIGS_COUNT-1)) {
				uiGlobal.getComboBoxFile().getSelectionModel().select(configOptions.lastConfig + 1);
			}			
		});

		
		uiGlobalMisc.getButtonGet().setOnAction(e-> sendSysexGlobalMiscRequest());
		uiGlobalMisc.getButtonSend().setOnAction(e-> sendSysexGlobalMisc());
		uiGlobalMisc.getCheckBoxLiveUpdates().selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	configOptions.liveUpdates = newValue;
		    }
		});
		uiGlobalMisc.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				controlsChangedGlobalMisc();
			}
		});
		uiGlobalMisc.getToggleButtonMidi().setOnAction(e-> openMidiPorts(uiGlobalMisc.getToggleButtonMidi().isSelected()));
		uiMisc.getButtonGet().setOnAction(e-> sendSysexMiscRequest());
		uiMisc.getButtonSend().setOnAction(e-> sendSysexMisc());
		uiMisc.getButtonLoad().setOnAction(e-> loadSysexMisc());
		uiMisc.getButtonSave().setOnAction(e-> saveSysexMisc());		
		
		uiMisc.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				controlsChangedMisc();
			}
		});

		uiPedal.getButtonSend().setOnAction(e-> sendSysexPedal());
		uiPedal.getButtonGet().setOnAction(e-> sendSysexPedalRequest());
		uiPedal.getButtonLoad().setOnAction(e-> loadSysexPedal());
		uiPedal.getButtonSave().setOnAction(e-> saveSysexPedal());		
		uiPedal.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				controlsChangedPedal();
			}
		});
		uiPad.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				Integer inputNumber = 0;
				if (padPair > 0) {
					inputNumber = ((padPair - 1)*2) +1; 
				}				
				//System.out.printf("Input %s control change\n", (parameter == Constants.CONTROL_CHANGE_EVENT_LEFT_INPUT) ? "left" : "right");
				switch (parameter) {
				case Constants.CONTROL_CHANGE_EVENT_LEFT_INPUT:
					if (uiPad.isCopyPressed()) {
						//System.out.printf("left copy pressed with valueId = %d\n", uiPad.getCopyPressedValueId());
						uiPad.resetCopyPressed();
						copyLeftInputValueToAllOthers();
						switchToSelectedPair(padPair);
					} else {
						controlsChangedInput(inputNumber, true);
					}
					break;
				case Constants.CONTROL_CHANGE_EVENT_RIGHT_INPUT:
					if (uiPad.isCopyPressed()) {
						//System.out.printf("right copy pressed with valueId = %d\n", uiPad.getCopyPressedValueId());
						uiPad.resetCopyPressed();
						copyPadPairValueToAllOthers();
					} else {
						controlsChangedInput(inputNumber + 1, false);						
					}
					break;
				case Constants.CONTROL_CHANGE_EVENT_3RD_INPUT:
					if (padPair > 0) {
						if (uiPad.isCopyPressed()) {
							//System.out.printf("3rd zone copy pressed with valueId = %d\n", uiPad.getCopyPressedValueId());
							uiPad.resetCopyPressed();
							copy3rdZoneValueToAllOthers();
						} else {
							controlsChangedInput(inputNumber + 1, false);						
							controlsChanged3rd(padPair);
						}
					}
					break;
				default:
					break;
				}
				if (uiPad.isNameChanged()) {
					uiPad.resetNameChanged();
					updateComboBoxInput(true);
				}
				/*
				tempMidiLevelBarsPanel.addNewBarData(
						configFull.configPads[0].channel,
						configFull.configPads[0].note,
						configFull.configPads[0].altNote,
						50);
				tempMidiLevelBarsPanel.setHiHatLevel(configFull.configPads[0].pressrollNote);
				*/
			}
		});

		padPair = 0;
		uiPad.getButtonGet().setOnAction(e-> {
			if (padPair == 0) {
				sendSysexInputRequest(0);
			} else {
				sendSysexPairRequest(padPair);
			}
		});
		uiPad.getButtonSend().setOnAction(e-> {
			if (padPair == 0) {
				sendSysexInput(0, true);
			} else {
				sendSysexPair(padPair);
			}
		});
		uiPad.getButtonGetAll().setOnAction(e-> {
			sendAllInputsSysexRequests();
		});
		uiPad.getButtonSendAll().setOnAction(e-> {
			sendAllInputsSysex();
		});
		uiPad.getButtonLoad().setOnAction(e-> loadSysexPad());
		uiPad.getButtonSave().setOnAction(e-> saveSysexPad());		
		uiPad.getButtonPrev().setOnAction(e-> {
			if (padPair > 0) {
				switchToSelectedPair(padPair - 1);
			}
		});
		uiPad.getButtonNext().setOnAction(e-> {
			if (padPair < ((configFull.configGlobalMisc.inputs_count/2) - 1)) {
				switchToSelectedPair(padPair + 1);
			}
		});
		uiPad.getButtonFirst().setOnAction(e-> {
			switchToSelectedPair(0);
		});
		uiPad.getButtonLast().setOnAction(e-> {
			switchToSelectedPair((configFull.configGlobalMisc.inputs_count/2) - 1);
		});
		uiPad.getComboBoxInput().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if (comboBoxInputChangedFromSet > 0) {
		    		comboBoxInputChangedFromSet--;
		        	//System.out.printf("changedFromSet reduced to %d for %s\n", changedFromSet, label.getText());
		    	} else {
		    		Integer newInValue = uiPad.getComboBoxInput().getSelectionModel().getSelectedIndex();
		        	//System.out.printf("Setting %s to %s\n", label.getText(), newValue);
		    		if (newInValue > -1) {
			    		switchToSelectedPair(newInValue);		    			
		    		}
		    	}				
			}
        });
		updateComboBoxInput(true);
		uiPad.setInputPair(0, configFull.configPads[0], configFull.configPos[0], null, null, null);
		
		uiPadsExtra.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				if (parameter.intValue() == Constants.CONTROL_CHANGE_EVENT_CURVE) {
					controlsChangedCurve();
				} else {
					if (parameter.intValue() >= Constants.CUSTOM_NAME_CHANGE_TEXT_START) {
						if (parameter.intValue() < Constants.CUSTOM_NAME_CHANGE_GET_START) {
							// Custom Name changed
							controlsChangedCustomName(parameter - Constants.CUSTOM_NAME_CHANGE_TEXT_START);
						} else if (parameter.intValue() < Constants.CUSTOM_NAME_CHANGE_SEND_START) {
							// Get button pressed
							sendSysexCustomNameRequest(parameter - Constants.CUSTOM_NAME_CHANGE_GET_START);
						} else {
							// Send button pressed
							sendSysexCustomName(parameter - Constants.CUSTOM_NAME_CHANGE_SEND_START);
						}
					}
				}
			}
		});
		uiPadsExtra.getComboBoxCustomNamesCount().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				switch (uiPadsExtra.getComboBoxCustomNamesCount().getSelectionModel().getSelectedIndex()) {
				case 0:
					configFull.customNamesCount = 2;
					break;
				case 1:
					configFull.customNamesCount = 16;
					break;
				case 2:
				default:
					configFull.customNamesCount = 32;
					break;
				}
			}
		});
		uiPadsExtra.getCustomNamesButtonGetAll().setOnAction(e-> sendAllCustomNamesSysexRequests());
		uiPadsExtra.getCustomNamesButtonSendAll().setOnAction(e-> sendAllCustomNamesSysex());
		uiPadsExtra.getCustomNamesButtonLoadAll().setOnAction(e-> loadSysexAllCustomNames());
		uiPadsExtra.getCustomNamesButtonSaveAll().setOnAction(e-> saveSysexAllCustomNames());
		setCustomNamesCountControl();
		uiPadsExtra.getCurvesButtonGet().setOnAction(e-> sendSysexCurveRequest());
		uiPadsExtra.getCurvesButtonSend().setOnAction(e-> sendSysexCurve());
		uiPadsExtra.getCurvesButtonGetAll().setOnAction(e-> sendAllCurvesSysexRequests());
		uiPadsExtra.getCurvesButtonSendAll().setOnAction(e-> sendAllCurvesSysex());
		uiPadsExtra.getCurvesButtonLoad().setOnAction(e-> loadSysexCurve());
		uiPadsExtra.getCurvesButtonSave().setOnAction(e-> saveSysexCurve());
		uiPadsExtra.getCurvesComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				int newCurve = uiPadsExtra.getCurvesComboBox().getSelectionModel().getSelectedIndex();
				if (newCurve != curvePointer) {
					switchToSelectedCurve(newCurve);
				}
			}
		});
		uiPadsExtra.getCurvesButtonFirst().setOnAction(e-> {
			switchToSelectedCurve(0);
		});
		uiPadsExtra.getCurvesButtonPrev().setOnAction(e-> {
			switchToSelectedCurve(curvePointer - 1);
		});
		uiPadsExtra.getCurvesButtonNext().setOnAction(e-> {
			switchToSelectedCurve(curvePointer + 1);
		});
		uiPadsExtra.getCurvesButtonLast().setOnAction(e-> {
			switchToSelectedCurve(Constants.CURVES_COUNT - 1);
		});
		
		topVBox = new VBox();

		mainMenuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		topVBox.getChildren().add(mainMenuBar);
		topVBox.getChildren().add(uiGlobal.getUI());
		topVBox.getChildren().add(uiGlobalMisc.getUI());
		//topVBox.setAlignment(Pos.TOP_CENTER);
		
		hBoxUIviews = new HBox(hBoxUIviewsGap);
		//hBoxUIviews.setStyle("-fx-padding: 1.0 0.0 0.0 1.0");
		hBoxUIviews.setPadding(new Insets(hBoxUIviewsVPadding, hBoxUIviewsHPadding, 0.0, hBoxUIviewsHPadding));
		
		topVBox.getChildren().add(hBoxUIviews);
		//topVBox.setPadding(new Insets(5, 5, 5, 5));
		//topVBox.setStyle("-fx-border-width: 2px; -fx-padding: 2.0 2.0 2.0 2.0; -fx-border-color: #2e8b57");
		//scene1 = new Scene(layout1, 300,500);
		scene1 = new Scene(topVBox);
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		optionsWindow = new UIOptions(configOptions);		
		optionsWindow.addMidiRescanEventListener(this);
		
		upgradeWindow = new UIUpgrade(midiController, fileManager, configOptions);
		
		window.setScene(scene1);
		window.setMinWidth(mainWindowMinWidth);
		window.setMinHeight(520);
		//window.sizeToScene();
		scene1.widthProperty().addListener((obs, oldVal, newVal) -> {
			respondToResize(scene1);
		});

		scene1.heightProperty().addListener((obs, oldVal, newVal) -> {
			respondToResize(scene1);
		});
		loadConfig();
		showGlobalMisc();
		showPanels();
		window.show();
	}

	public void respondToResize(Scene sc) {
/*
		if (timerResize != null) {
			timerResize.cancel();
		}
		timerResize = new Timer();
		timerTaskResize = new TimerTask() {
			
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
*/
						uiGlobalMisc.respondToResize(sc.getHeight()*0.05);
						Double mainMenuBarHeight = mainMenuBar.getHeight();
						Double globalBarHeight = uiGlobal.getUI().layoutBoundsProperty().getValue().getHeight();
						Double globalMiscBarHeight;
						if (uiGlobalMisc.getViewState() == Constants.PANEL_SHOW) {
							globalMiscBarHeight = sc.getHeight()*0.05;							
						} else {
							globalMiscBarHeight = 0.0;							
						}
						Double height = sc.getHeight() - mainMenuBarHeight - globalBarHeight;
						if (uiGlobalMisc.getViewState() == Constants.PANEL_SHOW) {
							height -= globalMiscBarHeight;
						}
						//Double width = height*2;
						Double width = sc.getWidth();
						Double controlH, controlW;
						Double mainWindowMaxWidth = 0.0;
						Double midiLogHeight = height*0.7;
						Double midiLogWidth = width*0.23;
						if (uiPad.getViewState() == Constants.PANEL_SHOW) {
							controlH= (height/uiPad.getVerticalControlsCount())*1.00 - 0.4;
							midiLogHeight = height*0.7;
						} else if (uiPedal.getViewState() == Constants.PANEL_SHOW) {
							controlH= height/uiPedal.getVerticalControlsCount()*1.045 - 0.95;
							midiLogHeight = height*0.7;
						} else if (uiPadsExtra.getViewState() == Constants.PANEL_SHOW) {
							controlH= height/uiPadsExtra.getVerticalControlsCount();
							midiLogHeight = height*0.7;
						} else if (uiMisc.getViewState() == Constants.PANEL_SHOW) {
							controlH= height/uiMisc.getVerticalControlsCount()*1.045 - 0.95;
							midiLogHeight = height*0.7;
						} else {
							controlH= height/uiMidiLog.getVerticalControlsCount();
							midiLogHeight = height*0.95;
							midiLogWidth = mainWindowMinWidth*0.99;
						}
						
						controlW = width;
						//controlW = width - hBoxUIviewsGap;
						Double controlWdivider = 0.0;
						if (uiMisc.getViewState() == Constants.PANEL_SHOW) {
							controlWdivider += Constants.FX_MISC_CONTROL_WIDTH_MUL*1.000;
							controlW -= hBoxUIviewsGap;
						} 
						if (uiPedal.getViewState() == Constants.PANEL_SHOW) {
							controlWdivider += Constants.FX_PEDAL_CONTROL_WIDTH_MUL*1.000;
							controlW -= hBoxUIviewsGap;
						}
						if (uiPad.getViewState() == Constants.PANEL_SHOW) {
							controlWdivider += Constants.FX_INPUT_CONTROL_WIDTH_MUL*2.000;
							controlW -= hBoxUIviewsGap*8;
						}
						if (uiPadsExtra.getViewState() == Constants.PANEL_SHOW) {
							controlW -= 340.0 + hBoxUIviewsGap;
						}
						if (uiMidiLog.getViewState() == Constants.PANEL_SHOW) {
							controlW -= midiLogWidth + hBoxUIviewsGap;
						}
						
						controlWdivider = (controlWdivider == 0)?1:controlWdivider;
						controlW = (controlW/controlWdivider)*1.0;
						controlW = (controlW > 360.0)?360.0:controlW;
						if (uiMisc.getViewState() == Constants.PANEL_SHOW) {
							uiMisc.respondToResize(width, height, height, controlW, controlH);
							mainWindowMaxWidth += controlW*Constants.FX_MISC_CONTROL_WIDTH_MUL;
						}
						if (uiPedal.getViewState() == Constants.PANEL_SHOW) {
							uiPedal.respondToResize(width, height, height, controlW, controlH);
							mainWindowMaxWidth += controlW*Constants.FX_PEDAL_CONTROL_WIDTH_MUL;
						}
						if (uiPad.getViewState() == Constants.PANEL_SHOW) {
							uiPad.respondToResize(width, height, height, controlW, controlH);
							mainWindowMaxWidth += (controlW*Constants.FX_INPUT_CONTROL_WIDTH_MUL)*2;
						}
						if (uiPadsExtra.getViewState() == Constants.PANEL_SHOW) {
							uiPadsExtra.respondToResize(width, height, height, controlW, controlH);							
							mainWindowMaxWidth += 340.0;
						}
						if (uiMidiLog.getViewState() == Constants.PANEL_SHOW) {
							uiMidiLog.respondToResize(midiLogWidth, midiLogHeight, controlH);							
							mainWindowMaxWidth += midiLogWidth;
						}
/*						
					}
				});
			}
		};
		timerResizeDelay = 10;
		timerResize.schedule(timerTaskResize, timerResizeDelay);
*/
	}

	public void respondToResizeDetached(Scene sc, UIPanel uiPanel) {
		Double height = sc.getHeight();
		Double width = sc.getWidth();
		uiPanel.respondToResizeDetached(width, height);
	}

	public void setCustomNamesCountControl() {
		switch (configFull.customNamesCount) {
		case 32:
			uiPadsExtra.getComboBoxCustomNamesCount().getSelectionModel().select(2);
			break;
		case 16:
			uiPadsExtra.getComboBoxCustomNamesCount().getSelectionModel().select(1);
			break;
		case 2:
		default:
			uiPadsExtra.getComboBoxCustomNamesCount().getSelectionModel().select(0);
			break;
		}
	}
	
	private void reCreateSlotsMenuItems() {
		allMenuItemsLoadFromSlot.clear();
		for (int i = 0; i < configFull.configNamesCount; i++) {
			final int iFinal = i;
			if (configFull.configGlobalMisc.config_names_en) {
				allMenuItemsLoadFromSlot.add(new MenuItem(Integer.toString(i + 1) + " " + configFull.configConfigNames[i].name));				
			} else {
				allMenuItemsLoadFromSlot.add(new MenuItem(Integer.toString(i + 1)));
			}
			allMenuItemsLoadFromSlot.get(i).setOnAction(e-> {
				sendSysexLoadFromSlotRequest(iFinal);
			});
		}
		menuLoadFromMdSlot.getItems().clear();
		menuLoadFromMdSlot.getItems().addAll(allMenuItemsLoadFromSlot);
		contextMenuLoadFromMdSlot.getItems().clear();
		contextMenuLoadFromMdSlot.getItems().addAll(allMenuItemsLoadFromSlot);
		allMenuItemsSaveSlot.clear();
		for (int i = 0; i < configFull.configNamesCount; i++) {
			final int iFinal = i;
			if (configFull.configGlobalMisc.config_names_en) {
				allMenuItemsSaveSlot.add(new MenuItem(Integer.toString(i + 1) + " " + configFull.configConfigNames[i].name));				
			} else {
				allMenuItemsSaveSlot.add(new MenuItem(Integer.toString(i + 1)));
			}
			allMenuItemsSaveSlot.get(i).setOnAction(e-> {
				sendSysexSaveToSlotRequest(iFinal);
			});
		}
		menuSaveToMdSlot.getItems().clear();
		menuSaveToMdSlot.getItems().addAll(allMenuItemsSaveSlot);		
		contextMenuSaveToMdSlot.getItems().clear();
		contextMenuSaveToMdSlot.getItems().addAll(allMenuItemsSaveSlot);		
	}
	
	private void createContextMenuCopyPad() {
		contextMenuCopyPad = new ContextMenu();
		menuCopyPad = new Menu("CopyPad");
		menuCopyHead = new Menu("CopyHead");
		menuCopyRim = new Menu("CopyRim");
		menuCopy3rd = new Menu("Copy3rd");
		contextMenuCopyPad.getItems().addAll(menuCopyPad, menuCopyHead, menuCopyRim, menuCopy3rd );
		uiPad.getButtonCopy().setOnAction(e-> {
			contextMenuCopyPad.show(uiPad.getButtonCopy(), Side.RIGHT, 0, 0);
		});
		menuItemsCopyPad = new ArrayList<MenuItem>();
		menuItemsCopyHead = new ArrayList<MenuItem>();
		menuItemsCopyRim = new ArrayList<MenuItem>();
		menuItemsCopy3rd = new ArrayList<MenuItem>();
	}
	
	private void createMainMenuBar() {
		mainMenuBar = new MenuBar();
		mainMenuBar.useSystemMenuBarProperty().set(true);
		mainMenuBar.setStyle("-fx-font-size: 10 pt");
		mainMenu = new Menu("Main");
		viewMenu = new Menu("View");
		helpMenu = new Menu("Help");
		aboutMenuItem = new MenuItem("About");
		aboutMenuItem.setOnAction(e-> {
			showAbout();
		});
		helpMenu.getItems().add(aboutMenuItem);
		
		mainMenuBar.getMenus().addAll(mainMenu,viewMenu,helpMenu);

		menuAllSettings = new Menu("All Settings");
		menuItemAllSettingsGet = new MenuItem("Get from MD");
		menuItemAllSettingsGet.setOnAction(e-> sendAllSysexRequests());
		menuItemAllSettingsSend = new MenuItem("Send to MD");
		menuItemAllSettingsSend.setOnAction(e-> sendAllSysex());
		menuItemAllSettingsLoad = new MenuItem("Load from file");
		menuItemAllSettingsLoad.setOnAction(e-> load_all());
		menuItemAllSettingsSave = new MenuItem("Save to file");
		menuItemAllSettingsSave.setOnAction(e-> save_all());
		menuLoadFromMdSlot = new Menu("Load from MD Slot:");
		contextMenuLoadFromMdSlot = new ContextMenu();
		allMenuItemsLoadFromSlot = new ArrayList<MenuItem>();
		menuSaveToMdSlot = new Menu("Save to MD Slot:");
		contextMenuSaveToMdSlot = new ContextMenu();
		allMenuItemsSaveSlot = new ArrayList<MenuItem>();
		reCreateSlotsMenuItems();
		menuAllSettings.getItems().addAll(menuItemAllSettingsGet, menuItemAllSettingsSend, menuItemAllSettingsLoad,
				menuItemAllSettingsSave, menuLoadFromMdSlot, menuSaveToMdSlot);

		menuGlobalMisc = new Menu("Global Misc Settings");
		menuItemGlobalMiscGet = new MenuItem("Get from MD");
		menuItemGlobalMiscGet.setOnAction(e-> sendSysexGlobalMiscRequest());
		menuItemGlobalMiscSend = new MenuItem("Send to MD");
		menuItemGlobalMiscSend.setOnAction(e-> sendSysexGlobalMisc());
		menuGlobalMisc.getItems().addAll(menuItemGlobalMiscGet, menuItemGlobalMiscSend);

		menuMisc = new Menu("Misc Settings");
		menuItemMiscGet = new MenuItem("Get from MD");
		menuItemMiscGet.setOnAction(e-> sendSysexMiscRequest());
		menuItemMiscSend = new MenuItem("Send to MD");
		menuItemMiscSend.setOnAction(e-> sendSysexMisc());
		menuMisc.getItems().addAll(menuItemMiscGet, menuItemMiscSend);

		menuHiHat = new Menu("HiHat Pedal Settings");
		menuItemHiHatGet = new MenuItem("Get from MD");
		menuItemHiHatGet.setOnAction(e-> sendSysexPedalRequest());
		menuItemHiHatSend = new MenuItem("Send to MD");
		menuItemHiHatSend.setOnAction(e-> sendSysexPedal());
		menuHiHat.getItems().addAll(menuItemHiHatGet, menuItemHiHatSend);
		
		menuAllPads = new Menu("All Pads Settings");
		menuItemAllPadsGet = new MenuItem("Get from MD");
		menuItemAllPadsGet.setOnAction(e-> sendAllInputsSysexRequests());
		menuItemAllPadsSend = new MenuItem("Send to MD");
		menuItemAllPadsSend.setOnAction(e-> sendAllInputsSysex());
		menuAllPads.getItems().addAll(menuItemAllPadsGet, menuItemAllPadsSend);

		menuSelectedPad = new Menu("Selected Pad Settings");
		menuItemSelectedPadGet = new MenuItem("Get from MD");
		menuItemSelectedPadGet.setOnAction(e-> {
			if (padPair == 0) {
				sendSysexInputRequest(0);
			} else {
				sendSysexPairRequest(padPair);
			}
		});
		menuItemSelectedPadSend = new MenuItem("Send to MD");
		menuItemSelectedPadSend.setOnAction(e-> {
			if (padPair == 0) {
				sendSysexInput(0, true);
			} else {
				sendSysexPair(padPair);
			}
		});
		menuSelectedPad.getItems().addAll(menuItemSelectedPadGet, menuItemSelectedPadSend);

		menuCustomCurves = new Menu("All Custom Curves");
		menuItemCustomCurvesGet = new MenuItem("Get from MD");
		menuItemCustomCurvesGet.setOnAction(e-> sendAllCurvesSysexRequests());
		menuItemCustomCurvesSend = new MenuItem("Send to MD");
		menuItemCustomCurvesSend.setOnAction(e-> sendAllCurvesSysex());
		menuCustomCurves.getItems().addAll(menuItemCustomCurvesGet, menuItemCustomCurvesSend);

		menuCustomNames = new Menu("All Custom Names");
		menuItemCustomNamesGet = new MenuItem("Get from MD");
		menuItemCustomNamesGet.setOnAction(e-> sendAllCustomNamesSysexRequests());
		menuItemCustomNamesSend = new MenuItem("Send to MD");
		menuItemCustomNamesSend.setOnAction(e-> sendAllCustomNamesSysex());
		menuCustomNames.getItems().addAll(menuItemCustomNamesGet, menuItemCustomNamesSend);

		firmwareUpgradeMenuItem = new MenuItem("Firmware Upgrade");
		firmwareUpgradeMenuItem.setOnAction(e-> {
			showUpgradeWindow();
		});
		optionsMenuItem = new MenuItem("Options");
		optionsMenuItem.setOnAction(e-> { 
			showOptionsWindow();
		});
		exitMenuItem = new MenuItem("Exit");
		exitMenuItem.setOnAction(e-> closeProgram());
		
		mainMenu.getItems().addAll(menuAllSettings, menuGlobalMisc, menuMisc,
				menuHiHat,menuAllPads,menuSelectedPad,menuCustomCurves, menuCustomNames,
				new SeparatorMenuItem(), firmwareUpgradeMenuItem, new SeparatorMenuItem(), optionsMenuItem,
				new SeparatorMenuItem(),exitMenuItem
				);
		
		Menu menuViewGlobalMisc = new Menu("Global Misc");
		uiGlobalMisc.getRadioMenuItemHide().setOnAction( e-> {
			uiGlobalMisc.setViewState(Constants.PANEL_HIDE);
			showGlobalMisc();
		});
		uiGlobalMisc.getRadioMenuItemShow().setOnAction( e-> {
			uiGlobalMisc.setViewState(Constants.PANEL_SHOW);
			showGlobalMisc();
		});
		menuViewGlobalMisc.getItems().addAll(uiGlobalMisc.getRadioMenuItemHide(), uiGlobalMisc.getRadioMenuItemShow());
		viewMenu.getItems().add(menuViewGlobalMisc);
		
		Menu menuViewMisc = new Menu("Misc");
		
		menuViewMisc.getItems().addAll(uiMisc.getRadioMenuItemHide(), uiMisc.getRadioMenuItemShow(), uiMisc.getRadioMenuItemDetach());
		viewMenu.getItems().add(menuViewMisc);
		
		Menu menuViewPedal = new Menu("Pedal");
		
		menuViewPedal.getItems().addAll(uiPedal.getRadioMenuItemHide(), uiPedal.getRadioMenuItemShow(), uiPedal.getRadioMenuItemDetach());
		viewMenu.getItems().add(menuViewPedal);

		Menu menuViewPads = new Menu("Pads");
		
		menuViewPads.getItems().addAll(uiPad.getRadioMenuItemHide(), uiPad.getRadioMenuItemShow(), uiPad.getRadioMenuItemDetach());
		viewMenu.getItems().add(menuViewPads);

		Menu menuViewPadsExtra = new Menu("PadsExtra");
		
		menuViewPadsExtra.getItems().addAll(uiPadsExtra.getRadioMenuItemHide(), uiPadsExtra.getRadioMenuItemShow(), uiPadsExtra.getRadioMenuItemDetach());
		viewMenu.getItems().add(menuViewPadsExtra);

		Menu menuViewMidiLog = new Menu("MidiLog");
		menuViewMidiLog.getItems().addAll(uiMidiLog.getRadioMenuItemHide(), uiMidiLog.getRadioMenuItemShow(), uiMidiLog.getRadioMenuItemDetach());
		viewMenu.getItems().add(menuViewMidiLog);
		
		for (int i = 0; i < allPanels.size(); i++) {
			final int iFinal = i;
			allPanels.get(i).getRadioMenuItemShow().setSelected(true);
			allPanels.get(i).getRadioMenuItemHide().setOnAction(e-> {
				allPanels.get(iFinal).setViewState(Constants.PANEL_HIDE);
				showPanels();
			});
			allPanels.get(i).getRadioMenuItemShow().setOnAction(e-> {
				allPanels.get(iFinal).setViewState(Constants.PANEL_SHOW);
				showPanels();
			});
			allPanels.get(i).getRadioMenuItemDetach().setOnAction(e-> {
				allPanels.get(iFinal).setViewState(Constants.PANEL_DETACH);
				showPanels();
			});
		}
	}

	private void showGlobalMisc() {
		if (uiGlobalMisc.getViewState() == Constants.PANEL_SHOW) {
			if (!topVBox.getChildren().contains(uiGlobalMisc.getUI())) {
				topVBox.getChildren().add(2, uiGlobalMisc.getUI());					
			}
		} else {
			topVBox.getChildren().remove(uiGlobalMisc.getUI());			
		}
		respondToResize(scene1);
	}
	
	private void showPanels() {
		hBoxUIviews.getChildren().clear();
		for (int i = 0; i < allPanels.size(); i++) {
			final int iFinal = i;
			switch (allPanels.get(i).getViewState()) {
			case Constants.PANEL_DETACH:
				if (!allPanels.get(i).isDetached()) {
					allPanels.get(i).setDetached(true);
					allPanels.get(iFinal).selectRadioMenuItemDetach();
					//hBoxUIviews.getChildren().remove(allPanels.get(i).getUI());
					VBox scenePane = new VBox();
					scenePane.setAlignment(Pos.TOP_CENTER);
					//scenePane.setStyle("-fx-border-width: 2px; -fx-padding: 2.0 2.0 2.0 2.0; -fx-border-color: #2e8b57");
					scenePane.getChildren().add(allPanels.get(i).getTopLayout());
					Scene scene = new Scene(scenePane);
					//scene.seto
					allPanels.get(i).getWindow().setScene(scene);
					allPanels.get(i).getWindow().sizeToScene();
					allPanels.get(i).getWindow().setOnCloseRequest(e-> {
						allPanels.get(iFinal).setLastX(allPanels.get(iFinal).getWindow().getX());
						allPanels.get(iFinal).setLastY(allPanels.get(iFinal).getWindow().getY());
						allPanels.get(iFinal).setLastW(allPanels.get(iFinal).getWindow().getWidth());
						allPanels.get(iFinal).setLastH(allPanels.get(iFinal).getWindow().getHeight());
						scenePane.getChildren().clear();
						allPanels.get(iFinal).setViewState(Constants.PANEL_HIDE);
						allPanels.get(iFinal).selectRadioMenuItemHide();
						allPanels.get(iFinal).setDetached(false);
					});
					scene.heightProperty().addListener(e-> {
						respondToResizeDetached(scene, allPanels.get(iFinal));
					});
					scene.widthProperty().addListener(e-> {
						respondToResizeDetached(scene, allPanels.get(iFinal));
					});
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
					// This is a hack on startup for detached windows.
						
						@Override
						public void run() {
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									allPanels.get(iFinal).getWindow().setX(allPanels.get(iFinal).getLastX());
									allPanels.get(iFinal).getWindow().setY(allPanels.get(iFinal).getLastY());
									allPanels.get(iFinal).getWindow().setWidth(allPanels.get(iFinal).getLastW());
									allPanels.get(iFinal).getWindow().setHeight(allPanels.get(iFinal).getLastH());
									allPanels.get(iFinal).getWindow().show();
								}
							});						
						}
					}, 1);
				} else {
					allPanels.get(i).getWindow().toFront();
				}
				break;
			case Constants.PANEL_HIDE:
				if (allPanels.get(i).getWindow().isShowing()) {
					allPanels.get(i).getWindow().close();
					allPanels.get(i).getWindow().setScene(null);
				}
				allPanels.get(iFinal).selectRadioMenuItemHide();
				allPanels.get(i).setDetached(false);
				break;
			case Constants.PANEL_SHOW:
			default:
				if (allPanels.get(i).getWindow().isShowing()) {
					allPanels.get(i).getWindow().close();
					allPanels.get(i).getWindow().setScene(null);
				}
				allPanels.get(iFinal).selectRadioMenuItemShow();
				allPanels.get(i).setDetached(false);
				hBoxUIviews.getChildren().add(allPanels.get(i).getUI());
				break;
			}
		}
		// This is a hack to repaint hBoxUIviews
		// after a UI has been removed from it.
/*		hBoxUIviews.setVisible(false);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				hBoxUIviews.setVisible(true);				
			}
		}, 100);*/
		//VBox vB = new VBox(1);
		//vB.setStyle("-fx-border-width: 2px; -fx-padding: 2.0 2.0 2.0 2.0; -fx-border-color: #2e8b57");
		//vB.getChildren().add(tempMidiLevelBar);
		respondToResize(scene1);
	}

	private void showMidiWarningIfNeeded() {
		if (configOptions.MidiInName.equals("") || configOptions.MidiOutName.equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText("MIDI ports warning!");
            WebView webView = new WebView();
            webView.getEngine().loadContent(Constants.MIDI_PORTS_WARNING);
            webView.setPrefSize(300, 120);
            alert.getDialogPane().setContent(webView);
			Timer warning_timer = new Timer();
			warning_timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					Platform.runLater(new Runnable() {
						public void run() {
							alert.showAndWait();
						}
					});
				}
			}, 500);
		}		
	}
	
	private void loadConfig() {
		configOptions  = fileManager.loadLastOptions(configOptions);
		uiGlobal.getComboBoxFile().getItems().clear();
		uiGlobal.getComboBoxFile().getItems().addAll(configOptions.configFileNames);
		uiGlobal.getComboBoxFile().getSelectionModel().select(configOptions.lastConfig);
		showMidiWarningIfNeeded();
		if (configOptions.autoOpenPorts) {
			openMidiPorts(true);
		}
		midiController.setChainId(configOptions.chainId);
		window.setX(configOptions.mainWindowPosition.getX());
		window.setY(configOptions.mainWindowPosition.getY());
		window.setWidth(configOptions.mainWindowSize.getX());
		window.setHeight(configOptions.mainWindowSize.getY());
		
		for (int i = 0;i<Constants.PANELS_COUNT;i++) {
			allPanels.get(i).setViewState(configOptions.showPanels[i]);
			allPanels.get(i).setLastX(configOptions.framesPositions[i].getX());
			allPanels.get(i).setLastY(configOptions.framesPositions[i].getY());
			allPanels.get(i).setLastW(configOptions.framesSizes[i].getX());
			allPanels.get(i).setLastH(configOptions.framesSizes[i].getY());
		}
		uiGlobalMisc.setViewState(configOptions.globalMiscViewState);
		uiGlobalMisc.getCheckBoxLiveUpdates().setSelected(configOptions.liveUpdates);
		setShowAdvanced();
		//checkBoxAutoResize.setSelected(configOptions.autoResize);
	}

	private void closeProgram() {
		System.out.println("Exiting\n");
		midiController.closeAllPorts();
		Point2D point2d = new Point2D(window.getX(), window.getY());
		configOptions.mainWindowPosition = point2d;
		point2d = new Point2D(window.getWidth(), window.getHeight());
		configOptions.mainWindowSize = point2d;
		for (int i = 0;i<Constants.PANELS_COUNT;i++) {
			if (allPanels.get(i).getViewState() == Constants.PANEL_DETACH) {
				point2d = new Point2D(allPanels.get(i).getWindow().getX(), allPanels.get(i).getWindow().getY());
				configOptions.framesPositions[i] = point2d;
				point2d = new Point2D(allPanels.get(i).getWindow().getWidth(), allPanels.get(i).getWindow().getHeight());
				configOptions.framesSizes[i] = point2d;
			}
			configOptions.showPanels[i] = allPanels.get(i).getViewState();
		}
		configOptions.globalMiscViewState = uiGlobalMisc.getViewState();
		if (configOptions.saveOnExit) {
			fileManager.saveLastOptions(configOptions);
		}
		window.close();
		System.exit(0);
	}
	
	private void copyLeftInputValueToAllOthers() {
		int maxInputs = configFull.configGlobalMisc.inputs_count - 1;
		int valueId = uiPad.getCopyPressedValueId();
		int currentInput = 0;
		if (padPair > 0) {
			currentInput = (padPair*2) - 1;
		}
		int value = configFull.configPads[currentInput].getValueById(valueId);
		for (int i = 0; i < maxInputs; i++) {
			if (i != currentInput) {
				if (((i&1) > 0) || (valueId != Constants.INPUT_VALUE_ID_TYPE)) {
					if ((valueId >= Constants.INPUT_VALUE_ID_POS_LEVEL) && (valueId <= Constants.INPUT_VALUE_ID_POS_HIGH)) {
						value = configFull.configPos[currentInput].getValueById(valueId);
						configFull.configPos[i].setValueById(valueId, value);					
					} else {
						configFull.configPads[i].setValueById(valueId, value);					
					}
				}
			}
		}
	}
	
	private void copyPadPairValueToAllOthers() {
		int maxPair = (configFull.configGlobalMisc.inputs_count/2) - 1;
		int valueId = uiPad.getCopyPressedValueId();
		int currentPair = padPair - 1;
		int valueLeft = configFull.configPads[currentPair*2  + 1].getValueById(valueId);
		int valueRight = configFull.configPads[currentPair*2 + 2].getValueById(valueId);
		//ui
		for (int i = 0; i < maxPair; i++) {
			if (i != currentPair) {
				if ((valueId >= Constants.INPUT_VALUE_ID_POS_LEVEL) && (valueId <= Constants.INPUT_VALUE_ID_POS_HIGH)) {
					valueLeft = configFull.configPos[currentPair*2 + 1].getValueById(valueId);
					valueRight = configFull.configPos[currentPair*2 + 2].getValueById(valueId);
					configFull.configPos[i*2 + 1].setValueById(valueId, valueLeft);			
					configFull.configPos[i*2 + 2].setValueById(valueId, valueRight);			
				} else {
					configFull.configPads[i*2 + 1].setValueById(valueId, valueLeft);					
					configFull.configPads[i*2 + 2].setValueById(valueId, valueRight);					
				}
			}
		}
	}
	
	private void copy3rdZoneValueToAllOthers() {
		int max3rd = (configFull.configGlobalMisc.inputs_count/2) - 1;
		int valueId = uiPad.getCopyPressedValueId();
		int current3rd = padPair - 1;
		int value = configFull.config3rds[current3rd].getValueById(valueId);
		for (int i = 0; i < max3rd; i++) {
			if (i != current3rd) {
				configFull.config3rds[i].setValueById(valueId, value);
			}
		}
	}
	
	private void sendSysexLastChanged() {
		sysexSendList.addAll(sysexLastChanged);
		sysexLastChanged.clear();
		sendSysex();
		//System.out.println("Sending last sysex config");			
	}
	
	private void sendSysex() {
		if (sysexThreadsStarted > 0) {
			sysexLastChanged.clear();
			sysexLastChanged.addAll(sysexSendList);
			sysexSendList.clear();
			//System.out.println("Still sending previous sysex");			
		} else {
			midiController.sendSysexTaskRecreate();
			uiGlobal.getProgressBarSysex().setVisible(true);
			midiController.addSendSysexTaskSucceedEventHandler(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					sysexThreadsStarted--;
					//System.out.println("SendSysexConfigsTask succeeded");
					uiGlobal.getProgressBarSysex().progressProperty().unbind();
					uiGlobal.getProgressBarSysex().setProgress(1.0);
					uiGlobal.getProgressBarSysex().setVisible(false);
					int [] status = midiController.getStatus();
					if (status[0] > 0) {
						uiGlobal.setSysexStatusLabel(status[0], status[1]);
					} else {
						if (sysexLastChanged.size() > 0) {
							sendSysexLastChanged();
						} else {
							if (sendSysexReadOnlyRequestFlag) {
								sendSysexReadOnlyRequestFlag = false;
								sendSysexReadOnlyRequest();
							}
							if (saveToSlotAfterSendAll) {
								saveToSlotAfterSendAll = false;
								sendSysexSaveToSlotOnlyRequest(saveToSlot);
							}
							if (sendNextAllSysexRequestsFlag) {
								sendNextAllSysexRequestsFlag = false;
								sendNextAllSysexRequests();
							}
							if (loadConfigAfterLoadSlot) {
								loadConfigAfterLoadSlot = false;
								sendAllSysexRequests();
							}
						}
						if (sysexThreadsStarted == 0) {
							uiGlobal.setSysexStatusLabel(Constants.MD_SYSEX_STATUS_OK, 0);
							//System.out.printf("Finished all sysex threads\n");
						} else {
							//System.out.printf("%d threads remainig\n", sysexThreadsStarted);							
						}
					}
				}
			});
			uiGlobal.setSysexStatusLabel(Constants.MD_SYSEX_STATUS_WORKING, 0);
			sysexThreadsStarted++;
			//System.out.printf("Sending %d sysexes in thread %d\n", sysexSendList.size(), sysexThreadsStarted);
			midiController.setChainId(configOptions.chainId);
			if (midiController.sendSysex(sysexSendList, uiGlobal.getProgressBarSysex(), 10, 50) > 0) {
				sysexThreadsStarted--;
				System.out.println("Not Ok");				
				uiGlobal.getProgressBarSysex().progressProperty().unbind();
				uiGlobal.getProgressBarSysex().setProgress(1.0);
				uiGlobal.getProgressBarSysex().setVisible(false);
				sysexSendList.clear();
				sysexLastChanged.clear();
				loadConfigAfterLoadSlot = false;
				sendNextAllSysexRequestsFlag = false;
				saveToSlotAfterSendAll = false;
				sendSysexReadOnlyRequestFlag = false;
				uiGlobal.setSysexStatusLabel(Constants.MD_SYSEX_STATUS_MIDI_IS_NOT_OPEN, 0);
			} else {
				//System.out.println("Ok");				
			}
		}
	}
	
	private void sendSysexReadOnlyRequest() {
		//sysexSendList.clear();
		byte [] typeAndId;
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_VERSION;
		sysexSendList.add(typeAndId);
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_MCU_TYPE;
		sysexSendList.add(typeAndId);
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_CONFIG_COUNT;
		sysexSendList.add(typeAndId);
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_CONFIG_CURRENT;
		sysexSendList.add(typeAndId);
		sendSysex();		
	}
	
	private void sendSysexGlobalMisc() {
		sysexSendList.add(configFull.configGlobalMisc.getSysexFromConfig());
		sendSysexReadOnlyRequestFlag = true;
		sendSysex();
	}
	
	private void controlsChangedGlobalMisc() {
		uiGlobalMisc.setConfigFromControls(configFull.configGlobalMisc);
		updateComboBoxInput(false);
		if (configOptions.liveUpdates) {
			sendSysexGlobalMisc();
		}
	}

	private void sendSysexGlobalMiscRequest() {
		//sysexSendList.clear();
		byte [] typeAndId;
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_GLOBAL_MISC;
		sysexSendList.add(typeAndId);
		sendSysex();
	}
	
	private void sendSysexCustomName(int id) {
		//sysexSendList.clear();
		sysexSendList.add(configFull.configCustomNames[id].getSysexFromConfig());
		sendSysex();
	}

	private void controlsChangedCustomName(int id) {
		uiPadsExtra.getCustomName(configFull.configCustomNames[id], id);
		if (configOptions.liveUpdates) {
			sendSysexCustomName(id);
		}
		addCustomNamesToPads();
	}
	
	private void sendSysexCustomNameRequest(int id) {
		//sysexSendList.clear();
		byte [] typeAndId;
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_CUSTOM_NAME;
		typeAndId[1] = (byte)id;
		if (configOptions.mcuType < 2) {
			//Atmega644 has 16 custom names max
			if (id > 15) {
				typeAndId[1] = 15;
			}
		}
		sysexSendList.add(typeAndId);
		sendSysex();
	}

	private void sendAllCustomNamesSysex() {
		//sysexSendList.clear();
		byte [] sysex;
		byte i;
		for (i = 0; i < configFull.customNamesCount; i++) {
			sysexSendList.add(configFull.configCustomNames[i].getSysexFromConfig());
		}
		sendSysex();
	}
	
	private void sendAllCustomNamesSysexRequests() {
		//sysexSendList.clear();
		byte [] typeAndId;
		byte i;
		for (i = 0; i < configFull.customNamesCount; i++) {
			if (configOptions.mcuType < 2) {
				//Atmega644 has 16 custom names max
				if (i > 15) {
					break;
				}
			}
			typeAndId = new byte[2];
			typeAndId[0] = Constants.MD_SYSEX_CUSTOM_NAME;
			typeAndId[1] = i;
			sysexSendList.add(typeAndId);
		}
		sendSysex();
	}


	private void sendSysexCurve() {
		sysexSendList.add(configFull.configCurves[curvePointer].getSysexFromConfig());
		sendSysex();
	}
	
	private void sendSysexLoadFromSlotRequest(int slot) {
		setAllStatesUnknown();
		//sysexSendList.clear();
		byte [] typeAndId;
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_CONFIG_LOAD;
		typeAndId[1] = (byte)slot;
		sysexSendList.add(typeAndId);
		sendSysex();

		// After sending MD_SYSEX_CONFIG_LOAD sysex to MegaDrum
		// We need to let MegaDrum ~200ms to load a config from EEPROM
		// before sending other sysexes, hence the timer.
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {			
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						byte [] typeAndId;
						typeAndId = new byte[2];
						typeAndId[0] = Constants.MD_SYSEX_GLOBAL_MISC;
						sysexSendList.add(typeAndId);
						typeAndId = new byte[2];
						typeAndId[0] = Constants.MD_SYSEX_VERSION;
						sysexSendList.add(typeAndId);
						typeAndId = new byte[2];
						typeAndId[0] = Constants.MD_SYSEX_MCU_TYPE;
						sysexSendList.add(typeAndId);
						typeAndId = new byte[2];
						typeAndId[0] = Constants.MD_SYSEX_CONFIG_COUNT;
						sysexSendList.add(typeAndId);
						typeAndId = new byte[2];
						typeAndId[0] = Constants.MD_SYSEX_CONFIG_CURRENT;
						sysexSendList.add(typeAndId);
						//loadConfigAfterLoadSlot = configOptions.liveUpdates;
						loadConfigAfterLoadSlot = true;
						sendSysex();
					}
				});
			}
		}, 200);
	}

	private void sendSysexSaveToSlotOnlyRequest(int slot) {
		//sysexSendList.clear();
		byte [] typeAndId;
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_CONFIG_SAVE;
		typeAndId[1] = (byte)slot;
		sysexSendList.add(typeAndId);
		sendSysex();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						byte [] typeAndIdFinal = new byte[2];
						typeAndIdFinal[0] = Constants.MD_SYSEX_CONFIG_CURRENT;
						sysexSendList.add(typeAndIdFinal);
						sendSysex();				
					}
				});
			}
		}, 3000);
	}
	
	private void sendSysexConfigName(int id) {
		sysexSendList.add(configFull.configConfigNames[id].getSysexFromConfig());
		sendSysex();
	}

	private void sendSysexSaveToSlotRequest(int slot) {
		//System.out.printf("Saving to slot %d\n", slot + 1);
		configFull.configConfigNames[slot].name = (uiGlobalMisc.getTextFieldSlotName().getText() + "            ").substring(0, 12);
		if (configFull.configGlobalMisc.config_names_en) {
			sendSysexConfigName(slot);
		}
		//Utils.copyConfigConfigNameToSysex(null, null, i, i);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						saveToSlot = slot;
						saveToSlotAfterSendAll = true;
						sendAllSysex();
					}
				});
			}
		}, 200);
	}
	
	private void controlsChangedCurve() {
		uiPadsExtra.getYvalues(configFull.configCurves[curvePointer].yValues);
		if (configOptions.liveUpdates) {
			sendSysexCurve();
		}
	}

	private void sendSysexCurveRequest() {
		//sysexSendList.clear();
		byte [] typeAndId;
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_CURVE;
		typeAndId[1] = (byte)curvePointer;
		sysexSendList.add(typeAndId);
		sendSysex();
	}

	private void sendAllCurvesSysex() {
		//sysexSendList.clear();
		byte i;
		for (i = 0; i < Constants.CURVES_COUNT; i++) {
			sysexSendList.add(configFull.configCurves[i].getSysexFromConfig());	
		}
		sendSysex();
	}
	
	private void sendAllCurvesSysexRequests() {
		//sysexSendList.clear();
		byte [] typeAndId;
		byte i;
		for (i = 0; i < Constants.CURVES_COUNT; i++) {
			typeAndId = new byte[2];
			typeAndId[0] = Constants.MD_SYSEX_CURVE;
			typeAndId[1] = i;
			sysexSendList.add(typeAndId);
		}
		sendSysex();
	}

	private void sendSysexMisc() {
		sysexSendList.add(configFull.configMisc.getSysexFromConfig());
		sendSysex();
	}

	private void controlsChangedMisc() {
		uiMisc.setConfigFromControls(configFull.configMisc);
		if (configOptions.liveUpdates) {
			sendSysexMisc();
		}
	}
	
	private void sendSysexMiscRequest() {
		//sysexSendList.clear();
		byte [] typeAndId;
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_MISC;
		sysexSendList.add(typeAndId);
		sendSysex();
	}

	private void sendSysexPedal() {
		sysexSendList.add(configFull.configPedal.getSysexFromConfig(configOptions.mcuType));
		sendSysex();
	}

	private void controlsChangedPedal() {
		uiPedal.setConfigFromControls(configFull.configPedal);
		if (configOptions.liveUpdates) {
			sendSysexPedal();
		}
	}
	
	private void sendSysexPedalRequest() {
		//sysexSendList.clear();
		byte [] typeAndId;
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_PEDAL;
		sysexSendList.add(typeAndId);
		sendSysex();
	}
	
	private void sendSysexInput(Integer input, Boolean leftInput) {
		sysexSendList.add(configFull.configPads[input].getSysexFromConfig());
		if (configOptions.mcuType > 2) {
			sysexSendList.add(configFull.configPos[input].getSysexFromConfig());
		} else {
			if (input > 2) {
				if ((input&1) > 0) {
					byte [] sysex;
					sysex = configFull.configPos[input].getSysexFromConfig();
					sysex[4] = (byte) ((input-3)/2);
					if (configOptions.mcuType == 1) {
						if (((input-3)/2) < 4) {
							sysexSendList.add(sysex);							
						}
					}
					if (configOptions.mcuType == 2) {
						if (((input-3)/2) < 8) {
							sysexSendList.add(sysex);							
						}
					}
				}
			}
		}
		sendSysex();
	}

	private void sendSysexPair(Integer pair) {
		//sysexSendList.clear();
		sysexSendList.add(configFull.configPads[(pair*2) - 1].getSysexFromConfig());		
		sysexSendList.add(configFull.configPads[(pair*2)].getSysexFromConfig());
		if (configOptions.mcuType > 2) {
			sysexSendList.add(configFull.configPos[(pair*2) - 1].getSysexFromConfig());		
			sysexSendList.add(configFull.configPos[(pair*2)].getSysexFromConfig());
		} else {
			int input = padPair*2 - 1;
			if (input > 2) {
				if ((input&1) > 0) {
					byte [] sysex;
					sysex = configFull.configPos[(pair*2) - 1].getSysexFromConfig();
					sysex[4] = (byte) ((input-3)/2);
					if (configOptions.mcuType == 1) {
						if (((input-3)/2) < 4) {
							sysexSendList.add(sysex);							
						}
					}
					if (configOptions.mcuType == 2) {
						if (((input-3)/2) < 8) {
							sysexSendList.add(sysex);							
						}
					}
				}
			}
		}
		sysexSendList.add(configFull.config3rds[pair - 1].getSysexFromConfig());
		sendSysex();
	}

	private void controlsChangedInput(Integer input, Boolean leftInput) {
		uiPad.setConfigFromControlsPad(configFull.configPads[input], leftInput);
		// Needs implementation for Positional on Atmega644 and Atmega1284
		uiPad.setConfigPosFromControlsPad(configFull.configPos[input], leftInput);
		if (configOptions.liveUpdates) {
			sendSysexInput(input, leftInput);
		}
	}
	
	private void sendSysex3rd(Integer pair) {
		sysexSendList.add(configFull.config3rds[pair - 1].getSysexFromConfig());
		sendSysex();
	}
	
	private void controlsChanged3rd(Integer pair) {
		uiPad.setConfig3rdFromControlsPad(configFull.config3rds[pair - 1]);
		if (configOptions.liveUpdates) {
			sendSysex3rd(pair);
		}
	}
	
	private void sendSysexInputRequest(Integer input) {
		//sysexSendList.clear();
		byte [] typeAndId;
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_PAD;
		typeAndId[1] = input.byteValue();
		sysexSendList.add(typeAndId);
		
		if (configOptions.mcuType > 2) {
			//STM32. All inputs
			typeAndId = new byte[2];
			typeAndId[0] = Constants.MD_SYSEX_POS;
			typeAndId[1] = input.byteValue();
			sysexSendList.add(typeAndId);
		} else {
			// Atmega644/1284. 4 or 8 head/bow inputs
			if (input > 2) {
				if ((input&1) > 0) {
					int in = (input - 3)/2;
					if (((in < 4) && (configOptions.mcuType > 0)) || ((in < 8) && (configOptions.mcuType > 1))) {
						typeAndId = new byte[2];
						typeAndId[0] = Constants.MD_SYSEX_POS;
						typeAndId[1] = (byte)in;
						sysexSendList.add(typeAndId);
					}				
				}
			}
		}
		sendSysex();
	}

	private void sendSysexPairRequest(Integer pair) {
		//sysexSendList.clear();
		byte [] typeAndId;
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_PAD;
		typeAndId[1] = (byte)((pair*2) - 1);
		sysexSendList.add(typeAndId);

		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_PAD;
		typeAndId[1] = (byte)(pair*2);
		sysexSendList.add(typeAndId);

		if (configOptions.mcuType > 2) {
			//STM32. All inputs
			typeAndId = new byte[2];
			typeAndId[0] = Constants.MD_SYSEX_POS;
			typeAndId[1] = (byte)((pair*2) - 1);
			sysexSendList.add(typeAndId);

			typeAndId = new byte[2];
			typeAndId[0] = Constants.MD_SYSEX_POS;
			typeAndId[1] = (byte)(pair*2);
			sysexSendList.add(typeAndId);
		} else {
			// Atmega644/1284. 4 or 8 head/bow inputs
			int input = (pair*2) - 1;
			if (input > 2) {
				if ((input&1) > 0) {
					int in = (input - 3)/2;
					if (((in < 4) && (configOptions.mcuType > 0)) || ((in < 8) && (configOptions.mcuType > 1))) {
						typeAndId = new byte[2];
						typeAndId[0] = Constants.MD_SYSEX_POS;
						typeAndId[1] = (byte)in;
						sysexSendList.add(typeAndId);
					}				
				}
			}
		}

		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_3RD;
		typeAndId[1] = (byte)(pair - 1);
		sysexSendList.add(typeAndId);
		sendSysex();
	}

	private void sendAllInputsSysex() {
		byte i;
		boolean doPos = true;
		for (i = 0; i < (configFull.configGlobalMisc.inputs_count - 1); i++) {
			sysexSendList.add(configFull.configPads[i].getSysexFromConfig());
			doPos = false;
			if (configOptions.mcuType > 2) {
				doPos = true;
			} else {
				if (configOptions.mcuType > 1) {
					if (i < 8) {
						doPos = true;
					}
				} else {
					if (i < 4) {
						doPos = true;
					}
				}
			}
			if (doPos) {
				sysexSendList.add(configFull.configPos[i].getSysexFromConfig());
			}
			if ((i&1) > 0) {
				sysexSendList.add(configFull.config3rds[(i-1)/2].getSysexFromConfig());
			}
		}
		sendSysex();
	}
	
	private void sendAllInputsSysexRequests() {
		//sysexSendList.clear();
		byte [] typeAndId;
		byte i;
		for (i = 0; i < (configFull.configGlobalMisc.inputs_count - 1); i++) {
			typeAndId = new byte[2];
			typeAndId[0] = Constants.MD_SYSEX_PAD;
			typeAndId[1] = i;
			sysexSendList.add(typeAndId);

			if (configOptions.mcuType > 2) {
				//STM32. All inputs
				typeAndId = new byte[2];
				typeAndId[0] = Constants.MD_SYSEX_POS;
				typeAndId[1] = i;
				sysexSendList.add(typeAndId);

			} else {
				// Atmega644/1284. 4 or 8 head/bow inputs
				int input = i;
				if (input > 2) {
					if ((input&1) > 0) {
						int in = (input - 3)/2;
						if (((in < 4) && (configOptions.mcuType > 0)) || ((in < 8) && (configOptions.mcuType > 1))) {
							typeAndId = new byte[2];
							typeAndId[0] = Constants.MD_SYSEX_POS;
							typeAndId[1] = (byte)in;
							sysexSendList.add(typeAndId);
						}				
					}
				}
			}
						
			if ((i&1) > 0) {
				typeAndId = new byte[2];
				typeAndId[0] = Constants.MD_SYSEX_3RD;
				typeAndId[1] = (byte)((i-1)/2);
				sysexSendList.add(typeAndId);
				
			}
		}
		sendSysex();
	}
	
	private void sendAllSysex() {
		byte i;
		boolean doPos;
		sysexSendList.add(configFull.configGlobalMisc.getSysexFromConfig());
		sysexSendList.add(configFull.configMisc.getSysexFromConfig());		
		sysexSendList.add(configFull.configPedal.getSysexFromConfig(configOptions.mcuType));
		for (i = 0; i < (configFull.configGlobalMisc.inputs_count - 1); i++) {
			sysexSendList.add(configFull.configPads[i].getSysexFromConfig());
			
			doPos = false;
			if (configOptions.mcuType > 2) {
				doPos = true;
			} else {
				if (configOptions.mcuType > 1) {
					if (i < 8) {
						doPos = true;
					}
				} else {
					if (i < 4) {
						doPos = true;
					}
				}
			}
			if (doPos) {
				sysexSendList.add(configFull.configPos[i].getSysexFromConfig());
			}

			if ((i&1) > 0) {
				sysexSendList.add(configFull.config3rds[(i-1)/2].getSysexFromConfig());
			}
		}
		for (i = 0; i < Constants.CURVES_COUNT; i++) {
			sysexSendList.add(configFull.configCurves[i].getSysexFromConfig());
			
		}
		//if (configFull.configGlobalMisc.custom_names_en) {
			for (i = 0; i < configFull.customNamesCount; i++) {
				sysexSendList.add(configFull.configCustomNames[i].getSysexFromConfig());
				
			}
		//}
		if (configFull.configGlobalMisc.config_names_en) {
			for (i = 0; i < configFull.configNamesCount; i++) {
				sysexSendList.add(configFull.configConfigNames[i].getSysexFromConfig());
			}
		}
		sendSysexReadOnlyRequestFlag = true;
		sendSysex();
	}
	
	private void sendAllSysexRequests() {
		byte [] typeAndId;
		//sysexSendList.clear();
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_GLOBAL_MISC;
		sysexSendList.add(typeAndId);
		sendNextAllSysexRequestsFlag = true;
		//System.out.println("Requesting Global Misc for Get All");
		sendSysex();
	}
	
	private void sendNextAllSysexRequests() {
		//System.out.println("Requesting the rest for Get All");
		byte [] typeAndId;
		byte i;
		//sysexSendList.clear();
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_MISC;
		sysexSendList.add(typeAndId);
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_PEDAL;
		sysexSendList.add(typeAndId);
		for (i = 0; i < (configFull.configGlobalMisc.inputs_count - 1); i++) {
			typeAndId = new byte[2];
			typeAndId[0] = Constants.MD_SYSEX_PAD;
			typeAndId[1] = i;
			sysexSendList.add(typeAndId);

			if (configOptions.mcuType > 2) {
				//STM32. All inputs
				typeAndId = new byte[2];
				typeAndId[0] = Constants.MD_SYSEX_POS;
				typeAndId[1] = i;
				sysexSendList.add(typeAndId);

			} else {
				// Atmega644/1284. 4 or 8 head/bow inputs
				int input = i;
				if (input > 2) {
					if ((input&1) > 0) {
						int in = (input - 3)/2;
						if (((in < 4) && (configOptions.mcuType > 0)) || ((in < 8) && (configOptions.mcuType > 1))) {
							typeAndId = new byte[2];
							typeAndId[0] = Constants.MD_SYSEX_POS;
							typeAndId[1] = (byte)in;
							sysexSendList.add(typeAndId);
						}				
					}
				}
			}
			
			if ((i&1) > 0) {
				typeAndId = new byte[2];
				typeAndId[0] = Constants.MD_SYSEX_3RD;
				typeAndId[1] = (byte)((i-1)/2);
				sysexSendList.add(typeAndId);				
			}
		}
		for (i = 0; i < Constants.CURVES_COUNT; i++) {
			typeAndId = new byte[2];
			typeAndId[0] = Constants.MD_SYSEX_CURVE;
			typeAndId[1] = i;
			sysexSendList.add(typeAndId);
		}
		for (i = 0; i < configFull.customNamesCount; i++) {
			if (configOptions.mcuType < 2) {
				//Atmega644 has 16 custom names max
				if (i > 15) {
					break;
				}
			}
			typeAndId = new byte[2];
			typeAndId[0] = Constants.MD_SYSEX_CUSTOM_NAME;
			typeAndId[1] = i;
			sysexSendList.add(typeAndId);
		}
		if (configFull.configGlobalMisc.config_names_en) {
			for (i = 0; i < configFull.configNamesCount; i++) {
				typeAndId = new byte[2];
				typeAndId[0] = Constants.MD_SYSEX_CONFIG_NAME;
				typeAndId[1] = i;
				sysexSendList.add(typeAndId);
			}
		}
		typeAndId = new byte[2];
		typeAndId[0] = Constants.MD_SYSEX_CONFIG_CURRENT;
		sysexSendList.add(typeAndId);
		sendSysex();
	}
	
	private void showUpgradeWindow() {
		upgradeWindow.show();
		switch (upgradeWindow.getUpgradeResult()) {
		case UIUpgrade.UPGRADE_SUCCESS:
			//System.out.println("Ok");
			openMidiPorts(true);
			break;
		case UIUpgrade.UPGRADE_FAILED:
			//System.out.println("Failed");
			openMidiPorts(false);
			break;
		case UIUpgrade.UPGRADE_NOT_PERFORMED:
		default:
			//System.out.println("Not performed");
			break;
		}
	}
	
	private void showOptionsWindow() {
		optionsUpdatePorts();
		optionsWindow.show();
		if (optionsWindow.getClosedWithOk()) {
			//System.out.println("Closed with ok");
			openMidiPorts(true);
			setShowAdvanced();
		}
	}
	
	private void setShowAdvanced() {
		for (int i=0; i < allPanels.size(); i++) {
			allPanels.get(i).setShowAdvanced(configOptions.showAdvancedSettings);
			if (allPanels.get(i).isDetached()) {
				respondToResizeDetached(allPanels.get(i).getWindow().getScene(), allPanels.get(i));
			} else {
				respondToResize(scene1);
			}
		}
	}
	
	private void setAllStatesUnknown() {
		uiGlobalMisc.setAllStatesUnknown();
		uiMisc.setAllStateUnknown();
		uiPedal.setAllStateUnknown();
		for (int i = 0; i < Constants.MAX_INPUTS; i++ ) {
			moduleConfigFull.configPads[i].sysexReceived = false;
			moduleConfigFull.configPos[i].sysexReceived = false;
			if ((i&1) > 0) {
				moduleConfigFull.config3rds[(i-1)/2].sysexReceived = false;
			}
		}
		uiPad.setAllStatesUnknown(false, false, false);
		uiPadsExtra.setCurveSysexReceived(false);
		uiPadsExtra.testCurveSyncState();
		for (int i = 0; i < Constants.CURVES_COUNT; i++) {
			moduleConfigFull.configCurves[i].sysexReceived = false;
		}
		uiPadsExtra.setAllCustomNamesStatesUnknown();
		
	}
	
	private void openMidiPorts(Boolean toOpen) {
		boolean clearMidiLabelStatus = true;
		if (toOpen) {
			if (midiController.isMidiOpen()) {
				midiController.closeAllPorts();
			}
			if (configOptions.useThruPort) {
				midiController.openMidi(configOptions.MidiInName, configOptions.MidiOutName, configOptions.MidiThruName);				
			} else {
				midiController.openMidi(configOptions.MidiInName, configOptions.MidiOutName, "");
			}
			if (!midiController.isMidiOpen()) {
				uiGlobal.setSysexStatusLabel(Constants.MD_SYSEX_STATUS_MIDI_INIT_ERROR, 0);	
				clearMidiLabelStatus = false;
			}
		} else {
			midiController.closeAllPorts();
			configOptions.mcuType = 0;
			configOptions.version = 0;
		}
		if (midiController.isMidiOpen()) {
			uiGlobalMisc.getToggleButtonMidi().setSelected(true);
			uiGlobalMisc.getToggleButtonMidi().setText("Close MIDI");
			sysexThreadsStarted = 0;
			sendSysexReadOnlyRequest();
		} else {
			uiGlobalMisc.getToggleButtonMidi().setSelected(false);
			uiGlobalMisc.getToggleButtonMidi().setText("Open MIDI");
			uiGlobalMisc.setAllStatesUnknown();
			if (clearMidiLabelStatus) {
				uiGlobal.clearSysexStatusLabel();
			}
			configOptions.mcuType = 0;
			setAllStatesUnknown();
		}
	}
	
	private void initMidi() {
		midiController = new MidiController();
		sysexSendList = new ArrayList<>();
		sysexLastChanged = new ArrayList<>();
		midiController.addMidiEventListener(new MidiEventListener() {
			@Override
			public void midiEventOccurred(MidiEvent evt) {
				//System.out.println("Received MidiEvent");
				byte [] buffer = new byte[midiController.getMidiDataList().get(0).length];
				System.arraycopy(midiController.getMidiDataList().get(0), 0, buffer, 0, buffer.length);
				midiController.getMidiDataList().remove(0);
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						if (buffer.length > 3) {
							processSysex(buffer);
						} else {
							processShortMidi(buffer);
						}
					}
				});
			}

			@Override
			public void midiEventOccurredWithBuffer(MidiEvent evt, byte[] buffer) {
				//System.out.println("Received MidiEvent with buffer");
				if (buffer.length > 3) {
					processSysex(buffer);
				} else {
					processShortMidi(buffer);
				}
			}
		});		
	}
	
	private void processShortMidi(byte [] buffer) {
		//panelMidiLog.addRawMidi(buffer);
		int type = MidiLevelBar.barTypeUnknown;
		switch (buffer.length) {
		case 1:
			//shortMessage.setMessage(buf[0]);
			break;
		case 2:
			//shortMessage.setMessage(buf[0], buf[1],0);
			break;
		default:
			//shortMessage.setMessage(buf[0], buf[1],buf[2]);
			//System.out.printf("MIDI Short = %02x %02x %02x\n", buffer[0], buffer[1], buffer[2]);
			uiMidiLog.addRawMidi(buffer);
			if (((buffer[0]&0xf0) == 0x90) && (buffer[2] > 0)) {
				type = MidiLevelBar.barTypeUnknown;
				for (int i = 0; i< configFull.configPads.length;i++) {
					if ((buffer[1]==configFull.configPads[i].note) ||
							(buffer[1]==configFull.configPads[i].altNote) ||
							(buffer[1]==configFull.configPads[i].pressrollNote)) {
						if (i==0) {
							type = MidiLevelBar.barTypeHead;
						} else {
							type = ((i&0x01)==1)?MidiLevelBar.barTypeHead:MidiLevelBar.barTypeRim;
						}
					}
					if ((i&0x01) == 1) {
						if ((buffer[1]==configFull.config3rds[(i-1)/2].note) ||
								(buffer[1]==configFull.config3rds[(i-1)/2].altNote) ||
								(buffer[1]==configFull.config3rds[(i-1)/2].pressrollNote) ||
								(buffer[1]==configFull.config3rds[(i-1)/2].dampenedNote)) {
							type = MidiLevelBar.barType3rd;
						}						
					}
				}
				uiMidiLog.addNewMidiData(type, buffer[1], buffer[2]);
			}
			if ((buffer[0]&0xf0) == 0xa0) {
				uiMidiLog.addNewMidiData((buffer[2]>0)?MidiLevelBar.barTypeChokeOn:MidiLevelBar.barTypeChokeOff,
						buffer[1], buffer[2]);
			}
			if (((buffer[0]&0xf0) == 0xb0) && (buffer[1] == 0x04)) {
				uiMidiLog.setHiHatLevel(127 - buffer[2]);
			}
			if (((buffer[0]&0xf0) == 0xb0) && (buffer[1] == 0x10)) {
				//uiMidiLog.addNewPositional(127 - buffer[2]);
				uiMidiLog.addNewPositional((int)buffer[2]);
			}
			if (((buffer[0]&0xf0) == 0xb0) && (buffer[1] == 0x13)) {
				int id = buffer[2];
				if (id > 0x3f) {
					id = (id - 0x40)*2 + 1; 
				} else {
					id--;
				}
				if (id > 0) {
					id = (id - 1)/2 + 1;
				} else {
					id = 0;
				}
				if (configFull.configMisc.send_triggered_in) {
					//controlsPads.switchAndShowPad(id);
				}
			}
			break;
		}		
	}
	
	private void processSysex(byte [] sysex) {
		if (sysex.length >= 5) {
			//System.out.printf("Sysex received type = %d\n", sysex[3]);			
			byte pointer = sysex[4];
	    	switch (sysex[3]) {
			case Constants.MD_SYSEX_3RD:
				//System.out.printf("Sysex 3rd pointer = %d\n", pointer);
				if (pointer < configFull.config3rds.length) {
					configFull.config3rds[pointer].setConfigFromSysex(sysex);
					moduleConfigFull.config3rds[pointer].setConfigFromSysex(sysex);
					moduleConfigFull.config3rds[pointer].syncState = Constants.SYNC_STATE_RECEIVED;
					moduleConfigFull.config3rds[pointer].sysexReceived = true;
					if ((pointer + 1) == padPair) {
						uiPad.setControlsFromConfig3rd(configFull.config3rds[pointer], true);					
					}
				}
				break;
			case Constants.MD_SYSEX_CONFIG_COUNT:
				if (sysex.length >= Constants.MD_SYSEX_CONFIG_COUNT_SIZE) {
					int b;
					b = (int)sysex[4];
					uiGlobalMisc.setConfigsCount(b);
					configFull.configNamesCount = b;
					configFull.configCountSysexReceived = true;
					reCreateSlotsMenuItems();
				}
				break;
			case Constants.MD_SYSEX_CONFIG_CURRENT:
				if (sysex.length >= Constants.MD_SYSEX_CONFIG_CURRENT_SIZE) {
					int b;
					b = (int)sysex[4];
					uiGlobalMisc.setConfigCurrent(b);
					configFull.configCurrentSysexReceived = true;
					uiGlobalMisc.getTextFieldSlotName().setText(configFull.configConfigNames[pointer].name.trim());
				}
				break;
			case Constants.MD_SYSEX_CONFIG_NAME:
				configFull.configConfigNames[pointer].setConfigFromSysex(sysex);
				moduleConfigFull.configConfigNames[pointer].setConfigFromSysex(sysex);
				configFull.configConfigNames[pointer].sysexReceived = true;
			    //System.out.printf("sysexReceived for ConfigName id %d set to true\n", buffer[4]);
				reCreateSlotsMenuItems();
				if (configFull.configCurrent == pointer) {
					//uiGlobalMisc.geTextFieldSlotName().setText(configFull.configConfigNames[pointer].name.trim());
				}
				break;
			case Constants.MD_SYSEX_CURVE:
				configFull.configCurves[pointer].setConfigFromSysex(sysex);
				moduleConfigFull.configCurves[pointer].setConfigFromSysex(sysex);
				moduleConfigFull.configCurves[pointer].syncState = Constants.SYNC_STATE_RECEIVED;
				moduleConfigFull.configCurves[pointer].sysexReceived = true;
				if (pointer == curvePointer) {
					uiPadsExtra.setYvalues(configFull.configCurves[pointer].yValues, true);					
				}
				break;
			case Constants.MD_SYSEX_CUSTOM_NAME:
				configFull.configCustomNames[pointer].setConfigFromSysex(sysex);
				moduleConfigFull.configCustomNames[pointer].setConfigFromSysex(sysex);
				moduleConfigFull.configCustomNames[pointer].syncState = Constants.SYNC_STATE_RECEIVED;
				moduleConfigFull.configCustomNames[pointer].sysexReceived = true;
				uiPadsExtra.setCustomName(configFull.configCustomNames[pointer], pointer, true);
				addCustomNamesToPads();
				break;
			case Constants.MD_SYSEX_GLOBAL_MISC:
				configFull.configGlobalMisc.setConfigFromSysex(sysex);
				moduleConfigFull.configGlobalMisc.setConfigFromSysex(sysex);
				moduleConfigFull.configGlobalMisc.syncState = Constants.SYNC_STATE_RECEIVED;
				moduleConfigFull.configGlobalMisc.sysexReceived = true;
				uiGlobalMisc.setControlsFromConfig(configFull.configGlobalMisc, true);
				updateComboBoxInput(false);
				break;
			case Constants.MD_SYSEX_MCU_TYPE:
				if (sysex.length >= Constants.MD_SYSEX_MCU_TYPE_SIZE) {
					configOptions.mcuType = (int)(sysex[4]<<4);
					configOptions.mcuType |= (int)sysex[5];
					if (configOptions.mcuType < Constants.MCU_TYPES.length ) {
						uiGlobalMisc.setMcu(configOptions.mcuType);								
					}
					uiMisc.setUnknownLabel(configOptions.mcuType);
				}
				break;
			case Constants.MD_SYSEX_MISC:
				configFull.configMisc.setConfigFromSysex(sysex);
				moduleConfigFull.configMisc.setConfigFromSysex(sysex);
				moduleConfigFull.configMisc.syncState = Constants.SYNC_STATE_RECEIVED;
				moduleConfigFull.configMisc.sysexReceived = true;
				uiMisc.setControlsFromConfig(configFull.configMisc, true);
				break;
			case Constants.MD_SYSEX_PAD:
				//System.out.printf("Sysex received wiht pad pointer = %d\n", pointer);
				configFull.configPads[pointer - 1].setConfigFromSysex(sysex);
				moduleConfigFull.configPads[pointer - 1].setConfigFromSysex(sysex);
				moduleConfigFull.configPads[pointer - 1].syncState = Constants.SYNC_STATE_RECEIVED;
				moduleConfigFull.configPads[pointer - 1].sysexReceived = true;
				if ((pointer - 1) == 0) {
					if (padPair == 0) {
						uiPad.setControlsFromConfigPad(configFull.configPads[pointer - 1], true, true);					
					}
				} else {
					if ((((pointer - 2)/2) + 1) == padPair) {
						uiPad.setControlsFromConfigPad(configFull.configPads[pointer - 1], (pointer&1) == 0, true);
					}
				}
				break;
			case Constants.MD_SYSEX_PEDAL:
				configFull.configPedal.setConfigFromSysex(sysex, configOptions.mcuType);
				moduleConfigFull.configPedal.setConfigFromSysex(sysex, configOptions.mcuType);
				moduleConfigFull.configPedal.syncState = Constants.SYNC_STATE_RECEIVED;
				moduleConfigFull.configPedal.sysexReceived = true;
				uiPedal.setControlsFromConfig(configFull.configPedal, true);
				break;
			case Constants.MD_SYSEX_POS:
				//System.out.printf("Sysex pos pointer = %d\n", pointer);
				//byte realPointer = pointer;
				if (configOptions.mcuType < 3) {
					//Atmega MCU, 8 (Atmega1284) or 4 (Atmega644) head/bow inputs with positional sensing starting from Snare
					pointer = (byte)(pointer*2 + 3);
				}
				configFull.configPos[pointer].setConfigFromSysex(sysex);
				moduleConfigFull.configPos[pointer].setConfigFromSysex(sysex);
				moduleConfigFull.configPos[pointer].syncState = Constants.SYNC_STATE_RECEIVED;
				moduleConfigFull.configPos[pointer].sysexReceived = true;
				//pointer = realPointer;
				if (pointer == 0) {
					if (padPair == 0) {
						uiPad.setControlsFromConfigPos(configFull.configPos[pointer], true, true);					
					}
				} else {
					if ((((pointer - 1)/2) + 1) == padPair) {
						uiPad.setControlsFromConfigPos(configFull.configPos[pointer], ((pointer+1)&1) == 0, true);
					}
				}
				break;
			case Constants.MD_SYSEX_VERSION:
				int ver = 0;
				if (sysex.length >= Constants.MD_SYSEX_VERSION_SIZE) {
					int b;
					for (int i=0;i<4;i++) {
						b = (int)(sysex[i*2 + 4]<<4);
						b |= (int)sysex[i*2 + 5];
						ver += b<<(8*i);
					}
					configOptions.version = ver;
					if (ver < Constants.MD_MINIMUM_VERSION) {
						uiGlobalMisc.setVersion(ver, "salmon");
						if (!versionWarningAlreadyShown) {
							versionWarningAlreadyShown = true;
							Alert alert = new Alert(AlertType.WARNING);
				            alert.setHeaderText("Firmware version is too old!");
				            WebView webView = new WebView();
				            webView.getEngine().loadContent(Constants.WARNING_VERSION);
				            webView.setPrefSize(300, 120);
				            alert.getDialogPane().setContent(webView);
							
							Timer warning_timer = new Timer();
							warning_timer.schedule(new TimerTask() {
								
								@Override
								public void run() {
									Platform.runLater(new Runnable() {
										public void run() {
											alert.showAndWait();
										}
									});
								}
							}, 200);
						}
					} else {
						uiGlobalMisc.setVersion(ver, "lightgreen");
					}
				}
				break;
			default:
				break;
			}			
		}
	}
	private void initConfigs() {
		fullConfigs = new ConfigFull[Constants.CONFIGS_COUNT];
		configFileNames = new String[Constants.CONFIGS_COUNT];
		for (Integer i = 0;i < Constants.CONFIGS_COUNT;i++) {
			fullConfigs[i] = new ConfigFull();
			configFileNames[i] = new String();
		}
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
		//System.out.println("Midi Rescan Event occured");
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
				result = result + configFull.configCustomNames[namePointer - totalCustomNames - 1].name;
			}
			if ((input & 1) > 0) {
				result += "h";
			} else {
				result += "r";
			}
		}
		return result;
	}
	
	private void copyPad(int index) {
		int src,dst;
		if (padPair == 0) {
			src = 0;
		} else {
			src = (padPair*2) - 1;
		}
		if (index == 0) {
			for (int i = 1; i < (configFull.configGlobalMisc.inputs_count/2); i++) {
				dst = (i*2) - 1;
				configFull.configPads[dst].setConfigFromSysex(configFull.configPads[src].getSysexFromConfig());						
				configFull.configPos[dst].setConfigFromSysex(configFull.configPos[src].getSysexFromConfig());						
				if (src != 0) {
					configFull.configPads[dst + 1].setConfigFromSysex(configFull.configPads[src + 1].getSysexFromConfig());					
					configFull.configPos[dst + 1].setConfigFromSysex(configFull.configPos[src + 1].getSysexFromConfig());
					configFull.config3rds[i - 1].setConfigFromSysex(configFull.config3rds[padPair - 1].getSysexFromConfig());
				}
			}
		} else {
			dst = index*2 - 1;
			configFull.configPads[dst].setConfigFromSysex(configFull.configPads[src].getSysexFromConfig());						
			configFull.configPos[dst].setConfigFromSysex(configFull.configPos[src].getSysexFromConfig());						
			if (src != 0) {
				configFull.configPads[dst + 1].setConfigFromSysex(configFull.configPads[src + 1].getSysexFromConfig());					
				configFull.configPos[dst + 1].setConfigFromSysex(configFull.configPos[src + 1].getSysexFromConfig());
				configFull.config3rds[index - 1].setConfigFromSysex(configFull.config3rds[padPair - 1].getSysexFromConfig());						
			}			
		}
	}
	
	private void copy3rd(int index) {
		int src,dst;
		if (padPair > 0) {
			src = padPair - 1;
			if (index == 0) {
				for (int i = 1; i < (configFull.configGlobalMisc.inputs_count/2); i++) {
					dst = i - 1;
					configFull.config3rds[dst].setConfigFromSysex(configFull.config3rds[src].getSysexFromConfig());						
				}
			} else {
				dst = index - 1;
				configFull.config3rds[dst].setConfigFromSysex(configFull.config3rds[src].getSysexFromConfig());						
			}
		}
	}

	private void copyHead(int index) {
		int src,dst;
		if (padPair == 0) {
			src = 0;
		} else {
			src = (padPair*2) - 1;
		}
		if (index == 0) {
			for (int i = 1; i < (configFull.configGlobalMisc.inputs_count/2); i++) {
				dst = (i*2) - 1;
				configFull.configPads[dst].setConfigFromSysex(configFull.configPads[src].getSysexFromConfig());						
				configFull.configPos[dst].setConfigFromSysex(configFull.configPos[src].getSysexFromConfig());						
			}
		} else {
			dst = index*2 - 1;
			configFull.configPads[dst].setConfigFromSysex(configFull.configPads[src].getSysexFromConfig());						
			configFull.configPos[dst].setConfigFromSysex(configFull.configPos[src].getSysexFromConfig());						
		}
	}
	
	private void copyRim(int index) {
		int src,dst;
		if (padPair == 0) {
			src = 0;
		} else {
			src = (padPair*2) - 1;
		}
		if (index == 0) {
			for (int i = 1; i < (configFull.configGlobalMisc.inputs_count/2); i++) {
				dst = (i*2) - 1;
				if (src != 0) {
					configFull.configPads[dst + 1].setConfigFromSysex(configFull.configPads[src + 1].getSysexFromConfig());					
					configFull.configPos[dst + 1].setConfigFromSysex(configFull.configPos[src + 1].getSysexFromConfig());
				}
			}
		} else {
			dst = index*2 - 1;
			if (src != 0) {
				configFull.configPads[dst + 1].setConfigFromSysex(configFull.configPads[src + 1].getSysexFromConfig());					
				configFull.configPos[dst + 1].setConfigFromSysex(configFull.configPos[src + 1].getSysexFromConfig());
			}			
		}
	}
	
	private void updateComboBoxInput(Boolean nameChaned) {
		MenuItem menuItem;
		if ((oldInputsCounts != configFull.configGlobalMisc.inputs_count) || nameChaned ) {
			menuItemsCopyPad.clear();
			menuItemsCopyPad.add(new MenuItem("To All Pads"));
			menuItemsCopyPad.get(0).setOnAction( e->{
				copyPad(0);
			});
			menuItemsCopyHead.clear();
			menuItemsCopyHead.add(new MenuItem("To All Heads"));
			menuItemsCopyHead.get(0).setOnAction( e->{
				copyHead(0);
			});
			menuItemsCopyRim.clear();
			menuItemsCopyRim.add(new MenuItem("To All Rims"));
			menuItemsCopyRim.get(0).setOnAction( e->{
				copyRim(0);
			});
			menuItemsCopy3rd.clear();
			menuItemsCopy3rd.add(new MenuItem("To All 3rds"));
			menuItemsCopy3rd.get(0).setOnAction( e->{
				copy3rd(0);
			});
			oldInputsCounts = configFull.configGlobalMisc.inputs_count;
			List<String> list;
			String name;
			list = new ArrayList<>();
			int inputPointer;
			for (int i = 0; i < ((configFull.configGlobalMisc.inputs_count/2)); i++) {
				final int iFinal = i;
				if (i == 0) {
					list.add(getInputName(i));
					menuItem = new MenuItem("To " + getInputName(i));
					menuItemsCopyHead.add(menuItem);
				} else {
					inputPointer = (i*2) - 1;
					name = getInputName(inputPointer);
					menuItem = new MenuItem("To " + getInputName(inputPointer));
					menuItem.setOnAction( e->{
						copyHead(iFinal);						
					});
					menuItemsCopyHead.add(menuItem);
					name = name + "/";
					inputPointer++;
					name = name + getInputName(inputPointer);
					menuItem = new MenuItem("To " + getInputName(inputPointer));
					menuItem.setOnAction( e->{
						copyRim(iFinal);						
					});
					menuItemsCopyRim.add(menuItem);
					list.add(name);
					menuItem = new MenuItem("To " + name);
					menuItem.setOnAction( e->{
						copyPad(iFinal);						
					});
					menuItemsCopyPad.add(menuItem);
					menuItem = new MenuItem("To " + name);
					menuItem.setOnAction( e->{
						copy3rd(iFinal);						
					});
					menuItemsCopy3rd.add(menuItem);
				}
			}
			//comboBoxInputChangedFromSet = 1;
			uiPad.getComboBoxInput().getItems().clear();
			uiPad.getComboBoxInput().getItems().addAll(list);
			
			menuCopyPad.getItems().clear();
			menuCopyPad.getItems().addAll(menuItemsCopyPad);
			menuCopyHead.getItems().clear();
			menuCopyHead.getItems().addAll(menuItemsCopyHead);
			menuCopyRim.getItems().clear();
			menuCopyRim.getItems().addAll(menuItemsCopyRim);
			menuCopy3rd.getItems().clear();
			menuCopy3rd.getItems().addAll(menuItemsCopy3rd);
			
			if ((configFull.configGlobalMisc.inputs_count)/2 > padPair) {
				uiPad.getComboBoxInput().getSelectionModel().select(padPair);				
			} else {
				switchToSelectedPair(0);
			}
		}
	}
	
	private void switchToSelectedPair(Integer newPadPair) {
		menuCopyPad.setDisable(newPadPair == 0);
		menuCopyRim.setDisable(newPadPair == 0);
		menuCopy3rd.setDisable(newPadPair == 0);
		if (padPair != newPadPair) {
			if (padPair == 0) {
				uiPad.setConfigFromControlsPad(configFull.configPads[0], true);			
				uiPad.setConfigPosFromControlsPad(configFull.configPos[0], true);			
			} else {
				uiPad.setConfigFromControlsPad(configFull.configPads[((padPair-1)*2) + 1], true);
				uiPad.setConfigPosFromControlsPad(configFull.configPos[((padPair-1)*2) + 1], true);
				uiPad.setConfigFromControlsPad(configFull.configPads[((padPair-1)*2) + 2], false);
				uiPad.setConfigPosFromControlsPad(configFull.configPos[((padPair-1)*2) + 2], false);
				uiPad.setConfig3rdFromControlsPad(configFull.config3rds[padPair - 1]);
			}		
			padPair = newPadPair;
		}
		if (padPair == 0) {
			uiPad.setAllStatesUnknown(moduleConfigFull.configPads[0].sysexReceived, false, false);
			if (moduleConfigFull.configPads[0].sysexReceived) {
				uiPad.setMdValuesPad(moduleConfigFull.configPads[0], configFull.configPos[0], true);
			}
			uiPad.setInputPair(padPair, configFull.configPads[0], configFull.configPos[0], null, null, null);
		} else {
			uiPad.setAllStatesUnknown(moduleConfigFull.configPads[((padPair-1)*2) + 1].sysexReceived, moduleConfigFull.configPads[((padPair-1)*2) + 2].sysexReceived, moduleConfigFull.config3rds[padPair - 1].sysexReceived);
			if (moduleConfigFull.configPads[((padPair-1)*2) + 1].sysexReceived) {
				uiPad.setMdValuesPad(moduleConfigFull.configPads[((padPair-1)*2) + 1], configFull.configPos[((padPair-1)*2) + 1], true);				
			}
			if (moduleConfigFull.configPads[((padPair-1)*2) + 2].sysexReceived) {
				uiPad.setMdValuesPad(moduleConfigFull.configPads[((padPair-1)*2) + 2], configFull.configPos[((padPair-1)*2) + 2], false);				
			}
			if (moduleConfigFull.config3rds[padPair - 1].sysexReceived) {
				uiPad.setMdValues3rd(moduleConfigFull.config3rds[padPair - 1]);
			}
			uiPad.setInputPair(padPair, configFull.configPads[((padPair-1)*2) + 1], configFull.configPos[((padPair-1)*2) + 1], configFull.configPads[((padPair-1)*2) + 2], configFull.configPos[((padPair-1)*2) + 2], configFull.config3rds[padPair - 1]);
		}
		//comboBoxInputChangedFromSet = 1;
		uiPad.getComboBoxInput().getSelectionModel().select(padPair);
	}
	
	private void switchToSelectedCurve(Integer curve) {
		if ((curve > -1) && (curve < Constants.CURVES_COUNT)) {
			//uiPadsExtra.getYvalues(configFull.configCurves[curvePointer].yValues);
			//int [] t = configFull.configCurves[curvePointer].yValues;
			//System.out.printf("Y values at %d: %d %d %d %d %d %d %d %d %d\n", curvePointer, t[0],t[1],t[2],t[3],t[4],t[5],t[6],t[7],t[8] );
			curvePointer = curve;
			if (moduleConfigFull.configCurves[curvePointer].sysexReceived) {
				uiPadsExtra.setMdYvalues(moduleConfigFull.configCurves[curvePointer].yValues);
				uiPadsExtra.setCurveSysexReceived(true);
			} else {
				uiPadsExtra.setCurveSysexReceived(false);				
			}
			uiPadsExtra.getCurvesComboBox().getSelectionModel().select(curvePointer);
			uiPadsExtra.setYvalues(configFull.configCurves[curvePointer].yValues, false);
			uiPadsExtra.testCurveSyncState();
		}
	}

	private void loadAllFromConfigFull() {

		configFull.configGlobalMisc.setConfigFromSysex(
				fullConfigs[configOptions.lastConfig].configGlobalMisc.getSysexFromConfig()
				);
		uiGlobalMisc.setControlsFromConfig(configFull.configGlobalMisc, false);

		configFull.configMisc.setConfigFromSysex(
				fullConfigs[configOptions.lastConfig].configMisc.getSysexFromConfig()
				);
		uiMisc.setControlsFromConfig(configFull.configMisc, false);

		configFull.configPedal.setConfigFromSysex(
				fullConfigs[configOptions.lastConfig].configPedal.getSysexFromConfig(configOptions.mcuType), configOptions.mcuType);
		uiPedal.setControlsFromConfig(configFull.configPedal, false);

		for (int i=0; i < (Constants.MAX_INPUTS - 1); i++) {
			configFull.configPads[i].setConfigFromSysex(
					fullConfigs[configOptions.lastConfig].configPads[i].getSysexFromConfig()
					);
			configFull.configPads[i].altNote_linked = fullConfigs[configOptions.lastConfig].configPads[i].altNote_linked;
			configFull.configPads[i].pressrollNote_linked = fullConfigs[configOptions.lastConfig].configPads[i].pressrollNote_linked;
			configFull.configPos[i].setConfigFromSysex(
					fullConfigs[configOptions.lastConfig].configPos[i].getSysexFromConfig()
					);
		}
		for (int i=0; i < ((Constants.MAX_INPUTS/2) - 1); i++) {
			configFull.config3rds[i].setConfigFromSysex(
					fullConfigs[configOptions.lastConfig].config3rds[i].getSysexFromConfig()
					);
			configFull.config3rds[i].altNote_linked = fullConfigs[configOptions.lastConfig].config3rds[i].altNote_linked;
			configFull.config3rds[i].pressrollNote_linked = fullConfigs[configOptions.lastConfig].config3rds[i].pressrollNote_linked;
		}				
		
		for (int i=0; i < (Constants.CURVES_COUNT); i++) {
			configFull.configCurves[i].setConfigFromSysex(
					fullConfigs[configOptions.lastConfig].configCurves[i].getSysexFromConfig()
					);
			if (i == curvePointer) {
				uiPadsExtra.setYvalues(configFull.configCurves[curvePointer].yValues, false);
			}
		}
		for (int i=0; i < (Constants.CUSTOM_NAMES_MAX); i++) {
			configFull.configCustomNames[i].setConfigFromSysex(
					fullConfigs[configOptions.lastConfig].configCustomNames[i].getSysexFromConfig()
							);
			uiPadsExtra.setCustomName(configFull.configCustomNames[i], i, false);
		}
		configFull.customNamesCount = fullConfigs[configOptions.lastConfig].customNamesCount;
		setCustomNamesCountControl();
		addCustomNamesToPads();
		switchToSelectedPair(padPair);
	}
	
	private void load_all() {
		configOptions.lastConfig = uiGlobal.getComboBoxFile().getSelectionModel().getSelectedIndex();
		fileManager.load_all(fullConfigs[configOptions.lastConfig], configOptions);
		uiGlobal.getComboBoxFile().getItems().clear();
		uiGlobal.getComboBoxFile().getItems().addAll(configOptions.configFileNames);
		uiGlobal.getComboBoxFile().getSelectionModel().select(configOptions.lastConfig);
		loadAllFromConfigFull();
	}

	private void loadSysexMisc() {
		byte [] sysex;
		sysex = configFull.configMisc.getSysexFromConfig();
		fileManager.loadSysex(sysex, configOptions);
		configFull.configMisc.setConfigFromSysex(sysex);
		uiMisc.setControlsFromConfig(configFull.configMisc, false);		
	}
	
	private void saveSysexMisc() {
		fileManager.saveSysex(configFull.configMisc.getSysexFromConfig(), configOptions);		
	}
	
	private void loadSysexPedal() {
		byte [] sysex;
		sysex = configFull.configPedal.getSysexFromConfig(configOptions.mcuType);
		fileManager.loadSysex(sysex, configOptions);
		configFull.configPedal.setConfigFromSysex(sysex, configOptions.mcuType);
		uiPedal.setControlsFromConfig(configFull.configPedal, false);		
	}
	
	private void saveSysexPedal() {
		fileManager.saveSysex(configFull.configPedal.getSysexFromConfig(configOptions.mcuType), configOptions);		
	}
	
	private void loadSysexPad() {
		byte [] sysex = new byte[Constants.MD_SYSEX_PAD_SIZE];
		byte [] sysex3rd = new byte[Constants.MD_SYSEX_3RD_SIZE];
		byte [] sysexPos = new byte[Constants.MD_SYSEX_POS_SIZE];
		if (padPair > 0 ) {
			int leftInput = (padPair - 1)*2 + 1;
			int rightInput = leftInput + 1;
			byte [] sysexPad = new byte[Constants.MD_SYSEX_PAD_SIZE*2 + Constants.MD_SYSEX_3RD_SIZE + Constants.MD_SYSEX_POS_SIZE*2];
			sysex = configFull.configPads[leftInput].getSysexFromConfig();
			System.arraycopy(sysex, 0, sysexPad, 0, sysex.length);
			sysex = configFull.configPads[rightInput].getSysexFromConfig();
			System.arraycopy(sysex, 0, sysexPad, Constants.MD_SYSEX_PAD_SIZE, sysex.length);
			sysex3rd = configFull.config3rds[padPair - 1].getSysexFromConfig();
			System.arraycopy(sysex3rd, 0, sysexPad, Constants.MD_SYSEX_PAD_SIZE*2, sysex3rd.length);
			sysexPos = configFull.configPos[leftInput].getSysexFromConfig();
			System.arraycopy(sysexPos, 0, sysexPad, Constants.MD_SYSEX_PAD_SIZE*2 + Constants.MD_SYSEX_3RD_SIZE, sysexPos.length);
			sysexPos = configFull.configPos[rightInput].getSysexFromConfig();
			System.arraycopy(sysexPos, 0, sysexPad, Constants.MD_SYSEX_PAD_SIZE*2 + Constants.MD_SYSEX_3RD_SIZE+ Constants.MD_SYSEX_POS_SIZE, sysexPos.length);
			fileManager.loadSysex(sysexPad, configOptions);
			System.arraycopy(sysexPad, 0, sysex, 0, sysex.length);
			configFull.configPads[leftInput].setConfigFromSysex(sysex);
			System.arraycopy(sysexPad, Constants.MD_SYSEX_PAD_SIZE, sysex, 0, sysex.length);
			configFull.configPads[rightInput].setConfigFromSysex(sysex);
			System.arraycopy(sysexPad, Constants.MD_SYSEX_PAD_SIZE*2, sysex3rd, 0, sysex3rd.length);
			configFull.config3rds[padPair - 1].setConfigFromSysex(sysex3rd);
			System.arraycopy(sysexPad, Constants.MD_SYSEX_PAD_SIZE*2 + Constants.MD_SYSEX_3RD_SIZE, sysexPos, 0, sysexPos.length);
			configFull.configPos[leftInput].setConfigFromSysex(sysexPos);
			System.arraycopy(sysexPad, Constants.MD_SYSEX_PAD_SIZE*2 + Constants.MD_SYSEX_3RD_SIZE + Constants.MD_SYSEX_POS_SIZE, sysexPos, 0, sysexPos.length);
			configFull.configPos[rightInput].setConfigFromSysex(sysexPos);
			uiPad.setControlsFromConfigPad(configFull.configPads[leftInput], true, false);
			uiPad.setControlsFromConfigPad(configFull.configPads[rightInput], false, false);
			uiPad.setControlsFromConfigPos(configFull.configPos[leftInput], true, false);
			uiPad.setControlsFromConfigPos(configFull.configPos[rightInput], false, false);
			uiPad.setControlsFromConfig3rd(configFull.config3rds[padPair - 1], false);
		} else {
			byte [] sysexPad = new byte[Constants.MD_SYSEX_PAD_SIZE + Constants.MD_SYSEX_POS_SIZE];
			sysex = configFull.configPads[0].getSysexFromConfig();
			System.arraycopy(sysex, 0, sysexPad, 0, sysex.length);
			sysexPos = configFull.configPos[0].getSysexFromConfig();
			System.arraycopy(sysexPos, 0, sysexPad, Constants.MD_SYSEX_PAD_SIZE, sysexPos.length);
			fileManager.loadSysex(sysexPad, configOptions);					
			System.arraycopy(sysexPad, 0, sysex, 0, sysex.length);
			configFull.configPads[0].setConfigFromSysex(sysex);
			System.arraycopy(sysexPad, Constants.MD_SYSEX_PAD_SIZE, sysexPos, 0, sysexPos.length);
			configFull.configPos[0].setConfigFromSysex(sysexPos);
			uiPad.setControlsFromConfigPad(configFull.configPads[0], true, false);
			uiPad.setControlsFromConfigPos(configFull.configPos[0], true, false);
		}
	}
	
	private void saveSysexPad() {
		byte [] sysex = new byte[Constants.MD_SYSEX_PAD_SIZE];
		byte [] sysex3rd = new byte[Constants.MD_SYSEX_3RD_SIZE];
		byte [] sysexPos = new byte[Constants.MD_SYSEX_POS_SIZE];
		if (padPair > 0 ) {
			int leftInput = (padPair - 1)*2 + 1;
			int rightInput = leftInput + 1;
			byte [] sysexPad = new byte[Constants.MD_SYSEX_PAD_SIZE*2 + Constants.MD_SYSEX_3RD_SIZE + Constants.MD_SYSEX_POS_SIZE*2];
			sysex = configFull.configPads[leftInput].getSysexFromConfig();
			System.arraycopy(sysex, 0, sysexPad, 0, sysex.length);
			sysex = configFull.configPads[rightInput].getSysexFromConfig();
			System.arraycopy(sysex, 0, sysexPad, Constants.MD_SYSEX_PAD_SIZE, sysex.length);
			sysex3rd = configFull.config3rds[padPair - 1].getSysexFromConfig();
			System.arraycopy(sysex3rd, 0, sysexPad, Constants.MD_SYSEX_PAD_SIZE*2, sysex3rd.length);
			sysexPos = configFull.configPos[leftInput].getSysexFromConfig();
			System.arraycopy(sysexPos, 0, sysexPad, Constants.MD_SYSEX_PAD_SIZE*2 + Constants.MD_SYSEX_3RD_SIZE, sysexPos.length);
			sysexPos = configFull.configPos[rightInput].getSysexFromConfig();
			System.arraycopy(sysexPos, 0, sysexPad, Constants.MD_SYSEX_PAD_SIZE*2 + Constants.MD_SYSEX_3RD_SIZE  + Constants.MD_SYSEX_POS_SIZE, sysexPos.length);
			fileManager.saveSysex(sysexPad, configOptions);
		} else {
			byte [] sysexPad = new byte[Constants.MD_SYSEX_PAD_SIZE + Constants.MD_SYSEX_POS_SIZE];
			sysex = configFull.configPads[0].getSysexFromConfig();
			System.arraycopy(sysex, 0, sysexPad, 0, sysex.length);
			sysexPos = configFull.configPos[0].getSysexFromConfig();
			System.arraycopy(sysex, 0, sysexPad, Constants.MD_SYSEX_PAD_SIZE, sysex.length);
			fileManager.saveSysex(sysexPad, configOptions);
		}
	}

	private void loadSysexCurve() {
		byte [] sysex;
		sysex = configFull.configCurves[curvePointer].getSysexFromConfig();
		fileManager.loadSysex(sysex, configOptions);
		configFull.configCurves[curvePointer].setConfigFromSysex(sysex);
		uiPadsExtra.setYvalues(configFull.configCurves[curvePointer].yValues, false);
	}
	
	private void saveSysexCurve() {
		fileManager.saveSysex(configFull.configCurves[curvePointer].getSysexFromConfig(), configOptions);
	}
	
	private void loadSysexAllCustomNames() {
		byte [] sysex;
		byte [] sysexAll = new byte[Constants.MD_SYSEX_CUSTOM_NAME_SIZE * configFull.customNamesCount];
		fileManager.loadSysex(sysexAll, configOptions);					
		for (int i = 0; i < configFull.customNamesCount; i++) {
			sysex = new byte[Constants.MD_SYSEX_CUSTOM_NAME_SIZE];
			System.arraycopy(sysexAll, i*Constants.MD_SYSEX_CUSTOM_NAME_SIZE, sysex, 0, Constants.MD_SYSEX_CUSTOM_NAME_SIZE);
			configFull.configCustomNames[i].setConfigFromSysex(sysex);
			uiPadsExtra.setCustomName(configFull.configCustomNames[i], i, false);
		}
		addCustomNamesToPads();
	}
	
	private void saveSysexAllCustomNames() {
		byte [] sysexAll = new byte[Constants.MD_SYSEX_CUSTOM_NAME_SIZE * configFull.customNamesCount];
		for (int i = 0; i < configFull.customNamesCount; i++) {
			System.arraycopy(configFull.configCustomNames[i].getSysexFromConfig(), 0, sysexAll, i*Constants.MD_SYSEX_CUSTOM_NAME_SIZE, Constants.MD_SYSEX_CUSTOM_NAME_SIZE);
		}
		fileManager.saveSysex(sysexAll, configOptions);
	}
		
	private void save_all() {
		configOptions.lastConfig = uiGlobal.getComboBoxFile().getSelectionModel().getSelectedIndex();
		fileManager.save_all(configFull, configOptions);
		uiGlobal.getComboBoxFile().getItems().clear();
		uiGlobal.getComboBoxFile().getItems().addAll(configOptions.configFileNames);
		uiGlobal.getComboBoxFile().getSelectionModel().select(configOptions.lastConfig);
	}	

	private void addCustomNamesToPads() {
		String [] names;
		//names = new String[configFull.customNamesCount];
		names = new String[configFull.configCustomNames.length];
		//for (int i =0; i < configFull.customNamesCount; i++) {
		for (int i =0; i < configFull.configCustomNames.length; i++) {
			names[i] = configFull.configCustomNames[i].name;
		}
		uiPad.addAllCustomNames(names);
		updateComboBoxInput(true);
	}
	
	private void showAbout() {
		String aboutString;
		if (System.getProperty("os.name").startsWith("Mac")) {
			aboutString = Constants.HELP_ABOUT + Constants.HELP_ABOUT_MMJ;
		} else {
			aboutString = Constants.HELP_ABOUT;
		}
		Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("About MegaDrum Manager FX");
        WebView webView = new WebView();
        webView.getEngine().loadContent(aboutString);
        webView.setPrefSize(400, 220);
        alert.getDialogPane().setContent(webView);
        alert.showAndWait();

        Timer warning_timer = new Timer();
		warning_timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						//alert.showAndWait();
					}
				});
			}
		}, 500);

	}
}
