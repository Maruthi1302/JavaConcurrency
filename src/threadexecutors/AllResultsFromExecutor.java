package threadexecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**ExecutorService API details
 void shutdown()
 Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted. 
 Invocation has no additional effect if already shut down.
 This method does not wait for previously submitted tasks to complete execution. Use awaitTermination to do that.

 List<Runnable> shutdownNow()
 Attempts to stop all actively executing tasks, halts the processing of waiting tasks, and returns a list of the tasks that were awaiting execution.
 This method does not wait for actively executing tasks to terminate. Use awaitTermination to do that.
 There are no guarantees beyond best-effort attempts to stop processing actively executing tasks. For example, 
 typical implementations will cancel via Thread.interrupt(), so any task that fails to respond to interrupts may never terminate.
 Returns:
 list of tasks that never commenced execution
 */

/**
 * 
 * When you want to wait for the finalization of a tasks, you can use the
 * following two methods: The isDone() method of the Future interface returns
 * true if the task has finished its execution. check the list of Future objects
 * of the submitted task to check all are completed execution or not.
 * 
 * The awaitTermination() method of the ThreadPoolExecutor class puts the thread
 * to sleep until all the tasks have finished their execution after a call to
 * the shutdown() method.
 * 
 * awaitTermination works after calling shutdown() method, and then Executor no
 * longer used.
 * 
 * So best option is to use invokeAll API which will return list of Future
 * objects one all the task have completed execution.
 * 
 */

class Result {
	private String name;
	private int value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}

class Task2 implements Callable<Result> {
	private String name;

	public Task2(String i) {
		this.name = i;
	}

	@Override
	public Result call() throws Exception {
		// TODO Auto-generated method stub
		System.out.printf("%s: Staring\n", this.name);
		try {
			long duration = (long) (Math.random() * 10);
			System.out.printf("%s: Waiting %d seconds for results.\n",
					this.name, duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int value = 0;
		for (int i = 0; i < 5; i++) {
			value += (int) (Math.random() * 100);
		}
		Result result = new Result();
		result.setName(this.name);
		result.setValue(value);
		System.out.println(this.name + ": Ends");
		return result;
	}
}

public class AllResultsFromExecutor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService executor = (ExecutorService) Executors
				.newCachedThreadPool();
		List<Task2> taskList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			Task2 task = new Task2("Name: " + i);
			taskList.add(task);
		}

		List<Future<Result>> resultList = null;
		try {
			resultList = executor.invokeAll(taskList);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();
		System.out.println("Main: Printing the results");
		for (int i = 0; i < resultList.size(); i++) {
			Future<Result> future = resultList.get(i);
			try {
				Result result = future.get();
				System.out.println(result.getName() + ": " + result.getValue());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

}
