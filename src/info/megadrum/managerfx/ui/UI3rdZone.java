package info.megadrum.managerfx.ui;

import java.util.ArrayList;

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
	
	public UI3rdZone() {
		allControls = new ArrayList<UIControl>();
		gridColmn = new ArrayList<Integer>();
		gridRow = new ArrayList<Integer>();

		GridPane layout = new GridPane();
		layout.setHgap(15);

		uiSpinnerNoteMainNote = new UISpinnerNote("Note", true);
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
		
		uiSpinnerThreshold = new UISpinner("Threshold", 0, 15, 0, 1, true);
		allControls.add(uiSpinnerThreshold);
		gridColmn.add(1);
		gridRow.add(2);

	
		for (int i = 0; i < allControls.size(); i++) {
			GridPane.setConstraints(allControls.get(i).getUI(), gridColmn.get(i), gridRow.get(i));
			GridPane.setHalignment(allControls.get(i).getUI(), HPos.LEFT);
			GridPane.setValignment(allControls.get(i).getUI(), VPos.CENTER);
        	layout.getChildren().add(allControls.get(i).getUI());
        	allControls.get(i).setLabelWidthMultiplier(Constants.FX_INPUT_LABEL_WIDTH_MUL);        	
        }
		
		titledPane = new TitledPane();
		titledPane.setText("3rd Zone");
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
			//allControls.get(i).respondToResize((h*2)/allControls.size(), w/2);
			allControls.get(i).respondToResize(controlH, controlW*Constants.FX_INPUT_CONTROL_WIDTH_MUL);
        }
		//titledPane.setMinHeight(h);
		//titledPane.setMaxHeight(h);
	}
}
