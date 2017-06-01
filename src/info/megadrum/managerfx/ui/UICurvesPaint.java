package info.megadrum.managerfx.ui;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.Arrays;

import javax.swing.event.EventListenerList;

import org.apache.commons.collections.functors.IfClosure;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class UICurvesPaint extends Pane {
	private Canvas canvas;
	private GraphicsContext gc;

	private Color lineColor = Color.BLACK;
	private Color bgColor = Color.WHITE;
	private Color gridColor = Color.LIGHTGRAY;
	private Color tickColor = Color.BLACK;
	private Color labelsColor = Color.BLACK;
	private Color hookColor = Color.RED;
	private static final int xShift = 24;
	private static final int yShift = 4;
	private int [] yValues = {2, 32, 64, 96, 128, 160, 192, 224, 255};
	private int [] MdYvalues = {2, 32, 64, 96, 128, 160, 192, 224, 255};
	private int posId;
	private boolean posCaptured;
	private int xPos;
	private int yPos;
	private Font labelsFont;

	protected EventListenerList listenerList = new EventListenerList();
	
	public void addControlChangeEventListener(ControlChangeEventListener listener) {
		listenerList.add(ControlChangeEventListener.class, listener);
	}
	public void removeControlChangeEventListener(ControlChangeEventListener listener) {
		listenerList.remove(ControlChangeEventListener.class, listener);
	}
	protected void fireControlChangeEvent(ControlChangeEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i = i+2) {
			if (listeners[i] == ControlChangeEventListener.class) {
				((ControlChangeEventListener) listeners[i+1]).controlChangeEventOccurred(evt, 0);
			}
		}
	}

	public UICurvesPaint() {
		// TODO Auto-generated constructor stub
		canvas = new Canvas(300, 276);
		setMinHeight(276);
		gc = canvas.getGraphicsContext2D();
		repaint();
		
		getChildren().add(canvas);
		
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
                new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
				// Capture initial position
				posCaptured = false;
				xPos = (int)event.getX();
				yPos = (int)event.getY();
				if ((xPos > xShift) && (xPos < (xShift + 256))) {
					posId = (xPos - xShift + 15)/32;
					posCaptured = true;
					updateYvalues(yPos);
					fireControlChangeEvent(new ControlChangeEvent(this));
				}
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
                new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
				if (posCaptured) {
					yPos = (int)event.getY();
					updateYvalues(yPos);
					fireControlChangeEvent(new ControlChangeEvent(this));
				}
            }
        });

	}
	
	
	private void updateYvalues(int y) {
		int yValue;
		
		if ((y > yShift) && (y < (yShift + 256))) {
			yValue = 256 - (y - yShift);
			if (yValue < 2) {
				yValue = 2;
			}
			if (yValue > 255) {
				yValue = 255;
			}
			yValues[posId] = yValue;
			//repaint();
			repaint();
		}		
	}

	public void repaint() {
		//TODO . How to match Pane background color?
		gc.setFill(gridColor);
		gc.fillRect(0, 0, 300, 300);
		gc.setFill(bgColor);
		gc.fillRect(xShift, yShift, 256, 256);
		labelsFont = new Font(8.0);
		gc.setFont(labelsFont);
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
		
		if (yValues != null) {
			for (int i = 0; i < 9; i++) {
				gc.setStroke(lineColor);
				if (i < 8 ) {
					gc.strokeLine(xShift + (i*32), 256 -yValues[i] + yShift,xShift + ((i+1)*32), 256 -yValues[i + 1] + yShift);
				}
				gc.setStroke(hookColor);
				gc.strokeOval(xShift + (i*32) - 3, 256 -yValues[i] + yShift - 3, 6, 6);
			}
		}
	}

	public void setYvalues(int [] values, Boolean setFromSysex) {
		for (int i = 0; i < yValues.length; i++ ) {
			yValues[i] = values[i];
			if (setFromSysex) {
				MdYvalues[i] = values[i];
			}
		}
		repaint();
	}
	
	public void setYvalue(Integer p, Integer value) {
		yValues[p] = value;
		repaint();
	}

	public void setMdYvalues(int [] values) {
		for (int i = 0; i < yValues.length; i++ ) {
			MdYvalues[i] = values[i];
		}
		repaint();
	}
	
	public void getYvalues(int [] values) {
		for (int i = 0; i < yValues.length; i++ ) {
			values[i] = yValues[i];
		}
	}
	
	public boolean isInSync() {
		return Arrays.equals(yValues, MdYvalues);
	}

}
