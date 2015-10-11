package threadexecutors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * You may want to execute a task after a period of time or to execute a task
 * periodically. For these purposes, the Executor framework provides the
 * ScheduledThreadPoolExecutor class.
 */

class Task3 implements Callable<String> {
	private String name;

	public Task3(String name) {
		this.name = name;
	}

	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		System.out.printf("%s: Starting at : %s\n", name, new Date());
		return "Hello, world";
	}

}

public class SecheduledExecutorExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors
				.newScheduledThreadPool(5);
		System.out.printf("Main: Starting at: %s\n", new Date());
		List<ScheduledFuture<String>> result = new ArrayList<>(); 
		for (int i = 0; i < 5; i++) {
			Task3 t3 = new Task3("Name : " + i);
			result.add(executor.schedule(t3, i + 1, TimeUnit.SECONDS));
		}

		// Request the finalization of the executor using the shutdown() method
//		Finally, you can configure the behavior of the ScheduledThreadPoolExecutor
//		class when you call the shutdown() method and there are pending tasks waiting for
//		the end of their delay time. The default behavior is that those tasks will be executed
//		despite the finalization of the executor. You can change this behavior using the
//		setExecuteExistingDelayedTasksAfterShutdownPolicy() method of the
//		ScheduledThreadPoolExecutor class. With false, at the time of shutdown(),
//		pending tasks won't get executed.
		executor.shutdown();
		try {

			// Wait for the finalization of all the tasks using the
			// awaitTermination() method
			// of the executor.
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Print the results from all the tasks
		for(ScheduledFuture<String> fut : result)
		{
			try {
				System.out.printf("Result of : %s\n",fut.get());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
