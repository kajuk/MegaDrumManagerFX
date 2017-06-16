package info.megadrum.managerfx.ui;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import info.megadrum.managerfx.data.ConfigOptions;
import info.megadrum.managerfx.data.FileManager;
import info.megadrum.managerfx.midi.MidiController;
import info.megadrum.managerfx.utils.Constants;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UIUpgrade {
	private Stage window;
	private Scene scene;
	private Boolean upgradeCompleted = false;
	private Boolean upgradeIsInProgress = false;
	//private TextArea textAreaInstruction;
	private Label textAreaInstruction;
	private Button buttonStart, buttonCancel, buttonClose, buttonOpen;
	private Label labelFile;
	private TextField textFieldFileName;
	private ProgressBar progressBar;
	private Label labelResult;
	private MidiController midiController;
	private File file;
	private FileManager fileManager;
	private ConfigOptions configOptions;

	public UIUpgrade(MidiController controller, FileManager fm, ConfigOptions config) {
		configOptions = config;
		fileManager = fm;
		midiController = controller;
		window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("MegaDrum Firmware Upgrade");
		window.setOnCloseRequest(e-> {
			e.consume();
			closeWindow();
		});

		textAreaInstruction = new Label();
		textAreaInstruction.setFont(new Font(16));
		textAreaInstruction.setPadding(new Insets(2, 2, 2, 2));
		textAreaInstruction.setMinHeight(400);
		textAreaInstruction.setMaxHeight(400);
		//textAreaInstruction.setDisable(true);
		VBox vBoxTop = new VBox();
		vBoxTop.setPadding(new Insets(2, 2, 2, 2));
		vBoxTop.setAlignment(Pos.TOP_CENTER);
		vBoxTop.getChildren().add(textAreaInstruction);
		
		labelFile = new Label("MegaDrum firmware:");
		textFieldFileName = new TextField();
		textFieldFileName.setMinWidth(450);
		textFieldFileName.setOnKeyReleased(e-> {
			textFieldChanged();
		});
		buttonOpen = new Button("Open");
		buttonOpen.setOnAction(e-> {
			selectFile();
		});
		HBox hBoxFileSelection = new HBox();
		hBoxFileSelection.setAlignment(Pos.CENTER);
		hBoxFileSelection.getChildren().addAll(labelFile,textFieldFileName,buttonOpen);
		
		vBoxTop.getChildren().add(hBoxFileSelection);
		
		progressBar = new ProgressBar();
		progressBar.setPadding(new Insets(2, 0, 2, 0));
		progressBar.prefWidthProperty().bind(vBoxTop.widthProperty().subtract(10));  //  -10 is for 
		   // padding from right and left, since we aligned it to TOP_CENTER.
		vBoxTop.getChildren().add(progressBar);
		labelResult = new Label();
		labelResult.setFont(new Font(16));
		labelResult.setPadding(new Insets(2, 0, 10, 0));
		vBoxTop.getChildren().add(labelResult);
		
		buttonStart = new Button("Start");
		buttonStart.setOnAction(e-> {
			startUpgrade();
		});
		buttonCancel = new Button("Cancel");
		buttonCancel.setOnAction(e-> {
			midiController.cancelUpgrade();
		});
		buttonClose = new Button("Close");
		buttonClose.setOnAction(e-> {
			closeWindow();
		});
		HBox hBoxButtons = new HBox();
		hBoxButtons.setAlignment(Pos.CENTER);
		hBoxButtons.getChildren().addAll(buttonStart,new Label("    "), buttonCancel, new Label("    "), buttonClose);

		vBoxTop.getChildren().add(hBoxButtons);
		
		scene = new Scene(vBoxTop);
		window.setScene(scene);
		//window.setMinWidth(400);
		//window.setMaxWidth(400);
		//window.setMinHeight(350);
		//window.setMaxHeight(350);

	}
	
	private void setLabelResult(String s, int error) {
		String fontColor = "blue";
		if (s.length() == 0) {
			labelResult.setText("");
		} else {
			if (error > 0) {
				fontColor = "red";
			} else {
				if (error < 0) {
					fontColor = "orange";
				} else {
					fontColor = "green";
				}
			}
			labelResult.setText(s);
		}
		labelResult.setStyle("-fx-text-fill: " + fontColor);
	}
	
	public void show() {
		progressBar.progressProperty().unbind();
		midiController.openMidi(configOptions.MidiInName, configOptions.MidiOutName, "");
		midiController.setInFirmwareUpgrade(true);
		if (configOptions.mcuType > 2) {
			textAreaInstruction.setText(Constants.UPGRADE_INSTRUCTION_ARM);
		} else {
			textAreaInstruction.setText(Constants.UPGRADE_INSTRUCTION_ATMEGA);
		}
		upgradeCompleted = false;
        window.setResizable(false);
        setButtons(true);
        setLabelResult("", 0);
		progressBar.setProgress(0);
        
        window.showAndWait();
	}

	private void closeWindow() {
		if (!upgradeIsInProgress) {
			midiController.setInFirmwareUpgrade(false);
			window.close();
		}
	}
	
	private void setButtons(Boolean state) {
		textFieldFileName.setDisable(!state);
		buttonStart.setDisable(!state);
		buttonClose.setDisable(!state);
		buttonCancel.setDisable(state);		
		buttonOpen.setDisable(!state);		
	}
	
	private void startUpgrade() {
		if (file != null) {
			upgradeIsInProgress = true;
			setLabelResult("Upgrade is in progress", -1);
			Task<Integer> taskUpgrade;
			try {
				setButtons(false);
				taskUpgrade = midiController.doFirmwareUpgrade(file, configOptions.mcuType, progressBar);
				taskUpgrade.setOnSucceeded(e->{
					upgradeFinished();
				});
				new Thread(taskUpgrade).start();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				setButtons(true);
				progressBar.progressProperty().unbind();
				upgradeIsInProgress = false;
			}
		} else {
			setLabelResult("Incorrect or no file specified", 1);
		}
	}	

	private void upgradeFinished() {
		upgradeIsInProgress = false;
		progressBar.progressProperty().unbind();
		setButtons(true);
		if (midiController.getUpgradeError() > 0) {
			
		} else {
			progressBar.setProgress(1);
		}
		setLabelResult(midiController.getUpgradeString(), midiController.getUpgradeError());
		//System.out.printf("Upgrade thread finished with error = %d\nResult text = %s\n",
		//		midiController.getUpgradeError(), midiController.getUpgradeString());
	}
	
	private void selectFile() {
		file = fileManager.selectFirmwareFile(configOptions);
		if (file != null) {
			textFieldFileName.setText(file.getAbsolutePath());
			buttonStart.setDisable(false);
		}
		window.toFront();
	}
	
	private void textFieldChanged() {		
		file = new File(textFieldFileName.getText());
		buttonStart.setDisable(!file.isFile());
	}
}
