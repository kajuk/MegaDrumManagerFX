package info.megadrum.managerfx.ui;

import info.megadrum.managerfx.utils.Constants;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class UIPanel {

	protected int	viewSate = Constants.PANEL_SHOW;
	protected TitledPane 	titledPane;
	protected VBox 		vBoxAll;
	//protected Parent topLayout;
	protected RadioMenuItem radioMenuItemHide;
	protected Boolean detached = false;
	protected String	panelTitle;

	public UIPanel (String title) {
		panelTitle = title;
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
	
	public void respondToResize(Double h, Double w, Double fullHeight, Double controlH, Double controlW) {
	}
	
	public void respondToResizeDetached(Double h, Double w) {
	}

	public final Parent getTopLayout() {
		if (detached) {
			return vBoxAll;
		} else {
			return titledPane;
		}
	}
	
	public void setRadioMenuItemHide(RadioMenuItem rm) {
		radioMenuItemHide = rm;
	}
	
	public void selectRadioMenuItemHide() {
		radioMenuItemHide.setSelected(true);
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
	
	public String getTitle() {
		return panelTitle;
	}
	
}
