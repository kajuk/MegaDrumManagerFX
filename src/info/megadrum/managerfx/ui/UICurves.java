package info.megadrum.managerfx.ui;

import info.megadrum.managerfx.utils.Constants;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class UICurves {
	private VBox		vBox;
	private ToolBar		toolBarNavigator;
	private ToolBar		toolBarTop;
	private Button 		buttonGet;
	private Button 		buttonSend;
	private Button 		buttonGetAll;
	private Button 		buttonSendAll;
	private Button 		buttonLoad;
	private Button 		buttonSave;
	
	private UICurvesPaint	curvesPaint;
	
	private Label		labelCurve;
	private ComboBox<String>	comboBoxCurve;
	private	Button		buttonFirst;
	private	Button		buttonPrev;
	private	Button		buttonNext;
	private	Button		buttonLast;
	

	public UICurves() {
		toolBarTop = new ToolBar();
		buttonGet = new Button("Get");
		buttonSend = new Button("Send");
		buttonGetAll = new Button("GetAll");
		buttonSendAll = new Button("SendAll");
		buttonLoad = new Button("Load");
		buttonSave = new Button("Save");
		toolBarTop.getItems().addAll(buttonGet,buttonSend,buttonGetAll,buttonSendAll,new Separator(),buttonLoad,buttonSave);

		toolBarNavigator = new ToolBar();
		labelCurve = new Label("Curve:");
		comboBoxCurve = new ComboBox<String>();
		buttonFirst = new Button("First");
		buttonPrev = new Button("Prev");
		buttonNext = new Button("Next");
		buttonLast = new Button("Last");
		toolBarNavigator.getItems().addAll(labelCurve, comboBoxCurve, buttonFirst, buttonPrev, buttonNext, buttonLast);

		curvesPaint = new UICurvesPaint();
		vBox = new VBox(1);
		vBox.setStyle("-fx-padding: 0.0em 0.0em 0.2em 0.0em");
		vBox.getChildren().addAll(toolBarTop,toolBarNavigator,curvesPaint);

	}

	public Node getUI() {
		return (Node) vBox;
	}

	public void respondToResize (Double h, Double w, Double fullHeight, Double controlH, Double controlW) {
		Double toolBarFontHeight = fullHeight*Constants.FX_TITLEBARS_FONT_SCALE;
		Double titledPaneFontHeight = toolBarFontHeight*1.4;
		Double h1,h2,h3;
		if (toolBarFontHeight > Constants.FX_TITLEBARS_FONT_MIN_SIZE) {
			//System.out.printf("ToolBar font size = %f\n",fontHeight);
			toolBarTop.setStyle("-fx-font-size: " + toolBarFontHeight.toString() + "pt");			
			toolBarNavigator.setStyle("-fx-font-size: " + toolBarFontHeight.toString() + "pt");			
		} else {
			toolBarTop.setStyle("-fx-font-size: " + Constants.FX_TITLEBARS_FONT_MIN_SIZE.toString() + "pt");			
			toolBarTop.setStyle("-fx-font-size: " + Constants.FX_TITLEBARS_FONT_MIN_SIZE.toString() + "pt");			
		}
		toolBarNavigator.setStyle("-fx-padding: 0.0em 0.0em 0.2em 0.0em");
		toolBarTop.setStyle("-fx-padding: 0.0em 0.0em 0.2em 0.0em");
		comboBoxCurve.setMinWidth(controlH*6);
		comboBoxCurve.setMaxWidth(controlH*6);
		labelCurve.setFont(new Font(controlH*0.4));
	}

}
