package info.megadrum.managerfx.ui;

import info.megadrum.managerfx.utils.Constants;
import javafx.scene.Node;

public class UIPanel {

	protected int	viewSate = Constants.PANEL_SHOW;

	public void setViewState(int state) {
		viewSate = state;
	}
	
	public int getViewState() {
		return viewSate;
	}
	
	public Node getUI() {
		return null;
	}
}
