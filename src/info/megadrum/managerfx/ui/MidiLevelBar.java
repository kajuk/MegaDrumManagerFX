package info.megadrum.managerfx.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MidiLevelBar extends Pane {
	private Canvas canvas;
	private GraphicsContext gc;
	final public int barTypeUnknown = 0;
	final public int barTypeHead = barTypeUnknown + 1;
	final public int barTypeRim = barTypeHead + 1;
	final public int barType3rd = barTypeRim + 1;
	final public int barTypeChokeOn = barType3rd + 1;
	final public int barTypeChokeOff = barTypeChokeOn + 1;
	final public int barTypeHiHat = barTypeChokeOff + 1;
	final private Double barRatio = 0.8;
	final private Double barStartRatio = (1-barRatio)*0.5;
	
	private int barType = barTypeUnknown;
	final public Color [] barColors = {Color.YELLOW, Color.BLUE, Color.GREEN, Color.ORANGE, Color.DARKGREY, Color.LIGHTGREY, Color.BROWN};
	private Color bgColor = Color.LIGHTGREY;
	private Color bgBarColor = Color.WHITE;
	private Color barColor = barColors[barTypeUnknown];
	private Double barWidth = 16.0;
	private Double barHeight = 300.0;
	private Double fontSize = 6.0;
	private int barInterval = 0;
	private int barLevel = 0;
	private int barNote = 0;
	
	public MidiLevelBar(Double w, Double h) {
		respondToResiz(w, h);
	}
	
	public void respondToResiz(Double w, Double h) {
		barWidth = w;
		barHeight = h;
		fontSize = w*0.5;
		rePaint();
	}

	public void setParameters (int type, int interval, int note, int level) {
		barType = type;
		barInterval = interval;
		barNote = note;
		barLevel = level;
		rePaint();
	}
	
	private void rePaint() {
		canvas = new Canvas(barWidth, barHeight);
		setMinSize(barWidth, barHeight);
		setMaxSize(barWidth, barHeight);
		getChildren().clear();
		getChildren().add(canvas);
		gc = canvas.getGraphicsContext2D();

		gc.setFill(bgColor);
		gc.fillRect(0, 0, barWidth, barHeight);
		gc.setFill(bgBarColor);
		gc.fillRect(0, barHeight - barHeight*barRatio - barHeight*barStartRatio , barWidth, barHeight*barRatio);
		Double barFillHeight = ((barHeight*barRatio)*barLevel)/127;
		gc.setFill(barColors[barType]);
		gc.fillRect(0, barHeight - barFillHeight - barHeight*barStartRatio , barWidth, barFillHeight);
/*		
		fontSize = barWidth*0.5;
		Font font = new Font(fontSize);
		gc.setFont(font);
		gc.setStroke(gridColor);
		for (int i = 0; i < 9; i++) {
			gc.setStroke(gridColor);
			gc.strokeLine((i*32) + xShift, yShift, (i*32) + xShift, yShift + 256);
			gc.strokeLine(xShift, (i*32) + yShift, xShift + 256, (i*32) + yShift);
			gc.setStroke(tickColor);
			gc.strokeLine((i*32) + xShift, yShift + 256, (i*32) + xShift, yShift + 256 + 4);
			gc.strokeLine(xShift - 4, (i*32) + yShift, xShift, (i*32) + yShift);
			gc.setFill(labelsColor);
			gc.fillText("P" + ((Integer)(i+1)).toString(), (i*32) + xShift - 5, yShift + 256 + 12);
			if (i > 0) {
				if (i < 8) {
					gc.fillText(((Integer)(256-i*32)).toString(), xShift - 20, (i*32) + yShift + 4);					
				} else {
					gc.fillText(((Integer)(2)).toString(), xShift - 20, (i*32) + yShift + 4);
				}
			} else {
				gc.fillText(((Integer)(255)).toString(), xShift - 20, (i*32) + yShift + 4);					
			}
		}
*/	
	}
}
