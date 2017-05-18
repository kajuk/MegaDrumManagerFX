package info.megadrum.managerfx.ui;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


public class UISlider extends UIControl {
	private Slider uiSlider;
	private Integer minValue;
	private Integer maxValue;
	private Integer initValue;
	private Integer currentValue;
	private HBox layout;

	public UISlider(Boolean showCopyButton) {
		super(showCopyButton);
		init();
	}
	
	public UISlider(Integer min, Integer max, Integer initial, Boolean showCopyButton) {
		super(showCopyButton);
		init(min, max, initial);
	}
	
	public UISlider(String labelText, Boolean showCopyButton) {
		super(labelText, showCopyButton);
		init();
	}

	public UISlider(String labelText, Integer min, Integer max, Integer initial, Boolean showCopyButton) {
		super(labelText, showCopyButton);
		init(min, max, initial);
	}

	private void init() {
		init(0,15,7);
	}
	
	private void init(Integer min, Integer max, Integer initial) {
		minValue = min;
		maxValue = max;
		initValue = initial;
		currentValue = initValue;

		uiSlider = new Slider(minValue, maxValue, intValue);
		uiSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
            		currentValue = new_val.intValue();
            		System.out.printf("Slider new value = %d\n", currentValue);
                }
            });
		
		uiSlider.setShowTickMarks(true);
		//uiSlider.setSnapToTicks(true);
		//uiSlider.setMajorTickUnit(16f);
		//uiSlider.setBlockIncrement(1.0f);
		uiSlider.setMinorTickCount(24);
		
	    layout = new HBox();
	    layout.setAlignment(Pos.CENTER_LEFT);
	    layout.getChildren().addAll(uiSlider);
		initControl(layout);
	}

    @Override
    public void respondToResize(Double h, Double w) {
    	Double width = (h*3.5 + w*0.1);
    	super.respondToResize(h, w);
		uiSlider.setMinHeight(h);
		uiSlider.setMaxHeight(h);
		uiSlider.setMaxWidth(width);
		uiSlider.setMinWidth(width);
		
    }
/*
    @Override
	public void setControlMinWidth(Double w) {
    	// don't change spinner control width so override setControlMinWidth here
	}
*/
}
