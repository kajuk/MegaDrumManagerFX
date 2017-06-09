package info.megadrum.managerfx.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
	private Pane paneBarCount, paneHead, paneRim, pane3rd, paneChokeOn, paneChokeOff, paneUnknown;
	private Label labelBarCount, labelHead, labelRim, label3rd, labelChokeOn, labelChokeOff, labelUnknown;
	private List<Pane> panesRight;
	private List<Label> labelsRight;
	private Button buttonClear;
	
	private long prevTime = 0;
	
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
		//paneBars.setBackground(new Background(new BackgroundFill(Color.RED, null, getInsets())));
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

		panesRight = new ArrayList<Pane>();
		labelsRight = new ArrayList<Label>();
		comboBoxBarCount = new ComboBox<String>();
		comboBoxBarCount.getItems().addAll("16","20","24","28","32","36","40","48");
		comboBoxBarCount.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				System.out.println("AAAAA");
				barsCount = Integer.valueOf(newValue);
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								//updateBars();
								respondToResize(width, height);						
								//updateHiHatBar();
								//reAddAllBars();
							}
						});
					}
				}, 200);
			}
			
		});
		paneBarCount = new Pane();
		paneBarCount.getChildren().add(comboBoxBarCount);		
		paneHead = new Pane();
		paneHead.setBackground(new Background(new BackgroundFill(
				MidiLevelBar.barColors[MidiLevelBar.barTypeHead],
				CornerRadii.EMPTY, Insets.EMPTY)));
		paneRim = new Pane();
		paneRim.setBackground(new Background(new BackgroundFill(
				MidiLevelBar.barColors[MidiLevelBar.barTypeRim],
				CornerRadii.EMPTY, Insets.EMPTY)));
		pane3rd = new Pane();
		pane3rd.setBackground(new Background(new BackgroundFill(
				MidiLevelBar.barColors[MidiLevelBar.barType3rd],
				CornerRadii.EMPTY, Insets.EMPTY)));
		paneChokeOn = new Pane();
		paneChokeOn.setBackground(new Background(new BackgroundFill(
				MidiLevelBar.barColors[MidiLevelBar.barTypeChokeOn],
				CornerRadii.EMPTY, Insets.EMPTY)));
		paneChokeOff = new Pane();
		paneChokeOff.setBackground(new Background(new BackgroundFill(
				MidiLevelBar.barColors[MidiLevelBar.barTypeChokeOff],
				CornerRadii.EMPTY, Insets.EMPTY)));
		paneUnknown = new Pane();
		paneUnknown.setBackground(new Background(new BackgroundFill(
				MidiLevelBar.barColors[MidiLevelBar.barTypeUnknown],
				CornerRadii.EMPTY, Insets.EMPTY)));
		panesRight.addAll(Arrays.asList(paneBarCount, paneHead, paneRim, pane3rd, paneChokeOn, paneChokeOff, paneUnknown));

		labelBarCount = new Label("Bar count");
		labelHead = new Label("Head hit");
		labelRim = new Label("Rim hit");
		label3rd = new Label("3rd zone hit");
		labelChokeOn = new Label("Choke on");
		labelChokeOff = new Label("Choke Off");
		labelUnknown = new Label("Unknown");
		Label labelBlank = new Label();
		labelsRight.addAll(Arrays.asList(labelBarCount, labelHead, labelRim,
				label3rd, labelChokeOn, labelChokeOff, labelUnknown, labelBlank));
		
		buttonClear = new Button("Clear Bars");
		buttonClear.setOnAction(e-> {
			for (int i = 0; i < maxBars; i++) {
				barDatas[i].type = MidiLevelBar.barTypeUnknown;
				barDatas[i].note = 0;
				barDatas[i].level = 0;
				barDatas[i].interval = 0;
				updateBars();
			}
		});
		Pane paneButton = new Pane();
		paneButton.getChildren().add(buttonClear);
		panesRight.add(paneButton);
		//reAddAllBars();
	}
	
	public void respondToResize(Double w, Double h) {
		width = w;
		height = h;
		setMinSize(width, height);
		setMaxSize(width, height);
		barsTotalWidth = width*0.8;
		vBoxLeft.setMinSize(barsTotalWidth, height);
		Double paneRightWidth = width - barsTotalWidth;
		paneRight.setMinSize(paneRightWidth, height);
		paneRight.setMaxSize(paneRightWidth, height);
		barHeight = height*0.9;
		barWidth = barsTotalWidth/(barsCount + 2);
		Double smallBarsHeight = (height - barHeight)*0.5;
		gridPaneTop.getColumnConstraints().clear();
		gridPaneTop.getColumnConstraints().add(new ColumnConstraints(barsTotalWidth*0.94));
		gridPaneTop.getColumnConstraints().add(new ColumnConstraints(barsTotalWidth*0.06));
		//gridPaneTop.getColumnConstraints().add(new ColumnConstraints(barWidth*1.5));
		gridPaneTop.setMinSize(barsTotalWidth, smallBarsHeight);
		gridPaneTop.setMaxSize(barsTotalWidth, smallBarsHeight);
		hBoxBottom.setMinSize(barsTotalWidth, smallBarsHeight);
		hBoxBottom.setMaxSize(barsTotalWidth, smallBarsHeight);
		paneBars.setMinSize(barsTotalWidth, barHeight);
		paneBars.setMaxSize(barsTotalWidth, barHeight);
		vBoxLeft.getChildren().clear();
		vBoxLeft.getChildren().addAll(gridPaneTop,paneBars,hBoxBottom);
		
		labelTop.setFont(new Font((height - barHeight)*0.4));
		//labelTopHiHat.setFont(new Font((height - barHeight)*0.4*(60.0/(60.0 + barsCount))));
		labelTopHiHat.setFont(new Font((height - barHeight)*0.3));
		labelBottom.setFont(new Font((height - barHeight)*0.4));
		
		paneRight.getChildren().clear();
		Double rowHight = (height*0.5)/panesRight.size();
		Double paneHeight = rowHight*0.8;
		Double paneWidth = paneRightWidth*0.4;
		Double yPos = rowHight - paneHeight;
		comboBoxBarCount.setMinSize(paneWidth, paneHeight);
		comboBoxBarCount.setMaxSize(paneWidth, paneHeight);
		comboBoxBarCount.setStyle("-fx-font-size: " + Double.valueOf(paneHeight*0.3).toString() + "pt");			

		for (int i = 0; i < panesRight.size(); i++) {
			panesRight.get(i).setMinSize(paneWidth, paneHeight);
			panesRight.get(i).setMaxSize(paneWidth, paneHeight);
			labelsRight.get(i).setFont(new Font(paneHeight*0.6));
			panesRight.get(i).setLayoutX(0);
			panesRight.get(i).setLayoutY(yPos);
			labelsRight.get(i).setLayoutX(paneWidth);
			labelsRight.get(i).setLayoutY(yPos + paneHeight*0.3 );
			yPos += rowHight;
		}
		paneRight.getChildren().addAll(panesRight);
		paneRight.getChildren().addAll(labelsRight);
		
		buttonClear.setFont(new Font(paneHeight*0.6));
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
	
	public void addNewBarData(int type, int note, int level) {
		int timeDiff;
		long currentTime;
		long diffTime;
		currentTime = System.nanoTime();
		diffTime = currentTime - prevTime;
		timeDiff = (int)(diffTime/1000000);
		//System.out.printf("Current = %25d , Previous = %25d , Diff = %10d\n", currentTime, prevTime, diffTime);
		prevTime = currentTime;

		barDatas[barDataPointer].type = type;
		barDatas[barDataPointer].note = note;
		barDatas[barDataPointer].level = level;
		barDatas[barDataPointer].interval = timeDiff;
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
