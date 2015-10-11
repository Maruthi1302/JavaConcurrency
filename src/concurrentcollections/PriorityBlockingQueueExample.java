package concurrentcollections;

import java.util.concurrent.PriorityBlockingQueue;

class Event implements Comparable<Event> {

	private int thread;
	private int priority;

	public Event(int thread, int priority) {
		this.thread = thread;
		this.priority = priority;
	}

	public int getThread() {
		return thread;
	}

	public int getPriority() {
		return priority;
	}

	@Override
	public int compareTo(Event arg0) {
		// TODO Auto-generated method stub
		if (this.priority > arg0.getPriority()) {
			return -1;
		} else if (this.priority < arg0.getPriority()) {
			return 1;
		} else {
			return 0;
		}
	}

}

class PriorityTask implements Runnable {

	private int id;
	private PriorityBlockingQueue<Event> queue;

	public PriorityTask(int id, PriorityBlockingQueue<Event> queue) {
		this.id = id;
		this.queue = queue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 1000; i++) {
			Event event = new Event(id, i);
			queue.add(event);
		}
	}

}

/**
 * All the elements you want to add to PriorityBlockingQueue have to implement
 * the Comparable interface.
 * 
 * PriorityBlockingQueue uses the compareTo() method when you insert an element
 * in it to determine the position of the element inserted. The greater elements
 * will be the tail of the queue.
 * 
 * Another important characteristic of PriorityBlockingQueue is that it's a
 * blocking data structure. It has methods that, if they can't do their
 * operation immediately, block the thread until they can do it.
 * 
 */
public class PriorityBlockingQueueExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PriorityBlockingQueue<Event> queue = new PriorityBlockingQueue<>();
		Thread taskThreads[] = new Thread[5];
		for (int i = 0; i < taskThreads.length; i++) {
			PriorityTask task = new PriorityTask(i, queue);
			taskThreads[i] = new Thread(task);
		}

		// Start Threads.
		for (int i = 0; i < taskThreads.length; i++) {
			taskThreads[i].start();
		}
		// Wait for the finalization of the threads.
		for (int i = 0; i < taskThreads.length; i++) {
			try {
				taskThreads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("Main: Queue Size: %d\n", queue.size());
		for (int i = 0; i < taskThreads.length * 1000; i++) {
			Event event = queue.poll();
			System.out.printf("Thread %s: Priority %d\n", event.getThread(),
					event.getPriority());
		}
		System.out.printf("Main: Queue Size: %d\n", queue.size());
		System.out.printf("Main: End of the program\n");
	}

}
