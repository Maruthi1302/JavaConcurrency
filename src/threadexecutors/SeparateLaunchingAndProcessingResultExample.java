package threadexecutors;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class ReportGenerator implements Callable<String> {
	private String sender;
	private String title;

	public ReportGenerator(String sender, String title) {
		this.sender = sender;
		this.title = title;
	}

	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		try {
			Long duration = (long) (Math.random() * 10);
			System.out
					.printf("%s_%s: ReportGenerator: Generating a report during %d seconds\n",
							this.sender, this.title, duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String ret = sender + ": " + title;
		return ret;
	}
}

class ReportRequest implements Runnable {
	private String name;
	private CompletionService<String> service;

	public ReportRequest(String name, CompletionService<String> service) {
		this.name = name;
		this.service = service;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ReportGenerator repGen = new ReportGenerator(name, "Report");
		service.submit(repGen);
	}

}

class ReportProcessor implements Runnable {
	private CompletionService<String> service;
	private boolean end;

	public ReportProcessor(CompletionService<String> service) {
		this.service = service;
		end = false;
	}

	/** 
	 * When one of these tasks is executed when the completion service finishes its execution, the
	 * completion service stores the Future object used to control its execution in a queue. The
	 * poll() method accesses this queue to see if there is any task that has finished its execution
	 * and, if so, returns the first element of that queue which is a Future object for a task that has
	 * finished its execution. When the poll() method returns a Future object, it deletes it from
	 * the queue.
	 * */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!end) {
			try {
				/** 
				 * 1) poll(): The version of the poll() method without arguments checks if there are
				 * any Future objects in the queue. If the queue is empty, it returns null immediately.
				 * Otherwise, it returns its first element and removes it from the queue.
				 * 2) take(): This method, without arguments, checks if there are any Future objects in
				 * the queue. If it is empty, it blocks the thread until the queue has an element. When
				 * the queue has elements, it returns and deletes its first element from the queue.
				*/
				
				Future<String> result = service.poll(20, TimeUnit.SECONDS);
				if (result != null) {
					String report = result.get();
					System.out.printf("ReportReceiver: Report Received:%s\n",
							report);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.printf("ReportSender: End\n");
	}

	public void setEnd(boolean end) {
		this.end = end;
	}
}

/**
 * Normally, when you execute concurrent tasks using an executor, you will send
 * Runnable or Callable tasks to the executor and get Future objects to control
 * the method. You can find situations, where you need to send the tasks to the
 * executor in one object and process the results in another one. For such
 * situations, the Java Concurrency API provides the CompletionService class.
 */

/**
 * This CompletionService class has a method to send the tasks to an executor
 * and a method to get the Future object for the next task that has finished its
 * execution. Internally, it uses an Executor object to execute the tasks. This
 * behavior has the advantage to share a CompletionService object, and sends
 * tasks to the executor so the others can process the results.
 */

/**
 * The limitation is that the second object can only get the Future objects for
 * those tasks that have finished its execution, so these Future objects can
 * only be used to get the results of the tasks.
 */

/**
 * The CompletionService class can execute Callable or Runnable tasks. In this example,
 * you have used Callable,
 */
public class SeparateLaunchingAndProcessingResultExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService executor = Executors.newCachedThreadPool();
		CompletionService<String> service = new ExecutorCompletionService<>(
				executor);
		ReportRequest faceRequest = new ReportRequest("Face", service);
		ReportRequest onlineRequest = new ReportRequest("Online", service);
		Thread faceThread = new Thread(faceRequest);
		Thread onlineThread = new Thread(onlineRequest);

		ReportProcessor processor = new ReportProcessor(service);
		Thread senderThread = new Thread(processor);

		System.out.printf("Main: Starting the Threads\n");
		faceThread.start();
		onlineThread.start();
		senderThread.start();
		try {
			System.out.printf("Main: Waiting for the report generators.\n");
			faceThread.join();
			onlineThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.printf("Main: Shutting down the executor.\n");
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		processor.setEnd(true);
		System.out.println("Main: Ends");
	}
}
