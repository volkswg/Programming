package lab2;

public abstract class MyBuffer{
	final int items[];

	public MyBuffer(){
		this(10); // set default item to 10
	}
	public MyBuffer(int num){
		items = new int[10];
	}

	public void showBuffer(){
		System.out.print("{");
		for(int num:items){
			System.out.print(num + ",");
		}
		System.out.print("}\n");
	}

	public double average(){
		double sum = 0;
		for(int num:items)
			sum += num;
		return (items.length>0)?(sum/items.length):0.0;
	}
}