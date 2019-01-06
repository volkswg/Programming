package javaLearn;

public class MainProg {

	static int[] st = new int[10];
	static int top = 0;

	static int[] queue = new int[10];
	static int head = -1;
	static int tail = -1;

	// Stack ====================================================
	static int push(int input) {
		if (top >= st.length)
			System.out.println("Stack is full");
		else {
			st[top++] = input;
		}
		return 0;
	}

	static int pop() {
		if (top > 0)
			return st[--top];
		System.out.println("Stack underflow");
		return 0;
	}
	// ===========================================================

	// Queue======================================================
	static int add(int input) {
		if ((tail == queue.length - 1 && head == 0) || (tail == head - 1)) {
			System.out.println("queue is full");
			return 0;
		}
		if (head == -1 && tail == -1) {// queue is empty
			head = 0;
			queue[++tail] = input;
		} else if (tail == queue.length - 1 && head != 0) {
			tail = 0;
			queue[tail] = input;
		} else {// queue is not empty
			queue[++tail] = input;
		}
		System.out.println("Added:" + input);
		return 1;
	}

	static int delete() {

		int buffRet = 0;
		if (head == -1) { // empty queue
			System.out.println("Queue is empty");
			return 0;
		}

		if (head == tail) { // last Element
			buffRet = queue[head];
			head = -1;
			tail = -1;
			return buffRet;

		}

		else if (head == queue.length - 1) { // circular
			buffRet = queue[head];
			head = 0;
			return buffRet;
		}

		return queue[head++];
	}
	// ===========================================================

	public static void main(String[] args) {
		int j;
		// Stack==================================
		for (j = 0; j <= 10; j++) {
			push(j + 1);
			// add(j + 1)
		}

		for (j = 0; j <= 10; j++) {
			System.out.println(pop());
			// System.out.println(delete());
		}
		// =======================================

		// Queue=================================
		for (int i = 0; i < 6; i++) {
			add(i);
		}
		for (int i = 0; i < 3; i++) {
			System.out.println("de:" + delete());
		}
		for (int i = 0; i < 9; i++) {
			add(i);
		}
		for (int i = 0; i < queue.length; i++) {
			System.out.println("de:" + delete());
		}
		// =======================================
	}
}
