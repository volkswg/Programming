package Ex4_2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Inventory {
	ArrayList<Item> items = new ArrayList<>();
	PrintWriter printWriter = null;
	String filename;

	public Inventory(String filename) {
		this.filename = filename;
		load();
	}

	private void load() {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new FileReader(this.filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (scanner.hasNext()) {
			String data = scanner.nextLine();
			// System.out.println(data);
			data = data.trim();
			String[] buffData = data.split("\\s*,\\s*");
			items.add(new Item(buffData[0], buffData[1], Integer.parseInt(buffData[2])));
		}
	}

	public void save() {
		try {
			printWriter = new PrintWriter(new FileWriter(this.filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Item item : items) {
			printWriter.println(item.getProductId() + ", " + item.getProductName() + ", " + item.getQuantity());
		}
		printWriter.close();
	}
	
	public void printQuantity(String productID) {
		Item foundItem = search(productID);
		if(foundItem == null)
			return;
		System.out.println(foundItem.getProductId() + ", " +foundItem.getProductName()+ ", " + foundItem.getQuantity());
		System.out.println();
	}

	public void add(String productID, int quantity) {
		Item foundItem = search(productID);
		if (foundItem == null)
			return;
		foundItem.add(quantity);
		System.out.println("added: " + foundItem.getProductName() + ", " + foundItem.getQuantity());
		System.out.println();
	}

	public void delete(String productID, int quantity) {
		Item foundItem = search(productID);
		if (foundItem == null)
			return;
		foundItem.delete(quantity);
		System.out.println("deleted: " + foundItem.getProductName() + ", " + foundItem.getQuantity());
		System.out.println();
	}
	
	private Item search(String productID) {
		for (Item buffItem : items) {
			if (buffItem.getProductId().equals(productID)) {
				return buffItem;
			}
		}
		System.out.println("Product ID not found");
		return null;
	}
}
