package info.megadrum.managerfx.ui;

import java.util.ArrayList;

import info.megadrum.managerfx.data.ConfigMisc;
import info.megadrum.managerfx.utils.Constants;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class UIMisc {
//	private VBox layout;
	private TitledPane titledPane;
	private Button buttonGet;
	private Button buttonSend;
	private Button buttonLoad;
	private Button buttonSave;
	private ToolBar toolBar;
	
	private UISpinner uiSpinnerNoteOffDelay;
	private UISpinner uiSpinnerPressrollTimeout;
	private UISpinner uiSpinnerLatency;
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
	
	public UIMisc(String title) {
		
		allControls = new ArrayList<UIControl>();
		buttonGet = new Button("Get");
		buttonSend = new Button("Send");
		buttonLoad = new Button("Load");
		buttonSave = new Button("Save");
		toolBar = new ToolBar();
		toolBar.getItems().add(buttonGet);
		toolBar.getItems().add(buttonSend);
		toolBar.getItems().add(new Separator());
		toolBar.getItems().add(buttonLoad);
		toolBar.getItems().add(buttonSave);


		VBox layout = new VBox();
		layout.getChildren().add(toolBar);
		layout.setStyle("-fx-padding: 0.0em 0.2em 0.0em 0.2em");

		uiSpinnerNoteOffDelay = new UISpinner("Note Off Delay", 20, 2000, 200, 20, false);
		allControls.add(uiSpinnerNoteOffDelay);
		
		uiSpinnerPressrollTimeout = new UISpinner("Pressroll Timeout", 0, 2000, 10, 10, false);
		allControls.add(uiSpinnerPressrollTimeout);
		
		uiSpinnerLatency = new UISpinner("Latency", 10, 100, 15, 1, false);
		allControls.add(uiSpinnerLatency);
		
		uiSpinnerNotesOctaveShift = new UISpinner("Notes Octave Shift", 0, 2, 2, 1, false);
		allControls.add(uiSpinnerNotesOctaveShift);

		uiCheckBoxBigVUmeter = new UICheckBox("Big VU meter", false);
		allControls.add(uiCheckBoxBigVUmeter);

		uiCheckBoxBigVUsplit = new UICheckBox("Big VU split", false);
		allControls.add(uiCheckBoxBigVUsplit);

		uiCheckBoxBigVUQuickAccess = new UICheckBox("Quick Access", false);
		allControls.add(uiCheckBoxBigVUQuickAccess);
		
		uiCheckBoxAltFalseTrSupp = new UICheckBox("AltFalseTrSupp", false);
		allControls.add(uiCheckBoxAltFalseTrSupp);

		uiCheckBoxInputsPriority = new UICheckBox("Inputs Priority", false);
		allControls.add(uiCheckBoxInputsPriority);

		uiCheckBoxUnknownSetting = new UICheckBox("Unknown", false);
		allControls.add(uiCheckBoxUnknownSetting);

		uiCheckBoxMIDIThru = new UICheckBox("MIDI Thru", false);
		allControls.add(uiCheckBoxMIDIThru);

		uiCheckBoxSendTriggeredIn = new UICheckBox("Send TriggeredIn", false);
		allControls.add(uiCheckBoxSendTriggeredIn);

		uiCheckBoxAltNoteChoking = new UICheckBox("AltNote Chokng", false);
		allControls.add(uiCheckBoxAltNoteChoking);
	
		for (int i = 0; i < allControls.size(); i++) {
        	layout.getChildren().add(allControls.get(i).getUI());
        	allControls.get(i).setLabelWidthMultiplier(Constants.FX_MISC_LABEL_WIDTH_MUL);        	
        }

		titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(layout);
		titledPane.setCollapsible(false);
		titledPane.setAlignment(Pos.CENTER);
		setAllStateUnknown();
	}

	private void setAllStateUnknown() {
		for (int i = 0; i < allControls.size(); i++ ) {
			allControls.get(i).setSyncState(Constants.SYNC_STATE_UNKNOWN);
		}
	}
	
	public Node getUI() {
		return (Node) titledPane;
	}

	public void respondToResize(Double h, Double w, Double fullHeight, Double controlH, Double controlW) {
		
		Double toolBarFontHeight = fullHeight*Constants.FX_TITLEBARS_FONT_SCALE;
		Double titledPaneFontHeight = toolBarFontHeight*1.4;
		if (toolBarFontHeight > Constants.FX_TITLEBARS_FONT_MIN_SIZE) {
			//System.out.printf("ToolBar font size = %f\n",fontHeight);
			toolBar.setStyle("-fx-font-size: " + toolBarFontHeight.toString() + "pt");			
			titledPane.setStyle("-fx-font-size: " + titledPaneFontHeight.toString() + "pt");			
		}
		//titledPane.setMaxHeight(h);
		toolBar.setStyle("-fx-padding: 0.0em 0.0em 0.2em 0.0em");
		//System.out.printf("Misc ControlW = %f\n", controlW);
		for (int i = 0; i < allControls.size(); i++) {
			//allControls.get(i).respondToResize((h - toolBar.getHeight())/allControls.size(), w);
			allControls.get(i).respondToResize(controlH, controlW*Constants.FX_MISC_CONTROL_WIDTH_MUL);
        }
		toolBar.setMinWidth(controlW*0.99);
		toolBar.setMaxWidth(controlW*0.99);
		titledPane.setMinWidth(controlW*Constants.FX_MISC_CONTROL_WIDTH_MUL);
		titledPane.setMaxWidth(controlW*Constants.FX_MISC_CONTROL_WIDTH_MUL);
	}
	
	public Button getButtonSend() {
		return buttonSend;
	}

	public Button getButtonGet() {
		return buttonGet;
	}
	
	public void setControlsFromConfig(ConfigMisc config) {
		uiSpinnerNoteOffDelay.uiCtlSetValue(config.getNoteOff()*10);
		uiSpinnerPressrollTimeout.uiCtlSetValue(config.pressroll);
		uiSpinnerLatency.uiCtlSetValue(config.latency);
		uiSpinnerNotesOctaveShift.uiCtlSetValue(config.octave_shift);
		uiCheckBoxBigVUmeter.uiCtlSetSelected(config.big_vu_meter);
		uiCheckBoxBigVUsplit.uiCtlSetSelected(config.big_vu_split);
		uiCheckBoxBigVUQuickAccess.uiCtlSetSelected(config.quick_access);
		uiCheckBoxAltFalseTrSupp.uiCtlSetSelected(config.alt_false_tr_supp);
		uiCheckBoxInputsPriority.uiCtlSetSelected(config.inputs_priority);
		uiCheckBoxUnknownSetting.uiCtlSetSelected(config.all_gains_low);
		uiCheckBoxMIDIThru.uiCtlSetSelected(config.midi_thru);
		uiCheckBoxSendTriggeredIn.uiCtlSetSelected(config.send_triggered_in);
		uiCheckBoxAltNoteChoking.uiCtlSetSelected(config.alt_note_choking);
	}
		
}
