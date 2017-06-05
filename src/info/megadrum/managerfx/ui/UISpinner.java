package info.megadrum.managerfx.ui;

import info.megadrum.managerfx.data.ConfigOptions;
import info.megadrum.managerfx.utils.Constants;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;


public class UISpinner extends UIControl {
	private SpinnerFast<Integer> spinnerFast;
	private Integer minValue;
	private Integer maxValue;
	private Integer lastHeight = 10;
	private Integer spinnerType = Constants.FX_SPINNER_TYPE_STANDARD;
	//private Integer initValue;
	//private Integer currentValue;
	private Integer step;
	private HBox layout;
	private SpinnerValueFactory<Integer> valueFactory;

	public UISpinner(Boolean showCopyButton) {
		super(showCopyButton);
		init();
	}
	
	public UISpinner(Integer min, Integer max, Integer initial, Integer s, Boolean showCopyButton) {
		super(showCopyButton);
		init(min, max, initial, s);
	}
	
	public UISpinner(String labelText, Boolean showCopyButton) {
		super(labelText, showCopyButton);
		init();
	}

	public UISpinner(String labelText, Integer min, Integer max, Integer initial, Integer s, Boolean showCopyButton) {
		super(labelText, showCopyButton);
		init(min, max, initial, s);
	}

	private void init() {
		init(0,100,0,1);
	}
	
	private void init(Integer min, Integer max, Integer initial, Integer s) {
		minValue = min;
		maxValue = max;
		intValue = initial;
		valueType = Constants.VALUE_TYPE_INT;

		step = s;
		valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, intValue, step);

		spinnerFast = new SpinnerFast<Integer>();
		spinnerFast.setValueFactory(valueFactory);
		spinnerFast.setEditable(true);
		//uispinner.getEditor().setStyle("-fx-text-fill: black; -fx-alignment: CENTER_RIGHT;"
		//		);    
		//uispinner.set
		spinnerFast.getEditor().textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// Spinner number validation
		    	if (changedFromSet > 0) {
		    		changedFromSet--;
		        	//System.out.printf("changedFromSet reduced to %d for %s\n", changedFromSet, label.getText());
		    	} else {
		        	//System.out.printf("Setting %s to %s\n", label.getText(), newValue);
		            if (!newValue.matches("\\d*")) {
		            	spinnerFast.getEditor().setText(intValue.toString());
		            } else {
						if (newValue.matches("")) {
							spinnerFast.getEditor().setText(intValue.toString());
						} else {
							if (intValue.intValue() != Integer.valueOf(newValue).intValue()) {
								//System.out.printf("%s: new value = %d, old value = %d\n",label.getText(),Integer.valueOf(newValue),intValue );
								intValue = Integer.valueOf(newValue);
								if (spinnerType == Constants.FX_SPINNER_TYPE_STANDARD) {
									fireControlChangeEvent(new ControlChangeEvent(this), 0);
									System.out.printf("%s: new value = %d, old value = %d\n",label.getText(),Integer.valueOf(newValue),Integer.valueOf(oldValue) );
									if (syncState != Constants.SYNC_STATE_UNKNOWN) {
										if (intValue.intValue() == mdIntValue.intValue()) {
											setSyncState(Constants.SYNC_STATE_SYNCED);						
										} else {
											setSyncState(Constants.SYNC_STATE_NOT_SYNCED);
										}
									}
								}
								//resizeFont();
							}
						}
		            }		    		
		    	}
			}
	    });
		
	    layout = new HBox();
	    layout.setAlignment(Pos.CENTER_LEFT);
	    layout.getChildren().addAll(spinnerFast);
		initControl(layout);
	}
    
    private void resizeFont(Double h) {
		Double we = h*2.2;
		Integer l = maxValue.toString().length();
		Double ll = (16/(16 + l.doubleValue()))*1.0;
		//uispinner.getEditor().setFont(new Font(h*0.4));
		switch (spinnerType) {
		case Constants.FX_SPINNER_TYPE_SYSEX:
			we = 13.0;
			break;
		case Constants.FX_SPINNER_TYPE_STANDARD:
		default:
			we = we*ll*0.3;
			break;
		}
		spinnerFast.getEditor().setFont(new Font(we));
		//System.out.printf("Setting spinner %s font to %f\n", label.getText(), we);
    }

    @Override
    public void respondToResize(Double h, Double w) {
    	super.respondToResize(h, w);
    	lastHeight = hashCode();
//    	Double width = w*0.28;
    	Double width = h*3.5;
		spinnerFast.setMinHeight(h);
		spinnerFast.setMaxHeight(h);
		//uispinner.setMaxWidth(h*2 + 30.0);
		//uispinner.setMinWidth(h*2 + 30.0);
		switch (spinnerType) {
		case Constants.FX_SPINNER_TYPE_SYSEX:
			width = w*0.15;
			break;
		case Constants.FX_SPINNER_TYPE_STANDARD:
		default:
			//width = w*0.25;
			break;
		}
		//System.out.printf("Setting spinner %s width to %f\n", label.getText(), width);
		spinnerFast.setMaxWidth(width);
		spinnerFast.setMinWidth(width);
		resizeFont(h);
		// Spinner buttons width seems to be fixed and not adjustable
		//uispinner.setStyle("-fx-body-color: ladder(#444, yellow 0%, red 100%)");
    }
/*
    @Override
	public void setControlMinWidth(Double w) {
    	// don't change spinner control width so override setControlMinWidth here
	}
*/
    public void uiCtlSetValue(Integer n, Boolean setFromSysex) {
    	if (intValue.intValue() != n.intValue()) {
        	changedFromSet++;
    		intValue = n;
    	}
    	//System.out.printf("changedFromSet = %d for %s\n", changedFromSet, label.getText());
    	if (setFromSysex) {
    		setSyncState(Constants.SYNC_STATE_SYNCED);
    		mdIntValue = n;
    	} else {
        	updateSyncStateConditional();
    	}
    	valueFactory.setValue(n);
    	spinnerFast.getEditor().setText(intValue.toString());
		resizeFont(spinnerFast.getHeight());
    }
    
   public Integer uiCtlGetValue() {
    	return intValue;
    }
    
    public void setSpinnerType(Integer type) {
    	spinnerType = type;
    }
    
}
