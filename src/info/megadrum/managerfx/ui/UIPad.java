package info.megadrum.managerfx.ui;

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
	
	public void respondToResize (Double h, Double w) {
		uiInputLeft.respondToResize(h*0.7, w/2);
		uiInputRight.respondToResize(h*0.7, w/2);
		ui3rdZone.respondToResize(h*0.1163, w);
	}

	public Node getUI() {
		return (Node) titledPane;
	}

}
