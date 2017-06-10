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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class UI3rdZone {
//	private VBox layout;
	//private GridPane layout;
	private TitledPane 		titledPane;
	
	private UISpinnerNote 	uiSpinnerNoteMainNote;
	private UISpinnerNote 	uiSpinnerNoteAltNote;
	private UISpinnerNote 	uiSpinnerNotePressNote;
	private UISpinnerNote 	uiSpinnerNoteDampenedNote;
	private UISlider		uiSliderMidpoint;
	private UISpinner 		uiSpinnerMidpointWidth;
	private UISpinner 		uiSpinnerThreshold;
		
	private ArrayList<UIControl> allControls;
	private ArrayList<Integer> gridColmn;
	private ArrayList<Integer> gridRow;
	
	static private final	Boolean zoneFromPizeo	= false;
	static private final	Boolean zoneFromSwitch 	= true;
	private boolean			zoneType = zoneFromPizeo;

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
		gridColmn = new ArrayList<Integer>();
		gridRow = new ArrayList<Integer>();

		GridPane layout = new GridPane();
		layout.setHgap(15);

		uiSpinnerNoteMainNote = new UISpinnerNote("Note", true);
		uiSpinnerNoteMainNote.setNoteIsMain(true);
		uiSpinnerNoteMainNote.setDisabledNoteAllowed(true);
		allControls.add(uiSpinnerNoteMainNote);
		gridColmn.add(0);
		gridRow.add(0);
				
		uiSpinnerNoteAltNote = new UISpinnerNote("AltNote", true, true);
		allControls.add(uiSpinnerNoteAltNote);
		gridColmn.add(0);
		gridRow.add(1);

		uiSpinnerNotePressNote = new UISpinnerNote("PressrollNote", true, true);
		allControls.add(uiSpinnerNotePressNote);
		gridColmn.add(0);
		gridRow.add(2);

		uiSpinnerNoteDampenedNote = new UISpinnerNote("DampenedNote", true);
		allControls.add(uiSpinnerNoteDampenedNote);
		gridColmn.add(0);
		gridRow.add(3);

		uiSliderMidpoint = new UISlider("Midpoint", 0, 15, 7, true);
		allControls.add(uiSliderMidpoint);
		gridColmn.add(1);
		gridRow.add(0);
		
		uiSpinnerMidpointWidth = new UISpinner("MidpointWidth", 0, 15, 0, 1, true);
		allControls.add(uiSpinnerMidpointWidth);
		gridColmn.add(1);
		gridRow.add(1);
		
		uiSpinnerThreshold = new UISpinner("Threshold", 0, 255, 0, 1, true);
		allControls.add(uiSpinnerThreshold);
		gridColmn.add(1);
		gridRow.add(2);

	
		for (int i = 0; i < allControls.size(); i++) {
			final int iFinal = i;
			GridPane.setConstraints(allControls.get(i).getUI(), gridColmn.get(i), gridRow.get(i));
			GridPane.setHalignment(allControls.get(i).getUI(), HPos.LEFT);
			GridPane.setValignment(allControls.get(i).getUI(), VPos.CENTER);
        	layout.getChildren().add(allControls.get(i).getUI());
        	allControls.get(i).setValueId(i + Constants.THIRD_ZONE_VALUE_ID_MIN);
        	allControls.get(i).setLabelWidthMultiplier(Constants.FX_INPUT_LABEL_WIDTH_MUL);        	
        	allControls.get(i).addControlChangeEventListener(new ControlChangeEventListener() {
				
				@Override
				public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
					// TODO Auto-generated method stub
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
		
		titledPane = new TitledPane();
		titledPane.setText("3rd Zone");
		titledPane.setContent(layout);
		titledPane.setCollapsible(false);
		setZoneType(zoneFromPizeo);
		setAllStateUnknown();
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

	public void respondToResize(Double w, Double h, Double fullHeight, Double controlW, Double controlH) {
		Double titledPaneFontHeight = fullHeight*Constants.FX_SUB_TITLEBARS_FONT_SCALE;
		if (titledPaneFontHeight < Constants.FX_TITLEBARS_FONT_MIN_SIZE) {
			titledPaneFontHeight = Constants.FX_TITLEBARS_FONT_MIN_SIZE;
		}
		//titledPane.setFont(new Font(titledPaneFontHeight));
		//Changing title font dynamically makes TitledPane header height different
		// between left and right panels
		// Use static size for now in UI3rdZone as well
		titledPane.setFont(new Font(9.0));
		for (int i = 0; i < allControls.size(); i++) {
			allControls.get(i).respondToResize(controlW*Constants.FX_INPUT_CONTROL_WIDTH_MUL, controlH);
        }
		//titledPane.setMinHeight(h);
		//titledPane.setMaxHeight(h);
	}
	
	private void setMidPointAndWidthFromThreshold(int threshold, Boolean setFromSysex) {
		uiSliderMidpoint.uiCtlSetValue((threshold&0xf0)>>4, setFromSysex);
		uiSpinnerMidpointWidth.uiCtlSetValue(threshold&0x0f, setFromSysex);
	}
	
	public void setMdValuesFromConfig3rd(Config3rd config) {
		uiSpinnerNoteMainNote.uiCtlSetMdValue(config.note);
		uiSpinnerNoteAltNote.uiCtlSetMdValue(config.altNote);
		uiSpinnerNotePressNote.uiCtlSetMdValue(config.pressrollNote);
		uiSpinnerNoteDampenedNote.uiCtlSetMdValue(config.dampenedNote);
		uiSpinnerThreshold.uiCtlSetMdValue(config.threshold);		
		uiSliderMidpoint.uiCtlSetMdValue((config.threshold&0xf0)>>4);
		uiSpinnerMidpointWidth.uiCtlSetMdValue(config.threshold&0x0f);
	}

	private void linkedNoteStateChanged() {
		if (uiSpinnerNoteAltNote.getLinked()) {
			uiSpinnerNoteAltNote.uiCtlSetValue(uiSpinnerNoteMainNote.uiCtlGetValue(), false);
		}
		uiSpinnerNoteAltNote.setLinked(uiSpinnerNoteAltNote.getLinked());
		if (uiSpinnerNotePressNote.getLinked()) {
			uiSpinnerNotePressNote.uiCtlSetValue(uiSpinnerNoteMainNote.uiCtlGetValue(), false);
		}
		uiSpinnerNotePressNote.setLinked(uiSpinnerNotePressNote.getLinked());
	}
	
	public void setControlsFromConfig3rd(Config3rd config, Boolean setFromSysex) {
		uiSpinnerNoteMainNote.uiCtlSetValue(config.note, setFromSysex);
		if (config.altNote_linked) {
			uiSpinnerNoteAltNote.uiCtlSetValue(config.note, setFromSysex);
		} else {
			uiSpinnerNoteAltNote.uiCtlSetValue(config.altNote, setFromSysex);			
		}
		uiSpinnerNoteAltNote.setLinked(config.altNote_linked);		
		if (config.pressrollNote_linked) {
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
		return gridRow.size();
	}
}
