package info.megadrum.managerfx.ui;

import java.lang.reflect.Array;
import java.util.Arrays;

import javax.swing.event.EventListenerList;

import info.megadrum.managerfx.utils.Constants;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
	
	private int			syncState = Constants.SYNC_STATE_UNKNOWN;
	private Boolean		sysexReceived = false;
	
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
				((ControlChangeEventListener) listeners[i+1]).controlChangeEventOccurred(evt, parameter);
			}
		}
	}

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
		toolBarNavigator.getItems().addAll( labelCurve, comboBoxCurve, buttonFirst, buttonPrev, buttonNext, buttonLast);

		curvesPaint = new UICurvesPaint();
		curvesPaint.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				// TODO Auto-generated method stub
				fireControlChangeEvent(new ControlChangeEvent(this), Constants.CONTROL_CHANGE_EVENT_CURVE);
				testSyncState();
			}
		});
		
		comboBoxCurve.getItems().clear();
		comboBoxCurve.getItems().addAll(Arrays.asList(Constants.CURVES_LIST));
		comboBoxCurve.getSelectionModel().select(0);
		vBox = new VBox(1);
		vBox.setStyle("-fx-padding: 0.0em 0.0em 0.2em 0.0em");
		vBox.getChildren().addAll(toolBarTop,toolBarNavigator,curvesPaint);
	}

	public Node getUI() {
		return (Node) vBox;
	}

	public void respondToResize (Double h, Double w, Double fullHeight, Double controlH, Double controlW) {
		Double toolBarFontHeight = fullHeight*Constants.FX_TITLEBARS_FONT_SCALE;
		if (toolBarFontHeight > Constants.FX_TITLEBARS_FONT_MIN_SIZE) {
			//System.out.printf("ToolBar font size = %f\n",fontHeight);
			toolBarTop.setStyle("-fx-font-size: " + toolBarFontHeight.toString() + "pt");			
			toolBarNavigator.setStyle("-fx-font-size: " + toolBarFontHeight.toString() + "pt");			
		} else {
			toolBarTop.setStyle("-fx-font-size: " + Constants.FX_TITLEBARS_FONT_MIN_SIZE.toString() + "pt");			
			toolBarNavigator.setStyle("-fx-font-size: " + Constants.FX_TITLEBARS_FONT_MIN_SIZE.toString() + "pt");			
		}
		toolBarTop.setStyle("-fx-padding: 0.0em 0.0em 0.2em 0.0em");
		toolBarNavigator.setStyle("-fx-padding: 0.0em 0.0em 0.2em 0.0em");
		//toolBarNavigator.setMaxWidth(controlH*15);
		//toolBarNavigator.setMinWidth(controlH*15);
		//toolBarNavigator.setMinWidth(vBox.getWidth()*0.95);
		//toolBarNavigator.setMaxWidth(vBox.getWidth()*0.95);
		comboBoxCurve.setMinWidth(controlH*4);
		comboBoxCurve.setMaxWidth(controlH*4);
		labelCurve.setFont(new Font(controlH*0.4));
		setSyncState(Constants.SYNC_STATE_UNKNOWN);
	}

	public void setYvalues(int [] values, Boolean setFromSysex) {
		curvesPaint.setYvalues(values, setFromSysex);
		if (setFromSysex) {
			setSysexReceived(true);
			testSyncState();
		}
	}

	public void setMdYvalues(int [] values) {
		curvesPaint.setMdYvalues(values);
	}

	public void getYvalues(int [] values) {
		curvesPaint.getYvalues(values);
	}

	public Button getButtonGet() {
		return buttonGet;
	}

	public Button getButtonSend() {
		return buttonSend;
	}

	public Button getButtonGetAll() {
		return buttonGetAll;
	}

	public Button getButtonSendAll() {
		return buttonSendAll;
	}

	public Button getButtonFirst() {
		return buttonFirst;
	}

	public Button getButtonPrev() {
		return buttonPrev;
	}

	public Button getButtonNext() {
		return buttonNext;
	}

	public Button getButtonLast() {
		return buttonLast;
	}

	public ComboBox<String> getComboBoxCurve() {
		return comboBoxCurve;
	}
	
	public  void testSyncState() {
		if (sysexReceived) {
			if (curvesPaint.isInSync()) {
				setSyncState(Constants.SYNC_STATE_SYNCED);
			} else {
				setSyncState(Constants.SYNC_STATE_NOT_SYNCED);
			}
		} else {
			setSyncState(Constants.SYNC_STATE_UNKNOWN);
		}
	}
	
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
		labelCurve.setTextFill(color);
	}

	public void setSysexReceived(Boolean received) {
		sysexReceived = received;
	}
}
