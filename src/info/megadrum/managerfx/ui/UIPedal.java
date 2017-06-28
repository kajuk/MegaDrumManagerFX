package info.megadrum.managerfx.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.event.EventListenerList;

import info.megadrum.managerfx.data.ConfigMisc;
import info.megadrum.managerfx.data.ConfigPedal;
import info.megadrum.managerfx.utils.Constants;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class UIPedal extends UIPanel implements PanelInterface {
	private HBox		toolBar;
	private TabPane		tabPane;
	private Tab 		tabMisc;
	private Tab 		tabLevels;
	private Tab 		tabNotes;
	private Pane 		paneMisc;
	private Pane 		paneLevels;
	private Pane 		paneNotes;

	private UIComboBox	uiComboBoxMiscType;
	private UIComboBox	uiComboBoxMiscCurve;
	private UIComboBox	uiComboBoxMiscChickCurve;
	private UIComboBox	uiComboBoxMiscHiHatInput;
	private UICheckBox	uiCheckBoxMiscAltInput;
	private UICheckBox	uiCheckBoxMiscReversLevels;
	private UICheckBox	uiCheckBoxMiscSoftChicks;
	private UICheckBox	uiCheckBoxMiscAutoLevels;
	private UICheckBox	uiCheckBoxMiscAlgorithm;
	private UISpinner	uiSpinnerMiscChickDelay;
	private UISpinner	uiSpinnerMiscCCNumber;
	private UISpinner	uiSpinnerMiscCCReduction;
	
	private UISpinner	uiSpinnerLevelsLow;
	private UISpinner	uiSpinnerLevelsHigh;
	private UISpinner	uiSpinnerLevelsOpen;
	private UISpinner	uiSpinnerLevelsSemiOpen;
	private UISpinner	uiSpinnerLevelsSemiOpen2;
	private UISpinner	uiSpinnerLevelsHalfOpen;
	private UISpinner	uiSpinnerLevelsHalfOpen2;
	private UISpinner	uiSpinnerLevelsClosed;
	private UISpinner	uiSpinnerLevelsChickThresh;
	private UISpinner	uiSpinnerLevelsShortChickThresh;
	private UISpinner	uiSpinnerLevelsLongChickThresh;
	private UISpinner	uiSpinnerLevelsChickMinVel;
	private UISpinner	uiSpinnerLevelsChickMaxVel;
	private UISpinner	uiSpinnerLevelsChickDeadPeriod;

	private UISpinnerNote	uiSpinnerNoteBowSemiOpen;
	private UISpinnerNote	uiSpinnerNoteEdgeSemiOpen;
	private UISpinnerNote	uiSpinnerNoteBellSemiOpen;
	private UISpinnerNote	uiSpinnerNoteBowSemiOpen2;
	private UISpinnerNote	uiSpinnerNoteEdgeSemiOpen2;
	private UISpinnerNote	uiSpinnerNoteBellSemiOpen2;
	
	private UISpinnerNote	uiSpinnerNoteBowHalfOpen;
	private UISpinnerNote	uiSpinnerNoteEdgeHalfOpen;
	private UISpinnerNote	uiSpinnerNoteBellHalfOpen;
	private UISpinnerNote	uiSpinnerNoteBowHalfOpen2;
	private UISpinnerNote	uiSpinnerNoteEdgeHalfOpen2;
	private UISpinnerNote	uiSpinnerNoteBellHalfOpen2;
	
	private UISpinnerNote	uiSpinnerNoteBowSemiClosed;
	private UISpinnerNote	uiSpinnerNoteEdgeSemiClosed;
	private UISpinnerNote	uiSpinnerNoteBellSemiClosed;
	private UISpinnerNote	uiSpinnerNoteBowClosed;
	private UISpinnerNote	uiSpinnerNoteEdgeClosed;
	private UISpinnerNote	uiSpinnerNoteBellClosed;
	
	private UISpinnerNote	uiSpinnerNoteChick;
	private UISpinnerNote	uiSpinnerNoteSplash;
	
	private ArrayList<UIControl> allMiscControls;
	private ArrayList<UIControl> allLevelsControls;
	private ArrayList<UIControl> allNotesControls;
	private List<String>	listHiHatInputs;
	
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
	
	public UIPedal(String title) {
		super(title);
		listHiHatInputs = new ArrayList<String>(Arrays.asList("2", "4", "6", "8"));
		allMiscControls = new ArrayList<UIControl>();
		allLevelsControls = new ArrayList<UIControl>();
		allNotesControls = new ArrayList<UIControl>();

		toolBar = new HBox();
		toolBar.setAlignment(Pos.CENTER_LEFT);
		toolBar.getChildren().addAll(buttonGet, buttonSend, new Separator(Orientation.VERTICAL), buttonLoad, buttonSave);
		toolBar.setStyle("-fx-padding: 0.1em 0.0em 0.2em 0.01em");


		vBoxAll.getChildren().add(toolBar);

		tabPane = new TabPane();
		//tabPane.setTabMaxHeight(20);
		//tabPane.setTabMinHeight(20);
		vBoxAll.getChildren().add(tabPane);

        tabMisc = new Tab("Misc");
        tabMisc.setClosable(false);
        tabLevels = new Tab("Levels");
        tabLevels.setClosable(false);
        tabNotes = new Tab("Notes");
        tabNotes.setClosable(false);
        tabPane.getTabs().addAll(tabMisc,tabLevels,tabNotes);
        paneMisc = new Pane();
        paneLevels = new Pane();
        paneNotes = new Pane();
        

        tabMisc.setContent(paneMisc);
        tabLevels.setContent(paneLevels);
        tabNotes.setContent(paneNotes);

        uiComboBoxMiscType = new UIComboBox("Type", false);
        uiComboBoxMiscType.uiCtlSetValuesArray(Arrays.asList(Constants.PEDAL_TYPES_LIST));
        uiComboBoxMiscType.uiCtlSetValue(0, false);
        allMiscControls.add(uiComboBoxMiscType);

        uiComboBoxMiscCurve = new UIComboBox("Curve", false);
        uiComboBoxMiscCurve.uiCtlSetValuesArray(Arrays.asList(Constants.CURVES_LIST));
        uiComboBoxMiscCurve.uiCtlSetValue(0, false);
        allMiscControls.add(uiComboBoxMiscCurve);

        uiComboBoxMiscChickCurve = new UIComboBox("Chick Curve", false);
        uiComboBoxMiscChickCurve.setAdvancedSetting(true);
        uiComboBoxMiscChickCurve.uiCtlSetValuesArray(Arrays.asList(Constants.CURVES_LIST));
        uiComboBoxMiscChickCurve.uiCtlSetValue(0, false);
        allMiscControls.add(uiComboBoxMiscChickCurve);
        
        uiComboBoxMiscHiHatInput = new UIComboBox("HiHat Input", false);
        uiComboBoxMiscHiHatInput.setAdvancedSetting(true);
        uiComboBoxMiscHiHatInput.uiCtlSetValuesArray(listHiHatInputs);
        uiComboBoxMiscHiHatInput.uiCtlSetValue(0, false);
        allMiscControls.add(uiComboBoxMiscHiHatInput);

        uiCheckBoxMiscAltInput = new UICheckBox("Alt Input", false);
        uiComboBoxMiscChickCurve.setAdvancedSetting(true);
        allMiscControls.add(uiCheckBoxMiscAltInput);
        
        uiCheckBoxMiscReversLevels = new UICheckBox("Reverse Levels", false);
        uiCheckBoxMiscReversLevels.setAdvancedSetting(true);
        allMiscControls.add(uiCheckBoxMiscReversLevels);
        
        uiCheckBoxMiscSoftChicks = new UICheckBox("Soft Chicks", false);
        allMiscControls.add(uiCheckBoxMiscSoftChicks);
        
        uiCheckBoxMiscAutoLevels = new UICheckBox("Auto Levels", false);
        allMiscControls.add(uiCheckBoxMiscAutoLevels);
        
        uiCheckBoxMiscAlgorithm = new UICheckBox("New Algorithm", false);
        uiCheckBoxMiscAlgorithm.setAdvancedSetting(true);
        allMiscControls.add(uiCheckBoxMiscAlgorithm);
        
        uiSpinnerMiscChickDelay = new UISpinner("Chick Delay", 0, 127, 0, 1, false);
        allMiscControls.add(uiSpinnerMiscChickDelay);
        
        uiSpinnerMiscCCNumber = new UISpinner("CC Number", 0, 127, 4, 1, false);
        allMiscControls.add(uiSpinnerMiscCCNumber);
        
        uiSpinnerMiscCCReduction = new UISpinner("CC Reduction Lvl", 0, 3, 1, 1, false);
        allMiscControls.add(uiSpinnerMiscCCReduction);
			
		for (int i = 0; i < allMiscControls.size(); i++) {
        	//vBoxMisc.getChildren().add(allMiscControls.get(i).getUI());
        	allMiscControls.get(i).setLabelWidthMultiplier(Constants.FX_PEDAL_LABEL_WIDTH_MUL);        	
        	allMiscControls.get(i).addControlChangeEventListener(new ControlChangeEventListener() {
				
				@Override
				public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
					fireControlChangeEvent(new ControlChangeEvent(this));					
				}
			});
		}
		
		uiSpinnerLevelsLow = new UISpinner("Low", 0, 1023, 16, 1, false);
        allLevelsControls.add(uiSpinnerLevelsLow);
        
        uiSpinnerLevelsHigh = new UISpinner("High", 0, 1023, 600, 1, false);
        allLevelsControls.add(uiSpinnerLevelsHigh);
        
        uiSpinnerLevelsOpen = new UISpinner("Open", 0, 127, 8, 1, false);
        allLevelsControls.add(uiSpinnerLevelsOpen);
        
        uiSpinnerLevelsSemiOpen = new UISpinner("SemiOpen", 0, 127, 40, 1, false);
        allLevelsControls.add(uiSpinnerLevelsSemiOpen);
        
        uiSpinnerLevelsSemiOpen2 = new UISpinner("SemiOpen2", 0, 127, 40, 1, false);
        allLevelsControls.add(uiSpinnerLevelsSemiOpen2);
        
        uiSpinnerLevelsHalfOpen = new UISpinner("HalfOpen", 0, 127, 70, 1, false);
        allLevelsControls.add(uiSpinnerLevelsHalfOpen);
        
        uiSpinnerLevelsHalfOpen2 = new UISpinner("HalfOpen2", 0, 127, 70, 1, false);
        allLevelsControls.add(uiSpinnerLevelsHalfOpen2);
        
        uiSpinnerLevelsClosed = new UISpinner("Closed", 0, 127, 100, 1, false);
        allLevelsControls.add(uiSpinnerLevelsClosed);
        
        uiSpinnerLevelsChickThresh = new UISpinner("ChickThresh", 0, 127, 120, 1, false);
        allLevelsControls.add(uiSpinnerLevelsChickThresh);
        
        uiSpinnerLevelsShortChickThresh = new UISpinner("SoftChickThresh", 0, 127, 115, 1, false);
        allLevelsControls.add(uiSpinnerLevelsShortChickThresh);
        
        uiSpinnerLevelsLongChickThresh = new UISpinner("LongChickThresh", 0, 127, 16, 1, false);
        allLevelsControls.add(uiSpinnerLevelsLongChickThresh);
        
        uiSpinnerLevelsChickMinVel = new UISpinner("Chick Min Velocity", 0, 1023, 400, 1, false);
        uiSpinnerLevelsChickMinVel.setAdvancedSetting(true);
        allLevelsControls.add(uiSpinnerLevelsChickMinVel);
        
        uiSpinnerLevelsChickMaxVel = new UISpinner("Chick Max Velocity", 0, 1023, 90, 1, false);
        uiSpinnerLevelsChickMaxVel.setAdvancedSetting(true);
        allLevelsControls.add(uiSpinnerLevelsChickMaxVel);
        
        uiSpinnerLevelsChickDeadPeriod = new UISpinner("Chick Dead Period", 0, 1023, 90, 1, false);
        uiSpinnerLevelsChickDeadPeriod.setAdvancedSetting(true);
        allLevelsControls.add(uiSpinnerLevelsChickDeadPeriod);
				
		for (int i = 0; i < allLevelsControls.size(); i++) {
        	//vBoxLevels.getChildren().add(allLevelsControls.get(i).getUI());
        	allLevelsControls.get(i).setLabelWidthMultiplier(Constants.FX_PEDAL_LABEL_WIDTH_MUL);        	
        	allLevelsControls.get(i).addControlChangeEventListener(new ControlChangeEventListener() {
				
				@Override
				public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
					fireControlChangeEvent(new ControlChangeEvent(this));
				}
			});
        }

		uiSpinnerNoteBowSemiOpen = new UISpinnerNote("Bow SemiOpen", false);
        allNotesControls.add(uiSpinnerNoteBowSemiOpen);
        
        uiSpinnerNoteEdgeSemiOpen = new UISpinnerNote("Edge SemiOpen", false);
        //uiSpinnerNoteEdgeSemiOpen.setAdvancedSetting(true);
        allNotesControls.add(uiSpinnerNoteEdgeSemiOpen);
        
        uiSpinnerNoteBellSemiOpen = new UISpinnerNote("Bell SemiOpen", false);
        //uiSpinnerNoteBellSemiOpen.setAdvancedSetting(true);
        allNotesControls.add(uiSpinnerNoteBellSemiOpen);

		uiSpinnerNoteBowSemiOpen2 = new UISpinnerNote("Bow SemiOpen2", false);
        allNotesControls.add(uiSpinnerNoteBowSemiOpen2);
        
        uiSpinnerNoteEdgeSemiOpen2 = new UISpinnerNote("Edge SemiOpen2", false);
        //uiSpinnerNoteEdgeSemiOpen2.setAdvancedSetting(true);
        allNotesControls.add(uiSpinnerNoteEdgeSemiOpen2);
        
        uiSpinnerNoteBellSemiOpen2 = new UISpinnerNote("Bell SemiOpen2", false);
        //uiSpinnerNoteBellSemiOpen2.setAdvancedSetting(true);
        allNotesControls.add(uiSpinnerNoteBellSemiOpen2);

		uiSpinnerNoteBowHalfOpen = new UISpinnerNote("Bow HalfOpen", false);
        allNotesControls.add(uiSpinnerNoteBowHalfOpen);
        
        uiSpinnerNoteEdgeHalfOpen = new UISpinnerNote("Edge HalfOpen", false);
        //uiSpinnerNoteEdgeHalfOpen.setAdvancedSetting(true);
        allNotesControls.add(uiSpinnerNoteEdgeHalfOpen);
        
        uiSpinnerNoteBellHalfOpen = new UISpinnerNote("Bell HalfOpen", false);
        //uiSpinnerNoteBellHalfOpen.setAdvancedSetting(true);
        allNotesControls.add(uiSpinnerNoteBellHalfOpen);
        
		uiSpinnerNoteBowHalfOpen2 = new UISpinnerNote("Bow HalfOpen2", false);
        allNotesControls.add(uiSpinnerNoteBowHalfOpen2);
        
        uiSpinnerNoteEdgeHalfOpen2 = new UISpinnerNote("Edge HalfOpen2", false);
        //uiSpinnerNoteEdgeHalfOpen2.setAdvancedSetting(true);
        allNotesControls.add(uiSpinnerNoteEdgeHalfOpen2);
        
        uiSpinnerNoteBellHalfOpen2 = new UISpinnerNote("Bell HalfOpen2", false);
        //uiSpinnerNoteBellHalfOpen2.setAdvancedSetting(true);
        allNotesControls.add(uiSpinnerNoteBellHalfOpen2);
		
        uiSpinnerNoteBowSemiClosed = new UISpinnerNote("Bow SemiClosed", false);
        allNotesControls.add(uiSpinnerNoteBowSemiClosed);
        
        uiSpinnerNoteEdgeSemiClosed = new UISpinnerNote("Edge SemiClosed", false);
        //uiSpinnerNoteEdgeSemiClosed.setAdvancedSetting(true);
        allNotesControls.add(uiSpinnerNoteEdgeSemiClosed);
        
        uiSpinnerNoteBellSemiClosed = new UISpinnerNote("Bell SemiClosed", false);
        //uiSpinnerNoteBellSemiClosed.setAdvancedSetting(true);
        allNotesControls.add(uiSpinnerNoteBellSemiClosed);
        
        uiSpinnerNoteBowClosed = new UISpinnerNote("Bow Closed", false);
        allNotesControls.add(uiSpinnerNoteBowClosed);
        
        uiSpinnerNoteEdgeClosed = new UISpinnerNote("Edge Closed", false);
        //uiSpinnerNoteEdgeClosed.setAdvancedSetting(true);
        allNotesControls.add(uiSpinnerNoteEdgeClosed);
        
        uiSpinnerNoteBellClosed = new UISpinnerNote("Bell Closed", false);
        //uiSpinnerNoteBellClosed.setAdvancedSetting(true);
        allNotesControls.add(uiSpinnerNoteBellClosed);

        uiSpinnerNoteChick = new UISpinnerNote("Chick", false);
        allNotesControls.add(uiSpinnerNoteChick);
        
        uiSpinnerNoteSplash = new UISpinnerNote("Splash", false);
        allNotesControls.add(uiSpinnerNoteSplash);
		
		for (int i = 0; i < allNotesControls.size(); i++) {
			verticalControlsCount++;
			if (!allNotesControls.get(i).isAdvancedSetting()) {
				verticalControlsCountWithoutAdvanced++;
			}
        	//vBoxNotes.getChildren().add(allNotesControls.get(i).getUI());
        	allNotesControls.get(i).setLabelWidthMultiplier(Constants.FX_PEDAL_LABEL_WIDTH_MUL);        	
        	allNotesControls.get(i).addControlChangeEventListener(new ControlChangeEventListener() {
				
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
		paneMisc.getChildren().clear();
		for (int i = 0; i < allMiscControls.size(); i++) {
			if (allMiscControls.get(i).isAdvancedSetting()) {
				if (showAdvanced) {
					paneMisc.getChildren().add(allMiscControls.get(i).getUI());
				}				
			} else {
				paneMisc.getChildren().add(allMiscControls.get(i).getUI());
			}
        }
		
		paneLevels.getChildren().clear();
		for (int i = 0; i < allLevelsControls.size(); i++) {
			if (allLevelsControls.get(i).isAdvancedSetting()) {
				if (showAdvanced) {
					paneLevels.getChildren().add(allLevelsControls.get(i).getUI());
				}				
			} else {
				paneLevels.getChildren().add(allLevelsControls.get(i).getUI());
			}
        }
		
		paneNotes.getChildren().clear();
		for (int i = 0; i < allNotesControls.size(); i++) {
			if (allNotesControls.get(i).isAdvancedSetting()) {
				if (showAdvanced) {
					paneNotes.getChildren().add(allNotesControls.get(i).getUI());
				}				
			} else {
				paneNotes.getChildren().add(allNotesControls.get(i).getUI());
			}
        }
	}
	
	@Override
	public void setShowAdvanced(Boolean show) {
		showAdvanced = show;
		reAddAllControls();
	}
	
	public void setAllStateUnknown() {
		for (int i = 0; i < allMiscControls.size(); i++ ) {
			allMiscControls.get(i).setSyncState(Constants.SYNC_STATE_UNKNOWN);
		}
		for (int i = 0; i < allLevelsControls.size(); i++ ) {
			allLevelsControls.get(i).setSyncState(Constants.SYNC_STATE_UNKNOWN);
		}
		for (int i = 0; i < allNotesControls.size(); i++ ) {
			allNotesControls.get(i).setSyncState(Constants.SYNC_STATE_UNKNOWN);
		}
	}
	
	public void respondToResizeDetached() {
		Double w = windowDetached.getScene().getWidth();
		Double h = windowDetached.getScene().getHeight();
		Double controlW = w/Constants.FX_PEDAL_CONTROL_WIDTH_MUL;
		int visibleControls;
		if (showAdvanced) {
			visibleControls = verticalControlsCount + 2;
		} else {
			visibleControls = verticalControlsCountWithoutAdvanced + 2;
		}
		Double controlH = (h/((visibleControls + 1)))*1.09 - 0.7;
		respondToResize(w, h, controlW, controlH);
	}
	
	public void respondToResize(Double w, Double h, Double cW, Double cH) {
		Double controlW, controlH;
		if (cH > cW*0.137) {
			controlH = cW*0.137;
		} else {
			controlH = cH;
		}
		controlW = cW;
		lastControlH = controlH;
		Double titledPaneFontHeight = controlH*Constants.FX_TITLEBARS_FONT_SCALE;
		Double tabsFontSize = controlH*Constants.FX_TABS_FONT_SCALE;
		Double tabHeaderPadding = -controlH*30*0.0005;
		Double tabHeaderHeight = controlH*30*0.013;
		titledPane.setFont(new Font(titledPaneFontHeight));
		titledPane.setTitleHeight(controlH);
		lastTitleHeight = controlH;
		titledPane.setWidth(controlW*Constants.FX_PEDAL_CONTROL_WIDTH_MUL);
		vBoxAll.setLayoutY(lastTitleHeight);
		vBoxAll.setMaxWidth(controlW*1.0*Constants.FX_PEDAL_CONTROL_WIDTH_MUL);
		toolBar.setMaxWidth(controlW*0.99*Constants.FX_PEDAL_CONTROL_WIDTH_MUL);
		toolBar.setMaxHeight(controlH);
		tabMisc.setStyle("-fx-font-size: " + tabsFontSize.toString() + "pt");
		tabLevels.setStyle("-fx-font-size: " + tabsFontSize.toString() + "pt");
		tabNotes.setStyle("-fx-font-size: " + tabsFontSize.toString() + "pt");
		tabPane.setStyle("-fx-padding: " + tabHeaderPadding.toString() + "em 0.0em 0.0em 0.0em; -fx-tab-max-height:" + tabHeaderHeight.toString() + "pt;-fx-tab-min-height:" + tabHeaderHeight.toString() + "pt;");
		Double buttonFontSize = controlH*0.31;
		toolBar.setStyle("-fx-font-size: " + buttonFontSize.toString() + "pt");
		//System.out.println("Responding to scene resize in UIMisc");
		int visibleControlsCount = -1;
		boolean showControl;
		for (int i = 0; i < allMiscControls.size(); i++ ) {
			showControl = false;
			if (allMiscControls.get(i).isAdvancedSetting()) {
				if (showAdvanced) {
					visibleControlsCount++;
					showControl = true;
				}				
			} else {
				visibleControlsCount++;
				showControl = true;
			}
			if (showControl) {
				allMiscControls.get(i).respondToResize(controlW*Constants.FX_PEDAL_CONTROL_WIDTH_MUL, controlH);
				allMiscControls.get(i).getUI().setLayoutX(0);
				allMiscControls.get(i).getUI().setLayoutY(visibleControlsCount*controlH);
			}
		}
		visibleControlsCount = -1;
		for (int i = 0; i < allLevelsControls.size(); i++ ) {
			showControl = false;
			if (allLevelsControls.get(i).isAdvancedSetting()) {
				if (showAdvanced) {
					visibleControlsCount++;
					showControl = true;
				}				
			} else {
				visibleControlsCount++;
				showControl = true;
			}
			if (showControl) {
				allLevelsControls.get(i).respondToResize(controlW*Constants.FX_PEDAL_CONTROL_WIDTH_MUL, controlH);
				allLevelsControls.get(i).getUI().setLayoutX(0);
				allLevelsControls.get(i).getUI().setLayoutY(visibleControlsCount*controlH);
			}
		}
		visibleControlsCount = -1;
		for (int i = 0; i < allNotesControls.size(); i++ ) {
			showControl = false;
			if (allNotesControls.get(i).isAdvancedSetting()) {
				if (showAdvanced) {
					visibleControlsCount++;
					showControl = true;
				}				
			} else {
				visibleControlsCount++;
				showControl = true;
			}
			if (showControl) {
				allNotesControls.get(i).respondToResize(controlW*Constants.FX_PEDAL_CONTROL_WIDTH_MUL, controlH);
				allNotesControls.get(i).getUI().setLayoutX(0);
				allNotesControls.get(i).getUI().setLayoutY(visibleControlsCount*controlH);
			}
		}
		tabPane.setMaxHeight(controlH*allNotesControls.size()*1.0 + tabHeaderHeight + toolBar.getHeight());
		tabPane.setMinHeight(0);
		titledPane.setMinWidth(controlW*Constants.FX_PEDAL_CONTROL_WIDTH_MUL);
		titledPane.setMaxWidth(controlW*Constants.FX_PEDAL_CONTROL_WIDTH_MUL);
	}

	public void setControlsFromConfig(ConfigPedal config, Boolean setFromSysex) {
		uiComboBoxMiscType.uiCtlSetValue(config.type ? 1:0, setFromSysex);
		uiComboBoxMiscCurve.uiCtlSetValue(config.curve, setFromSysex);
		uiComboBoxMiscChickCurve.uiCtlSetValue(config.chickCurve, setFromSysex);
		uiComboBoxMiscHiHatInput.uiCtlSetValue(((config.hhInput-2)/2), setFromSysex);
		uiCheckBoxMiscAltInput.uiCtlSetValue(config.altIn, setFromSysex);
		uiCheckBoxMiscReversLevels.uiCtlSetValue(config.reverseLevels, setFromSysex);
		uiCheckBoxMiscSoftChicks.uiCtlSetValue(config.softChicks, setFromSysex);
		uiCheckBoxMiscAutoLevels.uiCtlSetValue(config.autoLevels, setFromSysex);
		uiCheckBoxMiscAlgorithm.uiCtlSetValue(config.new_algorithm, setFromSysex);
		uiSpinnerMiscChickDelay.uiCtlSetValue(config.chickDelay, setFromSysex);
		uiSpinnerMiscCCNumber.uiCtlSetValue(config.cc, setFromSysex);
		uiSpinnerMiscCCReduction.uiCtlSetValue(config.ccRdcLvl, setFromSysex);
		
		uiSpinnerLevelsLow.uiCtlSetValue(config.lowLevel, setFromSysex);
		uiSpinnerLevelsHigh.uiCtlSetValue(config.highLevel, setFromSysex);
		uiSpinnerLevelsOpen.uiCtlSetValue(config.openLevel, setFromSysex);
		uiSpinnerLevelsSemiOpen.uiCtlSetValue(config.semiOpenLevel, setFromSysex);
		uiSpinnerLevelsSemiOpen2.uiCtlSetValue(config.semiOpenLevel2, setFromSysex);
		uiSpinnerLevelsHalfOpen.uiCtlSetValue(config.halfOpenLevel, setFromSysex);
		uiSpinnerLevelsHalfOpen2.uiCtlSetValue(config.halfOpenLevel2, setFromSysex);
		uiSpinnerLevelsClosed.uiCtlSetValue(config.closedLevel, setFromSysex);
		uiSpinnerLevelsChickThresh.uiCtlSetValue(config.chickThres, setFromSysex);
		uiSpinnerLevelsShortChickThresh.uiCtlSetValue(config.shortThres, setFromSysex);
		uiSpinnerLevelsLongChickThresh.uiCtlSetValue(config.longThres, setFromSysex);
		uiSpinnerLevelsChickMinVel.uiCtlSetValue(config.chickParam1, setFromSysex);
		uiSpinnerLevelsChickMaxVel.uiCtlSetValue(config.chickParam2, setFromSysex);
		uiSpinnerLevelsChickDeadPeriod.uiCtlSetValue(config.chickParam3, setFromSysex);
		
		uiSpinnerNoteBowSemiOpen.uiCtlSetValue(config.bowSemiOpenNote, setFromSysex);
		uiSpinnerNoteEdgeSemiOpen.uiCtlSetValue(config.edgeSemiOpenNote, setFromSysex);
		uiSpinnerNoteBellSemiOpen.uiCtlSetValue(config.bellSemiOpenNote, setFromSysex);
		uiSpinnerNoteBowSemiOpen2.uiCtlSetValue(config.bowSemiOpen2Note, setFromSysex);
		uiSpinnerNoteEdgeSemiOpen2.uiCtlSetValue(config.edgeSemiOpen2Note, setFromSysex);
		uiSpinnerNoteBellSemiOpen2.uiCtlSetValue(config.bellSemiOpen2Note, setFromSysex);
		uiSpinnerNoteBowHalfOpen.uiCtlSetValue(config.bowHalfOpenNote, setFromSysex);
		uiSpinnerNoteEdgeHalfOpen.uiCtlSetValue(config.edgeHalfOpenNote, setFromSysex);
		uiSpinnerNoteBellHalfOpen.uiCtlSetValue(config.bellHalfOpenNote, setFromSysex);
		uiSpinnerNoteBowHalfOpen2.uiCtlSetValue(config.bowHalfOpen2Note, setFromSysex);
		uiSpinnerNoteEdgeHalfOpen2.uiCtlSetValue(config.edgeHalfOpen2Note, setFromSysex);
		uiSpinnerNoteBellHalfOpen2.uiCtlSetValue(config.bellHalfOpen2Note, setFromSysex);
		uiSpinnerNoteBowSemiClosed.uiCtlSetValue(config.bowSemiClosedNote, setFromSysex);
		uiSpinnerNoteEdgeSemiClosed.uiCtlSetValue(config.edgeSemiClosedNote, setFromSysex);
		uiSpinnerNoteBellSemiClosed.uiCtlSetValue(config.bellSemiClosedNote, setFromSysex);
		uiSpinnerNoteBowClosed.uiCtlSetValue(config.bowClosedNote, setFromSysex);
		uiSpinnerNoteEdgeClosed.uiCtlSetValue(config.edgeClosedNote, setFromSysex);
		uiSpinnerNoteBellClosed.uiCtlSetValue(config.bellClosedNote, setFromSysex);
		uiSpinnerNoteChick.uiCtlSetValue(config.chickNote, setFromSysex);
		uiSpinnerNoteSplash.uiCtlSetValue(config.splashNote, setFromSysex);
	}
	
	public void setConfigFromControls(ConfigPedal config) {
		config.type = (uiComboBoxMiscType.uiCtlGetValue() > 0 ? true:false);
		config.curve = uiComboBoxMiscCurve.uiCtlGetValue();
		config.chickCurve = uiComboBoxMiscChickCurve.uiCtlGetValue();
		config.hhInput = (uiComboBoxMiscHiHatInput.uiCtlGetValue()*2) + 2;
		config.altIn = uiCheckBoxMiscAltInput.uiCtlIsSelected();
		config.reverseLevels = uiCheckBoxMiscReversLevels.uiCtlIsSelected();
		config.softChicks = uiCheckBoxMiscSoftChicks.uiCtlIsSelected();
		config.autoLevels = uiCheckBoxMiscAutoLevels.uiCtlIsSelected();
		config.new_algorithm = uiCheckBoxMiscAlgorithm.uiCtlIsSelected();
		config.chickDelay = uiSpinnerMiscChickDelay.uiCtlGetValue();
		config.cc = uiSpinnerMiscCCNumber.uiCtlGetValue();
		config.ccRdcLvl = uiSpinnerMiscCCReduction.uiCtlGetValue();
		
		config.lowLevel = uiSpinnerLevelsLow.uiCtlGetValue();
		config.highLevel = uiSpinnerLevelsHigh.uiCtlGetValue();
		config.openLevel = uiSpinnerLevelsOpen.uiCtlGetValue();
		config.semiOpenLevel = uiSpinnerLevelsSemiOpen.uiCtlGetValue();
		config.semiOpenLevel2 = uiSpinnerLevelsSemiOpen2.uiCtlGetValue();
		config.halfOpenLevel = uiSpinnerLevelsHalfOpen.uiCtlGetValue();
		config.halfOpenLevel2 = uiSpinnerLevelsHalfOpen2.uiCtlGetValue();
		config.closedLevel = uiSpinnerLevelsClosed.uiCtlGetValue();
		config.chickThres = uiSpinnerLevelsChickThresh.uiCtlGetValue();
		config.shortThres = uiSpinnerLevelsShortChickThresh.uiCtlGetValue();
		config.longThres = uiSpinnerLevelsLongChickThresh.uiCtlGetValue();
		config.chickParam1 = uiSpinnerLevelsChickMinVel.uiCtlGetValue();
		config.chickParam2 = uiSpinnerLevelsChickMaxVel.uiCtlGetValue();
		config.chickParam3 = uiSpinnerLevelsChickDeadPeriod.uiCtlGetValue();
		
		config.bowSemiOpenNote = uiSpinnerNoteBowSemiOpen.uiCtlGetValue();
		config.edgeSemiOpenNote = uiSpinnerNoteEdgeSemiOpen.uiCtlGetValue();
		config.bellSemiOpenNote = uiSpinnerNoteBellSemiOpen.uiCtlGetValue();
		config.bowSemiOpen2Note = uiSpinnerNoteBowSemiOpen2.uiCtlGetValue();
		config.edgeSemiOpen2Note = uiSpinnerNoteEdgeSemiOpen2.uiCtlGetValue();
		config.bellSemiOpen2Note = uiSpinnerNoteBellSemiOpen2.uiCtlGetValue();
		config.bowHalfOpenNote = uiSpinnerNoteBowHalfOpen.uiCtlGetValue();
		config.edgeHalfOpenNote = uiSpinnerNoteEdgeHalfOpen.uiCtlGetValue();
		config.bellHalfOpenNote = uiSpinnerNoteBellHalfOpen.uiCtlGetValue();
		config.bowHalfOpen2Note = uiSpinnerNoteBowHalfOpen2.uiCtlGetValue();
		config.edgeHalfOpen2Note = uiSpinnerNoteEdgeHalfOpen2.uiCtlGetValue();
		config.bellHalfOpen2Note = uiSpinnerNoteBellHalfOpen2.uiCtlGetValue();
		config.bowSemiClosedNote = uiSpinnerNoteBowSemiClosed.uiCtlGetValue();
		config.edgeSemiClosedNote = uiSpinnerNoteEdgeSemiClosed.uiCtlGetValue();
		config.bellSemiClosedNote = uiSpinnerNoteBellSemiClosed.uiCtlGetValue();
		config.bowClosedNote = uiSpinnerNoteBowClosed.uiCtlGetValue();
		config.edgeClosedNote = uiSpinnerNoteEdgeClosed.uiCtlGetValue();
		config.bellClosedNote = uiSpinnerNoteBellClosed.uiCtlGetValue();
		config.chickNote = uiSpinnerNoteChick.uiCtlGetValue();
		config.splashNote = uiSpinnerNoteSplash.uiCtlGetValue();
		
	}

	@Override
	public int getVerticalControlsCount() {
		if (showAdvanced) {
			return verticalControlsCount + 3;
		} else {
			return verticalControlsCountWithoutAdvanced + 3;
		}
	}
	

}
