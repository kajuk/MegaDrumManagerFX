package info.megadrum.managerfx.ui;

import javax.swing.event.EventListenerList;

import info.megadrum.managerfx.data.ConfigCustomName;
import info.megadrum.managerfx.utils.Constants;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class UIPadsExtra extends UIPanel implements PanelInterface {
	private TabPane			tabPane;
	private Tab 			tabCurves;
	private	Tab				tabCustomNames;
	private UICurves		uiCurves;
	private UICustomNames	uiCustomNames;

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
		super(title, false);
		tabPane = new TabPane();
        tabCurves = new Tab("Custom Curves");
        tabCurves.setClosable(false);
        tabCustomNames = new Tab("Custom Names");
        tabCustomNames.setClosable(false);

        uiCurves = new UICurves();
        uiCurves.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				fireControlChangeEvent(new ControlChangeEvent(this), parameter);
			}
		});
        tabCurves.setContent(uiCurves.getUI());        
        uiCustomNames = new UICustomNames();
        uiCustomNames.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				fireControlChangeEvent(new ControlChangeEvent(this), parameter);
			}
		});
        tabCustomNames.setContent(uiCustomNames.getUI());
        tabPane.getTabs().addAll(tabCurves,tabCustomNames);
        vBoxAll.getChildren().add(tabPane);
		setDetached(false);
		//vBoxAll.setMaxHeight(440);
		//vBoxAll.setMaxSize(340, 440);
		//titledPane.setMaxSize(300, 500);
	}

	public void respondToResizeDetached() {
		Double w = windowDetached.getScene().getWidth();
		Double h = windowDetached.getScene().getHeight();
		Double controlW = w*0.5;
		Double controlH = h*0.05;
		
		respondToResize(w, h + controlH, controlW, controlH);
	}

	public void respondToResize (Double w, Double h, Double cW, Double cH) {
		super.respondToResize(w, h, cW, cH);
		if (detached) {
			vBoxAll.setMaxWidth(w);
			titledPane.setWidth(w);
		} else {
			vBoxAll.setMaxWidth(340);
			vBoxAll.setMinWidth(340);
			titledPane.setWidth(340.0);
		}
		tabCurves.setStyle("-fx-font-size: " + tabsFontSize.toString() + "pt");
		tabCustomNames.setStyle("-fx-font-size: " + tabsFontSize.toString() + "pt");
		tabPane.setStyle("-fx-padding: " + tabHeaderPadding.toString() + "em 0.0em 0.0em 0.0em; -fx-tab-max-height:" + tabHeaderHeight.toString() + "pt;-fx-tab-min-height:" + tabHeaderHeight.toString() + "pt;");

		vBoxAll.setMinHeight(h - controlH*1.02);
		vBoxAll.setMaxHeight(h - controlH*1.02);
		uiCurves.respondToResize(w, h, 340*0.4, controlH);
		uiCustomNames.respondToResize(w, h, 340*0.4, controlH);
	}
	
	public void setAllCustomNamesStatesUnknown() {
		uiCustomNames.setAllStateUnknown();
	}
	
	public void setCustomName(ConfigCustomName config, int id, Boolean setFromSysex) {
		uiCustomNames.setCustomName(config, id, setFromSysex);
	}
	
	public void getCustomName(ConfigCustomName config, int id) {
		uiCustomNames.getCustomName(config, id);
	}
	
	public void setMdCustomName(ConfigCustomName config, int id) {
		uiCustomNames.setMdCustomName(config, id);
	}

	public Button getCustomNamesButtonGetAll() {
		return uiCustomNames.getButtonGetAll();
	}

	public Button getCustomNamesButtonSendAll() {
		return uiCustomNames.getButtonSendAll();
	}

	public Button getCustomNamesButtonLoadAll() {
		return uiCustomNames.getButtonLoadAll();
	}

	public Button getCustomNamesButtonSaveAll() {
		return uiCustomNames.getButtonSaveAll();
	}

	public ComboBox<String> getComboBoxCustomNamesCount() {
		return uiCustomNames.getComboBoxCustomNamesCount();
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
	
	public void setCurveSysexReceived(Boolean received) {
		uiCurves.setSysexReceived(received);
	}
	
	public void testCurveSyncState() {
		uiCurves.testSyncState();
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

	public Button getCurvesButtonLoad() {
		return uiCurves.getButtonLoad();
	}

	public Button getCurvesButtonSave() {
		return uiCurves.getButtonSave();
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

	@Override
	public int getVerticalControlsCount() {
		return 22;
	}
}
