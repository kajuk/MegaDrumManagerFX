package info.megadrum.managerfx.ui;

import java.util.ArrayList;

import info.megadrum.managerfx.utils.Constants;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class UIPedal {
//	private VBox layout;
	private TitledPane 	titledPane;
	private Button 		buttonGet;
	private Button 		buttonSend;
	private Button 		buttonLoad;
	private Button 		buttonSave;
	private ToolBar		toolBar;
	private TabPane		tabPane;

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
	private UISpinner	uiSpinnerLevelsSoftChickThresh;
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
	
	public UIPedal(String title) {
		
		allMiscControls = new ArrayList<UIControl>();
		allLevelsControls = new ArrayList<UIControl>();
		allNotesControls = new ArrayList<UIControl>();
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


		VBox vBox1 = new VBox();
		vBox1.getChildren().add(toolBar);
		vBox1.setStyle("-fx-padding: 0.0em 0.2em 0.0em 0.2em");

		tabPane = new TabPane();
		//tabPane.setTabMaxHeight(20);
		//tabPane.setTabMinHeight(20);
		vBox1.getChildren().add(tabPane);

        Tab tabMisc = new Tab("Misc");
        tabMisc.setClosable(false);
        Tab tabLevels = new Tab("Levels");
        tabLevels.setClosable(false);
        Tab tabNotes = new Tab("Notes");
        tabNotes.setClosable(false);
        tabPane.getTabs().addAll(tabMisc,tabLevels,tabNotes);
        VBox vBoxMisc = new VBox();
        VBox vBoxLevels = new VBox();
        VBox vBoxNotes = new VBox();
        

        tabMisc.setContent(vBoxMisc);
        tabLevels.setContent(vBoxLevels);
        tabNotes.setContent(vBoxNotes);

        uiComboBoxMiscType = new UIComboBox("Type", false);
        allMiscControls.add(uiComboBoxMiscType);
        uiComboBoxMiscCurve = new UIComboBox("Curve", false);
        allMiscControls.add(uiComboBoxMiscCurve);
        uiComboBoxMiscChickCurve = new UIComboBox("Chick Curve", false);
        allMiscControls.add(uiComboBoxMiscChickCurve);
        uiComboBoxMiscHiHatInput = new UIComboBox("HiHat Input", false);
        allMiscControls.add(uiComboBoxMiscHiHatInput);
        uiCheckBoxMiscAltInput = new UICheckBox("Alt Input", false);
        allMiscControls.add(uiCheckBoxMiscAltInput);
        uiCheckBoxMiscReversLevels = new UICheckBox("Reverse Levels", false);
        allMiscControls.add(uiCheckBoxMiscReversLevels);
        uiCheckBoxMiscSoftChicks = new UICheckBox("Soft Chicks", false);
        allMiscControls.add(uiCheckBoxMiscSoftChicks);
        uiCheckBoxMiscAutoLevels = new UICheckBox("Auto Levels", false);
        allMiscControls.add(uiCheckBoxMiscAutoLevels);
        uiCheckBoxMiscAlgorithm = new UICheckBox("New Algorithm", false);
        allMiscControls.add(uiCheckBoxMiscAlgorithm);
        uiSpinnerMiscChickDelay = new UISpinner("Chick Delay", 0, 127, 0, 1, false);
        allMiscControls.add(uiSpinnerMiscChickDelay);
        uiSpinnerMiscCCNumber = new UISpinner("CC Number", 0, 127, 4, 1, false);
        allMiscControls.add(uiSpinnerMiscCCNumber);
        uiSpinnerMiscCCReduction = new UISpinner("CC Reduction Lvl", 0, 3, 1, 1, false);
        allMiscControls.add(uiSpinnerMiscCCReduction);
			
		for (int i = 0; i < allMiscControls.size(); i++) {
        	vBoxMisc.getChildren().add(allMiscControls.get(i).getUI());
        	allMiscControls.get(i).setLabelWidthMultiplier(Constants.FX_PEDAL_LABEL_WIDTH_MUL);        	
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
        uiSpinnerLevelsSoftChickThresh = new UISpinner("SoftChickThresh", 0, 127, 115, 1, false);
        allLevelsControls.add(uiSpinnerLevelsSoftChickThresh);
        uiSpinnerLevelsLongChickThresh = new UISpinner("LongChickThresh", 0, 127, 16, 1, false);
        allLevelsControls.add(uiSpinnerLevelsLongChickThresh);
        uiSpinnerLevelsChickMinVel = new UISpinner("Chick Min Velocity", 0, 1023, 400, 1, false);
        allLevelsControls.add(uiSpinnerLevelsChickMinVel);
        uiSpinnerLevelsChickMaxVel = new UISpinner("Chick Max Velocity", 0, 1023, 90, 1, false);
        allLevelsControls.add(uiSpinnerLevelsChickMaxVel);
        uiSpinnerLevelsChickDeadPeriod = new UISpinner("Chick Dead Period", 0, 1023, 90, 1, false);
        allLevelsControls.add(uiSpinnerLevelsChickDeadPeriod);
				
		for (int i = 0; i < allLevelsControls.size(); i++) {
        	vBoxLevels.getChildren().add(allLevelsControls.get(i).getUI());
        	allLevelsControls.get(i).setLabelWidthMultiplier(Constants.FX_PEDAL_LABEL_WIDTH_MUL);        	
        }

		uiSpinnerNoteBowSemiOpen = new UISpinnerNote("Bow SemiOpen", false);
        allNotesControls.add(uiSpinnerNoteBowSemiOpen);
        uiSpinnerNoteEdgeSemiOpen = new UISpinnerNote("Edge SemiOpen", false);
        allNotesControls.add(uiSpinnerNoteEdgeSemiOpen);
        uiSpinnerNoteBellSemiOpen = new UISpinnerNote("Bell SemiOpen", false);
        allNotesControls.add(uiSpinnerNoteBellSemiOpen);

		uiSpinnerNoteBowSemiOpen2 = new UISpinnerNote("Bow SemiOpen2", false);
        allNotesControls.add(uiSpinnerNoteBowSemiOpen2);
        uiSpinnerNoteEdgeSemiOpen2 = new UISpinnerNote("Edge SemiOpen2", false);
        allNotesControls.add(uiSpinnerNoteEdgeSemiOpen2);
        uiSpinnerNoteBellSemiOpen2 = new UISpinnerNote("Bell SemiOpen2", false);
        allNotesControls.add(uiSpinnerNoteBellSemiOpen2);

		uiSpinnerNoteBowHalfOpen = new UISpinnerNote("Bow HalfOpen", false);
        allNotesControls.add(uiSpinnerNoteBowHalfOpen);
        uiSpinnerNoteEdgeHalfOpen = new UISpinnerNote("Edge HalfOpen", false);
        allNotesControls.add(uiSpinnerNoteEdgeHalfOpen);
        uiSpinnerNoteBellHalfOpen = new UISpinnerNote("Bell HalfOpen", false);
        allNotesControls.add(uiSpinnerNoteBellHalfOpen);
        
		uiSpinnerNoteBowHalfOpen2 = new UISpinnerNote("Bow HalfOpen2", false);
        allNotesControls.add(uiSpinnerNoteBowHalfOpen2);
        uiSpinnerNoteEdgeHalfOpen2 = new UISpinnerNote("Edge HalfOpen2", false);
        allNotesControls.add(uiSpinnerNoteEdgeHalfOpen2);
        uiSpinnerNoteBellHalfOpen2 = new UISpinnerNote("Bell HalfOpen2", false);
        allNotesControls.add(uiSpinnerNoteBellHalfOpen2);
		
        uiSpinnerNoteBowSemiClosed = new UISpinnerNote("Bow SemiClosed", false);
        allNotesControls.add(uiSpinnerNoteBowSemiClosed);
        uiSpinnerNoteEdgeSemiClosed = new UISpinnerNote("Edge SemiClosed", false);
        allNotesControls.add(uiSpinnerNoteEdgeSemiClosed);
        uiSpinnerNoteBellSemiClosed = new UISpinnerNote("Bell SemiClosed", false);
        allNotesControls.add(uiSpinnerNoteBellSemiClosed);
        
        uiSpinnerNoteBowClosed = new UISpinnerNote("Bow Closed", false);
        allNotesControls.add(uiSpinnerNoteBowClosed);
        uiSpinnerNoteEdgeClosed = new UISpinnerNote("Edge Closed", false);
        allNotesControls.add(uiSpinnerNoteEdgeClosed);
        uiSpinnerNoteBellClosed = new UISpinnerNote("Bell Closed", false);
        allNotesControls.add(uiSpinnerNoteBellClosed);

        uiSpinnerNoteChick = new UISpinnerNote("Chick", false);
        allNotesControls.add(uiSpinnerNoteChick);
        uiSpinnerNoteSplash = new UISpinnerNote("Splash", false);
        allNotesControls.add(uiSpinnerNoteSplash);
		
		for (int i = 0; i < allNotesControls.size(); i++) {
        	vBoxNotes.getChildren().add(allNotesControls.get(i).getUI());
        	allNotesControls.get(i).setLabelWidthMultiplier(Constants.FX_PEDAL_LABEL_WIDTH_MUL);        	
        }
		
		titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(vBox1);
		titledPane.setCollapsible(false);
		titledPane.setAlignment(Pos.CENTER);
		tabPane.setStyle("-fx-background-color: red");
		setAllStateUnknown();
	}

	private void setAllStateUnknown() {
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
		//tabPane.setMinHeight(h - toolBar.getHeight());
		//tabPane.setMaxHeight(h - toolBar.getHeight());
		System.out.printf("Pedal ControlW = %f\n", controlW);
		//tabPane.setMinWidth(controlW);
		//tabPane.setMaxWidth(controlW);
		titledPane.setMinWidth(controlW*Constants.FX_PEDAL_CONTROL_WIDTH_MUL);
		titledPane.setMaxWidth(controlW*Constants.FX_PEDAL_CONTROL_WIDTH_MUL);
		//tabPane.setStyle("-fx-padding: 0.0em 0.0em 0.0em 0.0em");

		//System.out.println("Responding to scene resize in UIMisc");
		for (int i = 0; i < allMiscControls.size(); i++ ) {
			allMiscControls.get(i).respondToResize(controlH, controlW*Constants.FX_PEDAL_CONTROL_WIDTH_MUL);
		}
		for (int i = 0; i < allLevelsControls.size(); i++ ) {
			allLevelsControls.get(i).respondToResize(controlH, controlW*Constants.FX_PEDAL_CONTROL_WIDTH_MUL);
		}
		for (int i = 0; i < allNotesControls.size(); i++ ) {
			allNotesControls.get(i).respondToResize(controlH, controlW*Constants.FX_PEDAL_CONTROL_WIDTH_MUL);
		}
	}
	
	public Button getButtonSend() {
		return buttonSend;
	}

	public Button getButtonGet() {
		return buttonGet;
	}
}
