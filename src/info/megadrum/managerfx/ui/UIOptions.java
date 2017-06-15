package info.megadrum.managerfx.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;

import javax.swing.event.EventListenerList;

import info.megadrum.managerfx.Controller;
import info.megadrum.managerfx.data.ConfigOptions;
import info.megadrum.managerfx.midi.MidiEvent;
import info.megadrum.managerfx.midi.MidiEventListener;
import info.megadrum.managerfx.midi.MidiRescanEvent;
import info.megadrum.managerfx.midi.MidiRescanEventListener;
import info.megadrum.managerfx.utils.Constants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UIOptions {
	private Stage window;
	private Scene scene;
	private UICheckBox	uiCheckBoxSamePort;
	private UIComboBox	uiComboBoxMidiIn;
	private UIComboBox	uiComboBoxMidiOut;
	private UIComboBox	uiComboBoxChainId;
	private UICheckBox	uiCheckBoxEnableMidiThru;
	private UIComboBox	uiComboBoxMidiThru;
	private UICheckBox	uiCheckBoxInitPortsStartup;
	private UISpinner	uiSpinnerSysexTimeout;
	private Button 		buttonRescanPort;
	private HBox		okCloseButtonsLayout;
	private TabPane		optionsTabs;
	private VBox 		layout;

	private UICheckBox	uiCheckBoxSaveOnExit;
	
	private ConfigOptions	configOptions;
	private Boolean			closedWithOk;

	private ArrayList<UIControl> allMidiControls;
	private ArrayList<UIControl> allMiscControls;
	
	//private TabPane optionsTabs;
	//private Tab midiTab, miscTab;
	protected EventListenerList listenerList = new EventListenerList();
	
	public void addMidiRescanEventListener(MidiRescanEventListener listener) {
		listenerList.add(MidiRescanEventListener.class, listener);
	}
	public void removeMidiRescanEventListener(MidiRescanEventListener listener) {
		listenerList.remove(MidiRescanEventListener.class, listener);
	}
	protected void fireMidiRescanEvent(MidiRescanEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i = i+2) {
			if (listeners[i] == MidiRescanEventListener.class) {
				((MidiRescanEventListener) listeners[i+1]).midiRescanEventOccurred(evt);
			}
		}
	}
	
	public UIOptions(ConfigOptions config) {
		configOptions = config;
        window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Options");

        optionsTabs = new TabPane();
        optionsTabs.setTabMaxHeight(20);
        optionsTabs.setTabMinHeight(20);
        
        Tab midiTab = new Tab("MIDI");
        midiTab.setClosable(false);
        Tab miscTab = new Tab("Misc");
        miscTab.setClosable(false);
        optionsTabs.getTabs().addAll(midiTab,miscTab);
        VBox midiLayout = new VBox();
        VBox miscLayout = new VBox();
        
        midiTab.setContent(midiLayout);
        miscTab.setContent(miscLayout);
        
        allMidiControls = new ArrayList<UIControl>();
        allMiscControls = new ArrayList<UIControl>();
        uiCheckBoxSamePort = new UICheckBox("Use same In/Out", false);
        uiCheckBoxSamePort.setIgnoreSyncState();
        uiCheckBoxSamePort.addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	//checkBox.setSelected(!newValue);
				//System.out.printf("%s: new value = %s, old value = %s\n",label.getText(),(newValue ? "true" : "false"),(oldValue ? "true" : "false") );
		    	setSameMidiInOut(newValue);
		    }
		});
        allMidiControls.add(uiCheckBoxSamePort);
        
        uiComboBoxMidiIn = new UIComboBox("MIDI In", false);
        uiComboBoxMidiIn.setComboBoxWide(true);
        uiComboBoxMidiIn.addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				//System.out.printf("Old value = %s, new value = %s\n", oldValue, newValue);
				setSameMidiInOut(uiCheckBoxSamePort.uiCtlIsSelected());
			}
        });
        
        allMidiControls.add(uiComboBoxMidiIn);
        
        uiComboBoxMidiOut = new UIComboBox("MIDI Out", false);
        uiComboBoxMidiOut.setComboBoxWide(true);
        uiComboBoxMidiOut.addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				//System.out.printf("Old value = %s, new value = %s\n", oldValue, newValue);
			}
        });
        allMidiControls.add(uiComboBoxMidiOut);

        uiComboBoxChainId = new UIComboBox("MegaDrum Chain Id", false);
        allMidiControls.add(uiComboBoxChainId);

        uiCheckBoxEnableMidiThru = new UICheckBox("Enable MIDI Thru", false);
        uiCheckBoxEnableMidiThru.setIgnoreSyncState();
        allMidiControls.add(uiCheckBoxEnableMidiThru);

        uiComboBoxMidiThru = new UIComboBox("MIDI Thru", false);
        uiComboBoxMidiThru.setComboBoxWide(true);
        uiComboBoxMidiThru.addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				//System.out.printf("Old value = %s, new value = %s\n", oldValue, newValue);
			}
        });
        allMidiControls.add(uiComboBoxMidiThru);

        uiCheckBoxInitPortsStartup = new UICheckBox("Init Ports on Startup", false);
        uiCheckBoxInitPortsStartup.setIgnoreSyncState();
        allMidiControls.add(uiCheckBoxInitPortsStartup);

        uiSpinnerSysexTimeout = new UISpinner("Sysex Timeout", 10, 100, 30, 1, false);
        uiSpinnerSysexTimeout.setSpinnerType(Constants.FX_SPINNER_TYPE_SYSEX);
        allMidiControls.add(uiSpinnerSysexTimeout);

        for (int i = 0; i < allMidiControls.size(); i++) {
            midiLayout.getChildren().add(allMidiControls.get(i).getUI());
            //allMidiControls.get(i).setColumnsSizes(150.0, 300.0);
            //allMidiControls.get(i).setControlMinWidth(300.0);
        }
        
        buttonRescanPort = new Button("Rescan MIDI ports");
        buttonRescanPort.setOnAction(e-> fireMidiRescanEvent(new MidiRescanEvent(this)));
        midiLayout.getChildren().add(buttonRescanPort);
        midiLayout.setAlignment(Pos.TOP_CENTER);
        
    	uiCheckBoxSaveOnExit = new UICheckBox("Save Options on Exit", false);
    	uiCheckBoxSaveOnExit.setIgnoreSyncState();
        allMiscControls.add(uiCheckBoxSaveOnExit);

        for (int i = 0; i < allMiscControls.size(); i++) {
            miscLayout.getChildren().add(allMiscControls.get(i).getUI());        	
            //allMiscControls.get(i).setColumnsSizes(150.0, 300.0);
            //allMiscControls.get(i).setControlMinWidth(300.0);
        }
        miscLayout.setAlignment(Pos.TOP_CENTER);
        

        Button okButton = new Button("Ok");
        okButton.setOnAction(e -> okAndClose());

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> window.close());

        okCloseButtonsLayout = new HBox();
        okCloseButtonsLayout.getChildren().addAll(okButton,cancelButton);
        okCloseButtonsLayout.setAlignment(Pos.CENTER_RIGHT);
        
        layout = new VBox();
        layout.getChildren().add(optionsTabs);
        layout.getChildren().add(okCloseButtonsLayout);
        layout.setAlignment(Pos.TOP_CENTER);
        //okCloseButtonsLayout.setStyle("-fx-background-color: red");

        //Display window and wait for it to be closed before returning
        scene = new Scene(layout);
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
			respondToResize(scene.getWidth(), scene.getHeight());
		});

        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
			respondToResize(scene.getWidth(), scene.getHeight());
		});
        
        uiComboBoxChainId.uiCtlSetValuesArray(Arrays.asList("0", "1", "2", "3" ));
        window.setScene(scene);
		//layout.setMinWidth(400);
        window.setMinWidth(600);
        window.setMaxWidth(600);
        window.setMinHeight(350);
        window.setMaxHeight(350);
	}
	
	public void show() {
		//updateControls();
		closedWithOk = false;
        //window.setResizable(false);
        
        window.showAndWait();
	}

	public void okAndClose() {
		// Tell something to controller here
		//System.out.println("Applying options");
		configOptions.useSamePort = uiCheckBoxSamePort.uiCtlIsSelected();
		configOptions.useThruPort = uiCheckBoxEnableMidiThru.uiCtlIsSelected();
		configOptions.autoOpenPorts = uiCheckBoxInitPortsStartup.uiCtlIsSelected();
		configOptions.saveOnExit = uiCheckBoxSaveOnExit.uiCtlIsSelected();
		configOptions.sysexDelay = uiSpinnerSysexTimeout.uiCtlGetValue();
		configOptions.MidiInName = uiComboBoxMidiIn.uiCtlGetSelected();
		configOptions.MidiOutName = uiComboBoxMidiOut.uiCtlGetSelected();
		configOptions.MidiThruName = uiComboBoxMidiThru.uiCtlGetSelected();
		configOptions.chainId = Integer.valueOf(uiComboBoxChainId.uiCtlGetSelected());
		closedWithOk = true;
		window.close();
	}
	
	public boolean getClosedWithOk() {
		return closedWithOk;
	}
	
	public void setMidiInList(List<String> list) {
		uiComboBoxMidiIn.uiCtlSetValuesArray(list);
	}
	
	public void setMidiOutList(List<String> list) {
		uiComboBoxMidiOut.uiCtlSetValuesArray(list);
	}
	
	public void setMidiThruList(List<String> list) {
		uiComboBoxMidiThru.uiCtlSetValuesArray(list);
	}
	
	private void setSameMidiInOut(Boolean same) {
		if (same) {
			uiComboBoxMidiOut.uiCtlSetValue(uiComboBoxMidiIn.uiCtlGetSelected());		
			uiComboBoxMidiOut.uiCtlSetDisable(true);
		} else {
			uiComboBoxMidiOut.uiCtlSetValue(configOptions.MidiOutName);
			uiComboBoxMidiOut.uiCtlSetDisable(false);
		}		
	}
	
	public void updateControls() {
		uiCheckBoxSamePort.uiCtlSetValue(configOptions.useSamePort, true);
		uiCheckBoxEnableMidiThru.uiCtlSetValue(configOptions.useThruPort, true);
		uiCheckBoxInitPortsStartup.uiCtlSetValue(configOptions.autoOpenPorts, true);
		uiCheckBoxSaveOnExit.uiCtlSetValue(configOptions.saveOnExit, true);
		uiSpinnerSysexTimeout.uiCtlSetValue(configOptions.sysexDelay, true);
		uiComboBoxMidiIn.uiCtlSetValue(configOptions.MidiInName);
		setSameMidiInOut(configOptions.useSamePort);
		uiComboBoxMidiThru.uiCtlSetValue(configOptions.MidiThruName);
		uiComboBoxChainId.uiCtlSetValue(String.valueOf(configOptions.chainId));
	}

	public void respondToResize(Double w, Double h) {
		optionsTabs.setMinHeight(h - okCloseButtonsLayout.getHeight());
		optionsTabs.setMaxHeight(h - okCloseButtonsLayout.getHeight());
		optionsTabs.setMinWidth(w);
		optionsTabs.setMaxWidth(w);
		//optionsTabs.setStyle("-fx-background-color: red");
		//Double tabHeight = optionsTabs.getTabMaxWidth();
		Double tabViewHeight = optionsTabs.getHeight() - optionsTabs.getTabMaxHeight();
		//System.out.printf("h = %f\n", tabViewHeight.doubleValue());
		for (int i = 0; i < allMidiControls.size(); i++) {
			allMidiControls.get(i).respondToResize(w*0.85, h*0.09);
        }
		for (int i = 0; i < allMiscControls.size(); i++) {
			allMiscControls.get(i).respondToResize(w*0.85, h*0.09);
        }

	}
}
