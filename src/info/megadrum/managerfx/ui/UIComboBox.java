package info.megadrum.managerfx.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;

public class UIComboBox extends UIControl {
	private ComboBox<String> comboBox;

	public UIComboBox() {
		super();
		init();
	}
	
	public UIComboBox(String labelText) {
		super(labelText);
		init();
	}
	
	private void init () {
		comboBox = new ComboBox<String>();
		comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				
			}
        });

		comboBox.setEditable(false);
		comboBox.setMinWidth(120.0);
		initControl(comboBox);		
	}

}
