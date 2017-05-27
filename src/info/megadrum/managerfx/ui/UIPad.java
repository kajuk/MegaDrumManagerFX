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

public class UIPad extends Parent {

	private TitledPane		titledPane;

	private VBox		vBox;
	private UIInput 	uiInputLeft;
	private UIInput 	uiInputRight;
	private UI3rdZone	ui3rdZone;
	private ToolBar		toolBarNavigator;
	private ToolBar		toolBarTop;
	private Button 		buttonGet;
	private Button 		buttonSend;
	private Button 		buttonGetAll;
	private Button 		buttonSendAll;
	private Button 		buttonLoad;
	private Button 		buttonSave;
	private Button 		buttonCopy;
	private Button 		buttonDisableOthers;
	
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
	private Integer		padPair;
	private Boolean		nameChanged = false;
	//private Integer		controlsCha

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
		// TODO Auto-generated constructor stub
		HBox hBox = new HBox(5);
		uiInputLeft = new UIInput("Head/Bow");
		uiInputLeft.setHeadEdgeType(Constants.PAD_TYPE_HEAD);
		uiInputRight = new UIInput("Rim/Edge");
		uiInputRight.setHeadEdgeType(Constants.PAD_TYPE_EDGE);
		ui3rdZone = new UI3rdZone();
		hBox.getChildren().addAll(uiInputLeft.getUI(),uiInputRight.getUI());
		vBox = new VBox(1);
		vBox.setStyle("-fx-padding: 0.0em 0.0em 0.2em 0.0em");

		toolBarTop = new ToolBar();
		buttonGet = new Button("Get");
		buttonSend = new Button("Send");
		buttonGetAll = new Button("GetAll");
		buttonSendAll = new Button("SendAll");
		buttonLoad = new Button("Load");
		buttonSave = new Button("Save");
		buttonCopy = new Button("Copy");
		buttonDisableOthers = new Button("Disable Others");
		toolBarTop.getItems().addAll(buttonGet,buttonSend,buttonGetAll,buttonSendAll,new Separator(),buttonLoad,buttonSave,new Separator(),buttonCopy,buttonDisableOthers);

		toolBarNavigator = new ToolBar();
		labelInput = new Label("Input(s):");
		comboBoxInput = new ComboBox<String>();
		buttonFirst = new Button("First");
		buttonPrev = new Button("Prev");
		buttonNext = new Button("Next");
		buttonLast = new Button("Last");
		toolBarNavigator.getItems().addAll(labelInput, comboBoxInput, buttonFirst, buttonPrev, buttonNext, buttonLast);
		
