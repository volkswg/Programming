package common;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PresetGridPane extends GridPane {

	public PresetGridPane() {
		this("Welcome", true);
	}

	public PresetGridPane(boolean addTitle) {
		this("", false);
	}

	public PresetGridPane(String title_text, boolean addTitle) {
		this.setVgap(10);
		this.setHgap(10);
		this.setAlignment(Pos.CENTER);
		if (addTitle) {
			Label title_label = new Label("Server Log");
			title_label.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));

			this.add(title_label, 0, 0);
		}
	}
}
