package page;

import gamemanager.GameManager;
import guiCustomObject.ButtonXO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Gameplay extends Page {

	public Gameplay() {
		this.getStylesheets().add(Gameplay.class.getResource("gameplay.css").toExternalForm());
		GameManager gameManager = new GameManager();
		gameManager.addGamePane(this);
		Button start_btt = new Button("Start");
		start_btt.setMaxWidth(Double.MAX_VALUE);

		int width = 100;
		int heigth = 100;

		ButtonXO xoc1r1_btt = new ButtonXO(width, heigth, gameManager, 0);
		ButtonXO xoc2r1_btt = new ButtonXO(width, heigth, gameManager, 1);
		ButtonXO xoc3r1_btt = new ButtonXO(width, heigth, gameManager, 2);

		ButtonXO xoc1r2_btt = new ButtonXO(width, heigth, gameManager, 3);
		ButtonXO xoc2r2_btt = new ButtonXO(width, heigth, gameManager, 4);
		ButtonXO xoc3r2_btt = new ButtonXO(width, heigth, gameManager, 5);

		ButtonXO xoc1r3_btt = new ButtonXO(width, heigth, gameManager, 6);
		ButtonXO xoc2r3_btt = new ButtonXO(width, heigth, gameManager, 7);
		ButtonXO xoc3r3_btt = new ButtonXO(width, heigth, gameManager, 8);
		GridPane xo_table = new GridPane();
		xo_table.setVgap(3);
		xo_table.setHgap(3);
		xo_table.setStyle("-fx-background-color: #464646");
		xo_table.setPadding(new Insets(3));
		xo_table.add(xoc1r1_btt, 0, 0);
		xo_table.add(xoc2r1_btt, 1, 0);
		xo_table.add(xoc3r1_btt, 2, 0);

		xo_table.add(xoc1r2_btt, 0, 1);
		xo_table.add(xoc2r2_btt, 1, 1);
		xo_table.add(xoc3r2_btt, 2, 1);

		xo_table.add(xoc1r3_btt, 0, 2);
		xo_table.add(xoc2r3_btt, 1, 2);
		xo_table.add(xoc3r3_btt, 2, 2);

		for (Node tmp : xo_table.getChildren()) {
			((ButtonXO) tmp).setPadding(Insets.EMPTY);
		}

		this.add(xo_table, 0, 1, 3, 1);
		Label result_label = new Label("X Turn");
		result_label.setMinSize(110, 10);
		result_label.setId("result_label");
		
		this.add(result_label, 0, 0);

		Button restart_btt = new Button("Restart");
		restart_btt.setId("restart_btt");
		restart_btt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				gameManager.initgame();
			}
		});
		this.add(restart_btt, 1, 2);
	}
}
