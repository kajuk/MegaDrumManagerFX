package info.megadrum.managerfx.ui;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;

public class UICheckBox extends UIControl{
	
	private CheckBox checkBox;

	public UICheckBox() {
		super();
		init();
	}
	
	public UICheckBox(String labelText) {
		super(labelText);
		init();
	}
	
	private void init () {
		checkBox = new CheckBox();
		initControl(checkBox);		
	}
}