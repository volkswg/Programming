import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class BouncingBall extends Circle {
	private int deltaX;
	private int deltaY;

	public BouncingBall(Pane canvas, int speed, Color color) {
		super(15, color);
		int xDir = randomDirection();
		int yDir = randomDirection();
		this.deltaX = 3 * xDir;
		this.deltaY = 3 * yDir;
		final Timeline loop = new Timeline(new KeyFrame(Duration.millis(speed), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				setLayoutX(getLayoutX() + deltaX);
				setLayoutY(getLayoutY() + deltaY);
				final Bounds bounds = canvas.getBoundsInLocal(); // get
				final boolean atRightBorder = getLayoutX() >= (bounds.getMaxX() - getRadius());
				final boolean atLeftBorder = getLayoutX() <= (bounds.getMinX() + getRadius());
				final boolean atBottomBorder = getLayoutY() >= (bounds.getMaxY() - getRadius());
				final boolean atTopBorder = getLayoutY() <= (bounds.getMinY() + getRadius());
				if (atRightBorder || atLeftBorder) {
					deltaX *= -1;
				}
				if (atBottomBorder || atTopBorder) {
					deltaY *= -1;
				}
			}
		}));
		loop.setCycleCount(Timeline.INDEFINITE);
		loop.play();
	}

	private int randomDirection() {
		Random rand = new Random();
		int direction = rand.nextInt(2);
		return direction == 0 ? -1 : direction;
	}

}
