package info.megadrum.managerfx.ui;

import info.megadrum.managerfx.utils.Constants;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TitledPane;

public class UIPanel {

	protected int	viewSate = Constants.PANEL_SHOW;
	protected Parent topLayout;
	protected RadioMenuItem radioMenuItemHide;
	protected Boolean detached = false;

	public void setViewState(int state) {
		viewSate = state;
	}
	
	public int getViewState() {
		return viewSate;
	}
	
	public Node getUI() {
		return null;
	}
	
	public void respondToResize(Double h, Double w, Double fullHeight, Double controlH, Double controlW) {
	}
	
	public void respondToResizeDetached(Double h, Double w) {
	}

	public Parent getTopLayout() {
		return topLayout;
	}
	
	public void setRadioMenuItemHide(RadioMenuItem rm) {
		radioMenuItemHide = rm;
	}
	
	public void selectRadioMenuItemHide() {
		radioMenuItemHide.setSelected(true);
	}
	
	public void setDetached(Boolean d) {
		detached = d;
	}
	
	public Boolean isDetached() {
		return detached;
	}
}
