package concurrentcollections;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

class Event2 implements Delayed {

	private Date startDate;

	public Event2(Date startDate) {
		this.startDate = startDate;
	}

	@Override
	public int compareTo(Delayed arg0) {
		// TODO Auto-generated method stub
		long result = this.getDelay(TimeUnit.NANOSECONDS)
				- arg0.getDelay(TimeUnit.NANOSECONDS);
		if (result < 0) {
			return -1;
		} else if (result > 0) {
			return 1;
		}
		return 0;
	}

	@Override
	public long getDelay(TimeUnit arg0) {
		// TODO Auto-generated method stub
		Date now = new Date();
		long diff = startDate.getTime() - now.getTime();
		return arg0.convert(diff, TimeUnit.MILLISECONDS);
	}

}

class Task implements Runnable {

	private int id;
	private DelayQueue<Event2> queue;

	public Task(int id, DelayQueue<Event2> queue) {
		this.id = id;
		this.queue = queue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Date now = new Date();
		Date delay = new Date();
		delay.setTime(now.getTime() + (id * 1000));
		System.out.printf("Thread %s: %s\n", id, delay);
		for (int i = 0; i < 100; i++) {
			Event2 event = new Event2(delay);
			queue.add(event);
		}
	}

}

/**
 * 1) An interesting data structure provided by the Java API, and that you can
 * use in concurrent applications, is implemented in the DelayedQueue class. In
 * this class, you can store elements with an activation date. The methods that
 * return or extract elements of the queue will ignore those elements whose data
 * is in the future. They are invisible to those methods
 * 
 * 2) To obtain this behavior, the elements you want to store in the
 * DelayedQueue class have to implement the Delayed interface. This interface
 * allows you to work with delayed objects, so you will implement the activation
 * date of the objects stored in the DelayedQueue class as the time remaining
 * until the activation date
 * 
 * This interface forces to implement the following two methods:
 * 
 * a) compareTo(Delayed o): The Delayed interface extends the Comparable
 * interface. This method will return a value less than zero if the object that
 * is executing the method has a delay smaller than the object passed as a
 * parameter, a value greater than zero if the object that is executing the
 * method has a delay bigger than the object passed as a parameter, and the zero
 * value if both objects have the same delay.
 * 
 * b) getDelay(TimeUnit unit): This method has to return the time remaining
 * until the activation date in the units is specified by the unit parameter.
 * The TimeUnit class is an enumeration with the following constants: DAYS,
 * HOURS, MICROSECONDS, MILLISECONDS, MINUTES, NANOSECONDS, and SECONDS.
 * 
 */
public class DelayedQueueExample {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		DelayQueue<Event2> queue = new DelayQueue<>();
		Thread threads[] = new Thread[5];
		for (int i = 0; i < threads.length; i++) {
			Task task = new Task(i + 1, queue);
			threads[i] = new Thread(task);
		}
		// Start threads.
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		// Wait for finalization.
		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}

		/**
		 * Write to the console the events stored in the queue. While the size of the queue
		 * is bigger than zero, use the poll() method to obtain an Event class. If it returns
		 * null, put the main thread for 500 milliseconds to wait for the activation of
		 * more events.
		 */
		do {
			int counter = 0;
			Event2 event;
			do {
				/**
				 * The DelayQueue class has other interesting methods, which are as follows:
				 * 1) clear(): This method removes all the elements of the queue.
				 * 2) offer(E e): E represents the class used to parameterize the DelayQueue class.
				 * This method inserts the element passed as a parameter in the queue.
				 * 3) peek(): This method retrieves, but doesn't remove the first element of the queue.
				 * 4) take(): This method retrieves and removes the first element of the queue. If there
				 * aren't any active elements in the queue, the thread that is executing the method will
				 * be blocked until the thread has some active elements
				 */
				event = queue.poll();
				if (event != null)
					counter++;
			} while (event != null);
			System.out.printf("At %s you have read %d events\n", new Date(),
					counter);
			TimeUnit.MILLISECONDS.sleep(500);
		} while (queue.size() > 0);

	}

}
