package server;

import common.PresetGridPane;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerUI extends Application {

	public static void main(String[] args) throws InterruptedException {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		TextArea serverLog_txtArea = new TextArea();
		serverLog_txtArea.setEditable(false);
		ServerAccept serverAccept = new ServerAccept(serverLog_txtArea);
		serverAccept.start();

		PresetGridPane grid = new PresetGridPane();

		Button stopServer = new Button("Stop");
		stopServer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				serverAccept.shutdown();
//				serverAccept.printallmem();
//				serverAccept.bCastMsg("Hello");
			}
		});
		Label title_label = new Label("Server Log");
		title_label.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
		
		grid.add(title_label, 0, 0);
		grid.add(serverLog_txtArea, 0, 1, 3, 2);
		grid.add(stopServer, 0, 3);

		Scene scene = new Scene(grid, 600, 300);
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				serverAccept.shutdown();
				System.exit(0);
			}
		});
	}
}
