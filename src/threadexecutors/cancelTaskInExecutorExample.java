package threadexecutors;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class Task5 implements Callable<String> {

	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		while (true) {
			System.out.printf("Task: Test\n");
			Thread.sleep(100);
		}
	}
}

/**
 * When you work with an executor, you don't have to manage threads. You only
 * implement the Runnable or Callable tasks and send them to the executor. It's
 * the executor that's responsible for creating threads, managing them in a
 * thread pool, and finishing them if they are not needed. Sometimes, you may
 * want to cancel a task that you sent to the executor. In that case, you can
 * use the cancel() method of Future that allows you to make that cancellation
 * operation
 */
public class cancelTaskInExecutorExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
				.newCachedThreadPool();
		Task5 task = new Task5();
		System.out.printf("Main: Executing the Task\n");
		Future<String> result = executor.submit(task);
		
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Main: Canceling the Task\n");
		result.cancel(true);
		/**
		 * If you use the get() method of a Future object that controls a task that has been canceled,
		 * the get() method will throw a CancellationException exception.
		 */
		System.out.printf("Main: Canceled: %s\n",result.isCancelled());
		System.out.printf("Main: Done: %s\n",result.isDone());
		executor.shutdown();
		System.out.printf("Main: The executor has finished\n");
	}

}
