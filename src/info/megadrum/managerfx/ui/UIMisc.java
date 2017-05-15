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

		uiSpinnerNoteOffDelay = new UISpinner("Note Off Delay", 20, 2000, 200, 20, true);
		allControls.add(uiSpinnerNoteOffDelay);
		
		uiSpinnerPressrollTimeout = new UISpinner("Pressroll Timeout", 0, 2000, 10, 10, true);
		allControls.add(uiSpinnerPressrollTimeout);
		
		uiSpinnerLatencytency = new UISpinner("Latency", 10, 100, 15, 1, true);
		allControls.add(uiSpinnerLatencytency);
		
		uiSpinnerNotesOctaveShift = new UISpinner("Notes Octave Shift", 0, 2, 2, 1, true);
		allControls.add(uiSpinnerNotesOctaveShift);

		uiCheckBoxBigVUmeter = new UICheckBox("Big VU meter", true);
		allControls.add(uiCheckBoxBigVUmeter);

		uiCheckBoxBigVUsplit = new UICheckBox("Big VU split", true);
		allControls.add(uiCheckBoxBigVUsplit);

		uiCheckBoxBigVUQuickAccess = new UICheckBox("Quick Access", true);
		allControls.add(uiCheckBoxBigVUQuickAccess);
		
		uiCheckBoxAltFalseTrSupp = new UICheckBox("AltFalseTrSupp", true);
		allControls.add(uiCheckBoxAltFalseTrSupp);

		uiCheckBoxInputsPriority = new UICheckBox("Inputs Priority", true);
		allControls.add(uiCheckBoxInputsPriority);

		uiCheckBoxUnknownSetting = new UICheckBox("Unknown", true);
		allControls.add(uiCheckBoxUnknownSetting);

		uiCheckBoxMIDIThru = new UICheckBox("MIDI Thru", true);
		allControls.add(uiCheckBoxMIDIThru);

		uiCheckBoxSendTriggeredIn = new UICheckBox("Send TriggeredIn", true);
		allControls.add(uiCheckBoxSendTriggeredIn);

		uiCheckBoxAltNoteChoking = new UICheckBox("AltNote Choking", true);
		allControls.add(uiCheckBoxAltNoteChoking);

		uiSpinnerNoteTest = new UISpinnerNote("Note Test", true);
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

	public void respondToResize(Double h, Double w) {
		layout.setMaxHeight(h);
		layout.setMaxWidth(w);
		//System.out.println("Responding to scene resize in UIMisc");
		for (int i = 0; i < allControls.size(); i++) {
			allControls.get(i).respondToResize((h - toolBar.getHeight())/allControls.size(), w);
        }

	}
}
