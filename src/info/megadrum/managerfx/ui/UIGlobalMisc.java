package info.megadrum.managerfx.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;

import info.megadrum.managerfx.data.ConfigGlobalMisc;
import info.megadrum.managerfx.data.ConfigMisc;
import info.megadrum.managerfx.utils.Constants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class UIGlobalMisc {
	
	private GridPane 	rootGridPane;
	private Label 		labelMidi;
	private ToggleButton	toggleButtonMidi;
	private Label		labelInputs;
	private ComboBox<String> 	comboBoxInputs;
	private List<String> listValuesInputs;
	private Label		labelFwVersion;
	private	Label		lblFwVersion;
	private Label		labelLcdContrast;
	private SpinnerFast<Integer>		spinnerLcdContrast;
	private SpinnerValueFactory<Integer> valueFactoryLcd;
	private Label		labelMcu;
	private	Label		lblMcu;
	private Label		labelConfigNamesEn;
	private CheckBox	checkBoxConfigNamesEn;
	private Label		labelPadsNamesEn;
	private CheckBox	checkBoxPadsNamesEn;
	private Label		labelSlotsCount;
	private Label		lblSlotsCount;
	private Label		labelSlotCurrent;
	private Label		lblSlotCurrent;
	private Label		labelSlotName;
	private TextField	textSlotName;
	private Label		labelMidi2ForSysex;
	private CheckBox	checkBoxMidi2ForSysex;
	private Button		buttonGet;
	private Button		buttonSend;
	private Label		labelLiveUpdates;
	private CheckBox	checkBoxLiveUpdates;
	//private Button		buttonLiveUpdates;
	
	private ArrayList<Label> allControlLabels;

	private int			valueInputsCount = 18;
	private int			valueModuleInputsCount = 18;
	private int			changedFromSetInputs = 0;
	private Integer		valueLcdContrast = 50;
	private Integer		valueModuleLcdContrast = 50;
	private int			changedFromSetLcdContrast= 0;
	private boolean		valuePadsNamesEn = false;
	private boolean		valueModulePadsNamesEn = false;
	private int			changedFromSetPadsNamesEn = 0;
	private boolean		valueConfigNamesEn = false;
	private boolean		valueModuleConfigNamesEn = false;
	private int			changedFromSetConfigNamesEn = 0;
	private boolean		valueMidi2ForSysex = false;
	private boolean		valueModuleMidi2ForSysex = false;
	private int			changedFromSetMidi2ForSysex = 0;
	private int			syncState = Constants.SYNC_STATE_UNKNOWN;

	protected EventListenerList listenerList = new EventListenerList();
	
	public void addControlChangeEventListener(ControlChangeEventListener listener) {
		listenerList.add(ControlChangeEventListener.class, listener);
	}
	public void removeControlChangeEventListener(ControlChangeEventListener listener) {
		listenerList.remove(ControlChangeEventListener.class, listener);
	}
	protected void fireControlChangeEvent(ControlChangeEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i = i+2) {
			if (listeners[i] == ControlChangeEventListener.class) {
				((ControlChangeEventListener) listeners[i+1]).controlChangeEventOccurred(evt, 0);
			}
		}
	}

	public UIGlobalMisc() {
		allControlLabels = new ArrayList<Label>();
		rootGridPane = new GridPane();
		rootGridPane.setStyle("-fx-border-color: lightgrey");
		rootGridPane.setVgap(1.0);
		rootGridPane.setHgap(5.0);

		labelMidi = new Label("MIDI:");
		GridPane.setConstraints(labelMidi, 0, 0);
		GridPane.setHalignment(labelMidi, HPos.RIGHT);
		GridPane.setValignment(labelMidi, VPos.CENTER);
		rootGridPane.getChildren().add(labelMidi);

		toggleButtonMidi = new ToggleButton("Open MIDI");
		toggleButtonMidi.setFont(new Font(10));
		GridPane.setConstraints(toggleButtonMidi, 1, 0);
		GridPane.setHalignment(toggleButtonMidi, HPos.LEFT);
		GridPane.setValignment(toggleButtonMidi, VPos.CENTER);
		rootGridPane.getChildren().add(toggleButtonMidi);
		
		labelInputs = new Label("Inputs:");
		allControlLabels.add(labelInputs);
		GridPane.setConstraints(labelInputs, 0, 1);
		GridPane.setHalignment(labelInputs, HPos.RIGHT);
		GridPane.setValignment(labelInputs, VPos.CENTER);
		rootGridPane.getChildren().add(labelInputs);

		comboBoxInputs = new ComboBox<String>();
		comboBoxInputs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
		    	if (changedFromSetInputs > 0) {
		    		changedFromSetInputs--;
		        	//System.out.printf("changedFromSet reduced to %d for %s\n", changedFromSet, label.getText());
		    	} else {
			    	valueInputsCount = comboBoxInputs.getSelectionModel().getSelectedIndex()*2 + 18;
					fireControlChangeEvent(new ControlChangeEvent(this));
					if (syncState != Constants.SYNC_STATE_UNKNOWN) {
						if (valueInputsCount == valueModuleInputsCount) {
							labelInputs.setTextFill(Constants.SYNC_STATE_SYNCED_COLOR);
						} else {
							labelInputs.setTextFill(Constants.SYNC_STATE_NOT_SYNCED_COLOR);
						}
					}
		    	}
			}			
		});
		
		GridPane.setConstraints(comboBoxInputs, 1, 1);
		GridPane.setHalignment(comboBoxInputs, HPos.LEFT);
		GridPane.setValignment(comboBoxInputs, VPos.CENTER);
		rootGridPane.getChildren().add(comboBoxInputs);

		labelFwVersion = new Label("FW version:");
		allControlLabels.add(labelFwVersion);
		GridPane.setConstraints(labelFwVersion, 2, 0);
		GridPane.setHalignment(labelFwVersion, HPos.RIGHT);
		GridPane.setValignment(labelFwVersion, VPos.CENTER);
		rootGridPane.getChildren().add(labelFwVersion);

		lblFwVersion = new Label("FwVersion");
		lblFwVersion.setStyle("-fx-border-insets: 0; -fx-border-width: 2px; -fx-border-color: black lightgray lightgray black;");
		lblFwVersion.setMinWidth(80);
		lblFwVersion.setAlignment(Pos.CENTER);
		GridPane.setConstraints(lblFwVersion, 3, 0);
		GridPane.setHalignment(lblFwVersion, HPos.LEFT);
		GridPane.setValignment(lblFwVersion, VPos.CENTER);
		rootGridPane.getChildren().add(lblFwVersion);

		labelMcu = new Label("MCU:");
		allControlLabels.add(labelMcu);
		GridPane.setConstraints(labelMcu, 2, 1);
		GridPane.setHalignment(labelMcu, HPos.RIGHT);
		GridPane.setValignment(labelMcu, VPos.CENTER);
		rootGridPane.getChildren().add(labelMcu);

		lblMcu = new Label("mcu");
		lblMcu.setStyle("-fx-border-insets: 0; -fx-border-width: 2px; -fx-border-color: black lightgray lightgray black;");
		lblMcu.setMinWidth(100);
		lblMcu.setAlignment(Pos.CENTER);
		GridPane.setConstraints(lblMcu, 3, 1);
		GridPane.setHalignment(lblMcu, HPos.LEFT);
		GridPane.setValignment(lblMcu, VPos.CENTER);
		rootGridPane.getChildren().add(lblMcu);

		labelPadsNamesEn = new Label("PadsNamesEn:");
		allControlLabels.add(labelPadsNamesEn);
		GridPane.setConstraints(labelPadsNamesEn, 4, 0);
		GridPane.setHalignment(labelPadsNamesEn, HPos.RIGHT);
		GridPane.setValignment(labelPadsNamesEn, VPos.CENTER);
		rootGridPane.getChildren().add(labelPadsNamesEn);

		checkBoxPadsNamesEn = new CheckBox();
		checkBoxPadsNamesEn.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	valuePadsNamesEn = newValue;
		    	if (changedFromSetPadsNamesEn > 0 ) {
		    		changedFromSetPadsNamesEn = 0;
		    	} else {
					fireControlChangeEvent(new ControlChangeEvent(this));		    		
		    	}
				if (syncState != Constants.SYNC_STATE_UNKNOWN) {
					if (valuePadsNamesEn == valueModulePadsNamesEn) {
						labelPadsNamesEn.setTextFill(Constants.SYNC_STATE_SYNCED_COLOR);
					} else {
						labelPadsNamesEn.setTextFill(Constants.SYNC_STATE_NOT_SYNCED_COLOR);
					}
				}
		    }
		});
		GridPane.setConstraints(checkBoxPadsNamesEn, 5, 0);
		GridPane.setHalignment(checkBoxPadsNamesEn, HPos.LEFT);
		GridPane.setValignment(checkBoxPadsNamesEn, VPos.CENTER);
		rootGridPane.getChildren().add(checkBoxPadsNamesEn);

		labelConfigNamesEn = new Label("ConfigNamesEn:");
		allControlLabels.add(labelConfigNamesEn);
		GridPane.setConstraints(labelConfigNamesEn, 4, 1);
		GridPane.setHalignment(labelConfigNamesEn, HPos.RIGHT);
		GridPane.setValignment(labelConfigNamesEn, VPos.CENTER);
		rootGridPane.getChildren().add(labelConfigNamesEn);

		checkBoxConfigNamesEn = new CheckBox();
		checkBoxConfigNamesEn.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	valueConfigNamesEn = newValue;
		    	if (changedFromSetConfigNamesEn > 0 ) {
		    		changedFromSetConfigNamesEn = 0;
		    	} else {
					fireControlChangeEvent(new ControlChangeEvent(this));		    		
		    	}
				if (syncState != Constants.SYNC_STATE_UNKNOWN) {
					if (valueConfigNamesEn == valueModuleConfigNamesEn) {
						labelConfigNamesEn.setTextFill(Constants.SYNC_STATE_SYNCED_COLOR);
					} else {
						labelConfigNamesEn.setTextFill(Constants.SYNC_STATE_NOT_SYNCED_COLOR);
					}
				}
		    }
		});
		GridPane.setConstraints(checkBoxConfigNamesEn, 5, 1);
		GridPane.setHalignment(checkBoxConfigNamesEn, HPos.LEFT);
		GridPane.setValignment(checkBoxConfigNamesEn, VPos.CENTER);
		rootGridPane.getChildren().add(checkBoxConfigNamesEn);

		labelSlotsCount = new Label("Slots Count:");
		allControlLabels.add(labelSlotsCount);
		GridPane.setConstraints(labelSlotsCount, 6, 0);
		GridPane.setHalignment(labelSlotsCount, HPos.RIGHT);
		GridPane.setValignment(labelSlotsCount, VPos.CENTER);
		rootGridPane.getChildren().add(labelSlotsCount);

		lblSlotsCount = new Label("33");
		lblSlotsCount.setStyle("-fx-border-insets: 0; -fx-border-width: 2px; -fx-border-color: black lightgray lightgray black;");
		lblSlotsCount.setMinWidth(22);
		lblSlotsCount.setAlignment(Pos.CENTER);
		GridPane.setConstraints(lblSlotsCount, 7, 0);
		GridPane.setHalignment(lblSlotsCount, HPos.LEFT);
		GridPane.setValignment(lblSlotsCount, VPos.CENTER);
		rootGridPane.getChildren().add(lblSlotsCount);

		labelSlotCurrent = new Label("Current Slot:");
		allControlLabels.add(labelSlotCurrent);
		GridPane.setConstraints(labelSlotCurrent, 6, 1);
		GridPane.setHalignment(labelSlotCurrent, HPos.RIGHT);
		GridPane.setValignment(labelSlotCurrent, VPos.CENTER);
		rootGridPane.getChildren().add(labelSlotCurrent);

		lblSlotCurrent = new Label("1");
		lblSlotCurrent.setStyle("-fx-border-insets: 0; -fx-border-width: 2px; -fx-border-color: black lightgray lightgray black;");
		lblSlotCurrent.setMinWidth(22);
		lblSlotCurrent.setAlignment(Pos.CENTER);
		GridPane.setConstraints(lblSlotCurrent, 7, 1);
		GridPane.setHalignment(lblSlotCurrent, HPos.LEFT);
		GridPane.setValignment(lblSlotCurrent, VPos.CENTER);
		rootGridPane.getChildren().add(lblSlotCurrent);

		labelLcdContrast = new Label("LCD contrast:");
		allControlLabels.add(labelLcdContrast);
		GridPane.setConstraints(labelLcdContrast, 8, 0);
		GridPane.setHalignment(labelLcdContrast, HPos.RIGHT);
		GridPane.setValignment(labelLcdContrast, VPos.CENTER);
		rootGridPane.getChildren().add(labelLcdContrast);

		spinnerLcdContrast = new SpinnerFast<Integer>();
		spinnerLcdContrast.getEditor().textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// Spinner number validation
		    	if (changedFromSetLcdContrast > 0) {
		    		changedFromSetLcdContrast--;
		        	//System.out.printf("changedFromSet reduced to %d for %s\n", changedFromSet, label.getText());
		    	} else {
		        	//System.out.printf("Setting %s to %s\n", label.getText(), newValue);
		            if (!newValue.matches("\\d*")) {
		            	spinnerLcdContrast.getEditor().setText(valueLcdContrast.toString());
		            } else {
						if (newValue.matches("")) {
							spinnerLcdContrast.getEditor().setText(valueLcdContrast.toString());
						} else {
							if (valueLcdContrast.intValue() != Integer.valueOf(newValue).intValue()) {
								System.out.printf("%s: new value = %d, old value = %d\n",labelLcdContrast.getText(),Integer.valueOf(newValue),valueLcdContrast );
								valueLcdContrast = Integer.valueOf(newValue);
								fireControlChangeEvent(new ControlChangeEvent(this));
								if (syncState != Constants.SYNC_STATE_UNKNOWN) {
									if (valueLcdContrast.intValue() == valueModuleLcdContrast.intValue()) {
										labelLcdContrast.setTextFill(Constants.SYNC_STATE_SYNCED_COLOR);
									} else {
										labelLcdContrast.setTextFill(Constants.SYNC_STATE_NOT_SYNCED_COLOR);
									}
								}
								//resizeFont();
							}
						}
		            }		    		
		    	}
			}
	    });

		spinnerLcdContrast.setMaxWidth(70.0);
		spinnerLcdContrast.setMinWidth(70.0);
		GridPane.setConstraints(spinnerLcdContrast, 9, 0);
		GridPane.setHalignment(spinnerLcdContrast, HPos.LEFT);
		GridPane.setValignment(spinnerLcdContrast, VPos.CENTER);
		rootGridPane.getChildren().add(spinnerLcdContrast);
		valueFactoryLcd = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 50, 1);
		spinnerLcdContrast.setValueFactory(valueFactoryLcd);

		labelSlotName = new Label("Slot Name:");
		GridPane.setConstraints(labelSlotName, 8, 1);
		GridPane.setHalignment(labelSlotName, HPos.RIGHT);
		GridPane.setValignment(labelSlotName, VPos.CENTER);
		rootGridPane.getChildren().add(labelSlotName);

		textSlotName = new TextField("textSlotName");
		GridPane.setConstraints(textSlotName, 9, 1);
		GridPane.setHalignment(textSlotName, HPos.LEFT);
		GridPane.setValignment(textSlotName, VPos.CENTER);
		rootGridPane.getChildren().add(textSlotName);

		buttonGet = new Button("Get Globals  ");
		GridPane.setConstraints(buttonGet, 10, 0);
		GridPane.setHalignment(buttonGet, HPos.CENTER);
		GridPane.setValignment(buttonGet, VPos.CENTER);
		rootGridPane.getChildren().add(buttonGet);

		buttonSend = new Button("Send Globals");
		GridPane.setConstraints(buttonSend, 10, 1);
		GridPane.setHalignment(buttonSend, HPos.CENTER);
		GridPane.setValignment(buttonSend, VPos.CENTER);
		rootGridPane.getChildren().add(buttonSend);
		
		labelMidi2ForSysex = new Label("MIDI2 for Sysex only");
		allControlLabels.add(labelMidi2ForSysex);
		GridPane.setConstraints(labelMidi2ForSysex, 11, 0);
		GridPane.setHalignment(labelMidi2ForSysex, HPos.RIGHT);
		GridPane.setValignment(labelMidi2ForSysex, VPos.CENTER);
		rootGridPane.getChildren().add(labelMidi2ForSysex);

		checkBoxMidi2ForSysex = new CheckBox();
		checkBoxMidi2ForSysex.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	valueMidi2ForSysex = newValue;
		    	if (changedFromSetMidi2ForSysex > 0 ) {
		    		changedFromSetMidi2ForSysex = 0;
		    	} else {
					fireControlChangeEvent(new ControlChangeEvent(this));		    		
		    	}
				if (syncState != Constants.SYNC_STATE_UNKNOWN) {
					if (valueMidi2ForSysex == valueModuleMidi2ForSysex) {
						labelMidi2ForSysex.setTextFill(Constants.SYNC_STATE_SYNCED_COLOR);
					} else {
						labelMidi2ForSysex.setTextFill(Constants.SYNC_STATE_NOT_SYNCED_COLOR);
					}
				}
		    }
		});
		GridPane.setConstraints(checkBoxMidi2ForSysex, 12, 0);
		GridPane.setHalignment(checkBoxMidi2ForSysex, HPos.LEFT);
		GridPane.setValignment(checkBoxMidi2ForSysex, VPos.CENTER);
		rootGridPane.getChildren().add(checkBoxMidi2ForSysex);

		labelLiveUpdates = new Label("Live Updates");
		GridPane.setConstraints(labelLiveUpdates, 11, 1);
		GridPane.setHalignment(labelLiveUpdates, HPos.RIGHT);
		GridPane.setValignment(labelLiveUpdates, VPos.CENTER);
		rootGridPane.getChildren().add(labelLiveUpdates);

		checkBoxLiveUpdates = new CheckBox();
