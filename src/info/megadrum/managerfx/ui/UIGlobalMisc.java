package info.megadrum.managerfx.ui;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class UIGlobalMisc {
	
	private GridPane 	rootGridPane;
	private Label 		labelMidi;
	private Button		buttonMidi;
	private Label		labelInputs;
	private ComboBox<Integer> 	comboBoxInputs;
	private Label		labelFwVersion;
	private	Label		lblFwVersion;
	private Label		labelLcdContrast;
	private Spinner<Integer>		spinnerLcdContrast;
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
	private Text		textSlotName;
	private Label		labelMidi2ForSysex;
	private CheckBox	checkBoxMidi2ForSysex;
	private Button		buttonGet;
	private Button		buttonSend;
	private Button		buttonLiveUpdates;
	
	
	
	public UIGlobalMisc() {
		rootGridPane = new GridPane();
		rootGridPane.setStyle("-fx-border-color: lightgrey");
		rootGridPane.setVgap(1.0);
		rootGridPane.setHgap(5.0);

		labelMidi = new Label("MIDI:");
		GridPane.setConstraints(labelMidi, 0, 0);
		GridPane.setHalignment(labelMidi, HPos.LEFT);
		GridPane.setValignment(labelMidi, VPos.CENTER);
		rootGridPane.getChildren().add(labelMidi);

		buttonMidi = new Button("Open MIDI");
		GridPane.setConstraints(buttonMidi, 1, 0);
		GridPane.setHalignment(buttonMidi, HPos.LEFT);
		GridPane.setValignment(buttonMidi, VPos.CENTER);
		rootGridPane.getChildren().add(buttonMidi);
		
		labelInputs = new Label("Inputs:");
		GridPane.setConstraints(labelInputs, 0, 1);
		GridPane.setHalignment(labelInputs, HPos.LEFT);
		GridPane.setValignment(labelInputs, VPos.CENTER);
		rootGridPane.getChildren().add(labelInputs);

		comboBoxInputs = new ComboBox<Integer>();
		GridPane.setConstraints(comboBoxInputs, 1, 1);
		GridPane.setHalignment(comboBoxInputs, HPos.LEFT);
		GridPane.setValignment(comboBoxInputs, VPos.CENTER);
		rootGridPane.getChildren().add(comboBoxInputs);

		labelFwVersion = new Label("FW version:");
		GridPane.setConstraints(labelFwVersion, 2, 0);
		GridPane.setHalignment(labelFwVersion, HPos.LEFT);
		GridPane.setValignment(labelFwVersion, VPos.CENTER);
		rootGridPane.getChildren().add(labelFwVersion);

		lblFwVersion = new Label("FwVersion");
		lblFwVersion.setStyle("-fx-border-insets: 0; -fx-border-width: 2px; -fx-border-color: black lightgray lightgray black;");
		GridPane.setConstraints(lblFwVersion, 3, 0);
		GridPane.setHalignment(lblFwVersion, HPos.LEFT);
		GridPane.setValignment(lblFwVersion, VPos.CENTER);
		rootGridPane.getChildren().add(lblFwVersion);

		labelLcdContrast = new Label("LCD contrast:");
		GridPane.setConstraints(labelLcdContrast, 2, 1);
		GridPane.setHalignment(labelLcdContrast, HPos.LEFT);
		GridPane.setValignment(labelLcdContrast, VPos.CENTER);
		rootGridPane.getChildren().add(labelLcdContrast);

		spinnerLcdContrast = new Spinner<Integer>();
		spinnerLcdContrast.setMaxWidth(65.0);
		spinnerLcdContrast.setMinWidth(65.0);
		GridPane.setConstraints(spinnerLcdContrast, 3, 1);
		GridPane.setHalignment(spinnerLcdContrast, HPos.LEFT);
		GridPane.setValignment(spinnerLcdContrast, VPos.CENTER);
		rootGridPane.getChildren().add(spinnerLcdContrast);

		labelMcu = new Label("MCU:");
		GridPane.setConstraints(labelMcu, 4, 0);
		GridPane.setHalignment(labelMcu, HPos.LEFT);
		GridPane.setValignment(labelMcu, VPos.CENTER);
		rootGridPane.getChildren().add(labelMcu);

		lblMcu = new Label("mcu");
		lblMcu.setStyle("-fx-border-insets: 0; -fx-border-width: 2px; -fx-border-color: black lightgray lightgray black;");
		GridPane.setConstraints(lblMcu, 5, 0);
		GridPane.setHalignment(lblMcu, HPos.LEFT);
		GridPane.setValignment(lblMcu, VPos.CENTER);
		rootGridPane.getChildren().add(lblMcu);

		labelConfigNamesEn = new Label("ConfigNamesEn:");
		GridPane.setConstraints(labelConfigNamesEn, 4, 1);
		GridPane.setHalignment(labelConfigNamesEn, HPos.LEFT);
		GridPane.setValignment(labelConfigNamesEn, VPos.CENTER);
		rootGridPane.getChildren().add(labelConfigNamesEn);

		checkBoxConfigNamesEn = new CheckBox();
		GridPane.setConstraints(checkBoxConfigNamesEn, 5, 1);
		GridPane.setHalignment(checkBoxConfigNamesEn, HPos.LEFT);
		GridPane.setValignment(checkBoxConfigNamesEn, VPos.CENTER);
		rootGridPane.getChildren().add(checkBoxConfigNamesEn);

		labelPadsNamesEn = new Label("PadsNamesEn:");
		GridPane.setConstraints(labelPadsNamesEn, 6, 0);
		GridPane.setHalignment(labelPadsNamesEn, HPos.LEFT);
		GridPane.setValignment(labelPadsNamesEn, VPos.CENTER);
		rootGridPane.getChildren().add(labelPadsNamesEn);

		checkBoxPadsNamesEn = new CheckBox();
		GridPane.setConstraints(checkBoxPadsNamesEn, 7, 0);
		GridPane.setHalignment(checkBoxPadsNamesEn, HPos.LEFT);
		GridPane.setValignment(checkBoxPadsNamesEn, VPos.CENTER);
		rootGridPane.getChildren().add(checkBoxPadsNamesEn);

		labelSlotsCount = new Label("Slots Count:");
		GridPane.setConstraints(labelSlotsCount, 6, 1);
		GridPane.setHalignment(labelSlotsCount, HPos.LEFT);
		GridPane.setValignment(labelSlotsCount, VPos.CENTER);
		rootGridPane.getChildren().add(labelSlotsCount);

		lblSlotsCount = new Label("33");
		lblSlotsCount.setStyle("-fx-border-insets: 0; -fx-border-width: 2px; -fx-border-color: black lightgray lightgray black;");
		GridPane.setConstraints(lblSlotsCount, 7, 1);
		GridPane.setHalignment(lblSlotsCount, HPos.LEFT);
		GridPane.setValignment(lblSlotsCount, VPos.CENTER);
		rootGridPane.getChildren().add(lblSlotsCount);

		labelSlotCurrent = new Label("Current Slot:");
		GridPane.setConstraints(labelSlotCurrent, 8, 0);
		GridPane.setHalignment(labelSlotCurrent, HPos.LEFT);
		GridPane.setValignment(labelSlotCurrent, VPos.CENTER);
		rootGridPane.getChildren().add(labelSlotCurrent);

		lblSlotCurrent = new Label("1");
		lblSlotCurrent.setStyle("-fx-border-insets: 0; -fx-border-width: 2px; -fx-border-color: black lightgray lightgray black;");
		GridPane.setConstraints(lblSlotCurrent, 9, 0);
		GridPane.setHalignment(lblSlotCurrent, HPos.LEFT);
		GridPane.setValignment(lblSlotCurrent, VPos.CENTER);
		rootGridPane.getChildren().add(lblSlotCurrent);

		labelSlotName = new Label("Slot Name:");
		GridPane.setConstraints(labelSlotName, 8, 1);
		GridPane.setHalignment(labelSlotName, HPos.LEFT);
		GridPane.setValignment(labelSlotName, VPos.CENTER);
		rootGridPane.getChildren().add(labelSlotName);

		textSlotName = new Text("textSlotName");
		GridPane.setConstraints(textSlotName, 9, 1);
		GridPane.setHalignment(textSlotName, HPos.LEFT);
		GridPane.setValignment(textSlotName, VPos.CENTER);
		rootGridPane.getChildren().add(textSlotName);

		buttonGet = new Button("Get Globals  ");
		GridPane.setConstraints(buttonGet, 10, 0);
		GridPane.setHalignment(buttonGet, HPos.LEFT);
		GridPane.setValignment(buttonGet, VPos.CENTER);
		rootGridPane.getChildren().add(buttonGet);

		buttonSend = new Button("Send Globals");
		GridPane.setConstraints(buttonSend, 10, 1);
		GridPane.setHalignment(buttonSend, HPos.LEFT);
		GridPane.setValignment(buttonSend, VPos.CENTER);
		rootGridPane.getChildren().add(buttonSend);
		
		labelMidi2ForSysex = new Label("MIDI2 for Sysex only");
		GridPane.setConstraints(labelMidi2ForSysex, 11, 0);
		GridPane.setHalignment(labelMidi2ForSysex, HPos.LEFT);
		GridPane.setValignment(labelMidi2ForSysex, VPos.CENTER);
		rootGridPane.getChildren().add(labelMidi2ForSysex);

		checkBoxMidi2ForSysex = new CheckBox();
		GridPane.setConstraints(checkBoxMidi2ForSysex, 12, 0);
		GridPane.setHalignment(checkBoxMidi2ForSysex, HPos.LEFT);
		GridPane.setValignment(checkBoxMidi2ForSysex, VPos.CENTER);
		rootGridPane.getChildren().add(checkBoxMidi2ForSysex);
		
		buttonLiveUpdates = new Button("Live Updates");
		GridPane.setConstraints(buttonLiveUpdates, 11, 1);
		GridPane.setHalignment(buttonLiveUpdates, HPos.RIGHT);
		GridPane.setValignment(buttonLiveUpdates, VPos.CENTER);
		rootGridPane.getChildren().add(buttonLiveUpdates);
		
	}

	public Node getUI() {
		return (Node) rootGridPane;
	}
}
