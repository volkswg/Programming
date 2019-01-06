import java.util.Random;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {

	public static Circle circle;
	public static Pane canvas;
	BouncingBall ball;
	BouncingBall ball2;
	BouncingBall ball3;

	public static void main(final String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) {
		canvas = new Pane();
		final Scene scene = new Scene(canvas, 800, 600);

		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton().equals(MouseButton.SECONDARY)) {
					Random rand = new Random();
					// Random Speed
					int speed = rand.nextInt(20) + 5;
					// Random Color
					int r = rand.nextInt(256);
					int g = rand.nextInt(256);
					int b = rand.nextInt(256);
					Color color = Color.rgb(r, g, b);

					ball = new BouncingBall(canvas, speed, color);

					// Random Position
					int x = rand.nextInt(700) + 30;
					int y = rand.nextInt(500) + 30;
					ball.relocate(x, y);

					canvas.getChildren().addAll(ball);
				} else if (event.getButton().equals(MouseButton.PRIMARY)) {
					try {
						canvas.getChildren().remove(0);
					} catch (IndexOutOfBoundsException e) {
						System.out.println("No Ball Left!!");
					}
				}
			}
		});

		primaryStage.setTitle("Game");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
