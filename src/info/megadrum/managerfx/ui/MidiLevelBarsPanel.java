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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SpinnerValueFactory;
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

class HitsPane extends Pane {
	private Canvas canvas;
	private GraphicsContext gc;
	private Double canvasWidth;
	private Double canvasHeight;
	private Double paddingY, paddingX;
	private List<BarData> hitsDataList;
	private Double lastHitMsPadding = 100.0;
	private int fullMsTimeRange = 1000*30; // 30s
	private int maxInterval = 1000*1;
	
	public HitsPane() {
		canvas = new Canvas();		
		getChildren().clear();
		getChildren().add(canvas);
		gc = canvas.getGraphicsContext2D();
		hitsDataList = new ArrayList<BarData>();
	}
	
	public void respondToResize(Double w, Double h) {
		canvasWidth = w*0.96;
		canvasHeight = h*0.96;
		paddingX = (w - canvasWidth)*0.5;
		paddingY = (h - canvasHeight)*0.5;
		canvas.setWidth(canvasWidth);
		canvas.setHeight(canvasHeight);
		canvas.setLayoutX(paddingX);
		canvas.setLayoutY(paddingY);
		
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, canvasWidth, canvasHeight);
		drawHits();
	}
	
	public void addHit(int type, int note, int level, int interval) {
		BarData hitData = new BarData();
		hitData.type = type;
		hitData.note = note;
		hitData.level = level;
		hitData.interval = (interval>maxInterval)?maxInterval:interval;
		hitsDataList.add(hitData);
		if (hitsDataList.size() > 2000) {
			hitsDataList.remove(0);
		}
		drawHits();
	}
	
	public void drawHits() {
		if (hitsDataList.size() > 0) {
			gc.setFill(Color.WHITE);
			gc.fillRect(0, 0, canvasWidth, canvasHeight);
			Double hitX, x, hitY;
			Double hitTimeDiff = lastHitMsPadding;
			Color color;
			int interval, level;
			int pointer = hitsDataList.size() - 1;
			while ((hitTimeDiff < fullMsTimeRange ) && (pointer > -1)) {
				color = MidiLevelBar.barColors[hitsDataList.get(pointer).type];
				interval = hitsDataList.get(pointer).interval;
				level = hitsDataList.get(pointer).level;
				x = (hitTimeDiff*canvasWidth)/fullMsTimeRange;
				hitX = canvasWidth - x;
				hitY = (level*canvasHeight)/127; 
				gc.setStroke(color);
				gc.strokeLine(hitX, canvasHeight, hitX, canvasHeight - hitY);
				hitTimeDiff += interval;
				pointer--;
			}
		}
	}
	
	public void setMaxInterval(int msInterval) {
		maxInterval = msInterval;
	}
	
	public void setTimeRange(int s) {
		fullMsTimeRange = 1000*s;
		drawHits();
	}
}

public class MidiLevelBarsPanel extends Pane {

	public final static int maxBars = 64;
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
	private Double barsTotalHeight;
	private Double paneHitsHeight;
	private Double paneHitsWidth;
	private int barsCount = 16;
	private GridPane gridPaneTop;
	private HBox hBoxBottom;
	private VBox vBoxLeft;
	private Pane paneRight;
	private Pane paneBars;
	private HitsPane paneHits;
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
	private List<Slider> slidersPos;
	private SpinnerFast<Double> spinnerTimeRange;
	private SpinnerFast<Double> spinnerMaxInterval;
	
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
		//paneRight.setStyle("-fx-background-color: lightgreen");
		paneBars = new Pane();
		//paneBars.setBackground(new Background(new BackgroundFill(Color.RED, null, getInsets())));
		//paneBars.setStyle("-fx-background-color: orange");
		paneHits = new HitsPane();
		//paneHits.setStyle("-fx-background-color: blue");
		paneHits.setPadding(new Insets(0, 0, 0, 0));
		hBoxRoot = new HBox();
		//hBoxRoot.setStyle("-fx-background-color: yellow");
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
		//gridPaneTop.setStyle("-fx-background-color: red");
		paneBars.setPadding(new Insets(0, 0, 0, 0));
		hBoxBottom.setPadding(new Insets(0, 0, 0, 0));
		labelBottom = new Label("note numbers");
		hBoxBottom.setAlignment(Pos.CENTER);
		hBoxBottom.getChildren().add(labelBottom);
		//hBoxBottom.setStyle("-fx-background-color: red");
		vBoxLeft.getChildren().addAll(gridPaneTop,paneBars,hBoxBottom,paneHits);

