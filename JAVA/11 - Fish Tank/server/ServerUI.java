package server;

import common.PresetGridPane;
import common.ServerLog;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerUI extends Application{

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		ServerLog serverLog = new ServerLog(350, 200);
		
		ServerAccept serverAccept = new ServerAccept(serverLog);
		serverAccept.start();
		
		Button testShutdown = new Button("Shutdown");
		testShutdown.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
//				serverAccept.shutdown();
//				serverAccept.printAllClientList();
//				serverAccept.bCastMsg("%$GenBall:left,15,50,1");
			}
		});
		
		PresetGridPane grid = new PresetGridPane();
		grid.add(serverLog, 0, 1, 3, 2);
		grid.add(testShutdown, 0, 3);

		Scene scene = new Scene(grid, 400, 300);
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				serverAccept.shutdown();
				System.out.println("Bye");
				System.exit(0);
			}
		});
	}
}
