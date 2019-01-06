package lab2;

public class Stack extends MyBuffer{

	private int top;
	// constructor 
	Stack(){
		this(10);
	}
	// overloading constructor
	Stack(int num){
		super(num);
		top = 0;
	}

	public void push(int input){
		if(top >= items.length){
			System.out.println("Stack is Full!!");
			return;
		}
		this.items[top++] = input;
		System.out.println("Push:" + input);
		System.out.print("Stack:");
		this.showBuffer();
	}

	public int pop(){
		if(top <= 0){
			System.out.println("Stack is empty!!");
			return -9999;
		}
		System.out.print("Stack:");
		this.showBuffer();
		return this.items[--top];
	}
	
	public void showBuffer() {
		int i;
		if(top <= 0) {
			System.out.println();
			return;
		}
		System.out.print("{");
		for(i = 0; i < top-1 ; i++) 
			System.out.print(this.items[i] +",");
		System.out.println(this.items[i]+"}");
	}
	
	public double average() {
		double sum = 0;
		if (top <= 0)
			return 0;
		for(int i = 0; i < top ; i++) 
			sum += this.items[i];
		return sum/top;
	}
}
