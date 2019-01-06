import myUtil.Queue;
import myUtil.Stack;
public class MyApplication {
	public static void main(String[] args) {
		Stack st = new Stack(10);
		Queue q = new Queue(10);
		
		int i;
		for (i = 0; i < 11; i++) {
			st.push(i);
			q.add(i);
			showAvg(st,q);			
		}
		
		for (i = 0; i < 7; i++) {
			System.out.printf("Pop(%d),Dequeue(%d)\n", st.pop(), q.delete());
			showAvg(st,q);		
		}
		
		for (i = 0; i < 5; i++) {
			st.push(i);
			q.add(i);
			showAvg(st,q);		
		}
		
		for (i = 0; i < 10; i++) {
			System.out.printf("Pop(%d),Dequeue(%d)\n", st.pop(), q.delete());
			showAvg(st,q);		
		}
	}
	static void showAvg(Stack stin, Queue qin) {
		System.out.println();
		System.out.println("Stack Avg:"+stin.average());
		System.out.println("Queue Avg:"+qin.average());
		System.out.println();
	}
}
