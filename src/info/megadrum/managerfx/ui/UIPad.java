package info.megadrum.managerfx.ui;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UIPad extends Parent {

	private VBox		vBox;
	private UIInput 	uiInputLeft;
	private UIInput 	uiInputRight;
	private UI3rdZone	ui3rdZone;
	
	public UIPad() {
		// TODO Auto-generated constructor stub
		HBox hBox = new HBox();
		uiInputLeft = new UIInput();
		uiInputRight = new UIInput();
		ui3rdZone = new UI3rdZone();
		hBox.getChildren().addAll(uiInputLeft.getUI(),uiInputRight.getUI());
		vBox = new VBox();
		vBox.getChildren().addAll(hBox,ui3rdZone.getUI());
	}
	
	public void respondToResize (Double h, Double w) {
		uiInputLeft.respondToResize(h*0.8, w/2);
		uiInputRight.respondToResize(h*0.8, w/2);
		ui3rdZone.respondToResize(h*0.122, w);
	}

	public Node getUI() {
		return (Node) vBox;
	}

}
