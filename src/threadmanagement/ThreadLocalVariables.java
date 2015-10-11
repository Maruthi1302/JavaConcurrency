package threadmanagement;

import java.util.Date;
import java.util.concurrent.TimeUnit;

//If you create an object of a class that implements the Runnable interface and then start
//various Thread objects using the same Runnable object, all the threads share the same
//attributes. This means that, if you change an attribute in a thread, all the threads will be
//affected by this change.\
// Above can be avoided by ThreadLocal<T> variables. These are intiialized by overiding initialValue() of ThreadLocal<T>

// Another object InheritableThreadLocal<T> variables. is for inheriting the Threadlocal variables to the child threads.
// that is if Thread A creates Thread B all the InheritableThreadLocal variables will be inherited to the child thread local variables
// then child can reinitialize those values by overiding childValue() method.
public class ThreadLocalVariables {

	class UnSafeTaskd implements Runnable {
		private Date startDate;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			startDate = new Date();
			System.out.printf("Starting Thread: %s : %s\n", Thread
					.currentThread().getId(), startDate);
			try {
				TimeUnit.SECONDS.sleep((int) Math.rint(Math.random() * 10));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.printf("Thread Finished: %s : %s\n", Thread
					.currentThread().getId(), startDate);
		}

	}

	class SafeTaskd implements Runnable {
		private ThreadLocal<Date> startDate = new ThreadLocal<Date>()
				{
				protected Date initialValue(){
					return new Date();
					}
				};

		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.printf("Starting Thread: %s : %s\n", Thread
					.currentThread().getId(), startDate.get());
			try {
				TimeUnit.SECONDS.sleep((int) Math.rint(Math.random() * 10));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.printf("Thread Finished: %s : %s\n", Thread
					.currentThread().getId(), startDate.get());
		}

	}
	/**
	 * @param args
	 */
	
	public void runUnsafeTask()
	{
		ThreadLocalVariables tl = new ThreadLocalVariables();
		ThreadLocalVariables.UnSafeTaskd task = tl.new UnSafeTaskd();
		for (int i = 0; i < 10; i++) {
			Thread thread = new Thread(task);
			thread.start();
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void runSafeTask()
	{
		ThreadLocalVariables tl = new ThreadLocalVariables();
		//ThreadLocalVariables.UnSafeTaskd task = tl.new UnSafeTaskd();
		ThreadLocalVariables.SafeTaskd task = tl.new SafeTaskd();
		for (int i = 0; i < 10; i++) {
			Thread thread = new Thread(task);
			thread.start();
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ThreadLocalVariables tl = new ThreadLocalVariables();
		//ThreadLocalVariables.UnSafeTaskd task = tl.new UnSafeTaskd();
		ThreadLocalVariables.SafeTaskd task = tl.new SafeTaskd();
		for (int i = 0; i < 10; i++) {
			Thread thread = new Thread(task);
			thread.start();
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
