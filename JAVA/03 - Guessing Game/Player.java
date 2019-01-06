package Ex3;

//import sun.security.mscapi.KeyStore.MY;

public class Player {
	int number;
	int numRange;

	Player(int numRange){
		this.numRange = numRange;
	}
	public int guess() {
		number = (int)(Math.random()*this.numRange);
		System.out.println("I'm guessing "+ number);
		return number;
	}
}
