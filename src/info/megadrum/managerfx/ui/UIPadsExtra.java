package info.megadrum.managerfx.ui;

import info.megadrum.managerfx.utils.Constants;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Font;

public class UIPadsExtra {
	private TitledPane 	titledPane;
	private TabPane		tabPane;
	private UICurves	uiCurves;

	public UIPadsExtra(String title) {
		tabPane = new TabPane();
        Tab tabCurves = new Tab("Custom Curves");
        tabCurves.setClosable(false);
        Tab tabCustomNames = new Tab("Custom Names");
        tabCustomNames.setClosable(false);
        tabPane.getTabs().addAll(tabCurves,tabCustomNames);

        uiCurves = new UICurves();
        tabCurves.setContent(uiCurves.getUI());
        
		titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(tabPane);
		titledPane.setCollapsible(false);
		titledPane.setAlignment(Pos.CENTER);
		titledPane.setMinSize(400, 400);
		titledPane.setMaxSize(400, 400);
	}

	public Node getUI() {
		return (Node) titledPane;
	}

	public void respondToResize (Double h, Double w, Double fullHeight, Double controlH, Double controlW) {
		Double toolBarFontHeight = fullHeight*Constants.FX_TITLEBARS_FONT_SCALE;
		Double titledPaneFontHeight = toolBarFontHeight*1.4;
		Double h1,h2,h3;
		if (toolBarFontHeight > Constants.FX_TITLEBARS_FONT_MIN_SIZE) {
			titledPane.setStyle("-fx-font-size: " + titledPaneFontHeight.toString() + "pt");			
		} else {
			titledPane.setStyle("-fx-font-size: " + Constants.FX_TITLEBARS_FONT_MIN_SIZE.toString() + "pt");						
		}
		uiCurves.respondToResize(h, w, fullHeight, controlH, controlW);
	}

}
