package info.megadrum.managerfx.ui;

import java.util.ArrayList;
import java.util.List;

import info.megadrum.managerfx.utils.Constants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class UIComboBox extends UIControl {
	private ComboBox<String> comboBox;
	private HBox layout;
	private Boolean comboBoxWide = false;

	public UIComboBox(Boolean showCopyButton) {
		super(showCopyButton);
		init();
	}
	
	public UIComboBox(String labelText, Boolean showCopyButton) {
		super(labelText, showCopyButton);
		init();
	}
	
	private void init () {
		valueType = Constants.VALUE_TYPE_INT;
		comboBox = new ComboBox<String>();
		comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				
			}
        });

		comboBox.setEditable(false);
		//comboBox.setMinWidth(240.0);
		layout = new HBox();
		layout.setAlignment(Pos.CENTER_LEFT);
		layout.getChildren().addAll(comboBox);
		initControl(layout);		
	}
    @Override
    public void respondToResize(Double h, Double w) {
    	Double width;
    	super.respondToResize(h, w);
    	comboBox.setMinHeight(h);
    	comboBox.setMaxHeight(h);
    	width = w*0.1;
    	if (comboBoxWide) {
    		width = w*0.67;
    	} else {    		
        	if (copyButtonShown) {
        		width = w*0.25;
        	} else {
        		width = w*0.41;
        	}    		
    	}
    	comboBox.setMinWidth(width);
    	comboBox.setMaxWidth(width);
    }
    
    public void uiCtlSetValuesArray(List<String> list) {
    	comboBox.getItems().clear();
    	comboBox.getItems().addAll(list);
    }
    
    public void uiCtlSetValue(String value) {
    	comboBox.setValue(value);
    }
    
    public String uiCtlGetSelected() {
    	return comboBox.getValue();
    }
    
    public void addListener(ChangeListener<String> listener) {
    	comboBox.getSelectionModel().selectedItemProperty().addListener(listener);
    }
    
    public void uiCtlSetDisable(Boolean state) {
    	comboBox.setDisable(state);
    }
    
    public void setComboBoxWide(Boolean w) {
    	comboBoxWide = true;
    }
}
