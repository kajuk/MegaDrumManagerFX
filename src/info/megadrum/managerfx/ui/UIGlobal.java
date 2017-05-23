package info.megadrum.managerfx.ui;

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
		buttonSendAll = new Button("SendAll");
		buttonLoadFromSlot = new Button("LoadFromSlot");
		buttonLoadFromSlot.setMinWidth(80);
		buttonSaveToSlot = new Button("SaveToSlot");
		buttonLoadAll = new Button("LoadAll");
		buttonSaveAll = new Button("SaveAll");
		comboBoxFile = new ComboBox<>();
		labelFileStatus = new Label("Ok");
		//labelFileStatus.setAlignment(Pos.BOTTOM_CENTER);
		buttonPrevFile = new Button("prevCfg");
		buttonNextFile = new Button("nextCfg");
		labelMidiStatus = new Label("MIDI");
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
	
	public Node getUI() {
		return (Node) hBoxPane;
	}

}
