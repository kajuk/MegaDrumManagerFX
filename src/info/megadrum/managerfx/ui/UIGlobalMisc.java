package info.megadrum.managerfx.ui;

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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
	private Button		buttonLiveUpdates;
	
	
	
	public UIGlobalMisc() {
		rootGridPane = new GridPane();
		rootGridPane.setStyle("-fx-border-color: lightgrey");
		rootGridPane.setVgap(1.0);
		rootGridPane.setHgap(5.0);

		labelMidi = new Label("MIDI:");
		GridPane.setConstraints(labelMidi, 0, 0);
		GridPane.setHalignment(labelMidi, HPos.RIGHT);
		GridPane.setValignment(labelMidi, VPos.CENTER);
		rootGridPane.getChildren().add(labelMidi);

		buttonMidi = new Button("Open MIDI");
		GridPane.setConstraints(buttonMidi, 1, 0);
		GridPane.setHalignment(buttonMidi, HPos.LEFT);
		GridPane.setValignment(buttonMidi, VPos.CENTER);
		rootGridPane.getChildren().add(buttonMidi);
		
		labelInputs = new Label("Inputs:");
		GridPane.setConstraints(labelInputs, 0, 1);
		GridPane.setHalignment(labelInputs, HPos.RIGHT);
		GridPane.setValignment(labelInputs, VPos.CENTER);
		rootGridPane.getChildren().add(labelInputs);

		comboBoxInputs = new ComboBox<Integer>();
		GridPane.setConstraints(comboBoxInputs, 1, 1);
		GridPane.setHalignment(comboBoxInputs, HPos.LEFT);
		GridPane.setValignment(comboBoxInputs, VPos.CENTER);
		rootGridPane.getChildren().add(comboBoxInputs);

		labelFwVersion = new Label("FW version:");
		GridPane.setConstraints(labelFwVersion, 2, 0);
		GridPane.setHalignment(labelFwVersion, HPos.RIGHT);
		GridPane.setValignment(labelFwVersion, VPos.CENTER);
		rootGridPane.getChildren().add(labelFwVersion);

		lblFwVersion = new Label("FwVersion");
		lblFwVersion.setStyle("-fx-border-insets: 0; -fx-border-width: 2px; -fx-border-color: black lightgray lightgray black;");
		lblFwVersion.setMinWidth(80);
		GridPane.setConstraints(lblFwVersion, 3, 0);
		GridPane.setHalignment(lblFwVersion, HPos.LEFT);
		GridPane.setValignment(lblFwVersion, VPos.CENTER);
		rootGridPane.getChildren().add(lblFwVersion);

		labelMcu = new Label("MCU:");
		GridPane.setConstraints(labelMcu, 2, 1);
		GridPane.setHalignment(labelMcu, HPos.RIGHT);
		GridPane.setValignment(labelMcu, VPos.CENTER);
		rootGridPane.getChildren().add(labelMcu);

		lblMcu = new Label("mcu");
		lblMcu.setStyle("-fx-border-insets: 0; -fx-border-width: 2px; -fx-border-color: black lightgray lightgray black;");
		lblMcu.setMinWidth(80);
		GridPane.setConstraints(lblMcu, 3, 1);
		GridPane.setHalignment(lblMcu, HPos.LEFT);
		GridPane.setValignment(lblMcu, VPos.CENTER);
		rootGridPane.getChildren().add(lblMcu);

		labelPadsNamesEn = new Label("PadsNamesEn:");
		GridPane.setConstraints(labelPadsNamesEn, 4, 0);
		GridPane.setHalignment(labelPadsNamesEn, HPos.RIGHT);
		GridPane.setValignment(labelPadsNamesEn, VPos.CENTER);
		rootGridPane.getChildren().add(labelPadsNamesEn);

		checkBoxPadsNamesEn = new CheckBox();
		GridPane.setConstraints(checkBoxPadsNamesEn, 5, 0);
		GridPane.setHalignment(checkBoxPadsNamesEn, HPos.LEFT);
		GridPane.setValignment(checkBoxPadsNamesEn, VPos.CENTER);
		rootGridPane.getChildren().add(checkBoxPadsNamesEn);

		labelConfigNamesEn = new Label("ConfigNamesEn:");
		GridPane.setConstraints(labelConfigNamesEn, 4, 1);
		GridPane.setHalignment(labelConfigNamesEn, HPos.RIGHT);
		GridPane.setValignment(labelConfigNamesEn, VPos.CENTER);
		rootGridPane.getChildren().add(labelConfigNamesEn);

		checkBoxConfigNamesEn = new CheckBox();
		GridPane.setConstraints(checkBoxConfigNamesEn, 5, 1);
		GridPane.setHalignment(checkBoxConfigNamesEn, HPos.LEFT);
		GridPane.setValignment(checkBoxConfigNamesEn, VPos.CENTER);
		rootGridPane.getChildren().add(checkBoxConfigNamesEn);

		labelSlotsCount = new Label("Slots Count:");
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
		GridPane.setConstraints(labelLcdContrast, 8, 0);
		GridPane.setHalignment(labelLcdContrast, HPos.RIGHT);
		GridPane.setValignment(labelLcdContrast, VPos.CENTER);
		rootGridPane.getChildren().add(labelLcdContrast);

		spinnerLcdContrast = new Spinner<Integer>();
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
		GridPane.setConstraints(labelMidi2ForSysex, 11, 0);
		GridPane.setHalignment(labelMidi2ForSysex, HPos.RIGHT);
		GridPane.setValignment(labelMidi2ForSysex, VPos.CENTER);
		rootGridPane.getChildren().add(labelMidi2ForSysex);

		checkBoxMidi2ForSysex = new CheckBox();
		GridPane.setConstraints(checkBoxMidi2ForSysex, 12, 0);
		GridPane.setHalignment(checkBoxMidi2ForSysex, HPos.LEFT);
		GridPane.setValignment(checkBoxMidi2ForSysex, VPos.CENTER);
		rootGridPane.getChildren().add(checkBoxMidi2ForSysex);
		
		buttonLiveUpdates = new Button("Live Updates");
		GridPane.setConstraints(buttonLiveUpdates, 11, 1);
		GridPane.setHalignment(buttonLiveUpdates, HPos.CENTER);
		GridPane.setValignment(buttonLiveUpdates, VPos.CENTER);
		rootGridPane.getChildren().add(buttonLiveUpdates);
	
		for (int i = 0; i < rootGridPane.getChildren().size(); i++) {
			if (rootGridPane.getChildren().get(i) instanceof Button) {
				((Button)rootGridPane.getChildren().get(i)).setFont(new Font(10));
				
			}
			if (rootGridPane.getChildren().get(i) instanceof Label) {
				((Label)rootGridPane.getChildren().get(i)).setFont(new Font(11));
				
			}
		}
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(40.0)); // 0
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(70.0)); // 1
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(70.0)); // 2
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(80.0)); // 3
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(90.0)); // 4
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(20.0)); // 5
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(70.0)); // 6
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(25.0)); // 7
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(80.0)); // 8
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(100.0)); // 9
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(80.0)); // 10
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(120.0)); // 11
		rootGridPane.getColumnConstraints().add(new ColumnConstraints(20.0)); // 12

	}

	public Node getUI() {
		return (Node) rootGridPane;
	}
}
