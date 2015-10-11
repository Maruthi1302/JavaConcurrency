package threadexecutors;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class Task4 implements Runnable {

	private String name;

	public Task4(String sName) {
		name = sName;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.printf("%s: Starting at : %s\n", name, new Date());
	}

}

/**
 * ScheduledThreadPoolExecutor provides other methods to schedule periodic tasks.
 * It is the scheduleWithFixedRate() method. It has the same parameters as the
 * scheduledAtFixedRate() method, but there is a difference worth noticing. In the
 * scheduledAtFixedRate() method, the third parameter determines the period of time
 * between the starting of two executions. In the scheduledWithFixedRate() method,
 * parameter determines the period of time between the end of an execution of the task and the
 * beginning of the next execution.
 */

/**
 * You can also configure the behavior of an instance of the ScheduledThreadPoolExecutor
 * class with the shutdown() method. The default behavior is that the scheduled
 * tasks finish when you call that method. You can change this behavior using the
 * setContinueExistingPeriodicTasksAfterShutdownPolicy() method of the
 * ScheduledThreadPoolExecutor class with a true value. The periodic tasks won't finish
 * upon calling the shutdown() method.
 */
public class TaskSchedulingAtFixedRate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
		System.out.printf("Main: Starting at: %s\n", new Date());
		Task4 task = new Task4("Task4");

//		You have used the scheduledAtFixedRate() method. This method accepts
//		four parameters: the task you want to execute periodically, the delay of time until the first
//		execution of the task, the period between two executions, and the time unit of the second
//		and third parameters. It's a constant of the TimeUnit class. The TimeUnit class is an
//		enumeration with the following constants: DAYS, HOURS, MICROSECONDS, MILLISECONDS,
//		MINUTES, NANOSECONDS, and SECONDS.
		
//		An important point to consider is that the period between two executions is the period of time
//		between these two executions that begins. If you have a periodic task that takes 5 sceconds
//		to execute and you put a period of 3 seconds, you will have two instances of the task
//		executing at a time.
		ScheduledFuture<?> result = executor.scheduleAtFixedRate(
				(Runnable) task, 1, 2, TimeUnit.SECONDS);
		
//		ScheduledThreadPoolExecutor provides other methods to schedule periodic tasks.
//		It is the scheduleWithFixedRate() method. It has the same parameters as the
//		scheduledAtFixedRate() method, but there is a difference worth noticing. In the
//		scheduledAtFixedRate() method, the third parameter determines the period of time
//		between the starting of two executions. In the scheduledWithFixedRate() method,
//		parameter determines the period of time between the end of an execution of the task and the
//		beginning of the next execution.
		
		for (int i = 0; i < 10; i++) {
			// getDelay() method of the ScheduledFuture object to get the number
			// of milliseconds until the next execution of the task.
			System.out.printf("Main: Delay: %d\n",
					result.getDelay(TimeUnit.MILLISECONDS));
			// Sleep the thread during 500 milliseconds.
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		executor.shutdown();
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Main: Finished at: %s\n", new Date());
	}
}
