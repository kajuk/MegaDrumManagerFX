package info.megadrum.managerfx.ui;

import java.util.Set;

import com.sun.glass.ui.Size;

import info.megadrum.managerfx.utils.Constants;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class UIControl extends Control implements UIControlInterface {
	
	@Override
	protected Skin<?> createDefaultSkin() {
		// TODO Auto-generated method stub
		return super.createDefaultSkin();
	}

	private GridPane layout;
	protected Label label;
	private Pane rootPane;
	protected int intValue = 0;
	protected int mdIntValue = 0;
	protected boolean booleanValue = false;
	protected boolean mdBooleanValue = false;
	protected int valueType = Constants.VALUE_TYPE_BOOLEAN;
	protected Pane uiControl;
	protected boolean copyButtonShown = false;
	protected Double padding = 2.0;
	private Button buttonCopy;

	public UIControl(Boolean showCopyButton) {
		copyButtonShown = showCopyButton;
		init("Unknown");
	}
	
	public UIControl(String labelText, Boolean showCopyButton) {
		copyButtonShown = showCopyButton;
		init(labelText);
	}
	
	private void init(String labelText) {
		
		rootPane = new Pane();
		//rootPane.setMaxHeight(30);
		//rootPane.setMaxWidth(240);
		
		layout = new GridPane();
		
		rootPane.getChildren().add(layout);
		layout.setPadding(new Insets(padding, padding, padding, padding));
		layout.setHgap(5);
		
		label = new Label();
		label.setText(labelText);
		GridPane.setConstraints(label, 0, 0);
		GridPane.setHalignment(label, HPos.RIGHT);
		GridPane.setValignment(label, VPos.CENTER);
		layout.getChildren().add(label);

		buttonCopy = new Button("c");
		GridPane.setConstraints(buttonCopy, 2, 0);
		GridPane.setHalignment(buttonCopy, HPos.RIGHT);
		GridPane.setValignment(buttonCopy, VPos.CENTER);
		buttonCopy.setVisible(copyButtonShown);
		
		layout.getChildren().add(buttonCopy);
		
		
		//setColumnsSizes(120.0, 120.0);  //column 0 and 1 are 120 wid
	}

	/*
	public void setColumnsSizes(Double labelSize, Double controlSize) {
		layout.getColumnConstraints().clear();
		layout.getColumnConstraints().add(new ColumnConstraints(labelSize));
		layout.getColumnConstraints().add(new ColumnConstraints(controlSize));
	}
	*/
	public Node getUI(){
		return (Node)layout;
	}
	
	public void initControl(Pane uiCtrl) {
		uiControl = uiCtrl;
		GridPane.setConstraints(uiControl, 1, 0);
		GridPane.setHalignment(uiControl, HPos.LEFT);
		GridPane.setValignment(uiControl, VPos.CENTER);
		layout.getChildren().add(uiControl);
		//uiControl.setStyle("-fx-background-color: red");
		//buttonCopy.setStyle("-fx-background-color: blue");
		//thisControl.setMinWidth(USE_COMPUTED_SIZE);
		//thisControl.setMaxWidth(400.0);
		//thisControl.setMinWidth(400.0);
	}
	
	/*
	public void setControlMinWidth(Double w) {
		thisControl.setMinWidth(w);
	}
	*/
	public void setTextLabel(String text) {
		label.setText(text);
	}
	
	@Override
	public void setSyncState(int state) {
		Color color;
		// TODO Auto-generated method stub
		switch (state) {
		case Constants.SYNC_STATE_UNKNOWN:
			color = Constants.SYNC_STATE_UNKNOWN_COLOR;
			break;
		case Constants.SYNC_STATE_SYNCED:
			color = Constants.SYNC_STATE_SYNCED_COLOR;
			break;
		case Constants.SYNC_STATE_NOT_SYNCED:
			color = Constants.SYNC_STATE_NOT_SYNCED_COLOR;
			break;
		default:
			color = Constants.SYNC_STATE_SYNCED_COLOR;
			break;
		}

		label.setTextFill(color);
	}
/*
	@Override
	public void setSyncOrNotSync(boolean synced) {
		if (synced) {
			setSyncState(Constants.SYNC_STATE_SYNCED);
		} else {
			setSyncState(Constants.SYNC_STATE_NOT_SYNCED);
		}
	}
*/
	public void updateSyncState() {
		boolean valuesAreEqual = false;
		switch (valueType) {
		case Constants.VALUE_TYPE_BOOLEAN:
			valuesAreEqual = (booleanValue == mdBooleanValue);
			break;
		case Constants.VALUE_TYPE_INT:
			valuesAreEqual = (intValue == mdIntValue);
			break;
		default:
			break;
		}
		if (valuesAreEqual) {
			setSyncState(Constants.SYNC_STATE_SYNCED);
		} else {
			setSyncState(Constants.SYNC_STATE_NOT_SYNCED);			
		}
	}

	public void respondToResize(Double h, Double w) {
		Double wCl0, wCl1, wCl2;
		wCl0 = (w - padding*2)*0.4;
		//wCl1 = (w - padding*2)*0.4;
/*		wCl1 = h*5.3;
		if (wCl1<(wCl0*0.6)) {
			wCl1 = wCl0*0.6;
			System.out.println("a");
		} else {
			System.out.println("b");
		}*/
		wCl1 = h*4.9 + w*0.02 + 5;
		wCl2 = h;
		layout.getColumnConstraints().clear();
		layout.getColumnConstraints().add(new ColumnConstraints(wCl0));
		layout.getColumnConstraints().add(new ColumnConstraints(wCl1));
		layout.getColumnConstraints().add(new ColumnConstraints(wCl2));
		buttonCopy.setMaxHeight(h/1.2);
		buttonCopy.setMinHeight(h/1.2);
		buttonCopy.setMaxWidth(h/1.2);
		buttonCopy.setMinWidth(h/1.2);
		buttonCopy.setFont(new Font(h/2.5));
		buttonCopy.setTooltip(new Tooltip("Copy this Input setting to all Inputs"));
        layout.getRowConstraints().clear();
        layout.getRowConstraints().add(new RowConstraints(h-padding*2 - 1));
        label.setFont(new Font(h/2));
        //uiControl.minHeight(h - padding*2 - 1);
        //uiControl.respondToResizeControl(h,w);
	}
}
