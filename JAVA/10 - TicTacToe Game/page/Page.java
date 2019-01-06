package page;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

public abstract class Page extends GridPane {
	PageController pageController;
	public Page() {
		this(10,10);
	}
	public Page(int vGap,int hGap) {
		this.setVgap(vGap);
		this.setHgap(hGap);
		this.setAlignment(Pos.CENTER);
	}
	
	public void addController(PageController pageController) {
		this.pageController = pageController;
	}
}
