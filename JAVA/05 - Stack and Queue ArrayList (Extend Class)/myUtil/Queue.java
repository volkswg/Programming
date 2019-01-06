package myUtil;

public class Queue extends MyBuffer {
	private int count = 0;

	public Queue() {
		this(10);
	}

	public Queue(int queueSize) {
		super(queueSize);
		System.out.println("Queue Created with size:" + this.maxItem);
	}

	public void add(int input) {
		if (count >= this.maxItem) {
			System.out.println("Queue is full!!");
			return;
		}
		this.items.add(input);
		count++;
		System.out.println("Queue:");
		System.out.println("Add:"+input);
		this.showBuffer();
	}

	public int delete() {
		int retVal = 0;
		if (count <= 0) {
			System.out.println("Queue is empty");
			return -999;
		}
		count--;
		retVal = this.items.get(0);
		this.items.remove(0);
		return retVal;
	}
}
