package info.megadrum.managerfx.ui;

import info.megadrum.managerfx.utils.Constants;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

class MdTitledPane extends Pane {
	private Label titleLabel;
	public MdTitledPane () {
		titleLabel = new Label();
		titleLabel.setLayoutX(0);
		titleLabel.setLayoutY(0);
		titleLabel.setAlignment(Pos.CENTER);
		setStyle("-fx-border-width: 1px; -fx-border-color: darkgrey");
		getChildren().add(titleLabel);
	}
	
	public void setText(String title) {
		titleLabel.setText(title);
	}
	
	public void setFont(Font font) {
		titleLabel.setFont(font);
	}
	
	public void setTitleHeight(Double h) {
		titleLabel.setMinHeight(h);
		titleLabel.setMaxHeight(h);
	}
	
	public void setWidth(Double w) {
		setMinWidth(w);
		setMaxWidth(w);
	}
}

public class UIPanel {

	protected int	viewSate = Constants.PANEL_SHOW;
	protected MdTitledPane 	titledPane;
	protected VBox 			vBoxAll;
	protected Pane			paneAll;
	protected Button 		buttonGet;
	protected Button 		buttonSend;
	protected Button 		buttonLoad;
	protected Button 		buttonSave;
	protected Boolean detached = false;
	protected String	panelTitle;
	protected ToggleGroup	toggleGroup;
	protected RadioMenuItem rbHide;
	protected RadioMenuItem rbShow;
	protected RadioMenuItem rbDetach;
	protected Stage			windowDetached;
	protected Double 		lastTitleHeight = 10.0;

	public UIPanel (String title) {
		panelTitle = title;
		ToggleGroup toggleGroup = new ToggleGroup();
		rbHide = new RadioMenuItem("Hide");
		rbHide.setToggleGroup(toggleGroup);
		rbShow = new RadioMenuItem("Show");
		rbShow.setToggleGroup(toggleGroup);
		rbDetach = new RadioMenuItem("Detach");
		rbDetach.setToggleGroup(toggleGroup);
		windowDetached = new Stage();
		windowDetached.setTitle(title);
		buttonGet = new Button("Get");
		buttonSend = new Button("Send");
		buttonLoad = new Button("Load");
		buttonSave = new Button("Save");
        vBoxAll = new VBox(1);
		vBoxAll.setStyle("-fx-padding: 0.0em 0.0em 0.0em 0.0em");
		vBoxAll.setAlignment(Pos.TOP_CENTER);
		vBoxAll.setLayoutX(0);
		paneAll = new Pane();
		paneAll.setStyle("-fx-padding: 0.0em 0.0em 0.0em 0.0em");
	}
	
	public void setViewState(int state) {
		viewSate = state;
	}
	
	public int getViewState() {
		return viewSate;
	}
	
	public final Node getUI() {
		if (detached) {
			return vBoxAll;
		} else {
			return titledPane;
		}
	}
	
	public void respondToResize(Double w, Double h, Double fullHeight, Double controlW, Double controlH) {
	}
	
	public void respondToResizeDetached(Double w, Double h) {
	}

	public final Parent getTopLayout() {
		if (detached) {
			return vBoxAll;
		} else {
			return titledPane;
		}
	}
	
	public Stage getWindow() {
		return windowDetached;
	}
	
	public void selectRadioMenuItemHide() {
		rbHide.setSelected(true);
	}
	
	public void setDetached(Boolean d) {
		detached = d;
		if (!detached) {
			titledPane = new MdTitledPane();
			titledPane.setText(panelTitle);
			//titledPane.setContent(vBoxAll);
			vBoxAll.setLayoutY(lastTitleHeight);
			titledPane.getChildren().add(vBoxAll);
			titledPane.setId("panelTitle");
			//titledPane.setCollapsible(false);
			//titledPane.setAlignment(Pos.CENTER);
		}
	}
	
	public Boolean isDetached() {
		return detached;
	}
	
	public final String getTitle() {
		return panelTitle;
	}
	
	public final Button getButtonGet() {
		return buttonGet;
	}

	public final Button getButtonSend() {
		return buttonSend;
	}

	public final Button getButtonLoad() {
		return buttonLoad;
	}
	
	public final Button getButtonSave() {
		return buttonSave;
	}

	public RadioMenuItem getRadioMenuItemHide() {
		return rbHide;
	}

	public RadioMenuItem getRadioMenuItemShow() {
		return rbShow;
	}

	public RadioMenuItem getRadioMenuItemDetach() {
		return rbDetach;
	}

}
