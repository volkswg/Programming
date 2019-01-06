package gamemanager;

import java.util.ArrayList;

import guiCustomObject.ButtonXO;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class GameManager {
	private Pane gamepane;
	private int table[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private int turnCount;
	private ArrayList<ButtonXO> allBtt;
	private String displayXturn = "X turn";
	private String displayOturn = "O turn";
	public GameManager() {
		turnCount = 0;
		allBtt = new ArrayList<>();
	}

	public void initgame() {
		for (int i = 0; i < table.length; i++) {
			table[i] = 0;
		}
		turnCount = 0;
		displayResult(displayXturn, "#95c623");
		// enable all button
		if (allBtt.size() == 0) {
			getAllXOButton();
		}
		for (ButtonXO tmp : allBtt) {
			tmp.resetButton();
		}
		displayResult(displayXturn, "#95c623");
//		gamepane.getChildren().remove(0);
	}

	public void mark(String marktype, int pos) {
		switch (marktype.toLowerCase()) {
		case "x":
			table[pos] = 1;
			if (isWinner(1)) {
				System.out.println("Ret:1");
				setEndGame();
				displayResult("X win","#95c623");
				return;
			}
			turnCount++;
			break;
		case "o":
			table[pos] = 2;
			if (isWinner(2)) {
				System.out.println("Ret:2");
				setEndGame();
				displayResult("O win","#464646");
				return;
			}
			turnCount++;
			break;
		}
		if (turnCount == 9) {
			System.out.println("Ret:3");
			setEndGame();
			displayResult("Draw"," #464646");
			return;
		}
		if(turnCount%2 == 0) {
			displayResult(this.displayXturn, "#95c623");
		}
		else {
			displayResult(this.displayOturn, "#464646");
		}
		System.out.println("Ret:0");
	}

	public void printTable() {
		for (int i = 0; i < table.length; i++) {
			System.out.print(table[i]);
			if ((i + 1) % 3 == 0) {
				System.out.println();
			}
		}
	}

	private boolean isWinner(int xoSym) {
		return ((table[6] == xoSym && table[7] == xoSym && table[8] == xoSym) || // across the top
				(table[3] == xoSym && table[4] == xoSym && table[5] == xoSym) || // across the middle
				(table[0] == xoSym && table[1] == xoSym && table[2] == xoSym) || // across the bottom
				(table[6] == xoSym && table[3] == xoSym && table[0] == xoSym) || // down the left side
				(table[7] == xoSym && table[4] == xoSym && table[1] == xoSym) || // down the middle
				(table[8] == xoSym && table[5] == xoSym && table[2] == xoSym) || // down the right side
				(table[6] == xoSym && table[4] == xoSym && table[2] == xoSym) || // diagonal
				(table[8] == xoSym && table[4] == xoSym && table[0] == xoSym)); // diagonal
	}
	

	public int getTurn() {
		
		return turnCount;
	}

	public void addGamePane(Pane gamepane) {
		this.gamepane = gamepane;
	}

	private void setEndGame() {
		// get sub gridpane(BGBoard)
		if (allBtt.size() == 0) {
			getAllXOButton();
		}
		for (ButtonXO tmp : allBtt) {
			tmp.disableButton();
		}
	}
	

	private void displayResult(String display_txt, String colorCode) {
		((Label) this.gamepane.getChildren().get(1)).setText(display_txt);
		((Label) this.gamepane.getChildren().get(1)).setStyle("-fx-text-fill: " + colorCode);
	}

	private void getAllXOButton() {
		Node tmptest = this.gamepane.getChildren().get(0);

		// get all ButtonXO
		for (Node tmp : ((GridPane) tmptest).getChildren()) {
			this.allBtt.add(((ButtonXO) tmp));
		}
	}
}
