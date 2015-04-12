package sjy.elwg.utility;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 具有容量限制的栈
 * 压栈时如果栈容量达到上限，则将栈中最老的元素挤出栈，因此压栈一定能成功.
 * 出栈时如果栈为空，则返回null.
 * @author jingyuan.sun
 *
 * @param <T>
 */
public class LmtStack<T> {
	
	private static final int CAPACITY = 10; //栈容量
	
	private Deque<T> deque = new LinkedList<T>();
	
	private int capacity = CAPACITY;
	
	public LmtStack(){
		
	}
	
	public LmtStack(int capacity){
		this.capacity = capacity;
	}
	
	public void push(T obj){
		deque.offerLast(obj);
		while(deque.size() > capacity){
			deque.pollFirst();
		}
	}
	
	public T poll(){
		return deque.pollLast();
	}
	
	public int size(){
		return deque.size();
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
}
