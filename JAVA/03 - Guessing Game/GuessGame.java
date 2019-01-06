package Ex3;

public class GuessGame {
	public void startGame() {
		int numRange = 10;
		int guessNumber;
		
		boolean gotWinner = false;
		boolean playerOneCorrect = false;
		boolean playerTwoCorrect = false;
		boolean playerThreeCorrect = false;
		
		int playerOneGuess;
		int playerTwoGuess;
		int playerThreeGuess;
		
		guessNumber = (int)(Math.random()*numRange);		
		System.out.println("I'm thinking of a number between 0 and "+(numRange-1) + "...");
		System.out.println("Number to guess is " + guessNumber);
		
		Player playerOne = new Player(numRange);
		Player playerTwo = new Player(numRange);
		Player playerThree = new Player(numRange);
			
		while(!gotWinner) {
			playerOneGuess = playerOne.guess();
			playerTwoGuess = playerTwo.guess();
			playerThreeGuess = playerThree.guess();
			
			System.out.println("Player one guessed " + playerOneGuess + "\n"
								+ "Player two guessed " + playerTwoGuess + "\n"
								+ "Player three guessed " + playerThreeGuess);
			
			if(playerOneGuess == guessNumber)
				playerOneCorrect = true;
			if(playerTwoGuess == guessNumber)
				playerTwoCorrect = true;
			if(playerThreeGuess == guessNumber)
				playerThreeCorrect = true;
			if(playerOneCorrect || playerTwoCorrect || playerThreeCorrect)
				gotWinner = true;
			if(gotWinner)
				break;
			System.out.println("Player will have to try again.");
		}
		System.out.println("We have a winner!");
		System.out.println("Player one get it right? "+playerOneCorrect);
		System.out.println("Player two get it right? "+playerTwoCorrect);
		System.out.println("Player three get it right? "+playerThreeCorrect);
		
	}
}
