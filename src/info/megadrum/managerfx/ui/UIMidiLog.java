package info.megadrum.managerfx.ui;

import info.megadrum.managerfx.utils.Constants;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class UIMidiLog extends UIPanel{
	private TabPane				tabPane;
	private Tab 				tabVisual;
	private	Tab					tabRaw;
	private MidiLevelBarsPanel	panelVisual;
	private Pane				panelRaw;

	public UIMidiLog(String title) {
		super(title);
		tabPane = new TabPane();
        tabVisual = new Tab("Visual MIDI");
        tabVisual.setClosable(false);
        tabRaw = new Tab("Raw MIDI");
        tabRaw.setClosable(false);
        panelVisual = new MidiLevelBarsPanel();
        tabVisual.setContent(panelVisual);
        tabPane.getTabs().addAll(tabVisual,tabRaw);
        vBoxAll.getChildren().add(tabPane);
		setDetached(false);
		vBoxAll.setMaxHeight(440);
	}

	public void respondToResizeDetached(Double w, Double h) {
		respondToResize(w, h - 5);
	}

	public void respondToResize (Double w, Double h) {
		Double titledPaneFontHeight = h*1.4*Constants.FX_TITLEBARS_FONT_SCALE;
		Double tabsFontSize = h*1.4*Constants.FX_TABS_FONT_SCALE;
		Double tabHeaderPadding = -h*0.0005;
		Double tabHeaderHeight = h*0.016;
		if (titledPaneFontHeight < Constants.FX_TITLEBARS_FONT_MIN_SIZE) {
			titledPaneFontHeight =Constants.FX_TITLEBARS_FONT_MIN_SIZE;
		}
		titledPane.setFont(new Font(titledPaneFontHeight));
		vBoxAll.setMaxSize(w, h);
		vBoxAll.setMinSize(w, h);
		tabVisual.setStyle("-fx-font-size: " + tabsFontSize.toString() + "pt");
		tabRaw.setStyle("-fx-font-size: " + tabsFontSize.toString() + "pt");
		tabPane.setStyle("-fx-padding: " + tabHeaderPadding.toString() + "em 0.0em 0.0em 0.0em; -fx-tab-max-height:" + tabHeaderHeight.toString() + "pt;-fx-tab-min-height:" + tabHeaderHeight.toString() + "pt;");

		panelVisual.respondToResize(w, h*0.965 - 5);
		//panelRaw.respondToResize(w, h);
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
}
