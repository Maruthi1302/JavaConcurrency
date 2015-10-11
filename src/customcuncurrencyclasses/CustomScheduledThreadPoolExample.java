package customcuncurrencyclasses;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Note: 1) All the tasks executed by a scheduled pool are an implementation of
 * the RunnableScheduledFuture interface. 2) It receives the Runnable object
 * that is going to be executed by a task, the result that will be returned by
 * this task. 3) RunnableScheduledFuture task that will be used to create the
 * MyScheduledTask object. 4) ScheduledThreadPoolExecutor object that is going
 * to execute the task.
 */
class MyScheduledTask<V> extends FutureTask<V> implements
		RunnableScheduledFuture<V> {
	private RunnableScheduledFuture<V> task;
	private ScheduledThreadPoolExecutor executor;
	private long period;
	private long startDate;

	public MyScheduledTask(Runnable arg0, V arg1,
			RunnableScheduledFuture<V> task,
			ScheduledThreadPoolExecutor executor) {
		super(arg0, arg1);
		this.task = task;
		this.executor = executor;
	}

	@Override
	public long getDelay(TimeUnit arg0) {
		// TODO Auto-generated method stub
		if (!isPeriodic()) {
			return task.getDelay(arg0);
		} else {
			if (startDate == 0) {
				return task.getDelay(arg0);
			} else {
				Date now = new Date();
				long delay = startDate - now.getTime();
				return arg0.convert(delay, TimeUnit.MILLISECONDS);
			}
		}
	}

	@Override
	public int compareTo(Delayed o) {
		// TODO Auto-generated method stub
		return task.compareTo(o);
	}

	@Override
	public boolean isPeriodic() {
		// TODO Auto-generated method stub
		return task.isPeriodic();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (isPeriodic() && (!executor.isShutdown())) {
			Date now = new Date();
			startDate = now.getTime() + period;
			// Add this task to the Queue.
			executor.getQueue().add(this);
		}
		System.out.printf("Pre-MyScheduledTask: %s\n", new Date());
		System.out.printf("MyScheduledTask: Is Periodic: %s\n", isPeriodic());
		/**
		 * execute the task calling the runAndReset() method, and then print
		 * another message with the actual date to the console.
		 */
		super.runAndReset();
		System.out.printf("Post-MyScheduledTask: %s\n", new Date());
	}

	public void setPeriod(long period) {
		this.period = period;
	}

}

/**
 * Scheduled thread pool is an extension of the basic thread pool of the
 * Executor framework that allows you to schedule the execution of tasks to be
 * executed after a period of time. It's implemented by the
 * ScheduledThreadPoolExecutor class and it permits the execution of the
 * following two kinds of tasks:
 * 
 * 1) Delayed tasks: These kinds of tasks are executed only once after a period
 * of time. 2) Periodic tasks: These kinds of tasks are executed after a delay
 * and then periodically every so often. NOTE: Delayed tasks can execute both,
 * the Callable and Runnable objects, but the periodic tasks can only execute
 * Runnable objects. All the tasks executed by a scheduled pool are an
 * implementation of the RunnableScheduledFuture interface
 */
/**
 * Below Example shows. how to implement your own implementation of the
 * RunnableScheduledFuture interface to execute delayed and periodic tasks.
 */
public class CustomScheduledThreadPoolExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
