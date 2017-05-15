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
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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
	protected Pane thisControl;

	public UIControl() {
		init("Unknown");
	}
	
	public UIControl(String labelText) {
		init(labelText);
	}
	
	private void init(String labelText) {
		
		rootPane = new Pane();
		rootPane.setMaxHeight(30);
		rootPane.setMaxWidth(240);
		
		layout = new GridPane();
		
		rootPane.getChildren().add(layout);
		layout.setPadding(new Insets(2, 2, 2, 2));
		layout.setHgap(5);
		
		label = new Label();
		label.setText(labelText);
		GridPane.setConstraints(label, 0, 0);
		GridPane.setHalignment(label, HPos.RIGHT);
		layout.getChildren().add(label);
		setColumnsSizes(120.0, 120.0);  //column 0 and 1 are 120 wid
	}
	
	public void setColumnsSizes(Double labelSize, Double controlSize) {
		layout.getColumnConstraints().clear();
		layout.getColumnConstraints().add(new ColumnConstraints(labelSize));
		layout.getColumnConstraints().add(new ColumnConstraints(controlSize));
	}
	
	public Node getUI(){
		return (Node)layout;
	}
	
	public void initControl(Pane uiControl) {
		GridPane.setConstraints(uiControl, 1, 0);
		GridPane.setHalignment(uiControl, HPos.LEFT);
		layout.getChildren().add(uiControl);
		thisControl = uiControl;
		//thisControl.setMinWidth(USE_COMPUTED_SIZE);
		//thisControl.setMaxWidth(400.0);
		//thisControl.setMinWidth(400.0);
	}
	
	public void setControlMinWidth(Double w) {
		thisControl.setMinWidth(w);
	}
	
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

}
