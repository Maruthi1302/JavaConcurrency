package threadexecutors;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class Task implements Runnable {

	private Date initDate;
	private String name;

	public Task(String name) {
		initDate = new Date();
		this.name = name;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.printf("%s: Task %s: Created on: %s\n", Thread
				.currentThread().getName(), name, initDate);
		System.out.printf("%s: Task %s: Started on: %s\n", Thread
				.currentThread().getName(), name, new Date());

		try {
			Long duration = (long) (Math.random() * 10);
			System.out.printf("%s: Task %s: Doing a task during %d seconds\n",
					Thread.currentThread().getName(), name, duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("%s: Task %s: Finished on: %s\n", Thread
				.currentThread().getName(), name, new Date());
	}
}

class Server {
	private ThreadPoolExecutor executor;

	/**
	 * Class Executors:
	 * 
	 * Factory and utility methods for Executor, ExecutorService, ScheduledExecutorService, ThreadFactory, 
	 * and Callable classes defined in this package. This class supports the following kinds of methods:
	 * 
	 *  1) Methods that create and return an ExecutorService set up with commonly useful configuration settings.
	 *  2) Methods that create and return a ScheduledExecutorService set up with commonly useful configuration settings.
	 *  3) Methods that create and return a "wrapped" ExecutorService, that disables reconfiguration by making implementation-specific 
	 *  methods inaccessible.
	 *  4) Methods that create and return a ThreadFactory that sets newly created threads to a known state.
	 *  5) Methods that create and return a Callable out of other closure-like forms, so they can be used in execution methods requiring Callable. 
	 */
	public Server() {
		/**
		 * The cached thread pool you have
		 * created creates new threads if needed to execute the new tasks, and reuses the existing ones
		 * if they have finished the execution of the task they were running, which are now available. The
		 * reutilization of threads has the advantage that it reduces the time taken for thread creation.
		 * The cached thread pool has, however, a disadvantage of constant lying threads for new tasks,
		 */
		/**
		 * The executor creates a new thread for each task that receives,
		 * (if there is no pooled thread free) so, if you send a large number of tasks and they have long
		 * duration, you can overload the system and provoke a poor performance of your application.
		 */
		
		executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		
		/**
		 * If you want to avoid this problem, the Executors class provides a method to create a
		 * fixed-size thread executor. This executor has a maximum number of threads. If you send
		 * more tasks than the number of threads, the executor won't create additional threads and
		 * the remaining tasks will be blocked until the executor has a free thread. With this behavior,
		 * you guarantee that the executor won't yield a poor performance of your application
		 */
		
		executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(5);
		
		/**The Executors class also provides the newSingleThreadExecutor() method. This is an
		extreme case of a fixed-size thread executor. It creates an executor with only one thread, so it
		can only execute one task at a time. */
		
		//executor = (ThreadPoolExecutor)Executors.newSingleThreadExecutor();
		
	}

	public void executeTask(Task task) {
		System.out.printf("Server: A new task has arrived\n");
		executor.execute(task);
		System.out.printf("Server: Pool Size: %d\n", executor.getPoolSize());
		System.out.printf("Server: Active Count: %d\n",
				executor.getActiveCount());
		System.out.printf("Server: Completed Tasks: %d\n",
				executor.getCompletedTaskCount());
	}

	public void printServerStats()
	{
		/**getLargestPoolSize() method that returns the maximum number of threads
		that has been in the pool at a time */
		System.out.printf("Server: Pool Size: %d\n", executor.getPoolSize()); //This method returns the actual number of threads in the pool of
		//the executor
		System.out.printf("Server: Active Count: %d\n",
				executor.getActiveCount());
		System.out.printf("Server: Completed Tasks: %d\n",
				executor.getCompletedTaskCount());
	}
	public void endServer() {
		/**
		 * One critical aspect of the ThreadPoolExecutor class, and of the executors in general,
		 * is that you have to end it explicitly. If you don't do this, the executor will continue its execution
		 * and the program won't end. If the executor doesn't have tasks to execute, it continues waiting
		 * for new tasks and it doesn't end its execution. A Java application won't end until all its
		 * non-daemon threads finish their execution, so, if you don't terminate the executor, your
		 * application will never end.
		 */
		executor.shutdown();
	}
}
// Other Helpfull APIS of ThreadPoolExecutor.
/**
 * shutdownNow(): This method shut downs the executor immediately. It doesn't
 * execute the pending tasks. It returns a list with all these pending tasks. The tasks
 * that are running when you call this method continue with their execution, but the
 * method doesn't wait for their finalization.
 */
/**
 * awaitTermination(long timeout, TimeUnit unit): This method blocks
 * the calling thread until the tasks of the executor have ended or the timeout occurs.
 * The TimeUnit class is an enumeration with the following constants: DAYS, HOURS,
 * MICROSECONDS, MILLISECONDS, MINUTES, NANOSECONDS, and SECONDS.
 */


public class ThreadExecutorExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server server=new Server();
		for (int i=0; i<100; i++){
			Task task=new Task("Task "+i);
			server.executeTask(task);
		}
		server.printServerStats();
		server.endServer();
	}

}
