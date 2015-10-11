package customcuncurrencyclasses;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

class MyThread extends Thread {
	private Date creationDate;
	private Date startDate;
	private Date finishDate;

	public MyThread(Runnable target, String name) {
		super(target, name);
		setCreationDate();
	}

	public void setCreationDate() {
		creationDate = new Date();
	}

	public void setStartDate() {
		startDate = new Date();
	}

	public void setFinishDate() {
		finishDate = new Date();
	}

	public long getExecutionTime() {
		return finishDate.getTime() - startDate.getTime();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		setStartDate();
		super.run();
		setFinishDate();
	}

}

class MyTask implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

class MyThreadFactory implements ThreadFactory {
	private int counter;
	private String prefix;

	public MyThreadFactory(String prefix) {
		this.prefix = prefix;
		counter = 1;
	}

	@Override
	public Thread newThread(Runnable arg0) {
		// TODO Auto-generated method stub
		MyThread myThread = new MyThread(arg0, prefix + "-" + counter);
		counter++;
		return myThread;
	}

}

/**
 * Internally, an Executor framework uses a ThreadFactory interface to create
 * the threads that it uses to generate the new threads. In this recipe, you
 * will learn how to implement your own thread class, a thread factory to create
 * threads of that class, and how to use that factory in an executor, so the
 * executor will execute your threads.
 * 
 */
public class ThreadFactInExecutorExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyThreadFactory threadFactory=new MyThreadFactory("MyThreadFactory");
		ExecutorService executor = Executors.newCachedThreadPool(threadFactory);
		MyTask task=new MyTask();
		executor.submit(task);
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.printf("Main: End of the program.\n");
	}

}
