package info.megadrum.managerfx.ui;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;

import info.megadrum.managerfx.data.ConfigMisc;
import info.megadrum.managerfx.utils.Constants;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class UIMisc extends UIPanel implements PanelInterface{
	private HBox toolBar;
	
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
	private Tooltip   tooltipUnused;
	private Tooltip   tooltipAllGainsLow;
	private Tooltip   tooltipAltSampling;
	private Tooltip   tooltipUnknown;
		
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
/*		toolBar = new ToolBar();
		toolBar.getItems().add(buttonGet);
		toolBar.getItems().add(buttonSend);
		toolBar.getItems().add(new Separator());
		toolBar.getItems().add(buttonLoad);
		toolBar.getItems().add(buttonSave);
*/
		toolBar = new HBox();
		toolBar.setAlignment(Pos.CENTER_LEFT);
		toolBar.getChildren().addAll(buttonGet, buttonSend, new Separator(Orientation.VERTICAL), buttonLoad, buttonSave);
		toolBar.setStyle("-fx-padding: 0.1em 0.0em 0.2em 0.01em");


		vBoxAll.getChildren().add(toolBar);

		uiSpinnerNoteOffDelay = new UISpinner("Note Off Delay", 20, 2000, 200, 20, false);
		allControls.add(uiSpinnerNoteOffDelay);
		
		uiSpinnerPressrollTimeout = new UISpinner("Pressroll Timeout", 0, 2000, 10, 10, false);
		uiSpinnerPressrollTimeout.setAdvancedSetting(true);
		allControls.add(uiSpinnerPressrollTimeout);
		
		uiSpinnerLatency = new UISpinner("Latency", 10, 100, 15, 1, false);
		//uiSpinnerLatency.setAdvancedSetting(true);
		allControls.add(uiSpinnerLatency);
		
		uiSpinnerNotesOctaveShift = new UISpinner("Notes Octave Shift", 0, 2, 2, 1, false);
		//uiSpinnerNotesOctaveShift.setAdvancedSetting(true);
		allControls.add(uiSpinnerNotesOctaveShift);

		uiCheckBoxBigVUmeter = new UICheckBox("Big VU meter", false);
		uiCheckBoxBigVUmeter.setAdvancedSetting(true);
		allControls.add(uiCheckBoxBigVUmeter);

		uiCheckBoxBigVUsplit = new UICheckBox("Big VU split", false);
		uiCheckBoxBigVUsplit.setAdvancedSetting(true);
		allControls.add(uiCheckBoxBigVUsplit);

		uiCheckBoxBigVUQuickAccess = new UICheckBox("Quick Access", false);
		uiCheckBoxBigVUQuickAccess.setAdvancedSetting(true);
		allControls.add(uiCheckBoxBigVUQuickAccess);
		
		uiCheckBoxAltFalseTrSupp = new UICheckBox("AltFalseTrSupp", false);
		//uiCheckBoxAltFalseTrSupp.setAdvancedSetting(true);
		allControls.add(uiCheckBoxAltFalseTrSupp);

		uiCheckBoxInputsPriority = new UICheckBox("Inputs Priority", false);
		uiCheckBoxInputsPriority.setAdvancedSetting(true);
		allControls.add(uiCheckBoxInputsPriority);

		uiCheckBoxUnknownSetting = new UICheckBox("Unknown", false);
		uiCheckBoxUnknownSetting.setAdvancedSetting(true);
		allControls.add(uiCheckBoxUnknownSetting);
		tooltipUnused = new Tooltip("This setting is unused on this MegaDrum hardware version");
		tooltipAllGainsLow = new Tooltip("When enabled, it will disable all individual input Gain levels\nand make Gain even lower than 'Gain Level' 0.\nIt could be used if all your pads are 'hot' and to get\na better dynamic range with such pads.");
		tooltipAltSampling = new Tooltip("When enabled, MegaDrum uses a new sampling algorithm\nwhich can reduce signal noise\nand improve sensitivity.");
		tooltipUnknown = new Tooltip("This setting function depends on the type of MegaDrum MCU");

		uiCheckBoxMIDIThru = new UICheckBox("MIDI Thru", false);
		uiCheckBoxMIDIThru.setAdvancedSetting(true);
		allControls.add(uiCheckBoxMIDIThru);

		uiCheckBoxSendTriggeredIn = new UICheckBox("Send TriggeredIn", false);
		//uiCheckBoxSendTriggeredIn.setAdvancedSetting(true);
		allControls.add(uiCheckBoxSendTriggeredIn);

		uiCheckBoxAltNoteChoking = new UICheckBox("AltNote Chokng", false);
		uiCheckBoxAltNoteChoking.setAdvancedSetting(true);
		allControls.add(uiCheckBoxAltNoteChoking);
	
		vBoxAll.getChildren().add(paneAll);
		for (int i = 0; i < allControls.size(); i++) {
			verticalControlsCount++;
			if (!allControls.get(i).isAdvancedSetting()) {
				verticalControlsCountWithoutAdvanced++;
			}
        	//vBoxAll.getChildren().add(allControls.get(i).getUI());
			//paneAll.getChildren().add(allControls.get(i).getUI());
        	allControls.get(i).setLabelWidthMultiplier(Constants.FX_MISC_LABEL_WIDTH_MUL);
        	allControls.get(i).addControlChangeEventListener(new ControlChangeEventListener() {
				
				@Override
				public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
					fireControlChangeEvent(new ControlChangeEvent(this));
				}
			});
        }

		reAddAllControls();
		setDetached(false);
		setAllStateUnknown();
	}

	private void reAddAllControls() {
		paneAll.getChildren().clear();
		for (int i = 0; i < allControls.size(); i++) {
			if (allControls.get(i).isAdvancedSetting()) {
				if (showAdvanced) {
					paneAll.getChildren().add(allControls.get(i).getUI());
				}				
			} else {
				paneAll.getChildren().add(allControls.get(i).getUI());
			}
        }
		
	}
	
	@Override
	public void setShowAdvanced(Boolean show) {
		showAdvanced = show;
		reAddAllControls();
	}
	
	public void setAllStateUnknown() {
		for (int i = 0; i < allControls.size(); i++ ) {
			allControls.get(i).setSyncState(Constants.SYNC_STATE_UNKNOWN);
		}
		uiCheckBoxUnknownSetting.setLabelText("Unknown");
		uiCheckBoxUnknownSetting.setControlTooltip(tooltipUnknown);

	}
	
	public void respondToResizeDetached() {
		Double w = windowDetached.getScene().getWidth();
		Double h = windowDetached.getScene().getHeight();
		Double controlW = w/Constants.FX_MISC_CONTROL_WIDTH_MUL;
		int visibleControls;
		if (showAdvanced) {
			visibleControls = verticalControlsCount;
		} else {
			visibleControls = verticalControlsCountWithoutAdvanced;
		}
		Double controlH = (h/((visibleControls + 1)))*1.00;
		respondToResize(w, h, controlW, controlH);
	}
	
	public void respondToResize(Double w, Double h, Double cW, Double cH) {
		super.respondToResize(w, h, cW, cH);
		titledPane.setWidth(controlW*Constants.FX_MISC_CONTROL_WIDTH_MUL);
		vBoxAll.setLayoutY(lastTitleHeight);
		vBoxAll.setMaxWidth(controlW*1.0*Constants.FX_MISC_CONTROL_WIDTH_MUL);
		toolBar.setMaxWidth(controlW*1.0*Constants.FX_MISC_CONTROL_WIDTH_MUL);
		toolBar.setMinWidth(controlW*1.0*Constants.FX_MISC_CONTROL_WIDTH_MUL);
		toolBar.setMaxHeight(controlH);
		//toolBar.setStyle("-fx-background-color: orange");
		//vBoxAll.setStyle("-fx-background-color: lightgreen");
		//System.out.printf("Button font height = %f\n", buttonFont.getSize());
		Double buttonFontSize = controlH*0.31;
		toolBar.setStyle("-fx-font-size: " + buttonFontSize.toString() + "pt");
		//System.out.printf("Misc ControlW = %f\n", controlW);
		paneAll.setMinWidth(controlW*Constants.FX_MISC_CONTROL_WIDTH_MUL);
		paneAll.setMaxWidth(controlW*Constants.FX_MISC_CONTROL_WIDTH_MUL);
		int visibleControlsCount = -1;
		boolean showControl;
		for (int i = 0; i < allControls.size(); i++) {
			showControl = false;
			if (allControls.get(i).isAdvancedSetting()) {
				if (showAdvanced) {
					visibleControlsCount++;
					showControl = true;
				}				
			} else {
				visibleControlsCount++;
				showControl = true;
			}
			if (showControl) {
				allControls.get(i).respondToResize(controlW*Constants.FX_MISC_CONTROL_WIDTH_MUL, controlH);
				allControls.get(i).getUI().setLayoutX(0);
				allControls.get(i).getUI().setLayoutY(visibleControlsCount*controlH);				
			}
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
	
	@Override
	public int getVerticalControlsCount() {
		if (showAdvanced) {
			return verticalControlsCount + 2;
		} else {
			return verticalControlsCountWithoutAdvanced + 2;
		}
	}
	
	public void setUnknownLabel(int mcuType) {
		if (mcuType < 3) {
			uiCheckBoxUnknownSetting.setLabelText("All Gains Low");
			uiCheckBoxUnknownSetting.setControlTooltip(tooltipAllGainsLow);
		} else {
			if (mcuType < 6) {
				uiCheckBoxUnknownSetting.setLabelText("Unused");
				uiCheckBoxUnknownSetting.setControlTooltip(tooltipUnused);
			} else {
				uiCheckBoxUnknownSetting.setLabelText("Alt Sampling Alg");
				uiCheckBoxUnknownSetting.setControlTooltip(tooltipAltSampling);
			}
		}
	}
}
