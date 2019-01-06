package common;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

public class PresetGridPane extends GridPane{

	public PresetGridPane() {
		this.setVgap(10);
		this.setHgap(10);
		this.setAlignment(Pos.CENTER);
	}
}
