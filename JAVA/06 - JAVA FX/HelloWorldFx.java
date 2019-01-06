
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class HelloWorldFx extends Application {
	public static void main(String[] args) {
		
		launch(args);
	}

	public void start(Stage primaryStage) throws NoSuchAlgorithmException, NoSuchProviderException {
		HashMap<String, String> userPass = new HashMap<>();
		PasswordEncyption passObj = new PasswordEncyption();
		userPass.put("Volkswg", passObj.genPassHash("Volkswg"));
		primaryStage.setTitle("JavaFX Welcome");
		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col0 = new ColumnConstraints();
		col0.setMinWidth(85);
		
		GridPane grid = new GridPane();
		grid.getColumnConstraints().addAll(col0,col1);
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		Text scenetitle = new Text("Welcome");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		Label userName = new Label("User Name:");
		grid.add(userName, 0, 1);

		TextField userTextField = new TextField();
		grid.add(userTextField, 1, 1);

		Label pw = new Label("Password:");
		grid.add(pw, 0, 2);

		PasswordField pwBox = new PasswordField();
		
		grid.add(pwBox, 1, 2);

		Button btn = new Button("Sign in");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		final Text actiontarget = new Text();
		hbBtn.getChildren().addAll(actiontarget,btn);
		grid.add(hbBtn, 0, 4,2,1);
		
		HBox hintHb = new HBox(10);
		Button showPass = new Button("Hint");
		hintHb.setAlignment(Pos.BOTTOM_RIGHT);
		hintHb.getChildren().add(showPass);
		grid.add(hintHb,1, 5);
		showPass.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				actiontarget.setFill(Color.FIREBRICK);
				userTextField.setText("Volkswg");
				actiontarget.setText("Password: Volkswg");
			}
		});

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				actiontarget.setFill(Color.FIREBRICK);
				// change text from password box to hash with objsalt
				String passIn = passObj.genPassHash(pwBox.getText().trim());
				
				//get username hash password if not have userHashPass = null
				String userHashPass = userPass.get(userTextField.getText().trim());
				if(userHashPass == null) {// not have username
					actiontarget.setText("Incorrect Username/Password");
				}
				else {
					if(userPass.get(userTextField.getText().trim()).equals(passIn)) {
						actiontarget.setFill(Color.LIGHTGREEN);
						actiontarget.setText("Logged in");
					}	
					else
					{
						actiontarget.setText("Incorrect Username/Password");
					}
				}
			}
		});
		scenetitle.setId("welcome-text");
		actiontarget.setId("actiontarget");
		Scene scene = new Scene(grid, 350, 275);
		scene.getStylesheets().add(HelloWorldFx.class.getResource("Login.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
