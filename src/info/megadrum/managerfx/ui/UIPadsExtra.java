package info.megadrum.managerfx.ui;

import javax.swing.event.EventListenerList;

import info.megadrum.managerfx.utils.Constants;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Font;

public class UIPadsExtra {
	private TitledPane 	titledPane;
	private TabPane		tabPane;
	private UICurves	uiCurves;

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
	
	public UIPadsExtra(String title) {
		tabPane = new TabPane();
        Tab tabCurves = new Tab("Custom Curves");
        tabCurves.setClosable(false);
        Tab tabCustomNames = new Tab("Custom Names");
        tabCustomNames.setClosable(false);
        tabPane.getTabs().addAll(tabCurves,tabCustomNames);

        uiCurves = new UICurves();
        uiCurves.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				// TODO Auto-generated method stub
				fireControlChangeEvent(new ControlChangeEvent(this), parameter);
			}
		});
        tabCurves.setContent(uiCurves.getUI());
        
		titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(tabPane);
		titledPane.setCollapsible(false);
		titledPane.setAlignment(Pos.CENTER);
		titledPane.setMinSize(400, 400);
		titledPane.setMaxSize(400, 400);
	}

	public Node getUI() {
		return (Node) titledPane;
	}

	public void respondToResize (Double h, Double w, Double fullHeight, Double controlH, Double controlW) {
		Double toolBarFontHeight = fullHeight*Constants.FX_TITLEBARS_FONT_SCALE;
		Double titledPaneFontHeight = toolBarFontHeight*1.4;
		Double h1,h2,h3;
		if (toolBarFontHeight > Constants.FX_TITLEBARS_FONT_MIN_SIZE) {
			titledPane.setStyle("-fx-font-size: " + titledPaneFontHeight.toString() + "pt");			
		} else {
			titledPane.setStyle("-fx-font-size: " + Constants.FX_TITLEBARS_FONT_MIN_SIZE.toString() + "pt");						
		}
		uiCurves.respondToResize(h, w, fullHeight, controlH, controlW);
	}

	public void setYvalues(int [] values, Boolean setFromSysex) {
		uiCurves.setYvalues(values, setFromSysex);
	}

	public void setMdYvalues(int [] values) {
		uiCurves.setMdYvalues(values);
	}

	public void getYvalues(int [] values) {
		uiCurves.getYvalues(values);
	}

	public Button getCurvesButtonGet() {
		return uiCurves.getButtonGet();
	}

	public Button getCurvesButtonSend() {
		return uiCurves.getButtonSend();
	}

	public Button getCurvesButtonGetAll() {
		return uiCurves.getButtonGetAll();
	}

	public Button getCurvesButtonSendAll() {
		return uiCurves.getButtonSendAll();
	}

	public ComboBox<String> getCurvesComboBox() {
		return uiCurves.getComboBoxCurve();
	}
	
	public Button getCurvesButtonFirst() {
		return uiCurves.getButtonFirst();
	}

	public Button getCurvesButtonPrev() {
		return uiCurves.getButtonPrev();
	}

	public Button getCurvesButtonNext() {
		return uiCurves.getButtonNext();
	}

	public Button getCurvesButtonLast() {
		return uiCurves.getButtonLast();
	}
}
