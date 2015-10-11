package concurrentcollections;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

class Client implements Runnable {

	private LinkedBlockingDeque<String> requestList;

	public Client(LinkedBlockingDeque<String> requestList) {
		this.requestList = requestList;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 5; j++) {
				StringBuilder request = new StringBuilder();
				request.append(i);
				request.append(":");
				request.append(j);
				try {
					/**
					 * the put() method to insert strings in the list. If the list is full
					 * (because you have created it with a fixed capacity), the method blocks the execution of its
					 * thread until there is an empty space in the list.
					 */
					requestList.put(request.toString());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.printf("Client: %s at %s.\n", request, new Date());
			}
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("Client: End.\n");
	}

}

/**
 * The main difference between blocking lists and non-blocking lists is that
 * blocking lists has methods to insert and delete elements on it that, if they
 * can't do the operation immediately, because the list is full or empty, they
 * block the thread that make the call until the operation can be made.
 */
public class LinkedBlockingDequeExample {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		LinkedBlockingDeque<String> list = new LinkedBlockingDeque<>();
		Client client = new Client(list);
		Thread thread = new Thread(client);
		thread.start();

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				/**
				 * Main class uses the take() method to get strings from the list. If the list is empty,
				 * the method blocks the execution of its thread until there are elements in the list.
				 */
				String request = list.take();
				System.out.printf("Main: Request: %s at %s. Size: %d\n",
						request, new Date(), list.size());
			}
			TimeUnit.MILLISECONDS.sleep(300);
		}
	}
	/**
	 * SOME USE FULL METHODS.
	 * 
	 * 1) takeFirst() and takeLast(): These methods return the first and the last element
	 * of the list respectively. They remove the returned element from the list. If the list is
	 * empty, these methods block the thread until there are elements in the list.
	 * 
	 * 2) getFirst() and getLast(): These methods return the first and last element from
	 * the list respectively. They don't remove the returned element from the list. If the list is
	 * empty, these methods throw a NoSuchElementExcpetion exception.
	 * 
	 * 3) peek(), peekFirst(), and peekLast(): These methods return the first and the
	 * last element of the list respectively. They don't remove the returned element from the
	 * list. If the list is empty, these methods return a null value.
	 * 
	 * 4) poll(), pollFirst(), and pollLast(): These methods return the first and the
	 * last element of the list respectively. They remove the returned element form the list.
	 * If the list is empty, these methods return a null value.
	 * 
	 * 5) add(), addFirst(), addLast(): These methods add an element in the first
	 * and the last position respectively. If the list is full (you have created it with a fixed
	 * capacity), these methods throw an IllegalStateException exception.
	 */

}
