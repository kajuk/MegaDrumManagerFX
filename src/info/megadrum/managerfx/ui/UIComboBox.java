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
import javafx.scene.text.Font;

public class UIComboBox extends UIControl {
	private ComboBox<String> comboBox;
	private HBox layout;
	private Boolean comboBoxWide = false;
	private List<String> listValues;
	private int maxStringLength;

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
		    	if (changedFromSet > 0) {
		    		changedFromSet--;
		        	//System.out.printf("changedFromSet reduced to %d for %s\n", changedFromSet, label.getText());
		    	} else {
		        	//System.out.printf("Setting %s to %s\n", label.getText(), newValue);
		    		//Integer newIntValue = listValues.indexOf(newValue);
		    		Integer newIntValue = comboBox.getSelectionModel().getSelectedIndex();
					//System.out.printf("%s: new value = %d, old value = %d\n",label.getText(),newIntValue.intValue(),intValue );
		    		if (newIntValue > -1) {
						if (intValue.intValue() != newIntValue.intValue()) {
				        	//System.out.printf("Setting %s to %s\n", label.getText(), newValue);
							//System.out.printf("%s: new value = %d, old value = %d\n",label.getText(),newIntValue.intValue(),intValue );
							intValue = newIntValue;
							fireControlChangeEvent(new ControlChangeEvent(this), 0);
							if (syncState != Constants.SYNC_STATE_UNKNOWN) {
								if (intValue.intValue() == mdIntValue.intValue()) {
									setSyncState(Constants.SYNC_STATE_SYNCED);						
								} else {
									setSyncState(Constants.SYNC_STATE_NOT_SYNCED);
								}
								
							}
							//resizeFont();
						}
		    		}
		    	}				
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
        		width = w*0.50;
        	} else {
        		width = w*0.50;
        	}    		
    	}
    	comboBox.setMinWidth(width);
    	comboBox.setMaxWidth(width);
		Double fontSize;
    	if (maxStringLength > 16) {
    		fontSize = h*0.26;    		
    	} else if (maxStringLength > 8) {
    		fontSize = h*0.31;    		
    	} else {
    		fontSize = h*0.425;    		    		
    	}
		comboBox.setStyle("-fx-font-size: " + fontSize.toString() + "pt");			
    }
    
    public void uiCtlSetValuesArray(List<String> list) {
    	maxStringLength = 0;
    	for (int i = 0; i < list.size(); i++) {
    		if (list.get(i).length() > maxStringLength) {
    			maxStringLength = list.get(i).length();
    		}
    	}
    	int s = comboBox.getSelectionModel().getSelectedIndex();
    	comboBox.getItems().clear();
    	comboBox.getItems().addAll(list);
    	listValues = list;
    	comboBox.getSelectionModel().select(s);
    }
    
    public void uiCtlSetValue(Integer n, Boolean setFromSysex) {
    	//String stringValue;
    	if (intValue.intValue() != n.intValue()) {
        	//changedFromSet++;
        	changedFromSet = 1;
    	   	intValue = n;
        }
    	//System.out.printf("changedFromSet = %d for %s\n", changedFromSet, label.getText());
    	if (setFromSysex) {
    		setSyncState(Constants.SYNC_STATE_SYNCED);
    		mdIntValue = n;
    	} else {
        	updateSyncStateConditional();
    	}
	   	//stringValue = listValues.get(intValue);
    	//System.out.printf("ComboBox: index = %d, string at this index = %s\n", intValue, stringValue);
    	comboBox.getSelectionModel().select(n);
   }
   
   public Integer uiCtlGetValue() {
    	return intValue;
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
