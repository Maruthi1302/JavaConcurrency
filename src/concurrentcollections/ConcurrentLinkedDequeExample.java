package concurrentcollections;

import java.util.concurrent.ConcurrentLinkedDeque;

class AddTask implements Runnable {

	private ConcurrentLinkedDeque<String> list;

	public AddTask(ConcurrentLinkedDeque<String> list) {
		this.list = list;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String name = Thread.currentThread().getName();
		for (int i = 0; i < 10000; i++) {
			list.add(name + ": Element " + i);
		}
	}

}

class PollTask implements Runnable {

	private ConcurrentLinkedDeque<String> list;

	public PollTask(ConcurrentLinkedDeque<String> list) {
		this.list = list;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 5000; i++) {
			/**
			 * The pollFirst() method returns and removes the first element of
			 * the list and the pollLast() method returns and removes the last
			 * element of the list. If the list is empty, these methods return a
			 * null value.
			 */
			/**
			 * getFirst() and getLast(): These methods return the first and last element from
			 * the list respectively. They don't remove the returned element from the list. If the list is
			 * empty, these methods throw a NoSuchElementExcpetion exception.
			 */
			/**
			 * peek(), peekFirst(), and peekLast(): These methods return the first and the
			 * last element of the list respectively. They don't remove the returned element from the
			 * list. If the list is empty, these methods return a null value.
			 */
			/**
			 * remove(), removeFirst(), removeLast(): These methods return the first
			 * and the last element of the list respectively. They remove the returned element
			 * from the list. If the list is empty, these methods throw a NoSuchElementException
			 * exception.
			 */
			list.pollFirst();
			list.pollLast();
		}
	}

}

public class ConcurrentLinkedDequeExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConcurrentLinkedDeque<String> list = new ConcurrentLinkedDeque<>();
		Thread threads[] = new Thread[100];
		for (int i = 0; i < threads.length; i++) {
			AddTask task = new AddTask(list);
			threads[i] = new Thread(task);
			threads[i].start();
		}
		System.out.printf("Main: %d AddTask threads have been launched\n",
				threads.length);
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("Main: Size of the List: %d\n", list.size());
		for (int i = 0; i < threads.length; i++) {
			PollTask task = new PollTask(list);
			threads[i] = new Thread(task);
			threads[i].start();
		}
		System.out.printf("Main: %d PollTask threads have been launched\n",
				threads.length);
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("Main: Size of the List: %d\n", list.size());
	}
}
