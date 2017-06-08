package info.megadrum.managerfx.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

class BarData {
	public int type = MidiLevelBar.barTypeUnknown;
	public int note = 0;
	public int level = 0;
	public int interval = 1000;
}

public class MidiLevelBarsPanel extends Pane {

	public final static int maxBars = 48;
	private List<MidiLevelBar> midiLevelBars;
	private BarData [] barDatas;
	private int barDataPointer = 0;
	private int lastHiHatLevel = 0;
	private MidiLevelBar hhMidiLevelBar;
	private Double width = 400.0;
	private Double height = 200.0;
	private Double barsTotalWidth;
	private Double barWidth = 10.0;
	private Double barHeight = 180.0 ;
	private int barsCount = 16;
	private GridPane gridPaneTop;
	private HBox hBoxBottom;
	private VBox vBoxLeft;
	private Pane paneRight;
	private Pane paneBars;
	private HBox hBoxRoot;
	private Label labelTop;
	private Label labelTopHiHat;
	private Label labelBottom;
	private ComboBox<String> comboBoxBarCount;
	
	
	public MidiLevelBarsPanel() {
		super();
		hhMidiLevelBar = new MidiLevelBar(10.0, 100.0);
		midiLevelBars = new ArrayList<MidiLevelBar>();
		barDatas = new BarData[maxBars];
		for (int i = 0; i < maxBars; i++) {
			MidiLevelBar hmlb = new MidiLevelBar(10.0, 100.0);
			midiLevelBars.add(hmlb);
			barDatas[i] = new BarData();
		}
		gridPaneTop = new GridPane();
		hBoxBottom = new HBox();
		vBoxLeft = new VBox();
		paneRight = new Pane();
		paneRight.setStyle("-fx-background-color: lightgreen");
		paneBars = new Pane();
		paneBars.setStyle("-fx-background-color: orange");
		hBoxRoot = new HBox();
		hBoxRoot.setStyle("-fx-background-color: yellow");
		hBoxRoot.setPadding(new Insets(0, 0, 0, 0));
		getChildren().add(hBoxRoot);
		vBoxLeft.setPadding(new Insets(0, 0, 0, 0));
		paneRight.setPadding(new Insets(0, 0, 0, 0));
		hBoxRoot.getChildren().addAll(vBoxLeft, paneRight);
		gridPaneTop.setPadding(new Insets(0, 0, 0, 0));
		labelTop = new Label("hits intervals (milliseconds)");
		GridPane.setConstraints(labelTop, 0, 0);
		GridPane.setHalignment(labelTop, HPos.CENTER);
		GridPane.setValignment(labelTop, VPos.CENTER);
		labelTopHiHat = new Label("HiHat");
		GridPane.setConstraints(labelTopHiHat, 1, 0);
		GridPane.setHalignment(labelTopHiHat, HPos.CENTER);
		GridPane.setValignment(labelTopHiHat, VPos.CENTER);
		gridPaneTop.getChildren().addAll(labelTop, labelTopHiHat);
		gridPaneTop.setStyle("-fx-background-color: red");
		paneBars.setPadding(new Insets(0, 0, 0, 0));
		hBoxBottom.setPadding(new Insets(0, 0, 0, 0));
		labelBottom = new Label("note numbers");
		hBoxBottom.setAlignment(Pos.CENTER);
		hBoxBottom.getChildren().add(labelBottom);
		hBoxBottom.setStyle("-fx-background-color: red");
		vBoxLeft.getChildren().addAll(gridPaneTop,paneBars,hBoxBottom);
	}
	
