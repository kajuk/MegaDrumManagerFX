package info.megadrum.managerfx.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MidiLevelBar extends VBox {
	private Canvas canvas;
	private GraphicsContext gc;
	final static public int barTypeUnknown = 0;
	final static public int barTypeHead = barTypeUnknown + 1;
	final static public int barTypeRim = barTypeHead + 1;
	final static public int barType3rd = barTypeRim + 1;
	final static public int barTypeChokeOn = barType3rd + 1;
	final static public int barTypeChokeOff = barTypeChokeOn + 1;
	final static public int barTypeHiHat = barTypeChokeOff + 1;
	final private Double barHeightRatio = 0.9;
	final private Double barHeightPadRatio = (1-barHeightRatio)*0.5;
	final private Double barWidthRatio = 0.9;
	final private Double barWidthPadRatio =  (1-barWidthRatio)*0.5;
	
	private int barType = barTypeUnknown;
	final static public Color [] barColors = {Color.YELLOW, Color.BLUE, Color.GREEN, Color.ORANGE, Color.DARKGREY, Color.LIGHTGREY, Color.BROWN};
	private Color bgColor = Color.LIGHTGREY;
	private Color bgBarColor = Color.WHITE;
	private Color fontColor = Color.BLACK;
	private Color barColor = barColors[barTypeUnknown];
	private Double barWidth = 16.0;
	private Double barHeight = 300.0;
	private Double fontSize = 6.0;
	private Integer barInterval = 1000;
	private Integer barLevel = 0;
	private Integer barNote = 0;
	
	public MidiLevelBar(Double w, Double h) {
		respondToResize(w, h);
	}
	
	public void respondToResize(Double w, Double h) {
		barWidth = w;
		barHeight = h;
		fontSize = w*0.5;
		rePaint();
	}

	public void setParameters (int type, int interval, int note, int level, Boolean repaint) {
		barType = ((type>=barTypeUnknown) && (type<=barTypeHiHat))?type:barTypeUnknown;
		barInterval = ((interval > -1) && (interval < 1001))?interval:1000;
		barNote = ((note > -1) && (note < 128))?note:0;
		barLevel = ((level > -1) && (level < 128))?level:0;
		if (repaint) {
			rePaint();
		}
	}
	
	private void rePaint() {
		canvas = new Canvas(barWidth, barHeight);
		setMinSize(barWidth, barHeight);
		setMaxSize(barWidth, barHeight);
		getChildren().clear();
		getChildren().add(canvas);
		gc = canvas.getGraphicsContext2D();

		gc.setFill(bgColor);
		gc.fillRect(0 + barWidth*barWidthPadRatio , 0, barWidth*barWidthRatio, barHeight);
		gc.setFill(bgBarColor);
		gc.fillRect(0 + barWidth*barWidthPadRatio, barHeight - barHeight*barHeightRatio - barHeight*barHeightPadRatio , barWidth*barWidthRatio, barHeight*barHeightRatio);
		Double barFillHeight = ((barHeight*barHeightRatio)*barLevel)/127;
		gc.setFill(barColors[barType]);
		gc.fillRect(0 + barWidth*barWidthPadRatio, barHeight - barFillHeight - barHeight*barHeightPadRatio , barWidth*barWidthRatio, barFillHeight);

		fontSize = barWidth*0.4;
		Font font = new Font(fontSize);
		Double textX, textW, textY;
		gc.setFont(font);
		textW = getTextWidth(font, barLevel.toString());
		textX = (barWidth - textW)*0.5;
		textY = barHeight - barHeight*barHeightPadRatio - barHeight*barHeightRatio*0.5;
		gc.setFill(fontColor);
		gc.fillText(barLevel.toString(), textX, textY);

		if (barType == barTypeHiHat) {
			String hhText = "Open";
			textW = getTextWidth(font, hhText);
			textX = (barWidth - textW)*0.5;
			textY = barHeight*barHeightPadRatio*0.5 + fontSize*0.3;
			gc.fillText(hhText, textX, textY);
			hhText = "Clsd";
			textW = getTextWidth(font, hhText);
			textX = (barWidth - textW)*0.5;
			textY = barHeight - barHeight*barHeightPadRatio*0.5 + fontSize*0.3;
			gc.fillText(hhText, textX, textY);
		} else {
			String msText = barInterval.toString();
			if (barInterval > 999) {
				msText = ">1s";
			}
			textW = getTextWidth(font, msText);
			textX = (barWidth - textW)*0.5;
			textY = barHeight*barHeightPadRatio*0.5 + fontSize*0.3;
			gc.fillText(msText, textX, textY);
			textW = getTextWidth(font, barNote.toString());
			textX = (barWidth - textW)*0.5;
			textY = barHeight - barHeight*barHeightPadRatio*0.5 + fontSize*0.3;
			gc.fillText(barNote.toString(), textX, textY);
		}

	}
	
	private Double getTextWidth(Font font, String s) {
		Text text = new Text(s);
		text.setFont(font);
		return text.getLayoutBounds().getWidth();		
	}
}
