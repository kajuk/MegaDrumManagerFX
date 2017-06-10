package info.megadrum.managerfx.ui;

import info.megadrum.managerfx.utils.Constants;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UIPanel {

	protected int	viewSate = Constants.PANEL_SHOW;
	protected TitledPane 	titledPane;
	protected VBox 		vBoxAll;
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
			titledPane = new TitledPane();
			titledPane.setText(panelTitle);
			titledPane.setContent(vBoxAll);
			titledPane.setId("panelTitle");
			titledPane.setCollapsible(false);
			titledPane.setAlignment(Pos.CENTER);
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
