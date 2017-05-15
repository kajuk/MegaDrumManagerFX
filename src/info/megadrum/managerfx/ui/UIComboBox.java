package info.megadrum.managerfx.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.layout.HBox;

public class UIComboBox extends UIControl {
	private ComboBox<String> comboBox;
	private HBox layout;

	public UIComboBox(Boolean showCopyButton) {
		super(showCopyButton);
		init();
	}
	
	public UIComboBox(String labelText, Boolean showCopyButton) {
		super(labelText, showCopyButton);
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
		//comboBox.setMinWidth(240.0);
		layout = new HBox();
		layout.setAlignment(Pos.CENTER_LEFT);
		layout.getChildren().addAll(comboBox);
		initControl(layout);		
	}
    @Override
    public void respondToResize(Double h, Double w) {
    	Double widthMul;
    	super.respondToResize(h, w);
    	comboBox.setMinHeight(h);
    	comboBox.setMaxHeight(h);
    	if (copyButtonShown) {
    		widthMul = 0.4;
    	} else {
    		widthMul = 0.58;
        	comboBox.setMinWidth(w*widthMul);
        	comboBox.setMaxWidth(w*widthMul);
    	}
    }

}
