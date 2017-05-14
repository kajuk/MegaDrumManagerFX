package info.megadrum.managerfx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class MainController implements Initializable {
	
    //@FXML private Label testLabel;
	//@FXML private UISpinner SpnrNoteOffDelay;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//testLabel.setText("Label from initialize");
		//System.out.printf("SpnrNoteOffDelay id = %d\n",SpnrNoteOffDelay.getId());
	}

	public void initController() {
		//testLabel.setText("Label from initController");
	}
	
	public void showOptions() {
		System.out.print("Showing options\n");
	}
	
	public void saveAndExit() {
		System.out.print("Save and Exit\n");
		System.exit(0);
	}

	public void testAction() {
		System.out.printf("testAction\n");
		
	}

}
