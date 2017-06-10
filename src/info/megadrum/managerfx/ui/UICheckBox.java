package info.megadrum.managerfx.ui;

import info.megadrum.managerfx.utils.Constants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class UICheckBox extends UIControl{
	
	private CheckBox checkBox;
	private HBox layout;
	private Boolean ignoreSyncState = false;

	public UICheckBox(Boolean showCopyButton) {
		super(showCopyButton);
		init();
	}
	
	public UICheckBox(String labelText, Boolean showCopyButton) {
		super(labelText, showCopyButton);
		init();
	}
	
	private void init () {
		checkBox = new CheckBox();
		checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	if (ignoreSyncState) {
		    		setSyncState(Constants.SYNC_STATE_SYNCED);
		    	} else {
			    	//checkBox.setSelected(!newValue);
					//System.out.printf("%s: new value = %s, old value = %s\n",label.getText(),(newValue ? "true" : "false"),(oldValue ? "true" : "false") );
		    		//System.out.println("Checkbox changed");
			    	if (changedFromSet > 0) {
			    		changedFromSet = 0;
			    	} else {
				    	booleanValue = newValue;
						fireControlChangeEvent(new ControlChangeEvent(this), 0);
						if (syncState != Constants.SYNC_STATE_UNKNOWN) {
							if (booleanValue == mdBooleanValue) {
								setSyncState(Constants.SYNC_STATE_SYNCED);						
							} else {
								setSyncState(Constants.SYNC_STATE_NOT_SYNCED);
							}
						}
			    	}
		    	}
		    }
		});
		layout = new HBox();
		layout.setAlignment(Pos.CENTER_LEFT);
		layout.getChildren().addAll(checkBox);
		//layout.setStyle("-fx-background-color: red");
		//checkBox.setPadding(new Insets(50, 50, 50, 50));
		initControl(layout);		
  }
	
    @Override
    public void respondToResize(Double w, Double h) {
    	super.respondToResize(w, h);
    	Double checkBoxFontSize = h*0.2;
    	// Padding setting really should be done via css and on init.
    	// This is a temp hack
    	if (copyButtonShown) {
          	checkBox.lookup(".box").setStyle("-fx-padding: 0.6em 0.6em 0.8em 0.8em;");
    	} else {
          	checkBox.lookup(".box").setStyle("-fx-padding: 0.4em 0.4em 0.6em 0.6em;");    		
    	}
    	checkBox.setStyle("-fx-font-size: " + checkBoxFontSize.toString() + "pt");
    	// Standard checkBox is not resizeable
    	//checkBox.setMinHeight(h*0.1);
    	//checkBox.setMaxHeight(h*0.1);
    }
    
    public void uiCtlSetValue(Boolean selected, Boolean setFromSysex) {
    	if (selected != checkBox.isSelected()) {
    		changedFromSet = 1;
        	checkBox.setSelected(selected);    		
    	}
		booleanValue = selected;
    	if (setFromSysex) {
    		setSyncState(Constants.SYNC_STATE_SYNCED);
    		mdBooleanValue = selected;
    	} else {
        	updateSyncStateConditional();
    	}
    }
        
    public Boolean uiCtlIsSelected() {
    	return checkBox.isSelected();
    }
    
    public void addListener(ChangeListener<Boolean> listener) {
    	checkBox.selectedProperty().addListener(listener);
    }

    public void setIgnoreSyncState() {
    	ignoreSyncState = true;
    }
}