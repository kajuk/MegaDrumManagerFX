package info.megadrum.managerfx.ui;

import java.util.ArrayList;

import info.megadrum.managerfx.utils.Constants;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;

public class UIInput {
//	private VBox layout;
	private TitledPane		titledPane;
	
	private UIComboBox 		uiComboBoxName;
	private UISpinnerNote 	uiSpinnerNoteMainNote;
	private UISpinnerNote 	uiSpinnerNoteAltNote;
	private UISpinnerNote 	uiSpinnerNotePressNote;
	private UISpinner 		uiSpinnerChannel;
	private UIComboBox 		uiComboBoxFunction;
	private UIComboBox 		uiComboBoxCurve;
	private UIComboBox 		uiComboBoxCompression;
	private UIComboBox 		uiComboBoxLevelShift;
	private UIComboBox 		uiComboBoxXTalkLevel;
	private UIComboBox 		uiComboBoxXTalkGroup;
	private UISpinner 		uiSpinnerThreshold;
	private UIComboBox 		uiComboBoxGain;
	private UICheckBox 		uiCheckBoxHighAuto;
	private UISpinner 		uiSpinnerHighLevel;
	private UISpinner 		uiSpinnerRetrigger;
	private UIComboBox 		uiComboBoxDynLevel;
	private UIComboBox 		uiComboBoxDynTime;
	private UISpinner 		uiSpinnerMinScan;
	private UIComboBox 		uiComboBoxPosLevel;
	private UISpinner 		uiSpinnerPosLow;
	private UISpinner 		uiSpinnerPosHigh;
	private UIComboBox 		uiComboBoxType;
		
	private ArrayList<UIControl> allControls;
	
	public UIInput(String title) {
		allControls = new ArrayList<UIControl>();

		VBox layout = new VBox();

		uiComboBoxName = new UIComboBox("Name", true);
		allControls.add(uiComboBoxName);

		uiSpinnerNoteMainNote = new UISpinnerNote("Note", true);
		uiSpinnerNoteMainNote.setDisabledNoteAllowed(true);
		allControls.add(uiSpinnerNoteMainNote);
		
		uiSpinnerNoteAltNote = new UISpinnerNote("Alt Note", true);
		allControls.add(uiSpinnerNoteAltNote);

		uiSpinnerNotePressNote = new UISpinnerNote("Pressroll Note", true);
		allControls.add(uiSpinnerNotePressNote);

		uiSpinnerChannel = new UISpinner("Channel", 1, 16, 10, 1, true);
		allControls.add(uiSpinnerChannel);
		
		uiComboBoxFunction = new UIComboBox("Function", true);
		allControls.add(uiComboBoxFunction);

		uiComboBoxCurve = new UIComboBox("Curve", true);
		allControls.add(uiComboBoxCurve);

		uiComboBoxCompression = new UIComboBox("Compression", true);
		allControls.add(uiComboBoxCompression);

		uiComboBoxLevelShift = new UIComboBox("Level Shift", true);
		allControls.add(uiComboBoxLevelShift);

		uiComboBoxXTalkLevel = new UIComboBox("XTalk Level", true);
		allControls.add(uiComboBoxXTalkLevel);

		uiComboBoxXTalkGroup = new UIComboBox("XTalk Group", true);
		allControls.add(uiComboBoxXTalkGroup);

		uiSpinnerThreshold = new UISpinner("Threshold", 0, 127, 30, 1, true);
		allControls.add(uiSpinnerThreshold);

		uiComboBoxGain = new UIComboBox("Gain", true);
		allControls.add(uiComboBoxGain);

		uiCheckBoxHighAuto = new UICheckBox("HighLevel Auto", true);
		allControls.add(uiCheckBoxHighAuto);
		
		uiSpinnerHighLevel = new UISpinner("HighLevel", 64, 1023, 64, 1, true);
		allControls.add(uiSpinnerHighLevel);

		uiSpinnerRetrigger = new UISpinner("Retrigger Mask", 0, 127, 8, 1, true);
		allControls.add(uiSpinnerRetrigger);

		uiComboBoxDynLevel = new UIComboBox("DynLevel", true);
		allControls.add(uiComboBoxDynLevel);

		uiComboBoxDynTime = new UIComboBox("DynTime", true);
		allControls.add(uiComboBoxDynTime);

		uiSpinnerMinScan = new UISpinner("MinScan", 10, 100, 20, 1, true);
		allControls.add(uiSpinnerMinScan);

		uiComboBoxPosLevel = new UIComboBox("Pos Level", true);
		allControls.add(uiComboBoxPosLevel);

		uiSpinnerPosLow = new UISpinner("Pos Low", 0, 100, 5, 1, true);
		allControls.add(uiSpinnerPosLow);

		uiSpinnerPosHigh = new UISpinner("Pos High", 0, 100, 15, 1, true);
		allControls.add(uiSpinnerPosHigh);

		uiComboBoxType = new UIComboBox("Type", true);
		allControls.add(uiComboBoxType);
	
		for (int i = 0; i < allControls.size(); i++) {
        	layout.getChildren().add(allControls.get(i).getUI());
        }

		titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(layout);
		titledPane.setCollapsible(false);
		
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

	public void respondToResize(Double h, Double w, Double fullHeight) {
		Double toolBarFontHeight = fullHeight*Constants.FX_TITLEBARS_FONT_SCALE;
		Double titledPaneFontHeight = toolBarFontHeight*1.0;
		if (toolBarFontHeight > Constants.FX_TITLEBARS_FONT_MIN_SIZE) {
			//System.out.printf("ToolBar font size = %f\n",fontHeight);
			titledPane.setStyle("-fx-font-size: " + titledPaneFontHeight.toString() + "pt");			
		}
		//System.out.println("Responding to scene resize in UIMisc");
		for (int i = 0; i < allControls.size(); i++) {
			allControls.get(i).respondToResize(h/allControls.size(), w);
        }

	}
}