/*		
		checkBoxMidi2ForSysex.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	//TODO
		    }
		});
*/
		GridPane.setConstraints(checkBoxLiveUpdates, 12, 1);
		GridPane.setHalignment(checkBoxLiveUpdates, HPos.LEFT);
		GridPane.setValignment(checkBoxLiveUpdates, VPos.CENTER);
		rootGridPane.getChildren().add(checkBoxLiveUpdates);
		
/*
		buttonLiveUpdates = new Button("Live Updates");
		GridPane.setConstraints(buttonLiveUpdates, 11, 1);
		GridPane.setHalignment(buttonLiveUpdates, HPos.CENTER);
		GridPane.setValignment(buttonLiveUpdates, VPos.CENTER);
		rootGridPane.getChildren().add(buttonLiveUpdates);
*/	
		for (int i = 0; i < rootGridPane.getChildren().size(); i++) {
			if (rootGridPane.getChildren().get(i) instanceof Button) {
				((Button)rootGridPane.getChildren().get(i)).setFont(new Font(10));
				
			}
			if (rootGridPane.getChildren().get(i) instanceof Label) {
				((Label)rootGridPane.getChildren().get(i)).setFont(new Font(11));
				
			}
		}
		setAllStatesUnknown();
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(40.0)); // 0
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(70.0)); // 1
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(70.0)); // 2
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(110.0)); // 3
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(90.0)); // 4
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(20.0)); // 5
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(70.0)); // 6
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(25.0)); // 7
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(80.0)); // 8
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(100.0)); // 9
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(80.0)); // 10
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(120.0)); // 11
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(20.0)); // 12

		setMaxInputsCount(56);
		
	}
	
	public void setAllStatesUnknown() {
		syncState = Constants.SYNC_STATE_UNKNOWN;
		for (int i = 0; i < allControlLabels.size(); i++ ) {
			allControlLabels.get(i).setTextFill(Constants.SYNC_STATE_UNKNOWN_COLOR);
		}
		lblFwVersion.setText("????????");
		lblMcu.setText("????????");
		lblSlotCurrent.setText("?");
		lblSlotsCount.setText("??");
	}

	private void setGlobalMiscStatesKnown() {
		syncState = Constants.SYNC_STATE_SYNCED;
		labelInputs.setTextFill(Constants.SYNC_STATE_SYNCED_COLOR);
		labelLcdContrast.setTextFill(Constants.SYNC_STATE_SYNCED_COLOR);
		labelPadsNamesEn.setTextFill(Constants.SYNC_STATE_SYNCED_COLOR);
		labelConfigNamesEn.setTextFill(Constants.SYNC_STATE_SYNCED_COLOR);
		labelMidi2ForSysex.setTextFill(Constants.SYNC_STATE_SYNCED_COLOR);
	}

	public Node getUI() {
		return (Node) rootGridPane;
	}
	
	public void setVersion(Integer version) {
		lblFwVersion.setText(version.toString());
		labelFwVersion.setTextFill(Constants.SYNC_STATE_SYNCED_COLOR);
		//TODO
/*
		if (version < Constants.MD_MINIMUM_VERSION) {
			lblFwVersion.setBackground(Color.RED);
		} else {
			lblFwVersion.setBackground(Color.GREEN);
		}
*/
	}

	public void setMcu(Integer mcu) {
		lblMcu.setText(Constants.MCU_TYPES[mcu]);
		labelMcu.setTextFill(Constants.SYNC_STATE_SYNCED_COLOR);
	}
	
	public void setConfigsCount(Integer count) {
		lblSlotsCount.setText(count.toString());
		labelSlotsCount.setTextFill(Constants.SYNC_STATE_SYNCED_COLOR);
	}

	public void setConfigCurrent(int current) {
		Integer c = current + 1;
		lblSlotCurrent.setText(c.toString());
		labelSlotCurrent.setTextFill(Constants.SYNC_STATE_SYNCED_COLOR);
	}

	public void setControlsFromConfig(ConfigGlobalMisc config, Boolean setFromSysex) {
		if (setFromSysex) {
			syncState = Constants.SYNC_STATE_SYNCED;
			valueModuleLcdContrast = config.lcd_contrast;
			valueModuleConfigNamesEn = config.config_names_en;
			valueModulePadsNamesEn = config.custom_names_en;
			valueModuleMidi2ForSysex = config.midi2_for_sysex;
			setGlobalMiscStatesKnown();
		}
		setInputsCount(config.inputs_count, setFromSysex);
		//spinnerLcdContrast.getEditor().setText(Integer.toString(config.lcd_contrast));
		if (spinnerLcdContrast.getValueFactory().getValue().intValue() != config.lcd_contrast ) {
			changedFromSetLcdContrast = 1;
			spinnerLcdContrast.getValueFactory().setValue(config.lcd_contrast);
		}
		if (checkBoxConfigNamesEn.isSelected() != config.config_names_en) {
			changedFromSetConfigNamesEn = 1;
			checkBoxConfigNamesEn.setSelected(config.config_names_en);			
		}
		if (checkBoxPadsNamesEn.isSelected() != config.custom_names_en) {
			changedFromSetPadsNamesEn = 1;
			checkBoxPadsNamesEn.setSelected(config.custom_names_en);
		}
		if (checkBoxMidi2ForSysex.isSelected() != config.midi2_for_sysex) {
			changedFromSetMidi2ForSysex = 1;
			checkBoxMidi2ForSysex.setSelected(config.midi2_for_sysex);
		}
	}
	
	public void setConfigFromControls(ConfigGlobalMisc config) {
		config.inputs_count = valueInputsCount;
		config.lcd_contrast = valueLcdContrast;
		config.config_names_en = valueConfigNamesEn;
		config.custom_names_en = valuePadsNamesEn;
		config.midi2_for_sysex = valueMidi2ForSysex;
	}
	
	private void setMaxInputsCount(Integer maxInputs) {
		Integer n;
		n = (maxInputs - 16)/2;
		if (listValuesInputs == null) {
			listValuesInputs = new ArrayList<String>();
		}
		listValuesInputs.clear();
		for (int i = 0; i < n; i++) {
			listValuesInputs.add(Integer.toString((i*2)+18));
		}
		comboBoxInputs.getItems().clear();
		comboBoxInputs.getItems().addAll(listValuesInputs);
		comboBoxInputs.getSelectionModel().select((valueInputsCount-18)/2);
	}
	
	private void setInputsCount(int count, Boolean setFromSysex) {
		if (count != valueInputsCount) {
			changedFromSetInputs = 1;
			valueInputsCount = count;			
		}
    	if (setFromSysex) {
    		labelInputs.setTextFill(Constants.SYNC_STATE_SYNCED_COLOR);
    		valueModuleInputsCount = count;
    	}
    	comboBoxInputs.getSelectionModel().select((count-18)/2);
	}
	
	//private int getInputsCount() {
	//	return comboBoxInputs.getSelectionModel().getSelectedIndex()*2 + 18;
	//}
	
	public Button getButtonGet() {
		return buttonGet;
	}
	
	public Button getButtonSend() {
		return buttonSend;
	}
	
	public ToggleButton getToggleButtonMidi() {
		return toggleButtonMidi;
	}
	
	public CheckBox getCheckBoxLiveUpdates() {
		return checkBoxLiveUpdates;
	}
	
	public TextField getTextFieldSlotName() {
		return textSlotName;
	}
	
}
