package info.megadrum.managerfx.ui;

import javax.swing.event.EventListenerList;

//import javax.swing.event.EventListenerList;

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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
	protected Integer intValue = 0;
	protected Integer mdIntValue = 0;
	protected Integer syncState = Constants.SYNC_STATE_UNKNOWN;
	protected Integer changedFromSet = 0;
	protected Boolean booleanValue = false;
	protected Boolean mdBooleanValue = false;
	protected int valueType = Constants.VALUE_TYPE_BOOLEAN;
	protected Pane uiControl;
	protected boolean copyButtonShown = false;
	protected Double padding = 2.0;
	private Button buttonCopy;
	private Double labelWidthMultiplier = 0.4;
	private Double controlWidthMultiplier;
	protected final static Boolean debugSizes = false;

	protected EventListenerList listenerList = new EventListenerList();
	
	public void addControlChangeEventListener(ControlChangeEventListener listener) {
		listenerList.add(ControlChangeEventListener.class, listener);
	}
	public void removeControlChangeEventListener(ControlChangeEventListener listener) {
		listenerList.remove(ControlChangeEventListener.class, listener);
	}
	protected void fireControlChangeEvent(ControlChangeEvent evt, Integer parameter) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i = i+2) {
			if (listeners[i] == ControlChangeEventListener.class) {
				if (label.getText().equals("Name")) {
					((ControlChangeEventListener) listeners[i+1]).controlChangeEventOccurred(evt, Constants.CONTROL_CHANGE_EVENT_NAME);					
				} else {
					((ControlChangeEventListener) listeners[i+1]).controlChangeEventOccurred(evt, parameter);
				}
			}
		}
	}
	
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

		if (debugSizes) {
			layout.setStyle("-fx-background-color: grey");
		}
		//label.setStyle("-fx-background-color: grey");
		layout.getChildren().add(buttonCopy);
		setLabelWidthMultiplier(labelWidthMultiplier);
		
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
		if (debugSizes) {
			uiControl.setStyle("-fx-background-color: green");			
		}
		GridPane.setHgrow(uiControl,Priority.ALWAYS);
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
		syncState = state;
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
	public void updateSyncStateConditional() {
		if (syncState != Constants.SYNC_STATE_UNKNOWN) {
			updateSyncState();
		}
	}
	
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
	
    public void uiCtlSetMdValue(Object value) {
		switch (valueType) {
		case Constants.VALUE_TYPE_BOOLEAN:
			mdBooleanValue = (Boolean)value;
			break;
		case Constants.VALUE_TYPE_INT:
			mdIntValue = (Integer)value;
			break;
		default:
			break;
		}
		setSyncState(Constants.SYNC_STATE_NOT_SYNCED);
    }

	public void setLabelWidthMultiplier(Double mul) {
		labelWidthMultiplier = mul;
		controlWidthMultiplier = 1.0 - mul - 0.05;
	}
	
	public void respondToResize(Double h, Double w) {
		Double wCl0, wCl1, wCl2;
		wCl0 = (w - padding*2)*labelWidthMultiplier;
		if (copyButtonShown) {
			wCl1 = (w - padding*2)*controlWidthMultiplier*0.8;
			wCl2 = (w - padding*2)*0.1;
		} else {
			wCl1 = (w - padding*2)*controlWidthMultiplier;
			wCl2 = 0.0;
		}
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
        label.setFont(new Font(h*0.5));
		//layout.setMaxWidth(w);
        //uiControl.minHeight(h - padding*2 - 1);
        //uiControl.respondToResizeControl(h,w);
	}
}