		vBox.getChildren().addAll(toolBarTop,toolBarNavigator,hBox,ui3rdZone.getUI());
		titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(vBox);
		titledPane.setCollapsible(false);
		reCreateNamesArray();
		switchToInputPair(0);
    	uiInputLeft.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				// TODO Auto-generated method stub
				if (parameter == Constants.CONTROL_CHANGE_EVENT_NAME) {
					nameChanged = true;
				}
				fireControlChangeEvent(new ControlChangeEvent(this), Constants.CONTROL_CHANGE_EVENT_LEFT_INPUT);
			}
		});
    	uiInputRight.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				// TODO Auto-generated method stub
				if (parameter == Constants.CONTROL_CHANGE_EVENT_NAME) {
					nameChanged = true;
				}
				fireControlChangeEvent(new ControlChangeEvent(this), Constants.CONTROL_CHANGE_EVENT_RIGHT_INPUT);
			}
		});
    	ui3rdZone.addControlChangeEventListener(new ControlChangeEventListener() {
			
			@Override
			public void controlChangeEventOccurred(ControlChangeEvent evt, Integer parameter) {
				// TODO Auto-generated method stub
				if (parameter == Constants.CONTROL_CHANGE_EVENT_NAME) {
					nameChanged = true;
				}
				fireControlChangeEvent(new ControlChangeEvent(this), Constants.CONTROL_CHANGE_EVENT_3RD_INPUT);
			}
		});
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
	
	public void respondToResize (Double h, Double w, Double fullHeight, Double controlH, Double controlW) {
		Double toolBarFontHeight = fullHeight*Constants.FX_TITLEBARS_FONT_SCALE;
		Double titledPaneFontHeight = toolBarFontHeight*1.4;
		Double h1,h2,h3;
		if (toolBarFontHeight > Constants.FX_TITLEBARS_FONT_MIN_SIZE) {
			//System.out.printf("ToolBar font size = %f\n",fontHeight);
			toolBarTop.setStyle("-fx-font-size: " + toolBarFontHeight.toString() + "pt");			
			toolBarNavigator.setStyle("-fx-font-size: " + toolBarFontHeight.toString() + "pt");			
			titledPane.setStyle("-fx-font-size: " + titledPaneFontHeight.toString() + "pt");			
		} else {
			toolBarTop.setStyle("-fx-font-size: " + Constants.FX_TITLEBARS_FONT_MIN_SIZE.toString() + "pt");			
			toolBarTop.setStyle("-fx-font-size: " + Constants.FX_TITLEBARS_FONT_MIN_SIZE.toString() + "pt");			
			titledPane.setStyle("-fx-font-size: " + Constants.FX_TITLEBARS_FONT_MIN_SIZE.toString() + "pt");						
		}
		toolBarNavigator.setStyle("-fx-padding: 0.0em 0.0em 0.2em 0.0em");
		toolBarTop.setStyle("-fx-padding: 0.0em 0.0em 0.2em 0.0em");
		uiInputLeft.respondToResize(h*0.6, w*0.5, fullHeight, controlH, controlW);
		uiInputRight.respondToResize(h*0.6, w*0.5, fullHeight, controlH, controlW);
		ui3rdZone.respondToResize(h*0.0915, w*1.0, fullHeight, controlH, controlW);
		comboBoxInput.setMinWidth(controlH*6);
		comboBoxInput.setMaxWidth(controlH*6);
		labelInput.setFont(new Font(controlH*0.4));
	}

	public Node getUI() {
		return (Node) titledPane;
	}
	
	public void switchToInputPairWithConfig(Integer pair, ConfigPad configLeft, ConfigPad configRight) {
		if (pair == 0) {
			
		} else {
			
		}
	}
		
	public void setControlsFromConfigPad(ConfigPad configPad, Boolean leftInput, Boolean setFromSysex) {
		if (leftInput) {
			uiInputLeft.setControlsFromConfigPad(configPad, setFromSysex);			
		} else {
			uiInputRight.setControlsFromConfigPad(configPad, setFromSysex);						
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
		} else {
			uiInputRight.setConfigPadFromControls(config);
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

	public void setInputPair(Integer pair, ConfigPad configPadLeft, ConfigPositional configPosLeft, ConfigPad configPadRight, ConfigPositional configPosRight, Config3rd config3rd) {
		switchToInputPair(pair);
		if (pair == 0) {
			setControlsFromConfigPad(configPadLeft, true, false);
			setControlsFromConfigPos(configPosLeft, true, false);
		} else {
			setControlsFromConfigPad(configPadLeft, true, false);
			setControlsFromConfigPos(configPosLeft, true, false);			
			setControlsFromConfigPad(configPadRight, false, false);
			setControlsFromConfigPos(configPosRight, false, false);
			setControlsFromConfig3rd(config3rd, false);
		}
	}
	
	private void switchToInputPair(Integer pair) {
		padPair = pair;
		List<String> listNames;
		if (pair == 0) {
			arrayNamesLeft[0] = Constants.PADS_NAMES_LIST[0];
			listNames = Arrays.asList(arrayNamesLeft);
			uiInputLeft.setNameList(listNames);
			uiInputRight.getUI().setVisible(false);
		} else {
			arrayNamesLeft[0] = Constants.PADS_NAMES_LIST[((pair - 1)*2) + 1];
			listNames = Arrays.asList(arrayNamesLeft);
			uiInputLeft.setNameList(listNames);
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
	
	public Button getButtonGet() {
		return buttonGet;
	}

	public Button getButtonSend() {
		return buttonSend;
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
	
	public ComboBox<String> getComboBoxInput() {
		return comboBoxInput;
	}

}
