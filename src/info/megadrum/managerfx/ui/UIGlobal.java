package info.megadrum.managerfx.ui;

import info.megadrum.managerfx.utils.Constants;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class UIGlobal {
	private HBox		hBoxPane;
	private Button		buttonGetAll;
	private Button		buttonSendAll;
	private Button		buttonLoadFromSlot;
	private Button		buttonSaveToSlot;
	private Button		buttonLoadAll;
	private Button		buttonSaveAll;
	private ComboBox<String>	comboBoxFile;
	private	Label		labelFileStatus;
	private Button		buttonPrevFile;
	private Button		buttonNextFile;
	private Label		labelMidiStatus;
	private ProgressBar	progressBarSysex;
	
	public UIGlobal () {
		hBoxPane = new HBox(2);
		//hBoxPane.setMinHeight(100);
		hBoxPane.setAlignment(Pos.CENTER_LEFT);
		hBoxPane.setStyle("-fx-border-color: lightgrey");
		buttonGetAll = new Button("GetAll");
		buttonGetAll.setMinWidth(50);
		buttonSendAll = new Button("SendAll");
		buttonSendAll.setMinWidth(50);
		buttonLoadFromSlot = new Button("LoadFromSlot");
		buttonLoadFromSlot.setMinWidth(80);
		buttonSaveToSlot = new Button("SaveToSlot");
		buttonSaveToSlot.setMinWidth(80);
		buttonLoadAll = new Button("LoadAll");
		buttonLoadAll.setMinWidth(50);
		buttonSaveAll = new Button("SaveAll");
		buttonSaveAll.setMinWidth(50);
		comboBoxFile = new ComboBox<>();
		labelFileStatus = new Label("Ok");
		labelFileStatus.setMinWidth(30);
		labelFileStatus.setAlignment(Pos.CENTER);
		//labelFileStatus.setAlignment(Pos.BOTTOM_CENTER);
		buttonPrevFile = new Button("prevCfg");
		buttonPrevFile.setMinWidth(55);
		buttonNextFile = new Button("nextCfg");
		buttonNextFile.setMinWidth(55);
		labelMidiStatus = new Label("");
		labelMidiStatus.setMinWidth(100);
		labelMidiStatus.setAlignment(Pos.CENTER);
		//labelMidiStatus.setAlignment(Pos.CENTER);
		progressBarSysex = new ProgressBar();
		progressBarSysex.setMinWidth(200);
		progressBarSysex.setMaxWidth(200);
		
		hBoxPane.getChildren().add(buttonGetAll);
		hBoxPane.getChildren().add(buttonSendAll);
		hBoxPane.getChildren().add(new Separator(Orientation.VERTICAL));
		hBoxPane.getChildren().add(buttonLoadFromSlot);
		hBoxPane.getChildren().add(buttonSaveToSlot);
		hBoxPane.getChildren().add(new Separator(Orientation.VERTICAL));
		hBoxPane.getChildren().add(buttonLoadAll);
		hBoxPane.getChildren().add(buttonSaveAll);
		hBoxPane.getChildren().add(comboBoxFile);
		hBoxPane.getChildren().add(labelFileStatus);
		hBoxPane.getChildren().add(buttonPrevFile);
		hBoxPane.getChildren().add(buttonNextFile);
		hBoxPane.getChildren().add(new Separator(Orientation.VERTICAL));
		hBoxPane.getChildren().add(labelMidiStatus);
		hBoxPane.getChildren().add(progressBarSysex);
		progressBarSysex.setVisible(false);
		for (int i = 0; i < hBoxPane.getChildren().size(); i++) {
			if (hBoxPane.getChildren().get(i) instanceof Button) {
				((Button)hBoxPane.getChildren().get(i)).setFont(new Font(10));
				
			}
		}
		comboBoxFile.getEditor().setFont(new Font(10));
		comboBoxFile.setMinWidth(200);
	}

	public ProgressBar getProgressBarSysex() {
		return progressBarSysex;
	}
	
	public Button getButtonGetAll() {
		return buttonGetAll;
	}
	
	public Button getButtonSendAll() {
		return buttonSendAll;
	}

	public Button getButtonLoadAll() {
		return buttonLoadAll;
	}
	
	public Button getButtonSaveAll() {
		return buttonSaveAll;
	}
	
	public ComboBox<String> getComboBoxFile() {
		return comboBoxFile;
	}

	public Button getButtonPrevFile() {
		return buttonPrevFile;
	}
	
	public Button getButtonNextFile() {
		return buttonNextFile;
	}

	public Node getUI() {
		return (Node) hBoxPane;
	}

	public void clearSysexStatusLabel() {
		labelMidiStatus.setVisible(false);
	}
	
	public void setSysexStatusLabel(int status, int sysex_type) {
		labelMidiStatus.setVisible(true);
		String backgroundColor = "lightgreen";
		String message = Constants.MD_SYSEX_STATUS_NAMES[Constants.MD_SYSEX_STATUS_OK];
		if (status == Constants.MD_SYSEX_STATUS_WORKING) {
			backgroundColor = "orange";
			message = Constants.MD_SYSEX_STATUS_NAMES[Constants.MD_SYSEX_STATUS_WORKING];
		} else if (status == Constants.MD_SYSEX_STATUS_MIDI_IS_NOT_OPEN) {
			backgroundColor = "pink";
			message = Constants.MD_SYSEX_STATUS_NAMES[Constants.MD_SYSEX_STATUS_MIDI_IS_NOT_OPEN];			
		} else if (status == Constants.MD_SYSEX_STATUS_MIDI_INIT_ERROR) {
			backgroundColor = "pink";
			message = Constants.MD_SYSEX_STATUS_NAMES[Constants.MD_SYSEX_STATUS_MIDI_INIT_ERROR];			
		} else if (status != Constants.MD_SYSEX_STATUS_OK) {
			backgroundColor = "red";
			message = "Sysex " + Constants.MD_SYSEX_NAMES[sysex_type] + " " + Constants.MD_SYSEX_STATUS_NAMES[status];
		}
		labelMidiStatus.setText(message);
		labelMidiStatus.setStyle("-fx-background-color: " + backgroundColor);
	}
}
