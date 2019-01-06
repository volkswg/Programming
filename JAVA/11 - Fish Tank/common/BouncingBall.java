package common;

import java.util.Random;

import client.ClientConnect;
//import client.ClientConnectRunable;
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
	final Timeline loop;

	public BouncingBall(Pane canvas, int speed, Color color, ClientConnect sendMsgAgent, int direction) {
		super(15, color);
		int xDir = direction;
		this.deltaX = 3 * xDir;
		loop = new Timeline(new KeyFrame(Duration.millis(speed), new EventHandler<ActionEvent>() {
			final Bounds bounds = canvas.getBoundsInLocal(); // get
			double boundMaxX = bounds.getMaxX();
			double boundMinX = bounds.getMinY();
			@Override
			public void handle(ActionEvent arg0) {
				setLayoutX(getLayoutX() + deltaX);
				final boolean atRightBorder = getLayoutX() >= boundMaxX+7;
				final boolean atLeftBorder = getLayoutX() <= boundMinX-7;
//				System.out.println(getLayoutX() +":"+bounds.getMaxX());
				if (atLeftBorder) {
					sendMsgAgent.sendMsg("%$HitBorder:right" + "," + speed + ","
							+ (Math.round(getLayoutY()) - Math.round(getRadius())) + "," + direction);
					stopLoop();
					sendMsgAgent.deleteBall(getId());
				}
				if (atRightBorder) {
					sendMsgAgent.sendMsg("%$HitBorder:left" + "," + speed + ","
							+ (Math.round(getLayoutY()) - Math.round(getRadius())) + "," + direction);
					stopLoop();
					sendMsgAgent.deleteBall(getId());
				}
			}
		}));
		loop.setCycleCount(Timeline.INDEFINITE);
		loop.play();
	}

	public void stopLoop() {
		loop.stop();
		this.setDisable(true);
		// this.setId("Fuck");
	}

	private int randomDirection() {
		Random rand = new Random();
		int direction = rand.nextInt(2);
		return direction == 0 ? -1 : direction;
	}

}
