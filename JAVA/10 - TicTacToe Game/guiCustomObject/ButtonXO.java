package guiCustomObject;

import gamemanager.GameManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ButtonXO extends Button {
	public ButtonXO(GameManager gameManager,int pos) {
		this(50,50,gameManager,pos);
	}
	public ButtonXO(int width,int height,GameManager gameManager,int pos) {
		this.setMinHeight(height);
		this.setMinWidth(width);
		this.getStylesheets().add(ButtonXO.class.getResource("xoButton.css").toExternalForm());
		this.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if(gameManager.getTurn()%2 == 0) {
					gameManager.mark("X", pos);
					setText("X");	
					setStyle("-fx-text-fill: #95c623;");
				}
				else {
					gameManager.mark("O", pos);
					setText("O");										
					setStyle("-fx-text-fill: #464646;");
				}
				disableButton();
			}
		});
	}
	
	public void disableButton() {
		setDisable(true);
	}
	public void resetButton() {
		setDisable(false);
		this.setText("");
	}
}
