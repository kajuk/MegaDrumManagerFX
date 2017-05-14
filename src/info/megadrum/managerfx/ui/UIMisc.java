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
		layout.getChildren().add(uiSpinnerNoteOffDelay.getUI());
		allControls.add(uiSpinnerNoteOffDelay);
		
		uiSpinnerPressrollTimeout = new UISpinner("Pressroll Timeout", 0, 2000, 10, 10);
		layout.getChildren().add(uiSpinnerPressrollTimeout.getUI());
		allControls.add(uiSpinnerPressrollTimeout);
		
		uiSpinnerLatencytency = new UISpinner("Latency", 10, 100, 15, 1);
		layout.getChildren().add(uiSpinnerLatencytency.getUI());
		allControls.add(uiSpinnerLatencytency);
		
		uiSpinnerNotesOctaveShift = new UISpinner("Notes Octave Shift", 0, 2, 2, 1);
		layout.getChildren().add(uiSpinnerNotesOctaveShift.getUI());
		allControls.add(uiSpinnerNotesOctaveShift);

		uiCheckBoxBigVUmeter = new UICheckBox("Big VU meter");
		layout.getChildren().add(uiCheckBoxBigVUmeter.getUI());
		allControls.add(uiCheckBoxBigVUmeter);

		uiCheckBoxBigVUsplit = new UICheckBox("Big VU split");
		layout.getChildren().add(uiCheckBoxBigVUsplit.getUI());
		allControls.add(uiCheckBoxBigVUsplit);

		uiCheckBoxBigVUQuickAccess = new UICheckBox("Quick Access");
		layout.getChildren().add(uiCheckBoxBigVUQuickAccess.getUI());
		allControls.add(uiCheckBoxBigVUQuickAccess);
		
		uiCheckBoxAltFalseTrSupp = new UICheckBox("AltFalseTrSupp");
		layout.getChildren().add(uiCheckBoxAltFalseTrSupp.getUI());
		allControls.add(uiCheckBoxAltFalseTrSupp);

		uiCheckBoxInputsPriority = new UICheckBox("Inputs Priority");
		layout.getChildren().add(uiCheckBoxInputsPriority.getUI());
		allControls.add(uiCheckBoxInputsPriority);

		uiCheckBoxUnknownSetting = new UICheckBox("Unknown");
		layout.getChildren().add(uiCheckBoxUnknownSetting.getUI());
		allControls.add(uiCheckBoxUnknownSetting);

		uiCheckBoxMIDIThru = new UICheckBox("MIDI Thru");
		layout.getChildren().add(uiCheckBoxMIDIThru.getUI());
		allControls.add(uiCheckBoxMIDIThru);

		uiCheckBoxSendTriggeredIn = new UICheckBox("Send TriggeredIn");
		layout.getChildren().add(uiCheckBoxSendTriggeredIn.getUI());
		allControls.add(uiCheckBoxSendTriggeredIn);

		uiCheckBoxAltNoteChoking = new UICheckBox("AltNote Choking");
		layout.getChildren().add(uiCheckBoxAltNoteChoking.getUI());
		allControls.add(uiCheckBoxAltNoteChoking);
		
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
