package lab2;

public class Queue extends MyBuffer {
	private int head;
	private int tail;
	private int count;

	Queue() {
		this(10);
	}

	Queue(int num) {
		super(num);
		head = 0; // all empty
		tail = 0;
	}

	public void add(int input) {
		if (count >= items.length) {
			System.out.println("Queue is Full!!");
			return;
		}

		this.items[tail++] = input;
		tail = tail % this.items.length;
		count++;
		System.out.println("Add:" + input);
		System.out.print("Queue:");
		this.showBuffer();
	}

	public int delete() {
		if (count <= 0) {
			System.out.println("Queue is empty!!");
			return -9999;
		}
		head = head % this.items.length;
		System.out.print("Queue:");
		this.showBuffer();
		count--;
		return this.items[head++];
	}

	public void showBuffer() {
		int i;
		if (count <= 0) {
			System.out.println();
			return;
		}
		System.out.print("{");
		for (i = head; i < count + head - 1; i++) {
			System.out.print(this.items[i % items.length] + ",");
		}
		System.out.println(this.items[i % items.length] + "}");
	}

	public double average() {
		double sum = 0;
		if (count <= 0)
			return 0;
		for (int i = head; i < count + head; i++)
			sum += this.items[i % items.length];
		return sum / count;
	}

}
