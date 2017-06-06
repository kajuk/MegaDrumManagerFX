package info.megadrum.managerfx.ui;

import info.megadrum.managerfx.utils.Constants;
import javafx.scene.Node;
import javafx.scene.Parent;

public class UIPanel {

	protected int	viewSate = Constants.PANEL_SHOW;
	protected Parent topLayout;

	public void setViewState(int state) {
		viewSate = state;
	}
	
	public int getViewState() {
		return viewSate;
	}
	
	public Node getUI() {
		return null;
	}
	
	public Parent getTopLayout() {
		return topLayout;
	}
}