		panesRight = new ArrayList<Pane>();
		labelsRight = new ArrayList<Label>();
		comboBoxBarCount = new ComboBox<String>();
		comboBoxBarCount.getItems().addAll("16","20","24","28","32","36","40","48","52","56","60","64");
		comboBoxBarCount.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				System.out.println("MidiLevelBarsPanel: Is there a better way to do it withot Platform.runLater?");
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
		slidersPos = new ArrayList<Slider>();
		for (int i = 0; i < 7; i++) {
			slidersPos.add(new Slider(0, 127, 63));
			slidersPos.get(i).setDisable(true);
		}
		spinnerTimeRange = new SpinnerFast<Double>();
		SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(10.0, 50.0, 30.0, 1.0);
		spinnerTimeRange.setValueFactory(valueFactory);
		spinnerTimeRange.getEditor().textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				Double value = Double.valueOf(newValue);
				paneHits.setTimeRange(value.intValue());
			}
			
		});
		//spinnerFast.setEditable(true);
		spinnerMaxInterval = new SpinnerFast<Double>();
		valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(1.0, 5.0, 1.0, 0.1);
		spinnerMaxInterval.setValueFactory(valueFactory);
		spinnerMaxInterval.getEditor().textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				Double value = Double.valueOf(newValue);
				paneHits.setMaxInterval((int)(value*1000));
			}
			
		});
		//spinnerMaxInterval.setEditable(true);
		
		comboBoxBarCount.getSelectionModel().select((barsCount - 16)/4);
	}
	
	public void respondToResize(Double w, Double h) {
		width = w;
		height = h;
		setMinSize(width, height);
		setMaxSize(width, height);
		Double paneRightWidth = width*0.2;
		paneRightWidth = (paneRightWidth > 120.0)?120.0:paneRightWidth;
		barsTotalWidth = width - paneRightWidth;
		vBoxLeft.setMinSize(barsTotalWidth, height);
		paneRight.setMinSize(paneRightWidth, height);
		paneRight.setMaxSize(paneRightWidth, height);
		barsTotalHeight = height*0.7;
		paneHitsHeight = height - barsTotalHeight;
		paneHitsWidth = barsTotalWidth;
		barHeight = (barsTotalHeight>400.0)?barsTotalHeight*0.94:barsTotalHeight*0.9;
		barWidth = barsTotalWidth/(barsCount + 2);
		Double smallBarsHeight = (barsTotalHeight - barHeight)*0.5;
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
		paneHits.setMinSize(paneHitsWidth, paneHitsHeight);
		paneHits.setMaxSize(paneHitsWidth, paneHitsHeight);
		paneHits.respondToResize(paneHitsWidth, paneHitsHeight);
		//vBoxLeft.getChildren().clear();
		//vBoxLeft.getChildren().addAll(gridPaneTop,paneBars,hBoxBottom,paneHits);
		
		Double labelTopBottomFontSize = (barsTotalHeight - barHeight)*0.4;
		labelTopBottomFontSize = (labelTopBottomFontSize>16.0)?16.0:labelTopBottomFontSize;
		labelTop.setFont(new Font(labelTopBottomFontSize));
		labelTopHiHat.setFont(new Font(labelTopBottomFontSize*0.8));
		labelBottom.setFont(new Font(labelTopBottomFontSize));
		
		paneRight.getChildren().clear();
		Double rowHight = (height*0.35)/panesRight.size();
		Double paneHeight = rowHight*0.8;
		Double paneWidth = paneRightWidth*0.46;
		Double yPos = rowHight - paneHeight;
		Double comboBoxBarCountFontSize = paneRightWidth*0.06;
		comboBoxBarCountFontSize = (comboBoxBarCountFontSize > 10.0)?10.0:comboBoxBarCountFontSize;
		comboBoxBarCountFontSize = (comboBoxBarCountFontSize > (paneHeight*0.3))?(paneHeight*0.3):comboBoxBarCountFontSize;
		comboBoxBarCount.setMinSize(paneWidth, (paneHeight > 20.0)?20.0:paneHeight);
		comboBoxBarCount.setMaxSize(paneWidth, (paneHeight > 20.0)?20.0:paneHeight);
		comboBoxBarCount.setStyle("-fx-font-size: " + comboBoxBarCountFontSize.toString() + "pt");			

		Double fontOnTheRightSize = paneRightWidth*0.08;
		fontOnTheRightSize = (fontOnTheRightSize > (paneHeight*0.4))?(paneHeight*0.4):fontOnTheRightSize;
		Font fontOnTheRight = new Font(fontOnTheRightSize);
		for (int i = 0; i < panesRight.size(); i++) {
			panesRight.get(i).setMinSize(paneWidth, paneHeight);
			panesRight.get(i).setMaxSize(paneWidth, paneHeight);
			labelsRight.get(i).setFont(fontOnTheRight);
			panesRight.get(i).setLayoutX(0);
			panesRight.get(i).setLayoutY(yPos);
			labelsRight.get(i).setLayoutX(paneWidth);
			labelsRight.get(i).setLayoutY(yPos + paneHeight*0.3 );
			yPos += rowHight;
		}
		paneRight.getChildren().addAll(panesRight);
		paneRight.getChildren().addAll(labelsRight);
		
		buttonClear.setFont(new Font(fontOnTheRightSize));
		
		Label labelLastPos = new Label("Last Positional");
		labelLastPos.setFont(fontOnTheRight);
		labelLastPos.setLayoutX((paneRightWidth - MidiLevelBar.getTextWidth(fontOnTheRight, labelLastPos.getText()))*0.5);
		labelLastPos.setLayoutY(panesRight.size()*rowHight + paneHeight*0.5);
		Label labelCenter = new Label("Center");
		labelCenter.setFont(fontOnTheRight);
		labelCenter.setLayoutX(0);
		labelCenter.setLayoutY(panesRight.size()*rowHight + paneHeight);
		Label labelRim = new Label("Rim");
		labelRim.setFont(fontOnTheRight);
		labelRim.setLayoutX(paneRightWidth - MidiLevelBar.getTextWidth(fontOnTheRight, labelRim.getText()));
		labelRim.setLayoutY(panesRight.size()*rowHight + paneHeight);
		paneRight.getChildren().addAll(labelLastPos, labelCenter, labelRim);
		
		Double sliderFontSize = paneHeight*0.55;
		sliderFontSize = (sliderFontSize>8)?8:sliderFontSize;
		for (int i = 0; i < slidersPos.size(); i++) {
			slidersPos.get(i).setMinSize(paneRightWidth, paneHeight);
			slidersPos.get(i).setMaxSize(paneRightWidth, paneHeight);
			slidersPos.get(i).setStyle("-fx-font-size: " + sliderFontSize.toString()+ "pt");		
			slidersPos.get(i).setLayoutX(0);
			slidersPos.get(i).setLayoutY((panesRight.size() + 1)*rowHight + i*paneHeight + paneHeight);
		}
		paneRight.getChildren().addAll(slidersPos);
		
		Double spinnerButtonsFontSize = fontOnTheRightSize*0.8;
		Double layoutY = (panesRight.size() + 1)*rowHight + slidersPos.size()*paneHeight + paneHeight*2;
		spinnerMaxInterval.setMinSize(paneWidth, paneHeight);
		spinnerMaxInterval.setMaxSize(paneWidth, paneHeight);
		spinnerMaxInterval.setLayoutX(0);
		spinnerMaxInterval.setLayoutY(layoutY);
		spinnerMaxInterval.setStyle("-fx-font-size: " + spinnerButtonsFontSize.toString() + "pt");
		Label label = new Label("Max interval");
		label.setLayoutX(paneWidth*1.1);
		label.setLayoutY(layoutY);
		label.setFont(fontOnTheRight);
		paneRight.getChildren().addAll(spinnerMaxInterval, label);
		
		layoutY += paneHeight;
		spinnerTimeRange.setMinSize(paneWidth, paneHeight);
		spinnerTimeRange.setMaxSize(paneWidth, paneHeight);
		spinnerTimeRange.setLayoutX(0);
		spinnerTimeRange.setLayoutY(layoutY);
		spinnerTimeRange.setStyle("-fx-font-size: " + spinnerButtonsFontSize.toString() + "pt");
		label = new Label("Full range");
		label.setLayoutX(paneWidth*1.1);
		label.setLayoutY(layoutY);
		label.setFont(fontOnTheRight);
		paneRight.getChildren().addAll(spinnerTimeRange, label);
		
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
	
	public void addNewPositional(Integer pos) {
		for (int i = (slidersPos.size() - 1); i > 0; i--) {
			slidersPos.get(i).setValue(slidersPos.get(i - 1).getValue());
		}
		slidersPos.get(0).setValue(pos);
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
		paneHits.addHit(type, note, level, timeDiff);
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