	public void respondToResize(Double w, Double h) {
		width = w;
		height = h;
		setMinSize(width, height);
		setMaxSize(width, height);
		barsTotalWidth = width*0.8;
		vBoxLeft.setMinSize(barsTotalWidth, height);
		paneRight.setMinSize(width - barsTotalWidth, height);
		barHeight = height*0.9;
		barWidth = barsTotalWidth/(barsCount + 2);
		Double smallBarsHeight = (height - barHeight)*0.5;
		gridPaneTop.getColumnConstraints().clear();
		gridPaneTop.getColumnConstraints().add(new ColumnConstraints(barWidth*0.28 + barWidth*barsCount));
		gridPaneTop.getColumnConstraints().add(new ColumnConstraints(barWidth*1.5));
		gridPaneTop.setMinSize(barsTotalWidth, smallBarsHeight);
		gridPaneTop.setMaxSize(barsTotalWidth, smallBarsHeight);
		hBoxBottom.setMinSize(barsTotalWidth, smallBarsHeight);
		hBoxBottom.setMaxSize(barsTotalWidth, smallBarsHeight);
		paneBars.setMinSize(barsTotalWidth, barHeight);
		paneBars.setMaxSize(barsTotalWidth, barHeight);
		vBoxLeft.getChildren().clear();
		vBoxLeft.getChildren().addAll(gridPaneTop,paneBars,hBoxBottom);
		
		labelTop.setFont(new Font((height - barHeight)*0.4));
		labelTopHiHat.setFont(new Font((height - barHeight)*0.45));
		labelBottom.setFont(new Font((height - barHeight)*0.4));
		updateBars();
		updateHiHatBar();
		reAddAllBars();
	}
	
	private void updateBars() {
		int pointer = barDataPointer;
		for (int i= 0; i < barsCount; i++) {
			pointer--;
			if (pointer < 0) {
				pointer = maxBars - 1;
			}
			int type = barDatas[pointer].type;
			int note = barDatas[pointer].note;
			int level = barDatas[pointer].level;
			int interval = barDatas[pointer].interval;
			midiLevelBars.get(maxBars - i - 1).setParameters(type, interval, note, level, true);
			//midiLevelBars.get(maxBars - i - 1).respondToResize(barWidth, barHeight);
		}
	}
	
	private void updateHiHatBar() {
		hhMidiLevelBar.setParameters(MidiLevelBar.barTypeHiHat, 0, 0, lastHiHatLevel, true);
	}
	
	private void reAddAllBars() {
		int pointerData = barDataPointer - barsCount;
		if (pointerData < 0) {
			pointerData += maxBars;
		}
		paneBars.getChildren().clear();
		int pointerBar =  maxBars - barsCount;
		for (int i= 0; i < barsCount; i++) {
			int type = barDatas[pointerData].type;
			int note = barDatas[pointerData].note;
			int level = barDatas[pointerData].level;
			int interval = barDatas[pointerData].interval;
			midiLevelBars.get(pointerBar).setParameters(type, interval, note, level, false);
			midiLevelBars.get(pointerBar).respondToResize(barWidth, barHeight);
			midiLevelBars.get(pointerBar).setLayoutX(barWidth*0.1 + i*barWidth);
			midiLevelBars.get(pointerBar).setLayoutY(0);
			paneBars.getChildren().add(midiLevelBars.get(pointerBar));
			pointerBar++;
			pointerData++;
			if (pointerData >= maxBars) {
				pointerData = 0;
			}
		}
		hhMidiLevelBar.setParameters(MidiLevelBar.barTypeHiHat, 0, 0, lastHiHatLevel, false);
		hhMidiLevelBar.respondToResize(barWidth, barHeight);
		hhMidiLevelBar.setLayoutX(barWidth*0.6 + barsCount*barWidth);
		hhMidiLevelBar.setLayoutY(0);
		paneBars.getChildren().add(hhMidiLevelBar);
	}
	
	public void addNewBarData(int type, int note, int level, int interval) {
		barDatas[barDataPointer].type = type;
		barDatas[barDataPointer].note = note;
		barDatas[barDataPointer].level = level;
		barDatas[barDataPointer].interval = interval;
		barDataPointer++;
		if (barDataPointer >= maxBars) {
			barDataPointer = 0;
		}
		updateBars();
	}
	
	public void setHiHatLevel(int level) {
		lastHiHatLevel = level;
		updateHiHatBar();
	}
}
