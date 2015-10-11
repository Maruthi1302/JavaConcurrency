package threadexecutors;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class RejectedTaskController implements RejectedExecutionHandler {

	@Override
	public void rejectedExecution(Runnable arg0, ThreadPoolExecutor arg1) {
		// TODO Auto-generated method stub
		System.out.printf(
				"RejectedTaskController: The task %s has been rejected\n",
				arg0.toString());
		System.out.printf("RejectedTaskController: %s\n", arg1.toString());
		System.out.printf("RejectedTaskController: Terminating:%s\n",
				arg1.isTerminating());
		System.out.printf("RejectedTaksController: Terminated: %s\n",
				arg1.isTerminated());
	}
}

class Task6 implements Runnable {

	private String name;

	public Task6(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Task " + name + ": Starting");
		try {
			long duration = (long) (Math.random() * 10);
			System.out
					.printf("Task %s: ReportGenerator: Generating a report during %d seconds\n",
							name, duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Task %s: Ending\n", name);
	}

	public String toString() {
		return name;
	}
}

/**
 * If you send a task to an executor between the shutdown() method and the end
 * of its execution, the task is rejected, because the executor no longer
 * accepts new tasks. The ThreadPoolExecutor class provides a mechanism, which
 * is called when a task is rejected.
 * 
 * RejectedExecutionHandler.
 * When an executor receives a task to execute, it checks if the shutdown() method
 * has been called. If so, it rejects the task. First, it looks for the handler established with
 * setRejectedExecutionHandler(). If there's one, it calls the rejectedExecution()
 * method of that class, otherwise it throws RejectedExecutionExeption. This is a runtime
 * exception, so you don't need to put a catch clause to control it.
 */
public class RejectedTaskHandlerExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RejectedTaskController controller = new RejectedTaskController();
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
				.newCachedThreadPool();
		executor.setRejectedExecutionHandler(controller);
		System.out.printf("Main: Starting.\n");
		for (int i = 0; i < 3; i++) {
			Task6 task = new Task6("Task" + i);
			executor.submit(task);
		}
		System.out.printf("Main: Shutting down the Executor.\n");
		executor.shutdown();
		// Try to submit the task after shutdown.
		System.out.printf("Main: Sending another Task.\n");
		Task6 task=new Task6("RejectedTask");
		executor.submit(task);
		System.out.println("Main: End");
		System.out.printf("Main: End.\n");
	}

}
