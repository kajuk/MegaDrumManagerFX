package info.megadrum.managerfx.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.swing.event.EventListenerList;

import info.megadrum.managerfx.data.Config3rd;
import info.megadrum.managerfx.data.ConfigPad;
import info.megadrum.managerfx.data.ConfigPositional;
import info.megadrum.managerfx.utils.Constants;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class UIPad extends UIPanel implements PanelInterface {

	private UIInput 	uiInputLeft;
	private UIInput 	uiInputRight;
	private UI3rdZone	ui3rdZone;
	private HBox		toolBarNavigator;
	private HBox		toolBarTop;
	private Button 		buttonGetAll;
	private Button 		buttonSendAll;
	private Button 		buttonCopy;
	//private Button 		buttonDisableOthers;
	
	private Label		labelInput;
	private ComboBox<String>	comboBoxInput;
	private	Button		buttonFirst;
	private	Button		buttonPrev;
	private	Button		buttonNext;
	private	Button		buttonLast;
	
	//private	Integer		inputPair; // 0 for Kick, 1 for HiHat Bow/Edge, 2 for Snare Head/Rim and so on
	private String[]	arrayNamesLeft;
	private String[]	arrayNamesRight;
	private Boolean		customNamesEnabled = false;
	//private Integer		customNamesCount = 0;
	private String[]	arrayCustomNames;
	//private ConfigPad	configPadLeft;
	//private ConfigPad	configPadRight;
	//private ConfigPositional	configPosLeft;
	//private ConfigPositional	configPosRight;
	//private Integer		padPair;
	private Boolean		nameChanged = false;
	//private Integer		controlsCha
	private Boolean			copyPressed = false;
	private int				copyPressedValueId = -1;
	private Double			inputsGap = 5.0;

	protected EventListenerList listenerList = new EventListenerList();
	
	public void addControlChangeEventListener(ControlChangeEventListener listener) {
		listenerList.add(ControlChangeEventListener.class, listener);
	}
	public void removeControlChangeEventListener(ControlChangeEventListener listener) {
		listenerList.remove(ControlChangeEventListener.class, listener);
	}
	
	protected void fireControlChangeEvent(ControlChangeEvent evt, Integer left) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i = i+2) {
			if (listeners[i] == ControlChangeEventListener.class) {
				((ControlChangeEventListener) listeners[i+1]).controlChangeEventOccurred(evt, left);
			}
		}
	}

	public UIPad(String title) {
		super(title);
		HBox hBox = new HBox(inputsGap);
		uiInputLeft = new UIInput("Head/Bow");
		uiInputLeft.setHeadEdgeType(Constants.PAD_TYPE_HEAD);
		uiInputRight = new UIInput("Rim/Edge");
		uiInputRight.setHeadEdgeType(Constants.PAD_TYPE_EDGE);
		ui3rdZone = new UI3rdZone();
		hBox.getChildren().addAll(uiInputLeft.getUI(),uiInputRight.getUI());
		hBox.setAlignment(Pos.TOP_CENTER);

		toolBarTop = new HBox();
		toolBarTop.setAlignment(Pos.CENTER_LEFT);
		buttonGetAll = new Button("GetAll");
		buttonSendAll = new Button("SendAll");
		buttonCopy = new Button("Copy");
		toolBarTop.getChildren().addAll(buttonGet,buttonSend,buttonGetAll,buttonSendAll,new Separator(Orientation.VERTICAL),buttonLoad,buttonSave,new Separator(Orientation.VERTICAL),buttonCopy);
		//buttonDisableOthers = new Button("Disable Others");
		//toolBarTop.getChildren().add(buttonDisableOthers);
		toolBarTop.setStyle("-fx-padding: 0.1em 0.0em 0.2em 0.05em");
		
		toolBarNavigator = new HBox();
		toolBarNavigator.setAlignment(Pos.CENTER_LEFT);
		labelInput = new Label("Input(s):");
		comboBoxInput = new ComboBox<String>();
		buttonFirst = new Button("First");
		buttonPrev = new Button("Prev");
		buttonNext = new Button("Next");
		buttonLast = new Button("Last");
		toolBarNavigator.getChildren().addAll(labelInput, comboBoxInput, buttonFirst, buttonPrev, buttonNext, buttonLast);
		toolBarNavigator.setStyle("-fx-padding: 0.05em 0.0em 0.2em 0.05em");
		
		vBoxAll.getChildren().addAll(toolBarTop,toolBarNavigator,hBox,ui3rdZone.getUI());
		//vBoxAll.setAlignment(Pos.TOP_CENTER);
		reCreateNamesArray();
		switchToInputPair(0);
    	uiInputLeft.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				if (parameter == Constants.CONTROL_CHANGE_EVENT_NAME) {
					nameChanged = true;
				}
				if (uiInputLeft.isCopyPressed()) {
					copyPressed = true;
					copyPressedValueId = uiInputLeft.getCopyPressedValueId();
					uiInputLeft.resetCopyPressed();
				}
				fireControlChangeEvent(new ControlChangeEvent(this), Constants.CONTROL_CHANGE_EVENT_LEFT_INPUT);
			}
		});
    	uiInputRight.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				if (parameter == Constants.CONTROL_CHANGE_EVENT_NAME) {
					nameChanged = true;
				}
				if (uiInputRight.isCopyPressed()) {
					copyPressed = true;
					copyPressedValueId = uiInputRight.getCopyPressedValueId();
					uiInputRight.resetCopyPressed();
				}
				fireControlChangeEvent(new ControlChangeEvent(this), Constants.CONTROL_CHANGE_EVENT_RIGHT_INPUT);
			}
		});
    	ui3rdZone.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				if (parameter == Constants.CONTROL_CHANGE_EVENT_NAME) {
					nameChanged = true;
				}
				if (ui3rdZone.isCopyPressed()) {
					copyPressed = true;
					copyPressedValueId = ui3rdZone.getCopyPressedValueId();
					ui3rdZone.resetCopyPressed();
				}
				fireControlChangeEvent(new ControlChangeEvent(this), Constants.CONTROL_CHANGE_EVENT_3RD_INPUT);
			}
		});
    	
		setDetached(false);
    	setAllStatesUnknown(false, false, false);
	}

	public int getCopyPressedValueId() {
		return copyPressedValueId;
	}
	
	public Boolean isCopyPressed() {
		return copyPressed;
	}
	
	public void resetCopyPressed() {
		copyPressed = false;
	}
	
	public Boolean isNameChanged() {
		return nameChanged;
	}
	
	public void resetNameChanged() {
		nameChanged = false;
	}
	
	private void reCreateNamesArray() {
		if (arrayNamesLeft != null) {
			arrayNamesLeft = null;
		}
		if (arrayNamesRight != null) {
			arrayNamesRight = null;
		}
		if (customNamesEnabled) {
			arrayNamesLeft = new String[Constants.CUSTOM_PADS_NAMES_LIST.length + arrayCustomNames.length + 1];			
			arrayNamesRight = new String[Constants.CUSTOM_PADS_NAMES_LIST.length + arrayCustomNames.length + 1];			
		} else {
			arrayNamesLeft = new String[Constants.CUSTOM_PADS_NAMES_LIST.length + 1];			
			arrayNamesRight = new String[Constants.CUSTOM_PADS_NAMES_LIST.length + 1];			
		}
		for (int i=0; i < Constants.CUSTOM_PADS_NAMES_LIST.length; i++) {
			arrayNamesLeft[i+1] = Constants.CUSTOM_PADS_NAMES_LIST[i];
			arrayNamesRight[i+1] = Constants.CUSTOM_PADS_NAMES_LIST[i];
		}
		if (customNamesEnabled) {
			for (int i=0; i < arrayCustomNames.length; i++) {
				arrayNamesLeft[i + 1 + Constants.CUSTOM_PADS_NAMES_LIST.length] = arrayCustomNames[i];
				arrayNamesRight[i + 1 + Constants.CUSTOM_PADS_NAMES_LIST.length] = arrayCustomNames[i];
			}
		}
	}

	public void respondToResizeDetached() {
		Double w = windowDetached.getScene().getWidth();
		Double h = windowDetached.getScene().getHeight();
		Double controlW = w/(Constants.FX_INPUT_CONTROL_WIDTH_MUL*2.05);
		Double controlH;
		if (showAdvanced) {
			controlH = (h/(uiInputLeft.getVerticalControlsCount() + ui3rdZone.getVerticalControlsCount()))*0.95 - 0.5;
		} else {
			controlH = (h/(uiInputLeft.getVerticalControlsCount() + ui3rdZone.getVerticalControlsCount()))*0.91 - 0.5;			
		}
		respondToResize(w, h, controlW*1.01, controlH);
	}

	public void respondToResize (Double w, Double h, Double cW, Double cH) {
		super.respondToResize(w, h, cW, cH);

		toolBarTop.setStyle("-fx-font-size: " + buttonFontSize.toString() + "pt");
		toolBarNavigator.setStyle("-fx-font-size: " + buttonFontSize.toString() + "pt");
		comboBoxInput.setStyle("-fx-font-size: " + comboBoxFontHeight.toString() + "pt");
		titledPane.setWidth(controlW*Constants.FX_INPUT_CONTROL_WIDTH_MUL*2 + inputsGap*2);
		vBoxAll.setLayoutY(lastTitleHeight);
		vBoxAll.setLayoutX(0);
		vBoxAll.setMaxWidth(controlW*1.0*Constants.FX_INPUT_CONTROL_WIDTH_MUL*2);
		toolBarTop.setMaxWidth(controlW*1.0*Constants.FX_INPUT_CONTROL_WIDTH_MUL*2 + inputsGap);
		toolBarNavigator.setMaxWidth(controlW*1.0*Constants.FX_INPUT_CONTROL_WIDTH_MUL*2 + inputsGap);
		toolBarTop.setMaxHeight(controlH);
		toolBarNavigator.setMaxHeight(controlH);
		//toolBarTop.setStyle("-fx-background-color: orange");
		//toolBarNavigator.setStyle("-fx-background-color: orange");
		//vBoxAll.setStyle("-fx-background-color: lightgreen");
		//titledPane.setStyle("-fx-background-color: lightblue");

		uiInputLeft.respondToResize( w*0.5, h*0.6, controlW, controlH);
		uiInputRight.respondToResize(w*0.5, h*0.6, controlW, controlH);
		ui3rdZone.respondToResize(controlW*Constants.FX_INPUT_CONTROL_WIDTH_MUL*2 + inputsGap*2, h*0.0915, controlW, controlH);
		comboBoxInput.setMinWidth(controlH*6);
		comboBoxInput.setMaxWidth(controlH*6);
		labelInput.setFont(new Font(controlH*0.4));
	}

	public void setControlsFromConfigPad(ConfigPad configPad, Boolean leftInput, Boolean setFromSysex) {
		if (leftInput) {
			uiInputLeft.setControlsFromConfigPad(configPad, setFromSysex);			
			ui3rdZone.getUI().setVisible(configPad.getTypeInt() > 0);
		} else {
			uiInputRight.setControlsFromConfigPad(configPad, setFromSysex);						
			ui3rdZone.setZoneType(configPad.getTypeInt() > 0);
		}
	}

	public void setControlsFromConfigPos(ConfigPositional configPos, Boolean leftInput, Boolean setFromSysex) {
		if (leftInput) {
			uiInputLeft.setControlsFromConfigPos(configPos, setFromSysex);			
		} else {
			uiInputRight.setControlsFromConfigPos(configPos, setFromSysex);						
		}
	}

	public void setConfigFromControlsPad(ConfigPad config,Boolean leftInput ) {
		if (leftInput) {
			uiInputLeft.setConfigPadFromControls(config);
			ui3rdZone.getUI().setVisible(config.getTypeInt() > 0);
		} else {
			uiInputRight.setConfigPadFromControls(config);
			ui3rdZone.setZoneType(config.getTypeInt() > 0);
		}
	}
	
	public void setConfigPosFromControlsPad(ConfigPositional config,Boolean leftInput ) {
		if (leftInput) {
			uiInputLeft.setConfigPosFromControls(config);
		} else {
			uiInputRight.setConfigPosFromControls(config);
		}
	}

	public void setControlsFromConfig3rd(Config3rd config,Boolean setFromSysex ) {
		ui3rdZone.setControlsFromConfig3rd(config, setFromSysex);
	}

	public void setConfig3rdFromControlsPad(Config3rd config ) {
		ui3rdZone.setConfig3rdFromControls(config);
	}

	public void setAllStatesUnknown(Boolean leftKnown, Boolean rightKnown, Boolean zone3rdKnow) {
		if (!leftKnown) {
			uiInputLeft.setAllStateUnknown();
		}
		if (!rightKnown) {
			uiInputRight.setAllStateUnknown();
		}
		if (!zone3rdKnow) {
			ui3rdZone.setAllStateUnknown();
		}
	}
	
	public void setMdValuesPad(ConfigPad configPad, ConfigPositional configPos, Boolean left ) {
		UIInput ui;
		if (left) {
			ui = uiInputLeft;
		} else {
			ui = uiInputRight;
		}
		ui.setMdValuesFromConfigPad(configPad);
		ui.setMdValuesFromConfigPos(configPos);
	}
	
	public void setMdValues3rd(Config3rd config) {
		ui3rdZone.setMdValuesFromConfig3rd(config);
	}
	
	public void setZoneType(Boolean zoneSwitchType) {
		ui3rdZone.setZoneType(zoneSwitchType);
	}
	
	public void setInputPair(Integer pair, ConfigPad configPadLeft, ConfigPositional configPosLeft, ConfigPad configPadRight, ConfigPositional configPosRight, Config3rd config3rd) {
		switchToInputPair(pair);
		if (pair == 0) {
			setControlsFromConfigPad(configPadLeft, true, false);
			setControlsFromConfigPos(configPosLeft, true, false);
			ui3rdZone.getUI().setVisible(false);
		} else {
			setControlsFromConfigPad(configPadLeft, true, false);
			setControlsFromConfigPos(configPosLeft, true, false);			
			setControlsFromConfigPad(configPadRight, false, false);
			setControlsFromConfigPos(configPosRight, false, false);
			setControlsFromConfig3rd(config3rd, false);
		}
	}
	
	private void switchToInputPair(Integer pair) {
		//padPair = pair;
		List<String> listNames;
		if (pair == 0) {
			arrayNamesLeft[0] = Constants.PADS_NAMES_LIST[0];
			listNames = Arrays.asList(arrayNamesLeft);
			uiInputLeft.setNameList(listNames);
			uiInputLeft.setTypeDisable(true);
			uiInputRight.getUI().setVisible(false);
		} else {
			arrayNamesLeft[0] = Constants.PADS_NAMES_LIST[((pair - 1)*2) + 1];
			listNames = Arrays.asList(arrayNamesLeft);
			uiInputLeft.setNameList(listNames);
			uiInputLeft.setTypeDisable(false);
			arrayNamesRight[0] = Constants.PADS_NAMES_LIST[((pair - 1)*2) + 2];
			listNames = Arrays.asList(arrayNamesRight);
			uiInputRight.setNameList(listNames);
			uiInputRight.getUI().setVisible(true);
		}
	}

	public void addAllCustomNames(String[] names) {
		arrayCustomNames = names;
		customNamesEnabled = true;
		reCreateNamesArray();
	}
	
	public void removeAllCustomNames() {
		customNamesEnabled = false;
		reCreateNamesArray();
	}
	
	public Button getButtonGetAll() {
		return buttonGetAll;
	}

	public Button getButtonSendAll() {
		return buttonSendAll;
	}

	public Button getButtonPrev() {
		return buttonPrev;
	}
	
	public Button getButtonNext() {
		return buttonNext;
	}
	
	public Button getButtonFirst() {
		return buttonFirst;
	}
	
	public Button getButtonLast() {
		return buttonLast;
	}
	
	public Button getButtonCopy() {
		return buttonCopy;
	}
	
	public ComboBox<String> getComboBoxInput() {
		return comboBoxInput;
	}

	@Override
	public int getVerticalControlsCount() {
		return uiInputLeft.getVerticalControlsCount() + ui3rdZone.getVerticalControlsCount() + 3;
	}

	@Override
	public void setShowAdvanced(Boolean show) {
		showAdvanced = show;
		uiInputLeft.setShowAdvanced(show);
		uiInputRight.setShowAdvanced(show);
		ui3rdZone.setShowAdvanced(show);
	}

}
