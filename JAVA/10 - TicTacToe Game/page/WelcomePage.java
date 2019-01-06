package page;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class WelcomePage extends Page{
	public WelcomePage() {
		this(10,10);
	}
	public WelcomePage(int vGap,int hGap) {
		super(vGap, hGap);
		this.getStylesheets().add(WelcomePage.class.getResource("welcomePage.css").toExternalForm());
		this.setId("page");
		
		HBox title_box = new HBox();
		
		Label x_label = new Label("X");
		Label o_label = new Label("O");
		x_label.setId("x_label");
		o_label.setId("o_label");
		
		title_box.getChildren().addAll(x_label,o_label);
		
		Button start_btt  = new Button("Start");
		start_btt.setMaxWidth(Double.MAX_VALUE);
		start_btt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				pageController.activate("gameplay");
			}
		});
		this.add(title_box, 0, 0);
		this.add(start_btt, 0, 1);
	}
}
