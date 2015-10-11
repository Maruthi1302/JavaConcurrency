package customcuncurrencyclasses;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class MyPriorityTask implements Runnable, Comparable<MyPriorityTask> {

	private int priority;
	private String name;

	public MyPriorityTask(String name, int priority) {
		this.name = name;
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	@Override
	public int compareTo(MyPriorityTask arg0) {
		// TODO Auto-generated method stub
		if (this.getPriority() < arg0.getPriority()) {
			return 1;
		}
		if (this.getPriority() > arg0.getPriority()) {
			return -1;
		}
		return 0;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.printf("MyPriorityTask: %s Priority : %d\n", name, priority);
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

/**
 * To convert an executor to a priority-based one is simple. You only have to pass a
 * PriorityBlockingQueue object parameterized with the Runnable interface as a
 * parameter. But with the executor, you should know that all the objects stored in a priority
 * queue have to implement the Comparable interface.
 * 
 * You can configure Executor to use any implementation of the BlockingQueue interface.
 * One interesting implementation is DelayQueue. This class is used to store elements with a
 * delayed activation. It provides methods that only return the active objects. You can use this
 * class to implement your own version of the ScheduledThreadPoolExecutor class.
 *
 */
public class PriorityBasedExecutorExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 1,
				TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());
		for (int i = 0; i < 4; i++) {
			MyPriorityTask task = new MyPriorityTask("Task " + i, i);
			executor.execute(task);
		}
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = 4; i < 8; i++) {
			MyPriorityTask task = new MyPriorityTask("Task " + i, i);
			executor.execute(task);
		}
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Main: End of the program.\n");
	}

}
