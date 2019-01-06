package client;

import common.PresetGridPane;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientUI extends Application {
	public static void main(String[] args) throws InterruptedException {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		TextArea chat_log_txtarea = new TextArea();
		chat_log_txtarea.setPrefWidth(270);
		chat_log_txtarea.setPrefHeight(280);
		chat_log_txtarea.setEditable(false);
		
		Label title_label = new Label("Chat Room");
		title_label.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
		
		ClientConnect client = new ClientConnect(chat_log_txtarea,title_label);
		client.start();
		
		TextField chat_field = new TextField();
		chat_field.setPrefWidth(230);
		
		HBox chat_Hbox = new HBox(10);
		chat_Hbox.setMinWidth(270);

		chat_field.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					String textSend = chat_field.getText();
					System.out.println("txt"+textSend);
					if(!textSend.equals("")) {
						client.sendMsg(chat_field.getText());
						chat_field.setText("");					
					}
				}
			}
		});

		Button sendMsg_btt = new Button("Send");
		sendMsg_btt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String textSend = chat_field.getText();
				System.out.println("txt"+textSend);
				if(!textSend.equals("")) {
					client.sendMsg(chat_field.getText());
					chat_field.setText("");					
				}
			}
		});
		chat_Hbox.getChildren().addAll(chat_field, sendMsg_btt);
		
		PresetGridPane grid = new PresetGridPane();

		grid.add(title_label, 0, 0);
		grid.add(chat_log_txtarea, 0, 1);
		grid.add(chat_Hbox, 0, 2);

		Scene scene = new Scene(grid, 300, 400);
		primaryStage.setTitle("Client");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				client.sendMsg("%$GoodBye");
				client.shutdown();
				System.exit(0);
			}
		});

	}
}
