package client;

import java.util.Random;

import common.Fish;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientTank extends Application {
	public static ClientConnect clientConnect;

	public static void main(String[] args) {
		launch(args);
	}

	static int ballId;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane canvas = new Pane();
		String image = ClientTank.class.getResource("tankBG.png").toExternalForm();
		
		canvas.setStyle("-fx-background-image: url('" + image + "'); " +
						"-fx-background-position: center center; " +
						"-fx-background-size:400 400;");
		TextField deleteBall = new TextField();
		TextField createBall = new TextField();
		ballId = 0;
		deleteBall.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.equalsIgnoreCase("")) {
				// System.out.println("del:" + newValue);
				deleteBall(canvas, newValue);
			}
			deleteBall.setText("");
		});
		createBall.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.equalsIgnoreCase("")) {
				// System.out.println("GenBall");
				ballId++;
				String[] parameter = createBall.getText().split(",");

				String border = parameter[0];
				int speed = Integer.parseInt(parameter[1]);
				int posY = Integer.parseInt(parameter[2]);
				int direction = Integer.parseInt(parameter[3]);
				int fishType = Integer.parseInt(parameter[4]);

				generateFish(canvas, ballId, border, speed, posY, direction, fishType);
			}
			createBall.setText("");
		});
		clientConnect = new ClientConnect(createBall, deleteBall);
		clientConnect.start();
		Scene scene = new Scene(canvas, 400, 400);

		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton().equals(MouseButton.SECONDARY)) {
					// Direction
					int direction;
					int temp = (int) (Math.random() * 10) % 2;
					direction = temp == 0 ? -1 : temp;
					String spawnPos = direction == 1 ? "left" : "right";

					// Position, Speed, FishType
					int posY = (int) event.getSceneY();
					int speed = (int) ((Math.random() * 100) % 30) + 30;
					int fishType = (int) ((Math.random() * 10) % 8) + 1;
					//
					createBall.setText(spawnPos + "," + speed + "," + posY + "," + direction + "," + fishType);
				} else if (event.getButton().equals(MouseButton.PRIMARY)) {
					try {
						((Fish) canvas.getChildren().get(0)).stopLoop();
						canvas.getChildren().remove(0);
					} catch (IndexOutOfBoundsException e) {
						System.out.println("No Ball Left!!");
					}
				}
			}
		});

		primaryStage.setTitle("Client(Tank)");
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				clientConnect.shutdown();
				System.out.println("Bye");
				System.exit(0);
			}
		});
	}

	public void deleteBall(Pane canvas, String ballId) {
		Platform.runLater(() -> {
			// System.out.println(canvas.getChildren().size());
			for (int i = 0; i < canvas.getChildren().size(); i++) {
				if (((Fish) canvas.getChildren().get(i)).getId().equalsIgnoreCase(ballId)) {
					((Fish) canvas.getChildren().get(i)).stopLoop();
					canvas.getChildren().remove(i);
				}
			}
		});

		//
	}

	public void generateFish(Pane canvas, int ballID, String border, int speed, float posY, int direction,
			int fishType) {
		Platform.runLater(() -> {

			Random rand = new Random();
			// Random Speed
			// Random Color
			int r = rand.nextInt(256);
			int g = rand.nextInt(256);
			int b = rand.nextInt(256);
			Color color = Color.rgb(r, g, b);

			Fish fish = new Fish(canvas, speed, color, clientConnect, direction, fishType);
			int x = 0;
			if (border.equalsIgnoreCase("left")) {
				x = -15;
			} else {
				x = (int) canvas.getBoundsInLocal().getMaxX() - 15;
			}
			// Random Position
			int y = Math.round(posY);
			fish.relocate(x, y);
			fish.setId("B" + ballID);

			canvas.getChildren().addAll(fish);
		});

	}
}
