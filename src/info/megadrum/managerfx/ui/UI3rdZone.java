package info.megadrum.managerfx.ui;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;

import info.megadrum.managerfx.data.Config3rd;
import info.megadrum.managerfx.data.ConfigPad;
import info.megadrum.managerfx.utils.Constants;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class UI3rdZone implements PanelInterface {
	private MdTitledPane	titledPane;
	private Pane			paneAll;
	
	private UICheckBox		uiCheckBoxDisabled;
	private UISpinnerNote 	uiSpinnerNoteMainNote;
	private UISpinnerNote 	uiSpinnerNoteAltNote;
	private UISpinnerNote 	uiSpinnerNotePressNote;
	private UISpinnerNote 	uiSpinnerNoteDampenedNote;
	private UISlider		uiSliderMidpoint;
	private UISpinner 		uiSpinnerMidpointWidth;
	private UISpinner 		uiSpinnerThreshold;
		
	private ArrayList<UIControl> allControls;
	
	static private final	Boolean zoneFromPizeo	= false;
	//static private final	Boolean zoneFromSwitch 	= true;
	private boolean			zoneType = zoneFromPizeo;
	private Double			lastTitleHeight = 5.0;
	private Boolean			showAdvanced = true;
	private int 			verticalControlsCount = 0;
	private int 			verticalControlsCountWithoutAdvanced = 0;

	private Boolean			copyPressed = false;
	private int				copyPressedValueId = -1;

	protected EventListenerList listenerList = new EventListenerList();
	
	public void addControlChangeEventListener(ControlChangeEventListener listener) {
		listenerList.add(ControlChangeEventListener.class, listener);
	}
	public void removeControlChangeEventListener(ControlChangeEventListener listener) {
		listenerList.remove(ControlChangeEventListener.class, listener);
	}
	protected void fireControlChangeEvent(ControlChangeEvent evt, Integer parameter) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i = i+2) {
			if (listeners[i] == ControlChangeEventListener.class) {
				((ControlChangeEventListener) listeners[i+1]).controlChangeEventOccurred(evt, parameter);
			}
		}
	}

	public UI3rdZone() {
		allControls = new ArrayList<UIControl>();

		paneAll = new Pane();
		paneAll.setLayoutX(0);

		uiCheckBoxDisabled = new UICheckBox("Disabled", false);
		allControls.add(uiCheckBoxDisabled);
		
		uiSliderMidpoint = new UISlider("Midpoint", 0, 15, 7, true);
		allControls.add(uiSliderMidpoint);
		
		uiSpinnerNoteMainNote = new UISpinnerNote("Note", true);
		uiSpinnerNoteMainNote.setNoteIsMain(true);
		//uiSpinnerNoteMainNote.setDisabledNoteAllowed(true);
		allControls.add(uiSpinnerNoteMainNote);
				
		uiSpinnerThreshold = new UISpinner("Threshold", 0, 255, 0, 1, true);
		allControls.add(uiSpinnerThreshold);

		uiSpinnerNoteAltNote = new UISpinnerNote("AltNote", true, true);
		uiSpinnerNoteAltNote.setAdvancedSetting(true);
		allControls.add(uiSpinnerNoteAltNote);

		uiSpinnerMidpointWidth = new UISpinner("MidpointWidth", 0, 15, 0, 1, true);
		uiSpinnerMidpointWidth.setAdvancedSetting(true);
		allControls.add(uiSpinnerMidpointWidth);
		
		uiSpinnerNotePressNote = new UISpinnerNote("PressrollNote", true, true);
		uiSpinnerNotePressNote.setAdvancedSetting(true);
		allControls.add(uiSpinnerNotePressNote);

		uiSpinnerNoteDampenedNote = new UISpinnerNote("DampenedNote", true);
		uiSpinnerNoteDampenedNote.setAdvancedSetting(true);
		allControls.add(uiSpinnerNoteDampenedNote);
	
		verticalControlsCount = 4;
		verticalControlsCountWithoutAdvanced = 2;
		for (int i = 0; i < allControls.size(); i++) {
			final int iFinal = i;
        	allControls.get(i).setValueId(i + Constants.THIRD_ZONE_VALUE_ID_MIN);
        	allControls.get(i).setLabelWidthMultiplier(Constants.FX_INPUT_LABEL_WIDTH_MUL);        	
        	allControls.get(i).addControlChangeEventListener(new ControlChangeEventListener() {
				
				@Override
				public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
					if (allControls.get(iFinal).isCopyPressed()) {
						allControls.get(iFinal).resetCopyPressed();
						copyPressedValueId = allControls.get(iFinal).getValueId();
						copyPressed = true;
						fireControlChangeEvent(new ControlChangeEvent(this), parameter);
					} else {
						if (parameter == Constants.CONTROL_CHANGE_EVENT_NOTE_LINKED) {
							linkedNoteStateChanged();
						} else {
							if (parameter == Constants.CONTROL_CHANGE_EVENT_NOTE_MAIN) {
								linkedNoteStateChanged();
							}
							setMidPointAndThreshold();
							fireControlChangeEvent(new ControlChangeEvent(this), parameter);
						}
					}
				}
			});
        }
		
		titledPane = new MdTitledPane();
		titledPane.setText("3rd Zone");
		titledPane.getChildren().add(paneAll);
		setZoneType(zoneFromPizeo);
		reAddAllControls();
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
	
	public void setShowAdvanced(Boolean show) {
		showAdvanced = show;
		reAddAllControls();
	}
	
	public int getCopyPressedValueId() {
		return copyPressedValueId;
	}
	
	public Boolean isCopyPressed() {
		return copyPressed;
	}
	
	public void resetCopyPressed() {
		copyPressed = false;
	}

	public void setAllStateUnknown() {
		for (int i = 0; i < allControls.size(); i++ ) {
			allControls.get(i).setSyncState(Constants.SYNC_STATE_UNKNOWN);
		}
	}

	public void setAllStateNotSynced() {
		for (int i = 0; i < allControls.size(); i++ ) {
			allControls.get(i).setSyncState(Constants.SYNC_STATE_NOT_SYNCED);
		}
	}

	public Node getUI() {
		return (Node) titledPane;
	}

	public void respondToResize(Double w, Double h, Double controlW, Double controlH) {
		Double titledPaneFontHeight = controlH*30*Constants.FX_SUB_TITLEBARS_FONT_SCALE;
		if (titledPaneFontHeight < Constants.FX_TITLEBARS_FONT_MIN_SIZE) {
			titledPaneFontHeight = Constants.FX_TITLEBARS_FONT_MIN_SIZE;
		}
		titledPane.setFont(new Font(titledPaneFontHeight));
		titledPane.setTitleHeight(controlH);
		lastTitleHeight = controlH;
		paneAll.setLayoutY(lastTitleHeight);
		paneAll.setMaxWidth(w);
		titledPane.setWidth(w);
		Double xPos, yPos;
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
				xPos = (visibleControlsCount&1)*controlW*Constants.FX_INPUT_CONTROL_WIDTH_MUL + controlW*0.01 + (visibleControlsCount&1)*controlW*0.05;
				allControls.get(i).getUI().setLayoutX(xPos);
				yPos = (visibleControlsCount/2)*controlH + controlH*0.2;
				allControls.get(i).getUI().setLayoutY(yPos);
				allControls.get(i).respondToResize(controlW*Constants.FX_INPUT_CONTROL_WIDTH_MUL, controlH);
			}
        }
	}
	
	private void setMidPointAndWidthFromThreshold(int threshold, Boolean setFromSysex) {
		uiSliderMidpoint.uiCtlSetValue((threshold&0xf0)>>4, setFromSysex);
		uiSpinnerMidpointWidth.uiCtlSetValue(threshold&0x0f, setFromSysex);
	}
	
	public void setMdValuesFromConfig3rd(Config3rd config) {
		uiCheckBoxDisabled.uiCtlSetMdValue(config.disabled);
		uiSpinnerNoteMainNote.uiCtlSetMdValue(config.note);
		uiSpinnerNoteAltNote.uiCtlSetMdValue(config.altNote);
		uiSpinnerNotePressNote.uiCtlSetMdValue(config.pressrollNote);
		uiSpinnerNoteDampenedNote.uiCtlSetMdValue(config.dampenedNote);
		uiSpinnerThreshold.uiCtlSetMdValue(config.threshold);		
		uiSliderMidpoint.uiCtlSetMdValue((config.threshold&0xf0)>>4);
		uiSpinnerMidpointWidth.uiCtlSetMdValue(config.threshold&0x0f);
	}

	private void linkedNoteStateChanged() {
		if (uiSpinnerNoteAltNote.getLinked() || (!showAdvanced)) {
			uiSpinnerNoteAltNote.uiCtlSetValue(uiSpinnerNoteMainNote.uiCtlGetValue(), false);
		}
		uiSpinnerNoteAltNote.setLinked(uiSpinnerNoteAltNote.getLinked());
		if (uiSpinnerNotePressNote.getLinked() || (!showAdvanced)) {
			uiSpinnerNotePressNote.uiCtlSetValue(uiSpinnerNoteMainNote.uiCtlGetValue(), false);
		}
		uiSpinnerNotePressNote.setLinked(uiSpinnerNotePressNote.getLinked());
	}
	
	public void setControlsFromConfig3rd(Config3rd config, Boolean setFromSysex) {
		uiCheckBoxDisabled.uiCtlSetValue(config.disabled, setFromSysex);
		uiSpinnerNoteMainNote.uiCtlSetValue(config.note, setFromSysex);
		if (config.altNote_linked || (!showAdvanced)) {
			uiSpinnerNoteAltNote.uiCtlSetValue(config.note, setFromSysex);
		} else {
			uiSpinnerNoteAltNote.uiCtlSetValue(config.altNote, setFromSysex);			
		}
		uiSpinnerNoteAltNote.setLinked(config.altNote_linked);		
		if (config.pressrollNote_linked || (!showAdvanced)) {
			uiSpinnerNotePressNote.uiCtlSetValue(config.note, setFromSysex);
		} else {
			uiSpinnerNotePressNote.uiCtlSetValue(config.pressrollNote, setFromSysex);			
		}
		uiSpinnerNotePressNote.setLinked(config.pressrollNote_linked);
		uiSpinnerNoteDampenedNote.uiCtlSetValue(config.dampenedNote, setFromSysex);
		uiSpinnerThreshold.uiCtlSetValue(config.threshold, setFromSysex);
		setMidPointAndWidthFromThreshold(config.threshold, setFromSysex);
	}
	
	public void setConfig3rdFromControls(Config3rd config) {
		config.disabled = uiCheckBoxDisabled.uiCtlIsSelected();
		config.note = uiSpinnerNoteMainNote.uiCtlGetValue();
		if (uiSpinnerNoteAltNote.getLinked()) {
			config.altNote = uiSpinnerNoteMainNote.uiCtlGetValue();
		} else {
			config.altNote = uiSpinnerNoteAltNote.uiCtlGetValue();			
		}
		config.altNote_linked = uiSpinnerNoteAltNote.getLinked();
		if (uiSpinnerNotePressNote.getLinked()) {
			config.pressrollNote = uiSpinnerNoteMainNote.uiCtlGetValue();
		} else {
			config.pressrollNote = uiSpinnerNotePressNote.uiCtlGetValue();			
		}
		config.pressrollNote_linked = uiSpinnerNotePressNote.getLinked();
		config.dampenedNote = uiSpinnerNoteDampenedNote.uiCtlGetValue();
		config.threshold = uiSpinnerThreshold.uiCtlGetValue();
	}
	
	public void setZoneType(Boolean zoneSwitchType) {
		zoneType = zoneSwitchType;
		if (zoneSwitchType) {
			uiSliderMidpoint.uiCtlSetDisable(true);
			uiSpinnerMidpointWidth.uiCtlSetDisable(true);
			uiSpinnerThreshold.uiCtlSetDisable(false);
		} else {
			uiSliderMidpoint.uiCtlSetDisable(false);
			uiSpinnerMidpointWidth.uiCtlSetDisable(false);
			uiSpinnerThreshold.uiCtlSetDisable(true);			
		}
	}
	
	private void setMidPointAndThreshold() {
		if (zoneType) {
			uiSliderMidpoint.uiCtlSetValue((uiSpinnerThreshold.uiCtlGetValue()&0xf0)>>4, false);
			uiSpinnerMidpointWidth.uiCtlSetValue(uiSpinnerThreshold.uiCtlGetValue()&0x0f, false);
		} else {
			uiSpinnerThreshold.uiCtlSetValue((uiSliderMidpoint.uiCtlGetValue()<<4) + uiSpinnerMidpointWidth.uiCtlGetValue(), false);
		}
	}
	
	public int getVerticalControlsCount() {
		if (showAdvanced) {
			return verticalControlsCount + 1;
		} else {
			return verticalControlsCountWithoutAdvanced + 1;
		}
	}
}
