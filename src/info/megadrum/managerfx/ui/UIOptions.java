package info.megadrum.managerfx.ui;

import java.util.ArrayList;

import info.megadrum.managerfx.Controller;
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

	private UICheckBox	uiCheckBoxSaveOnExit;

	private ArrayList<UIControl> allMidiControls;
	private ArrayList<UIControl> allMiscControls;
	
	//private TabPane optionsTabs;
	//private Tab midiTab, miscTab;
	
	public UIOptions(Controller controller) {
        window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Options");
        window.setMinWidth(250);

        TabPane optionsTabs = new TabPane();
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
        uiCheckBoxSamePort = new UICheckBox("Use same In/Out");
        allMidiControls.add(uiCheckBoxSamePort);
        
        uiComboBoxMidiIn = new UIComboBox("MIDI In");
        allMidiControls.add(uiComboBoxMidiIn);
        
        uiComboBoxMidiOut = new UIComboBox("MIDI Out");
        allMidiControls.add(uiComboBoxMidiOut);

        uiComboBoxChainId = new UIComboBox("MegaDrum Chain Id");
        allMidiControls.add(uiComboBoxChainId);

        uiCheckBoxEnableMidiThru = new UICheckBox("Enable MIDI Thru");
        allMidiControls.add(uiCheckBoxEnableMidiThru);

        uiComboBoxMidiThru = new UIComboBox("MIDI Thru");
        allMidiControls.add(uiComboBoxMidiThru);

        uiCheckBoxInitPortsStartup = new UICheckBox("Init Ports on Startup");
        allMidiControls.add(uiCheckBoxInitPortsStartup);

        uiSpinnerSysexTimeout = new UISpinner("Sysex Timeout", 10, 100, 30, 1);
        allMidiControls.add(uiSpinnerSysexTimeout);

        for (int i = 0; i < allMidiControls.size(); i++) {
            midiLayout.getChildren().add(allMidiControls.get(i).getUI());
            allMidiControls.get(i).setColumnsSizes(150.0, 300.0);
            allMidiControls.get(i).setControlMinWidth(300.0);
        }
        
        buttonRescanPort = new Button("Rescan MIDI ports");
        midiLayout.getChildren().add(buttonRescanPort);
        midiLayout.setAlignment(Pos.TOP_CENTER);
        
    	uiCheckBoxSaveOnExit = new UICheckBox("Save Options on Exit");
        allMiscControls.add(uiCheckBoxSaveOnExit);

        for (int i = 0; i < allMiscControls.size(); i++) {
            miscLayout.getChildren().add(allMiscControls.get(i).getUI());        	
            allMiscControls.get(i).setColumnsSizes(150.0, 300.0);
            allMiscControls.get(i).setControlMinWidth(300.0);
        }
        miscLayout.setAlignment(Pos.TOP_CENTER);
        

        Button okButton = new Button("Ok");
        okButton.setOnAction(e -> okAndClose());

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> window.close());

        HBox okCloseButtonsLayout = new HBox();
        okCloseButtonsLayout.getChildren().addAll(okButton,cancelButton);
        okCloseButtonsLayout.setAlignment(Pos.CENTER_RIGHT);
        
        VBox layout = new VBox();
        layout.getChildren().add(optionsTabs);
        layout.getChildren().addAll(okCloseButtonsLayout);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        scene = new Scene(layout);
        window.setScene(scene);
	}
	
	public void show() {
        window.setResizable(false);
        window.showAndWait();
	}
	
	public void okAndClose() {
		// Tell something to controller here
		System.out.println("Applying options");
		window.close();
	}
}
