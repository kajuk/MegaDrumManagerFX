package info.megadrum.managerfx.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class UICheckBox extends UIControl{
	
	private CheckBox checkBox;
	private HBox layout;

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
		checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	//checkBox.setSelected(!newValue);
				//System.out.printf("%s: new value = %s, old value = %s\n",label.getText(),(newValue ? "true" : "false"),(oldValue ? "true" : "false") );
		    	booleanValue = newValue;
		    }
		});
		layout = new HBox();
		layout.setAlignment(Pos.CENTER_LEFT);
		layout.getChildren().addAll(checkBox);
		//layout.setStyle("-fx-background-color: red");
		initControl(layout);		
	}
}