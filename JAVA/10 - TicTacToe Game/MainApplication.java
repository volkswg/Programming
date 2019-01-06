import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import page.Gameplay;
import page.PageController;
import page.WelcomePage;

public class MainApplication extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		// Page Declaration
		WelcomePage welcomePage = new WelcomePage();
		Gameplay gameplay = new Gameplay();
		
		// Scene Declaration
		Scene scene = new Scene(welcomePage, 400, 500);
		
		// Page controller
		PageController pageController = new PageController(scene);
		
		// Add page to controller
		pageController.addScreen(welcomePage, "welcome_page");
		pageController.addScreen(gameplay, "gameplay");
		
		//Send Page controller to all Page
		welcomePage.addController(pageController);
		gameplay.addController(pageController);
		
		
		primaryStage.setTitle("TicTacToe");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
}
