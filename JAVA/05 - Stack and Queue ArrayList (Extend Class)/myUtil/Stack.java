package myUtil;

public class Stack extends MyBuffer {
	private int count = 0;

	public Stack() {
		this(10);
	}

	public Stack(int stackSize) {
		super(stackSize);
		System.out.println("Queue Created with size:" + this.maxItem);
	}

	public void push(int input) {
		if (count >= this.maxItem) {
			System.out.println("Stack is full!!");
			return;
		}
		this.items.add(input);
		count++;
		System.out.println("Stack:");
		System.out.println("Push:"+input);
		this.showBuffer();
	}

	public int pop() {
		int retVal = 0;
		if (count <= 0) {
			System.out.println("Stack is empty");
			return -999;
		}
		count--;
		retVal = this.items.get(count);
		this.items.remove(count);
		return retVal;
	}
}
