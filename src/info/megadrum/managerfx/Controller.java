package info.megadrum.managerfx;

import javax.management.OperationsException;

import info.megadrum.managerfx.ui.UIInput;
import info.megadrum.managerfx.ui.UIMisc;
import info.megadrum.managerfx.ui.UIOptions;
import info.megadrum.managerfx.ui.UIPad;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller {
	private Stage window;
	private Scene scene1;
	private MenuBar mainMenuBar;
	private Menu mainMenu, viewMenu, aboutMenu;
	private Menu allSettingsMenu, miscMenu, hihatMenu, allPadMenu, selectedPadMenu,customCurvesMenu;
	private Menu loadFromMdSlotMenu, saveToMdSlotMenu;
	private MenuItem firmwareUpgradeMenu, optionsMenu, exitMenu;
	private UIOptions optionsWindow;
	private UIMisc uiMisc;
	private UIPad uiPad;
	
	public Controller(Stage primaryStage) {
		window = primaryStage;
		window.setTitle("MegaDrumManagerFX");
		window.setOnCloseRequest(e-> {
			e.consume();
			closeProgram();
		});

		createMainMenuBar();
		uiMisc = new UIMisc("Misc");
		uiPad = new UIPad("Pads");
		VBox layout1VBox = new VBox();

		mainMenuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		layout1VBox.getChildren().add(mainMenuBar);
		
		HBox layout2HBox = new HBox(5);
		Button button = new Button("b");
		layout2HBox.getChildren().add(button);
		layout2HBox.getChildren().add(uiMisc.getUI());
		layout2HBox.getChildren().add(uiPad.getUI());

		layout1VBox.getChildren().add(layout2HBox);

		//scene1 = new Scene(layout1, 300,500);
		scene1 = new Scene(layout1VBox);
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		optionsWindow = new UIOptions(this);
		window.setScene(scene1);
		window.sizeToScene();
		scene1.widthProperty().addListener((obs, oldVal, newVal) -> {
			respondToResize(scene1);
		});

		scene1.heightProperty().addListener((obs, oldVal, newVal) -> {
			respondToResize(scene1);
		});
		window.show();
	}
	
	public void respondToResize(Scene sc) {
		Double height = sc.getHeight() - mainMenuBar.getHeight();
		Double width = height*2;
		//System.out.println("Responding to scene resize in Controller");
		uiMisc.respondToResize((height)*0.45, sc.getWidth()*0.3, height);
		uiPad.respondToResize((height)*1.0, sc.getWidth()*0.6, height);
		//uiMisc.respondToResize(height*0.5, width*0.25);
		//uiPad.respondToResize(height, width*0.3);
	}
	
	public void createMainMenuBar() {
		mainMenuBar = new MenuBar();
		mainMenu = new Menu("Main");
		viewMenu = new Menu("View");
		aboutMenu = new Menu("About");
		
		mainMenuBar.getMenus().addAll(mainMenu,viewMenu,aboutMenu);
		allSettingsMenu = new Menu("All Settings");
		miscMenu = new Menu("Misc Settings");
		hihatMenu = new Menu("HiHat Pedal Settings");
		allPadMenu = new Menu("All Pads Settings");
		selectedPadMenu = new Menu("Selected Pad Settings");
		customCurvesMenu = new Menu("Custom Curves");
		firmwareUpgradeMenu = new MenuItem("Firmware Upgrade");
		optionsMenu = new MenuItem("Options");
		optionsMenu.setOnAction(e-> optionsWindow.show());
		exitMenu = new MenuItem("Exit");
		exitMenu.setOnAction(e-> closeProgram());
		
		mainMenu.getItems().addAll(allSettingsMenu,miscMenu,
				hihatMenu,allPadMenu,selectedPadMenu,customCurvesMenu,
				new SeparatorMenuItem(), firmwareUpgradeMenu, new SeparatorMenuItem(), optionsMenu,
				new SeparatorMenuItem(),exitMenu
				);
	}
	
	private void closeProgram() {
		System.out.println("Exiting\n");
		window.close();
	}

}
