package myUtil;

import java.util.ArrayList;

public abstract class MyBuffer{
	final ArrayList<Integer> items;
	int maxItem;

	public MyBuffer(){
		this(10); // set default item to 10
	}
	
	public MyBuffer(int maxItem){
		items = new ArrayList<>();
		this.maxItem = maxItem;
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
		return (items.size()>0)?(sum/items.size()):0.0;
	}
}