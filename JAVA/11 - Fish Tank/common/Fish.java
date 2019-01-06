package common;

import java.io.File;

import client.ClientConnect;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Fish extends ImageView {

	private int deltaX;
	final Timeline loop;

	public Fish(Pane canvas, int speed, Color color, ClientConnect sendMsgAgent, int direction, int fishType) {
		String filePath = "";
		switch (fishType) {
		case 1:
			filePath = "src/sprite/Fish1.png";
			break;
		case 2:
			filePath = "src/sprite/Fish2.png";
			break;
		case 3:
			filePath = "src/sprite/Fish3.png";
			break;
		case 4:
			filePath = "src/sprite/Fish4.png";
			break;
		case 5:
			filePath = "src/sprite/Fish5.png";
			break;
		case 6:
			filePath = "src/sprite/Fish6.png";
			break;
		case 7:
			filePath = "src/sprite/Fish7.png";
			break;
		}

		Image image = new Image(new File(filePath).toURI().toString());
		setRotationAxis(Rotate.Y_AXIS);
		setFitWidth(30);
		setPreserveRatio(true);
		setSmooth(true);
		setImage(image);
		if (direction == -1) {
			setRotate(180);
		}

		// System.out.println(sendMsgAgent.checkStatus());
		int xDir = direction;
		this.deltaX = 3 * xDir;
		loop = new Timeline(new KeyFrame(Duration.millis(speed), new EventHandler<ActionEvent>() {
			final Bounds bounds = canvas.getBoundsInLocal(); // get
			double boundMaxX = bounds.getMaxX();
			double boundMinX = bounds.getMinY();

			@Override
			public void handle(ActionEvent arg0) {
				setLayoutX(getLayoutX() + deltaX);
				final boolean atRightBorder = getLayoutX() >= boundMaxX + 15;
				final boolean atLeftBorder = getLayoutX() <= boundMinX - 15;
				// System.out.println(getLayoutX() +":"+bounds.getMaxX());
				if (atLeftBorder) {
					sendMsgAgent.sendMsg("%$HitBorder:right" + "," + speed + ","
							+ (Math.round(getLayoutY()) /*- Math.round(getFitWidth())*/) + "," + direction + ","
							+ fishType);
					stopLoop();
					sendMsgAgent.deleteBall(getId());
				}
				if (atRightBorder) {
					sendMsgAgent.sendMsg("%$HitBorder:left" + "," + speed + ","
							+ (Math.round(getLayoutY()) /*- Math.round(getFitWidth())*/) + "," + direction + ","
							+ fishType);
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
}
