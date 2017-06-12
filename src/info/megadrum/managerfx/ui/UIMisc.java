package info.megadrum.managerfx.ui;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;

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
import javafx.scene.text.Font;

public class UIMisc extends UIPanel {
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
		
	protected EventListenerList listenerList = new EventListenerList();
	
	public void addControlChangeEventListener(ControlChangeEventListener listener) {
		listenerList.add(ControlChangeEventListener.class, listener);
	}
	public void removeControlChangeEventListener(ControlChangeEventListener listener) {
		listenerList.remove(ControlChangeEventListener.class, listener);
	}
	protected void fireControlChangeEvent(ControlChangeEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i = i+2) {
			if (listeners[i] == ControlChangeEventListener.class) {
				((ControlChangeEventListener) listeners[i+1]).controlChangeEventOccurred(evt, 0);
			}
		}
	}
	
	public UIMisc(String title) {
		super(title);
		allControls = new ArrayList<UIControl>();
		toolBar = new ToolBar();
		toolBar.getItems().add(buttonGet);
		toolBar.getItems().add(buttonSend);
		toolBar.getItems().add(new Separator());
		toolBar.getItems().add(buttonLoad);
		toolBar.getItems().add(buttonSave);
		toolBar.setStyle("-fx-padding: 0.1em 0.0em 0.2em 0.01em");


		vBoxAll.getChildren().add(toolBar);

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
	
		vBoxAll.getChildren().add(paneAll);
		for (int i = 0; i < allControls.size(); i++) {
        	//vBoxAll.getChildren().add(allControls.get(i).getUI());
			paneAll.getChildren().add(allControls.get(i).getUI());
        	allControls.get(i).setLabelWidthMultiplier(Constants.FX_MISC_LABEL_WIDTH_MUL);
        	allControls.get(i).addControlChangeEventListener(new ControlChangeEventListener() {
				
				@Override
				public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
					// TODO Auto-generated method stub
					fireControlChangeEvent(new ControlChangeEvent(this));
				}
			});
        }

		setDetached(false);
		setAllStateUnknown();
	}

	public void setAllStateUnknown() {
		for (int i = 0; i < allControls.size(); i++ ) {
			allControls.get(i).setSyncState(Constants.SYNC_STATE_UNKNOWN);
		}
	}
	
	public void respondToResizeDetached(Double w, Double h) {
		Double controlW = w/Constants.FX_MISC_CONTROL_WIDTH_MUL;
		Double controlH = (h/((allControls.size() + 1)))*1.00;
		respondToResize(w, h, h*1.6, controlW, controlH);
	}
	
	public void respondToResize(Double w, Double h, Double fullHeight, Double controlW, Double controlH) {
		Font buttonFont;
		Double toolBarFontHeight = fullHeight*Constants.FX_TOOLBARS_FONT_SCALE;
		Double titledPaneFontHeight = fullHeight*Constants.FX_TITLEBARS_FONT_SCALE;
		if (titledPaneFontHeight > Constants.FX_TITLEBARS_FONT_MIN_SIZE) {
			buttonFont = new Font(toolBarFontHeight);
		} else {
			buttonFont = new Font(Constants.FX_TOOLBARS_FONT_MIN_SIZE);
			titledPaneFontHeight =Constants.FX_TITLEBARS_FONT_MIN_SIZE;
		}
		titledPane.setFont(new Font(titledPaneFontHeight));
		buttonGet.setFont(buttonFont);
		buttonSend.setFont(buttonFont);
		buttonLoad.setFont(buttonFont);
		buttonSave.setFont(buttonFont);
		//System.out.printf("Misc ControlW = %f\n", controlW);
		paneAll.setMinWidth(controlW*Constants.FX_MISC_CONTROL_WIDTH_MUL);
		paneAll.setMaxWidth(controlW*Constants.FX_MISC_CONTROL_WIDTH_MUL);		
		for (int i = 0; i < allControls.size(); i++) {
			allControls.get(i).respondToResize(controlW*Constants.FX_MISC_CONTROL_WIDTH_MUL, controlH);
			allControls.get(i).getUI().setLayoutX(0);
			allControls.get(i).getUI().setLayoutY(i*controlH);
        }
	}

	public void setControlsFromConfig(ConfigMisc config, Boolean setFromSysex) {
		uiSpinnerNoteOffDelay.uiCtlSetValue(config.getNoteOff()*10, setFromSysex);
		uiSpinnerPressrollTimeout.uiCtlSetValue(config.pressroll, setFromSysex);
		uiSpinnerLatency.uiCtlSetValue(config.latency, setFromSysex);
		uiSpinnerNotesOctaveShift.uiCtlSetValue(config.octave_shift, setFromSysex);
		uiCheckBoxBigVUmeter.uiCtlSetValue(config.big_vu_meter, setFromSysex);
		uiCheckBoxBigVUsplit.uiCtlSetValue(config.big_vu_split, setFromSysex);
		uiCheckBoxBigVUQuickAccess.uiCtlSetValue(config.quick_access, setFromSysex);
		uiCheckBoxAltFalseTrSupp.uiCtlSetValue(config.alt_false_tr_supp, setFromSysex);
		uiCheckBoxInputsPriority.uiCtlSetValue(config.inputs_priority, setFromSysex);
		uiCheckBoxUnknownSetting.uiCtlSetValue(config.all_gains_low, setFromSysex);
		uiCheckBoxMIDIThru.uiCtlSetValue(config.midi_thru, setFromSysex);
		uiCheckBoxSendTriggeredIn.uiCtlSetValue(config.send_triggered_in, setFromSysex);
		uiCheckBoxAltNoteChoking.uiCtlSetValue(config.alt_note_choking, setFromSysex);
	}
		
	public void setConfigFromControls(ConfigMisc config) {
		config.setNoteOff(uiSpinnerNoteOffDelay.uiCtlGetValue()/10);
		config.pressroll = uiSpinnerPressrollTimeout.uiCtlGetValue();
		config.latency = uiSpinnerLatency.uiCtlGetValue();
		config.octave_shift = uiSpinnerNotesOctaveShift.uiCtlGetValue();
		config.big_vu_meter = uiCheckBoxBigVUmeter.uiCtlIsSelected();
		config.big_vu_split = uiCheckBoxBigVUsplit.uiCtlIsSelected();
		config.quick_access = uiCheckBoxBigVUQuickAccess.uiCtlIsSelected();
		config.alt_false_tr_supp = uiCheckBoxAltFalseTrSupp.uiCtlIsSelected();
		config.inputs_priority= uiCheckBoxInputsPriority.uiCtlIsSelected();
		config.all_gains_low = uiCheckBoxUnknownSetting.uiCtlIsSelected();
		config.midi_thru = uiCheckBoxMIDIThru.uiCtlIsSelected();
		config.send_triggered_in = uiCheckBoxSendTriggeredIn.uiCtlIsSelected();
		config.alt_note_choking = uiCheckBoxAltNoteChoking.uiCtlIsSelected();
	}

}
