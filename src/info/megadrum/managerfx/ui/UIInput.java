package info.megadrum.managerfx.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	private List<String>    listInputType;
	private int				inputType;
		
	private ArrayList<UIControl> allControls;
	
	public UIInput(String title) {
		allControls = new ArrayList<UIControl>();

		VBox layout = new VBox();

		uiComboBoxName = new UIComboBox("Name", true);
		allControls.add(uiComboBoxName);

		uiSpinnerNoteMainNote = new UISpinnerNote("Note", true);
		uiSpinnerNoteMainNote.setDisabledNoteAllowed(true);
		allControls.add(uiSpinnerNoteMainNote);
		
		uiSpinnerNoteAltNote = new UISpinnerNote("AltNote", true, true);
		allControls.add(uiSpinnerNoteAltNote);

		uiSpinnerNotePressNote = new UISpinnerNote("PressrollNote", true, true);
		allControls.add(uiSpinnerNotePressNote);

		uiSpinnerChannel = new UISpinner("Channel", 1, 16, 10, 1, true);
		allControls.add(uiSpinnerChannel);
		
		uiComboBoxFunction = new UIComboBox("Function", true);
		uiComboBoxFunction.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_FUNCTION_LIST));
		allControls.add(uiComboBoxFunction);

		uiComboBoxCurve = new UIComboBox("Curve", true);
		uiComboBoxCurve.uiCtlSetValuesArray(Arrays.asList(Constants.CURVES_LIST));
		allControls.add(uiComboBoxCurve);

		uiComboBoxCompression = new UIComboBox("Compression", true);
		uiComboBoxCompression.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_COMPRESSION_LIST));
		allControls.add(uiComboBoxCompression);

		uiComboBoxLevelShift = new UIComboBox("Level Shift", true);
		uiComboBoxLevelShift.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_LEVEL_SHIFT_LIST));
		allControls.add(uiComboBoxLevelShift);

		uiComboBoxXTalkLevel = new UIComboBox("XTalk Level", true);
		uiComboBoxXTalkLevel.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_XTALK_LEVEL_LIST));
		allControls.add(uiComboBoxXTalkLevel);

		uiComboBoxXTalkGroup = new UIComboBox("XTalk Group", true);
		uiComboBoxXTalkGroup.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_XTALK_GROUP_LIST));
		allControls.add(uiComboBoxXTalkGroup);

		uiSpinnerThreshold = new UISpinner("Threshold", 0, 127, 30, 1, true);
		allControls.add(uiSpinnerThreshold);

		uiComboBoxGain = new UIComboBox("Gain", true);
		uiComboBoxGain.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_GAIN_LIST));
		allControls.add(uiComboBoxGain);

		uiCheckBoxHighAuto = new UICheckBox("HighLevel Auto", true);
		allControls.add(uiCheckBoxHighAuto);
		
		uiSpinnerHighLevel = new UISpinner("HighLevel", 64, 1023, 64, 1, true);
		allControls.add(uiSpinnerHighLevel);

		uiSpinnerRetrigger = new UISpinner("Retrigger Mask", 0, 127, 8, 1, true);
		allControls.add(uiSpinnerRetrigger);

		uiComboBoxDynLevel = new UIComboBox("DynLevel", true);
		uiComboBoxDynLevel.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_DYN_LEVEL_LIST));
		allControls.add(uiComboBoxDynLevel);

		uiComboBoxDynTime = new UIComboBox("DynTime", true);
		uiComboBoxDynTime.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_DYN_TIME_LIST));
		allControls.add(uiComboBoxDynTime);

		uiSpinnerMinScan = new UISpinner("MinScan", 10, 100, 20, 1, true);
		allControls.add(uiSpinnerMinScan);

		uiComboBoxPosLevel = new UIComboBox("Pos Level", true);
		uiComboBoxPosLevel.uiCtlSetValuesArray(Arrays.asList(Constants.PAD_POS_LEVEL_LIST));
		allControls.add(uiComboBoxPosLevel);

		uiSpinnerPosLow = new UISpinner("Pos Low", 0, 100, 5, 1, true);
		allControls.add(uiSpinnerPosLow);

		uiSpinnerPosHigh = new UISpinner("Pos High", 0, 100, 15, 1, true);
		allControls.add(uiSpinnerPosHigh);
		
		uiComboBoxType = new UIComboBox("Type", true);
		setHeadEdgeType(Constants.PAD_TYPE_HEAD);
		allControls.add(uiComboBoxType);
	
		for (int i = 0; i < allControls.size(); i++) {
        	layout.getChildren().add(allControls.get(i).getUI());
        	allControls.get(i).setLabelWidthMultiplier(Constants.FX_INPUT_LABEL_WIDTH_MUL);        	
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

	public void respondToResize(Double h, Double w, Double fullHeight, Double controlH, Double controlW) {
		Double toolBarFontHeight = fullHeight*Constants.FX_TITLEBARS_FONT_SCALE;
		Double titledPaneFontHeight = toolBarFontHeight*1.0;
		if (toolBarFontHeight > Constants.FX_TITLEBARS_FONT_MIN_SIZE) {
			//System.out.printf("ToolBar font size = %f\n",fontHeight);
			titledPane.setStyle("-fx-font-size: " + titledPaneFontHeight.toString() + "pt");			
		}
		//System.out.println("Responding to scene resize in UIMisc");
		for (int i = 0; i < allControls.size(); i++) {
			//allControls.get(i).respondToResize(h/allControls.size(), w);
			allControls.get(i).respondToResize(controlH, controlW*Constants.FX_INPUT_CONTROL_WIDTH_MUL);
        }
		//titledPane.setMinHeight(h);
		//titledPane.setMaxHeight(h);

	}
	
	public void setHeadEdgeType(int type) {
		inputType = type;
		if (type == Constants.PAD_TYPE_HEAD) {
			listInputType = Arrays.asList(Constants.PAD_TYPE_HEAD_LIST);
		}
		if (type == Constants.PAD_TYPE_EDGE) {
			listInputType = Arrays.asList(Constants.PAD_TYPE_EDGE_LIST);
		}
		uiComboBoxType.uiCtlSetValuesArray(listInputType);
	}
}
