package Ex4_2;

import java.util.Scanner;

public class Application {

	public static void main(String[] args) {
		Scanner scn = new Scanner(System.in);
		Inventory inventory = new Inventory("testFile.txt");
		String input = "";
		boolean quit = false;
		do {
			input = scn.nextLine();
			String[] buffStr = input.split(" ");
			
			//Check Command
			switch (buffStr[0].toLowerCase()) {
				case "add":
					if (buffStr.length != 3) {
						System.out.println("Incorrect parameter(add [productID] [Amount])");
					} else {
						int quantity = 0;
						try {
							quantity = Integer.parseInt(buffStr[2]);
	
						} catch (Exception e) {
							System.out.println("Please Input Amount with Number");
							break;
						}
						inventory.add(buffStr[1], quantity);
					}
					break;
					
					
				case "delete":
					if (buffStr.length != 3) {
						System.out.println("Incorrect parameter(delete [productID] [Amount])");
					} else {
						int quantity = 0;
						try {
							quantity = Integer.parseInt(buffStr[2]);
	
						} catch (Exception e) {
							System.out.println("Please Input Amount with Number");
							break;
						}
						inventory.delete(buffStr[1], quantity);
					}
					break;
					
					
				case "search":
					if (buffStr.length != 2) {
						System.out.println("Incorrect parameter(search [productID]");
					} else {
						inventory.printQuantity(buffStr[1]);
					}
					break;
					
				case "save":
					inventory.save();
					System.out.println("Saved!!");
					break;
					
				case "quit":
					quit = true;
					System.out.println("Goodbye!");
					break;
					
				default:
					System.out.println("Command Not Found!!");
					break;
			}//switch
		}while(!quit);//while
		scn.close();
	}//main
}
