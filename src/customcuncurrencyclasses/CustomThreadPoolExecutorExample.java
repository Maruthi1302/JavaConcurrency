package customcuncurrencyclasses;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class MyExecutor extends ThreadPoolExecutor {
	private ConcurrentHashMap<String, Date> startTimes;

	public MyExecutor(int arg0, int arg1, long arg2, TimeUnit arg3,
			BlockingQueue<Runnable> arg4) {
		super(arg0, arg1, arg2, arg3, arg4);
		// TODO Auto-generated constructor stub

		startTimes = new ConcurrentHashMap<>();
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		System.out.printf("MyExecutor: Going to shutdown.\n");
		System.out.printf("MyExecutor: Executed tasks: %d\n",
				getCompletedTaskCount());
		System.out.printf("MyExecutor: Running tasks: %d\n", getActiveCount());
		System.out.printf("MyExecutor: Pending tasks: %d\n", getQueue().size());
		super.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		// TODO Auto-generated method stub
		System.out.printf("MyExecutor: Going to immediately shutdown.\n");
		System.out.printf("MyExecutor: Executed tasks: %d\n",
				getCompletedTaskCount());
		System.out.printf("MyExecutor: Running tasks: %d\n", getActiveCount());
		System.out.printf("MyExecutor: Pending tasks: %d\n", getQueue().size());
		return super.shutdownNow();
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		// TODO Auto-generated method stub
		Future<?> result = (Future<?>) r;
		System.out.printf("*********************************\n");
		System.out.printf("MyExecutor: A task is finishing.\n");
		try {
			System.out.printf("MyExecutor: Result: %s\n", result.get());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date startDate = startTimes.remove(String.valueOf(r.hashCode()));
		Date finishDate = new Date();
		long diff = finishDate.getTime() - startDate.getTime();
		System.out.printf("MyExecutor: Duration: %d\n", diff);
		System.out.printf("*********************************\n");

		super.afterExecute(r, t);
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		// TODO Auto-generated method stub
		System.out.printf("MyExecutor: A task is beginning: %s : %s\n",
				t.getName(), r.hashCode());
		startTimes.put(String.valueOf(r.hashCode()), new Date());
		super.beforeExecute(t, r);
	}

}

class SleepTwoSecondsTask implements Callable<String> {

	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		TimeUnit.SECONDS.sleep(2);
		return new Date().toString();
	}
}

/**
 * The Executor framework is a mechanism that allows you to separate the thread
 * creation from its execution. It's based on the Executor and ExecutorService
 * interfaces with the ThreadPoolExecutor class that implements both interfaces.
 * It has an internal pool of threads and provides methods that allow you to
 * send two kinds of tasks for their execution in the pooled threads. These
 * tasks are:
 * 
 * 1)The Runnable interface to implement tasks that don't return a result 2) The
 * Callable interface to implement tasks that return a result
 * 
 * In both cases, you only send the task to the executor. The executor uses one
 * of its pooled threads or creates a new one to execute those tasks. The
 * executor also decides the moment in which the task is executed.
 */
public class CustomThreadPoolExecutorExample {

	public static void main(String[] args) {

		/**
		 * LinkedBlockingDeque is parameterized with the Runnable interface as the
		 * queue that this executor will use to store its pending tasks.
		 */
		MyExecutor myExecutor = new MyExecutor(2, 4, 1000,
				TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());

		List<Future<String>> results = new ArrayList<>();

		for (int idx = 0; idx < 15; idx++) {
			results.add(myExecutor.submit(new SleepTwoSecondsTask()));
		}

		// Get the result of the first five using get method of the future.
		for (int i = 0; i < 5; i++) {
			try {
				String result = results.get(i).get();
				System.out.printf("Main: Result for Task %d :%s\n", i, result);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		myExecutor.shutdown();

		// Get the remainng result using get method of the future.
		for (int i = 0; i < 10; i++) {
			try {
				String result = results.get(i).get();
				System.out.printf("Main: Result for Task %d :%s\n", i, result);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		// Wait for the completion of the executor using the awaitTermination()
		// method.
		try {
			myExecutor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.printf("Main: End of the program.\n");

	}
}
