package threadexecutors;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

class ExecutableTask implements Callable<String> {

	private String name;

	public ExecutableTask(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		try {
			long duration = (long) (Math.random() * 10);
			System.out.printf("%s: Waiting %d seconds for results.\n",
					this.name, duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
		}
		return "Hello, world. I'm " + name;
	}

}

class ResultTask extends FutureTask<String> {

	private String name;

	public ResultTask(Callable<String> arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
		this.name = ((ExecutableTask) arg0).getName();
	}

	@Override
	protected void done() {
		// Code to be executed after Task completes its execution.
		if (isCancelled()) {
			System.out.printf("%s: Has been canceled\n", name);
		} else {
			System.out.printf("%s: Has finished\n", name);
		}
	}

}

/**
 * The FutureTask class provides a method called done() that allows you to
 * execute some code after the finalization of a task executed in an executor.
 * It can be used to make some post-process operations, generating a report,
 * sending results by e-mail, or releasing some resources. This method is called
 * internally by the FutureTask class when the execution of the task that this
 * FutureTask object is controlling finishes. The method is called after the
 * result of the task is set and its status is changed to the isDone status,
 * regardless of whether the task has been canceled or finished normally.
 * 
 * FutureTask implements Runnable, Future<V>, RunnableFuture<V>
 * 
 */
public class TaskControlInExecutorExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService executor = (ExecutorService) Executors
				.newCachedThreadPool();
		ResultTask resultTasks[] = new ResultTask[5];
		for (int i = 0; i < 5; i++) {
			ExecutableTask executableTask = new ExecutableTask("Task " + i);
			resultTasks[i] = new ResultTask(executableTask);
			executor.submit(resultTasks[i]);
		}
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		for (int i = 0; i < resultTasks.length; i++) {
			resultTasks[i].cancel(true);
		}
		for (int i = 0; i < resultTasks.length; i++) {
			try {
				if (!resultTasks[i].isCancelled()) {
					System.out.printf("%s\n", resultTasks[i].get());
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		executor.shutdown();
	}

}
