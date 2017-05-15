package info.megadrum.managerfx.ui;

import java.util.ArrayList;

import info.megadrum.managerfx.utils.Constants;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;

public class UIMisc {
//	private VBox layout;
	private VBox layout;
	private Button buttonGet;
	private Button buttonSend;
	private Button buttonLoad;
	private Button buttonSave;
	private ToolBar toolBar;
	
	private UISpinner uiSpinnerNoteOffDelay;
	private UISpinner uiSpinnerPressrollTimeout;
	private UISpinner uiSpinnerLatencytency;
	private UISpinner uiSpinnerNotesOctaveShift;
	private UICheckBox uiCheckBoxBigVUmeter;
	private UICheckBox uiCheckBoxBigVUsplit;
	private UICheckBox uiCheckBoxBigVUQuickAccess;
	private UICheckBox uiCheckBoxAltFalseTrSupp;
	private UICheckBox uiCheckBoxInputsPriority;
	private UICheckBox uiCheckBoxUnknownSetting;
	private UICheckBox uiCheckBoxMIDIThru;
	private UICheckBox uiCheckBoxSendTriggeredIn;
	private UICheckBox uiCheckBoxAltNoteChoking;
	private UISpinnerNote uiSpinnerNoteTest;
	private ArrayList<UIControl> allControls;
	
	public UIMisc() {
		allControls = new ArrayList<UIControl>();
		buttonGet = new Button("Get");
		buttonSend = new Button("Send");
		buttonLoad = new Button("Load");
		buttonSave = new Button("Save");
//		toolBar = new ToolBar( buttonGet,buttonSend,new Separator(), buttonLoad,buttonSave);
		toolBar = new ToolBar();
		toolBar.getItems().add(buttonGet);
		toolBar.getItems().add(buttonSend);
		toolBar.getItems().add(new Separator());
		toolBar.getItems().add(buttonLoad);
		toolBar.getItems().add(buttonSave);

		layout = new VBox();
		layout.getChildren().add(toolBar);

		uiSpinnerNoteOffDelay = new UISpinner("Note Off Delay", 20, 2000, 200, 20);
		allControls.add(uiSpinnerNoteOffDelay);
		
		uiSpinnerPressrollTimeout = new UISpinner("Pressroll Timeout", 0, 2000, 10, 10);
		allControls.add(uiSpinnerPressrollTimeout);
		
		uiSpinnerLatencytency = new UISpinner("Latency", 10, 100, 15, 1);
		allControls.add(uiSpinnerLatencytency);
		
		uiSpinnerNotesOctaveShift = new UISpinner("Notes Octave Shift", 0, 2, 2, 1);
		allControls.add(uiSpinnerNotesOctaveShift);

		uiCheckBoxBigVUmeter = new UICheckBox("Big VU meter");
		allControls.add(uiCheckBoxBigVUmeter);

		uiCheckBoxBigVUsplit = new UICheckBox("Big VU split");
		allControls.add(uiCheckBoxBigVUsplit);

		uiCheckBoxBigVUQuickAccess = new UICheckBox("Quick Access");
		allControls.add(uiCheckBoxBigVUQuickAccess);
		
		uiCheckBoxAltFalseTrSupp = new UICheckBox("AltFalseTrSupp");
		allControls.add(uiCheckBoxAltFalseTrSupp);

		uiCheckBoxInputsPriority = new UICheckBox("Inputs Priority");
		allControls.add(uiCheckBoxInputsPriority);

		uiCheckBoxUnknownSetting = new UICheckBox("Unknown");
		allControls.add(uiCheckBoxUnknownSetting);

		uiCheckBoxMIDIThru = new UICheckBox("MIDI Thru");
		allControls.add(uiCheckBoxMIDIThru);

		uiCheckBoxSendTriggeredIn = new UICheckBox("Send TriggeredIn");
		allControls.add(uiCheckBoxSendTriggeredIn);

		uiCheckBoxAltNoteChoking = new UICheckBox("AltNote Choking");
		allControls.add(uiCheckBoxAltNoteChoking);

		uiSpinnerNoteTest = new UISpinnerNote("Note Test");
		allControls.add(uiSpinnerNoteTest);
	
		for (int i = 0; i < allControls.size(); i++) {
        	layout.getChildren().add(allControls.get(i).getUI());
        }
		
		setAllStateUnknown();
	}

	private void setAllStateUnknown() {
		for (int i = 0; i < allControls.size(); i++ ) {
			allControls.get(i).setSyncState(Constants.SYNC_STATE_UNKNOWN);
		}
	}
	
	public Node getUI() {
		return (Node) layout;
	}
}
