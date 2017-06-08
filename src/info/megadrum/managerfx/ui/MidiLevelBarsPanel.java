package info.megadrum.managerfx.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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
	private int barsCount = 48;
	private HBox hBoxTop;
	private HBox hBoxBottom;
	private VBox vBoxLeft;
	private VBox vBoxRight;
	private HBox hBoxBars;
	private HBox hBoxRoot;
	
	
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
		hBoxTop = new HBox();
		hBoxBottom = new HBox();
		vBoxLeft = new VBox();
		vBoxRight = new VBox();
		hBoxBars = new HBox();
		hBoxRoot = new HBox();
		hBoxRoot.setPadding(new Insets(0, 0, 0, 0));
		getChildren().add(hBoxRoot);
		vBoxLeft.setPadding(new Insets(0, 0, 0, 0));
		vBoxRight.setPadding(new Insets(0, 0, 0, 0));
		hBoxRoot.getChildren().addAll(vBoxLeft, vBoxRight);
		hBoxTop.setPadding(new Insets(0, 0, 0, 0));
		hBoxBars.setPadding(new Insets(0, 0, 0, 0));
		hBoxBottom.setPadding(new Insets(0, 0, 0, 0));
		vBoxLeft.getChildren().addAll(hBoxTop,hBoxBars,hBoxBottom);
	}
	
	public void respondToResize(Double w, Double h) {
		width = w;
		height = h;
		setMinSize(width, height);
		setMaxSize(width, height);
		barsTotalWidth = width*0.8;
		vBoxLeft.setMinSize(barsTotalWidth, height);
		vBoxRight.setMinSize(width - barsTotalWidth, height);
		barHeight = height*0.9;
		barWidth = barsTotalWidth/(barsCount + 2);
		hBoxTop.setMinSize(barsTotalWidth, (height - barHeight)*0.5);
		hBoxTop.setMaxSize(barsTotalWidth, (height - barHeight)*0.05);
		hBoxBottom.setMinSize(barsTotalWidth, (height - barHeight)*0.05);
		hBoxBottom.setMaxSize(barsTotalWidth, (height - barHeight)*0.05);
		hBoxBars.setMinSize(barsTotalWidth, barHeight);
		hBoxBars.setMaxSize(barsTotalWidth, barHeight);
		
		barWidth = barsTotalWidth/(barsCount + 2);
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
		hBoxBars.getChildren().clear();
		int pointerBar =  maxBars - barsCount;
		for (int i= 0; i < barsCount; i++) {
			int type = barDatas[pointerData].type;
			int note = barDatas[pointerData].note;
			int level = barDatas[pointerData].level;
			int interval = barDatas[pointerData].interval;
			midiLevelBars.get(pointerBar).setParameters(type, interval, note, level, false);
			midiLevelBars.get(pointerBar).respondToResize(barWidth, barHeight);
			if (i == 0) {
				midiLevelBars.get(pointerBar).setPadding(new Insets(0, 0, 0, barWidth*0.5));
			} else {
				midiLevelBars.get(pointerBar).setPadding(new Insets(0, 0, 0, 0));				
			}
			hBoxBars.getChildren().add(midiLevelBars.get(pointerBar));
			pointerBar++;
			pointerData++;
			if (pointerData >= maxBars) {
				pointerData = 0;
			}
		}
		hhMidiLevelBar.setParameters(MidiLevelBar.barTypeHiHat, 0, 0, lastHiHatLevel, false);
		hhMidiLevelBar.respondToResize(barWidth, barHeight);
		hhMidiLevelBar.setPadding(new Insets(0, 0, 0, barWidth*0.5));
		hBoxBars.getChildren().add(hhMidiLevelBar);
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
