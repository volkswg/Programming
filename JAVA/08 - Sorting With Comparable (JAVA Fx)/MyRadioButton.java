import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class MyRadioButton extends RadioButton{
	public MyRadioButton(String text,ToggleGroup toggleGroup) {
		super(text);
		this.setUserData(text);
		this.setToggleGroup(toggleGroup);
	}
	public String getValue() {
		return this.getUserData().toString();
	}
}
