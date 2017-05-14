package info.megadrum.managerfx.ui;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class UISpinner extends UIControl {
	private Spinner<Integer> uispinner;
	private Integer minValue;
	private Integer maxValue;
	private Integer initValue;
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
		step = s;
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, initValue, step);

		uispinner = new Spinner<Integer>();
		uispinner.setValueFactory(valueFactory);
		spinnerWidth = 60.0; 
		if (maxValue > 99) spinnerWidth = 70.0; 
		if (maxValue > 999) spinnerWidth = 90.0; 
		uispinner.setMaxWidth(spinnerWidth);
		uispinner.setEditable(true);
		initControl(uispinner);
	}

}
