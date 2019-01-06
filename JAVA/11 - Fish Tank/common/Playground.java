package common;

import javafx.scene.layout.Pane;

public class Playground extends Pane{

	public void addSome(BouncingBall ball) {
		this.getChildren().add(ball);
	}
}
