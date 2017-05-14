package info.megadrum.managerfx;
	
import info.megadrum.managerfx.ui.UICheckBox;
import info.megadrum.managerfx.ui.UIMisc;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;


public class Main extends Application {

	//MainController mainController;
	Stage window;
	Scene scene1;

	@Override
	public void start(Stage primaryStage) {
		try {
			/*
			FXMLLoader loader = new FXMLLoader(
					    getClass().getResource(
					      "MainUI.fxml"
					    )
					  );
			//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("MainUI.fxml"));
			BorderPane root = loader.load();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			mainController = (MainController)loader.getController();
			//mainController.initController();
			 
			 */
			window = primaryStage;

/*			UICheckBox uiCheckBox1 = new UICheckBox();
			uiCheckBox1.setTextLabel("UICheckBox1");
			UICheckBox uiCheckBox2 = new UICheckBox();
			uiCheckBox2.setTextLabel("UICheckBox Two");
*/			
			UIMisc uiMisc = new UIMisc();
			VBox layout1 = new VBox(30);
			layout1.getChildren().add(uiMisc.getUI());

			scene1 = new Scene(layout1, 240,400);
			scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			window.setScene(scene1);;
			window.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
