package info.megadrum.managerfx.ui;

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
	
	
	
	public UIPad(String title) {
		// TODO Auto-generated constructor stub
		HBox hBox = new HBox(5);
		uiInputLeft = new UIInput("Head/Bow");
		uiInputRight = new UIInput("Rim/Edge");
		ui3rdZone = new UI3rdZone();
		hBox.getChildren().addAll(uiInputLeft.getUI(),uiInputRight.getUI());
		vBox = new VBox(5);
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

	}
	
	public void respondToResize (Double h, Double w, Double fullHeight) {
		Double toolBarFontHeight = fullHeight*Constants.FX_TITLEBARS_FONT_SCALE;
		Double titledPaneFontHeight = toolBarFontHeight*1.4;
		if (toolBarFontHeight > Constants.FX_TITLEBARS_FONT_MIN_SIZE) {
			//System.out.printf("ToolBar font size = %f\n",fontHeight);
			toolBarTop.setStyle("-fx-font-size: " + toolBarFontHeight.toString() + "pt");			
			toolBarNavigator.setStyle("-fx-font-size: " + toolBarFontHeight.toString() + "pt");			
			titledPane.setStyle("-fx-font-size: " + titledPaneFontHeight.toString() + "pt");			
		}
		toolBarNavigator.setStyle("-fx-padding: 0.0em 0.0em 0.2em 0.0em");
		toolBarTop.setStyle("-fx-padding: 0.0em 0.0em 0.2em 0.0em");
		uiInputLeft.respondToResize(h*0.6, w*0.5, fullHeight);
		uiInputRight.respondToResize(h*0.6, w*0.5, fullHeight);
		ui3rdZone.respondToResize(h*0.0915, w*1.0, fullHeight);
	}

	public Node getUI() {
		return (Node) titledPane;
	}

}
