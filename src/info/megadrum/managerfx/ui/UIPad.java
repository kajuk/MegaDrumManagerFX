package info.megadrum.managerfx.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import info.megadrum.managerfx.utils.Constants;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class UIPad extends Parent {

	private TitledPane		titledPane;

	private VBox		vBox;
	private UIInput 	uiInputLeft;
	private UIInput 	uiInputRight;
	private UI3rdZone	ui3rdZone;
	private ToolBar		toolBarNavigator;
	private ToolBar		toolBarTop;
	private Button 		buttonGet;
	private Button 		buttonSend;
	private Button 		buttonGetAll;
	private Button 		buttonSendAll;
	private Button 		buttonLoad;
	private Button 		buttonSave;
	private Button 		buttonCopy;
	private Button 		buttonDisableOthers;
	
	private UIComboBox	uiComboBoxInput;
	private	Button		buttonFirst;
	private	Button		buttonPrev;
	private	Button		buttonNext;
	private	Button		buttonLast;
	
	private	Integer		inputPair; // 0 for Kick, 1 for HiHat Bow/Edge, 2 for Snare Head/Rim and so on
	private String[]	arrayNames;
	private Boolean		customNamesEnabled = false;
	//private Integer		customNamesCount = 0;
	private String[]	arrayCustomNames;
	
	public UIPad(String title) {
		// TODO Auto-generated constructor stub
		HBox hBox = new HBox(5);
		uiInputLeft = new UIInput("Head/Bow");
		uiInputLeft.setHeadEdgeType(Constants.PAD_TYPE_HEAD);
		uiInputRight = new UIInput("Rim/Edge");
		uiInputRight.setHeadEdgeType(Constants.PAD_TYPE_EDGE);
		ui3rdZone = new UI3rdZone();
		hBox.getChildren().addAll(uiInputLeft.getUI(),uiInputRight.getUI());
		vBox = new VBox(1);
		vBox.setStyle("-fx-padding: 0.0em 0.0em 0.2em 0.0em");

		toolBarTop = new ToolBar();
		buttonGet = new Button("Get");
		buttonSend = new Button("Send");
		buttonGetAll = new Button("GetAll");
		buttonSendAll = new Button("SendAll");
		buttonLoad = new Button("Load");
		buttonSave = new Button("Save");
		buttonCopy = new Button("Copy");
		buttonDisableOthers = new Button("Disable Others");
		toolBarTop.getItems().addAll(buttonGet,buttonSend,buttonGetAll,buttonSendAll,new Separator(),buttonLoad,buttonSave,new Separator(),buttonCopy,buttonDisableOthers);

		toolBarNavigator = new ToolBar();
		uiComboBoxInput = new UIComboBox("Input",false);
		buttonFirst = new Button("First");
		buttonPrev = new Button("Prev");
		buttonNext = new Button("Next");
		buttonLast = new Button("Last");
		toolBarNavigator.getItems().addAll(uiComboBoxInput.getUI(), buttonFirst, buttonPrev, buttonNext, buttonLast);
		
		vBox.getChildren().addAll(toolBarTop,toolBarNavigator,hBox,ui3rdZone.getUI());
		titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(vBox);
		titledPane.setCollapsible(false);
		reCreateNamesArray();
		switchToInputPair(1);

	}
	
	private void reCreateNamesArray() {
		if (arrayNames != null) {
			arrayNames = null;
		}
		if (customNamesEnabled) {
			arrayNames = new String[Constants.CUSTOM_PADS_NAMES_LIST.length + arrayCustomNames.length + 1];			
		} else {
			arrayNames = new String[Constants.CUSTOM_PADS_NAMES_LIST.length + 1];			
		}
		for (int i=0; i < Constants.CUSTOM_PADS_NAMES_LIST.length; i++) {
			arrayNames[i+1] = Constants.CUSTOM_PADS_NAMES_LIST[i];
		}
		if (customNamesEnabled) {
			for (int i=0; i < arrayCustomNames.length; i++) {
				arrayNames[i + 1 + Constants.CUSTOM_PADS_NAMES_LIST.length] = arrayCustomNames[i];
			}
		}
	}
	
	public void respondToResize (Double h, Double w, Double fullHeight, Double controlH, Double controlW) {
		Double toolBarFontHeight = fullHeight*Constants.FX_TITLEBARS_FONT_SCALE;
		Double titledPaneFontHeight = toolBarFontHeight*1.4;
		Double h1,h2,h3;
		if (toolBarFontHeight > Constants.FX_TITLEBARS_FONT_MIN_SIZE) {
			//System.out.printf("ToolBar font size = %f\n",fontHeight);
			toolBarTop.setStyle("-fx-font-size: " + toolBarFontHeight.toString() + "pt");			
			toolBarNavigator.setStyle("-fx-font-size: " + toolBarFontHeight.toString() + "pt");			
			titledPane.setStyle("-fx-font-size: " + titledPaneFontHeight.toString() + "pt");			
		} else {
			toolBarTop.setStyle("-fx-font-size: " + Constants.FX_TITLEBARS_FONT_MIN_SIZE.toString() + "pt");			
			toolBarTop.setStyle("-fx-font-size: " + Constants.FX_TITLEBARS_FONT_MIN_SIZE.toString() + "pt");			
			titledPane.setStyle("-fx-font-size: " + Constants.FX_TITLEBARS_FONT_MIN_SIZE.toString() + "pt");						
		}
		toolBarNavigator.setStyle("-fx-padding: 0.0em 0.0em 0.2em 0.0em");
		toolBarTop.setStyle("-fx-padding: 0.0em 0.0em 0.2em 0.0em");
		uiInputLeft.respondToResize(h*0.6, w*0.5, fullHeight, controlH, controlW);
		uiInputRight.respondToResize(h*0.6, w*0.5, fullHeight, controlH, controlW);
		ui3rdZone.respondToResize(h*0.0915, w*1.0, fullHeight, controlH, controlW);
	}

	public Node getUI() {
		return (Node) titledPane;
	}
	
	public void switchToInputPair(Integer pair) {
		List<String> listNames;
		if (pair == 0) {
			arrayNames[0] = Constants.CUSTOM_PADS_NAMES_LIST[0];
			listNames = Arrays.asList(arrayNames);
			uiInputLeft.setNameList(listNames);
		} else {
			arrayNames[0] = Constants.PADS_NAMES_LIST[(pair - 1) + 1];
			listNames = Arrays.asList(arrayNames);
			uiInputLeft.setNameList(listNames);
			arrayNames[0] = Constants.PADS_NAMES_LIST[(pair - 1) + 2];
			listNames = Arrays.asList(arrayNames);
			uiInputRight.setNameList(listNames);
		}
	}

	public void addAllCustomNames(String[] names) {
		arrayCustomNames = names;
		customNamesEnabled = true;
		reCreateNamesArray();
	}
	public void removeAllCustomNames() {
		customNamesEnabled = false;
		reCreateNamesArray();
	}
}
