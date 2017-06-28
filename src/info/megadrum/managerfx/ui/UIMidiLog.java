package info.megadrum.managerfx.ui;

import info.megadrum.managerfx.utils.Constants;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class UIMidiLog extends UIPanel implements PanelInterface {
	private TabPane				tabPane;
	private Tab 				tabVisual;
	private	Tab					tabRaw;
	private MidiLevelBarsPanel	panelVisual;
	private MidiRaw				panelRaw;

	public UIMidiLog(String title) {
		super(title);
		tabPane = new TabPane();
        tabVisual = new Tab("Visual MIDI");
        tabVisual.setClosable(false);
        tabRaw = new Tab("Raw MIDI");
        tabRaw.setClosable(false);
        panelVisual = new MidiLevelBarsPanel();
        tabVisual.setContent(panelVisual);
        panelRaw = new MidiRaw();
        tabRaw.setContent(panelRaw);
        tabPane.getTabs().addAll(tabVisual,tabRaw);
        vBoxAll.getChildren().add(tabPane);
		setDetached(false);
		vBoxAll.setMaxHeight(440);
	}

	public void respondToResizeDetached() {
		Double w = windowDetached.getScene().getWidth();
		Double h = windowDetached.getScene().getHeight();
		respondToResize(w, h - 5, w, h*0.1);
	}

	public void respondToResize (Double w, Double h, Double cW, Double cH) {
		super.respondToResize(w, h, cW, cH);
		titledPane.setWidth(w);
		vBoxAll.setMaxSize(w, h);
		vBoxAll.setMinSize(w, h);
		tabVisual.setStyle("-fx-font-size: " + tabsFontSize.toString() + "pt");
		tabRaw.setStyle("-fx-font-size: " + tabsFontSize.toString() + "pt");
		tabPane.setStyle("-fx-padding: " + tabHeaderPadding.toString() + "em 0.0em 0.0em 0.0em; -fx-tab-max-height:" + tabHeaderHeight.toString() + "pt;-fx-tab-min-height:" + tabHeaderHeight.toString() + "pt;");

		panelVisual.respondToResize(w, h*0.965 - 5);
		panelRaw.respondToResize(w, h*0.965 - 5);
	}

	public void addNewPositional(Integer pos) {
		panelVisual.addNewPositional(pos);
	}

	public void addNewMidiData(int type, int note, int level) {
		panelVisual.addNewBarData(type, note, level);
	}
	
	public void setHiHatLevel(int level) {
		panelVisual.setHiHatLevel(level);
	}
	
	public void addRawMidi (byte [] buffer) {
		panelRaw.addRawMidi(buffer);
	}

	@Override
	public int getVerticalControlsCount() {
		return 25;
	}
}
