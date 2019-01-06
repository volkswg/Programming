package Ex4_2;

public class Item {
	private String productId;
	private String productName;
	private int quantity;
	
	public Item(String productId, String productName, int quantity) {
		this.productId = productId;
		this.productName = productName;
		this.quantity = quantity;
	}

	public void add(int quantity) {
		this.quantity += quantity;
	}
	
	public void delete(int quantity) {
		this.quantity -= quantity;
	}
	
	public String getProductId() {
		return productId;
	}
	public String getProductName() {
		return productName;
	}
	public int getQuantity() {
		return quantity;
	}
	
}
