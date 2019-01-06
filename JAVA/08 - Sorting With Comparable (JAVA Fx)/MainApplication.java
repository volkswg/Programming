import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainApplication extends Application {
	public static StudentDB studentDB = new StudentDB();

	public static void main(String[] args) {
		launch(args);
	}

	public static GridPane mainPane;

	@Override
	public void start(final Stage primaryStage) {
		StudentDB studentDB = new StudentDB();

		mainPane = new GridPane();
		final Scene scene = new Scene(mainPane, 600, 380);
		mainPane.setHgap(10);
		mainPane.setVgap(10);
		mainPane.setAlignment(Pos.CENTER);

		Label titleText = new Label("Sorting Students");
		titleText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		mainPane.add(titleText, 0, 0, 2, 1);

		ToggleGroup sort_group = new ToggleGroup();
		MyRadioButton buttonId = new MyRadioButton("ID Number", sort_group);
		buttonId.setSelected(true);
		MyRadioButton buttonName = new MyRadioButton("Name", sort_group);
		MyRadioButton buttonLName = new MyRadioButton("Lastname", sort_group);
		MyRadioButton buttonGPA = new MyRadioButton("GPA", sort_group);
		buttonGPA.setToggleGroup(sort_group);

		Label sortBy_label = new Label("Sort By");
		mainPane.add(sortBy_label, 0, 1);
		mainPane.add(buttonId, 0, 2);
		mainPane.add(buttonName, 0, 3);
		mainPane.add(buttonLName, 0, 4);
		mainPane.add(buttonGPA, 0, 5);

		ToggleGroup oder_group = new ToggleGroup();
		MyRadioButton buttonAsc = new MyRadioButton("Ascending", oder_group);
		buttonAsc.setSelected(true);
		MyRadioButton buttonDsc = new MyRadioButton("Descending", oder_group);
		Label order_label = new Label("Order By");

		mainPane.add(order_label, 0, 6);
		mainPane.add(buttonAsc, 0, 7);
		mainPane.add(buttonDsc, 0, 8);

		TextArea display_text = new TextArea();
		display_text.setMaxWidth(200);
		display_text.setEditable(false);

		display_text.setText(studentDB.getAllData());
		mainPane.add(display_text, 1, 1, 1, 10);
		Button sort_btt = new Button("Sort");
		mainPane.add(sort_btt, 0, 9);
		sort_btt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				boolean ascOrder = true;
				String orderValue = ((MyRadioButton) oder_group.getSelectedToggle()).getValue();
				String sortValue = ((MyRadioButton) sort_group.getSelectedToggle()).getValue();

				if (orderValue.toLowerCase().equals("ascending")) {
					ascOrder = true;
				} else
					ascOrder = false;
				switch (sortValue.toLowerCase()) {
				case "id number":
					studentDB.sortByID(ascOrder);
					break;
				case "name":
					studentDB.sortByName(ascOrder);
					break;
				case "lastname":
					studentDB.sortByLName(ascOrder);
					break;
				default:
					studentDB.sortByGPA(ascOrder);
					break;
				}
				display_text.setText(studentDB.getAllData());
			}
		});

		Label add_student = new Label("Add Student");
		mainPane.add(add_student, 2, 1);

		Label student_id = new Label("Id: ");
		TextField id_field = new TextField();
		mainPane.add(student_id, 2, 2);
		mainPane.add(id_field, 3, 2);

		Label student_name = new Label("Name: ");
		TextField name_field = new TextField();
		mainPane.add(student_name, 2, 3);
		mainPane.add(name_field, 3, 3);

		Label student_lname = new Label("Lastname: ");
		TextField lname_field = new TextField();
		mainPane.add(student_lname, 2, 4);
		mainPane.add(lname_field, 3, 4);

		Label student_gpa = new Label("GPA: ");
		TextField gpa_field = new TextField();
		mainPane.add(student_gpa, 2, 5);
		mainPane.add(gpa_field, 3, 5);

		Button add_btt = new Button("Add");
		mainPane.add(add_btt, 2, 6);
		add_btt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (id_field.getText().isEmpty() || name_field.getText().isEmpty() || lname_field.getText().isEmpty()
						|| gpa_field.getText().isEmpty()) {
				} else {
					double gpa = 0;
					try {
						gpa	= Double.parseDouble(gpa_field.getText());
					}catch (Exception e1) {
						System.out.println("GPA -> Double or Integer");
						return;
					}
					studentDB.addStudent(id_field.getText(), name_field.getText(), lname_field.getText(),gpa);
					display_text.setText(studentDB.getAllData());
				}
			}
		});

		Button delete_btt = new Button("Delete");
		mainPane.add(delete_btt, 3, 6);
		delete_btt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				studentDB.delStudent();
				display_text.setText(studentDB.getAllData());
			}
		});

		primaryStage.setTitle("Sorting Student");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
