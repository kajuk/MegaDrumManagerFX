package info.megadrum.managerfx.ui;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


public class UISpinner extends UIControl {
	private Spinner<Integer> uispinner;
	private Integer minValue;
	private Integer maxValue;
	private Integer initValue;
	private Integer currentValue;
	private Integer step;
	private Double spinnerWidth;

	public UISpinner() {
		super();
		init();
	}
	
	public UISpinner(Integer min, Integer max, Integer initial, Integer s) {
		super();
		init(min, max, initial, s);
	}
	
	public UISpinner(String labelText) {
		super(labelText);
		init();
	}

	public UISpinner(String labelText, Integer min, Integer max, Integer initial, Integer s) {
		super(labelText);
		init(min, max, initial, s);
	}

	private void init() {
		init(0,100,0,1);
	}
	
	private void init(Integer min, Integer max, Integer initial, Integer s) {
		minValue = min;
		maxValue = max;
		initValue = initial;
		currentValue = initValue;
		step = s;
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, initValue, step);

		uispinner = new Spinner<Integer>();
		uispinner.setValueFactory(valueFactory);
//		spinnerWidth = 60.0; 
//		if (maxValue > 99) spinnerWidth = 67.0; 
//		if (maxValue > 999) spinnerWidth = 80.0; 
		spinnerWidth = 80.0;
		uispinner.setMaxWidth(spinnerWidth);
		uispinner.setEditable(true);
		//uispinner.getEditor().setStyle("-fx-text-fill: black; -fx-alignment: CENTER_RIGHT;"
		//		);    
		//uispinner.set
		uispinner.getEditor().textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// Spinner number validation
	            if (!newValue.matches("\\d*")) {
	            	uispinner.getEditor().setText(currentValue.toString());
	            } else {
					if (newValue.matches("")) {
						uispinner.getEditor().setText(currentValue.toString());
					} else {
						if (currentValue != Integer.valueOf(newValue)) {
							System.out.printf("%s: new value = %d, old value = %d\n",label.getText(),Integer.valueOf(newValue),currentValue );
							currentValue = Integer.valueOf(newValue);
							intValue = currentValue;
						}
					}
	            }
				
			}
	    });
		
		uispinner.getEditor().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    uispinner.increment(1);
                    break;
                case DOWN:
                    uispinner.decrement(1);
                    break;
			default:
				break;
            }
        });
		
	    IncrementHandler handler = new IncrementHandler();
	    uispinner.addEventFilter(MouseEvent.MOUSE_PRESSED, handler);
	    uispinner.addEventFilter(MouseEvent.MOUSE_RELEASED, evt -> {
	        if (evt.getButton() == MouseButton.PRIMARY) {
	            handler.stop();
	        }
	    });

		initControl(uispinner);
	}

    class IncrementHandler implements EventHandler<MouseEvent> {

        private Spinner<?> spinner;
        private boolean increment;
        private long startTimestamp;

        private static final long DELAY = 1000l * 1000L * 750L; // 0.75 sec
        private Node button;

        private final AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (now - startTimestamp >= DELAY) {
                    // trigger updates every frame once the initial delay is over
                    if (increment) {
                        spinner.increment();
                    } else {
                        spinner.decrement();
                    }
                }
            }
        };

        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY) {
                Spinner<?> source = (Spinner<?>) event.getSource();
                Node node = event.getPickResult().getIntersectedNode();

                Boolean increment = null;
                // find which kind of button was pressed and if one was pressed
                while (increment == null && node != source) {
                    if (node.getStyleClass().contains("increment-arrow-button")) {
                        increment = Boolean.TRUE;
                    } else if (node.getStyleClass().contains("decrement-arrow-button")) {
                        increment = Boolean.FALSE;
                    } else {
                        node = node.getParent();
                    }
                }
                if (increment != null) {
                    event.consume();
                    source.requestFocus();
                    spinner = source;
                    this.increment = increment;

                    // timestamp to calculate the delay
                    startTimestamp = System.nanoTime();

                    button = node;

                    // update for css styling
                    node.pseudoClassStateChanged(PseudoClass.getPseudoClass("pressed"), true);

                    // first value update
                    timer.handle(startTimestamp + DELAY);

                    // trigger timer for more updates later
                    timer.start();
                }
            }
        }
        public void stop() {
            timer.stop();
            if (button != null) {
            	button.pseudoClassStateChanged(PseudoClass.getPseudoClass("pressed"), false);
            	button = null;
            	spinner = null;
            }
        }

    }

    @Override
	public void setControlMinWidth(Double w) {
    	// don't change spinner control width so override setControlMinWidth here
	}

}
