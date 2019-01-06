package page;

import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;


public class PageController {
	 private HashMap<String, Pane> screenMap = new HashMap<>();
	 private Scene main;
	 
	 public PageController(Scene main) {
		 this.main = main;
	 }
	 
	public void addScreen(Pane pane, String name){
         screenMap.put(name, pane);
    }

//    public void removeScreen(String name){
//        screenMap.remove(name);
//    }
    
//    public PageTemplate getPage(String name) {
//    	return screenMap.get(name);
//    }

    public void activate(String name){
        main.setRoot(screenMap.get(name));
    }
}
