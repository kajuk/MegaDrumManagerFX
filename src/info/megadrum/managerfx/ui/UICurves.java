package info.megadrum.managerfx.ui;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.event.EventListenerList;

import info.megadrum.managerfx.utils.Constants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class UICurves {
	private VBox		vBox;
	private GridPane	gridPaneSpinners;
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
	private ArrayList<SpinnerFast<Integer>> allSpinners;
	private Integer[]	changedFromSetSpinners;

	
	private int			syncState = Constants.SYNC_STATE_UNKNOWN;
	private Boolean		sysexReceived = false;
	private static final Double leftSpacer = 16.0;
	
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
				setSpinnersFromCurve();
				testSyncState();
			}
		});
		comboBoxCurve.getItems().clear();
		comboBoxCurve.getItems().addAll(Arrays.asList(Constants.CURVES_LIST));
		comboBoxCurve.getSelectionModel().select(0);
		BorderPane borderPane = new BorderPane(curvesPaint);
		Pane leftPane = new Pane();
		leftPane.setPrefWidth(leftSpacer);
		borderPane.setLeft(leftPane);
		Pane topPane = new Pane();
		topPane.setPrefHeight(5);
		borderPane.setTop(topPane);
		vBox = new VBox(1);
		vBox.setStyle("-fx-border-width: 2px; -fx-padding: 2.0 2.0 2.0 2.0; -fx-border-color: #2e8b57");
		vBox.setStyle("-fx-padding: 0.0em 0.0em 0.2em 0.0em");
		//vBox.getChildren().addAll(toolBarTop,toolBarNavigator,curvesPaint);
		vBox.getChildren().addAll(toolBarTop,toolBarNavigator,borderPane);
		allSpinners = new ArrayList<SpinnerFast<Integer>>();
		gridPaneSpinners = new GridPane();
		gridPaneSpinners.setMinHeight(30);
		gridPaneSpinners.setMaxHeight(30);
		Pane spacer1 = new Pane();
		GridPane.setConstraints(spacer1, 0, 0);
		GridPane.setHalignment(spacer1, HPos.CENTER);
		GridPane.setValignment(spacer1, VPos.CENTER);
		gridPaneSpinners.getChildren().add(spacer1);
		gridPaneSpinners.getColumnConstraints().add(new ColumnConstraints(leftSpacer + 8));
		spacer1.setMinWidth(leftSpacer + 8);
		spacer1.setMaxWidth(leftSpacer + 8);
		SpinnerValueFactory<Integer> valueFactory;
		changedFromSetSpinners = new Integer[9];
		
		for (int i = 0; i < 9; i++) {
			final int iFinal = i;
			changedFromSetSpinners[i] = 0;
			valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 255, 2, 1);
			allSpinners.add(new SpinnerFast<Integer>());
			allSpinners.get(i).getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
			//allSpinners.get(i).getEditor().setMinSize(30, 20);
			//allSpinners.get(i).getEditor().setMaxSize(30, 20);
			allSpinners.get(i).setValueFactory(valueFactory);
			allSpinners.get(i).setMaxSize(26, 30);
			allSpinners.get(i).setMinSize(26, 30);
			allSpinners.get(i).getEditor().setFont(new Font(8));
			allSpinners.get(i).getEditor().setStyle("-fx-text-fill: black; -fx-alignment: CENTER;");
			gridPaneSpinners.getColumnConstraints().add(new ColumnConstraints(32));
			GridPane.setConstraints(allSpinners.get(i), i + 1, 0);
			GridPane.setHalignment(allSpinners.get(i), HPos.CENTER);
			GridPane.setValignment(allSpinners.get(i), VPos.CENTER);
			gridPaneSpinners.getChildren().add(allSpinners.get(i));
			final Integer sp = i;
			allSpinners.get(i).getEditor().textProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					System.out.printf("Spinner %d changed value to %d\n", sp, Integer.valueOf(newValue));
					curvesPaint.setYvalue(sp, Integer.valueOf(newValue));
					testSyncState();
					if (changedFromSetSpinners[iFinal] > 0) {
						changedFromSetSpinners[iFinal] = 0;
					} else {
						fireControlChangeEvent(new ControlChangeEvent(this), Constants.CONTROL_CHANGE_EVENT_CURVE);
					}
				}
				
			});
			allSpinners.get(i).setStyle("-fx-font-size: 5pt");
		}
		vBox.getChildren().add(gridPaneSpinners);
		vBox.setAlignment(Pos.TOP_CENTER);
		setSyncState(Constants.SYNC_STATE_UNKNOWN);
		setSpinnersFromCurve();
		toolBarTop.setStyle("-fx-padding: 0.1em 0.0em 0.2em 0.01em");
		toolBarNavigator.setStyle("-fx-padding: 0.0em 0.0em 0.15em 0.01em");
	}

	public Node getUI() {
		return (Node) vBox;
	}

	public void respondToResize (Double h, Double w, Double fullHeight, Double controlH, Double controlW) {
		Font buttonFont;
		Double toolBarFontHeight = fullHeight*Constants.FX_TOOLBARS_FONT_SCALE;
		Double comboBoxFontHeight = fullHeight*Constants.FX_COMBOBOX_FONT_SCALE;
		if (fullHeight > 600) {
			buttonFont = new Font(600*Constants.FX_TOOLBARS_FONT_SCALE);
		} else if (toolBarFontHeight > Constants.FX_TOOLBARS_FONT_MIN_SIZE) {
			buttonFont = new Font(toolBarFontHeight);
		} else {
			buttonFont = new Font(Constants.FX_TOOLBARS_FONT_MIN_SIZE);
		}
		buttonGet.setFont(buttonFont);
		buttonSend.setFont(buttonFont);
		buttonGetAll.setFont(buttonFont);
		buttonSendAll.setFont(buttonFont);
		buttonLoad.setFont(buttonFont);
		buttonSave.setFont(buttonFont);
		buttonFirst.setFont(buttonFont);
		buttonPrev.setFont(buttonFont);
		buttonNext.setFont(buttonFont);
		buttonLast.setFont(buttonFont);
		comboBoxCurve.setStyle("-fx-font-size: " + comboBoxFontHeight.toString() + "pt");
		toolBarTop.setMaxWidth(vBox.getWidth()*0.995);
		toolBarNavigator.setMaxWidth(vBox.getWidth()*0.995);
		comboBoxCurve.setMinWidth(controlH*5.3);
		comboBoxCurve.setMaxWidth(controlH*5.3);
		labelCurve.setFont(new Font(controlH*0.4));
		labelCurve.setMinWidth(controlH*2);
		labelCurve.setMaxWidth(controlH*2);
	}

	private void setSpinnersFromCurve() {
		int [] v;
		v = new int[9];
		curvesPaint.getYvalues(v);
		for (int i = 0; i < 9; i++) {
			allSpinners.get(i).getValueFactory().setValue(v[i]);
		}
	}
	
	public void setYvalues(int [] values, Boolean setFromSysex) {
		curvesPaint.setYvalues(values, setFromSysex);
		if (setFromSysex) {
			setSysexReceived(true);
			testSyncState();
		}
		int [] v;
		v = new int[9];
		curvesPaint.getYvalues(v);
		for (int i=0; i< 9; i++) {
			//if (values[i] != v[i]) {
				changedFromSetSpinners[i] = 1;
			//}
		}
		setSpinnersFromCurve();
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

	public Button getButtonLoad() {
		return buttonLoad;
	}

	public Button getButtonSave() {
		return buttonSave;
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
