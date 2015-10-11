package threadmanagement;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ThreadGroupExample {

	class Result {
		private String sName = "";

		public String getName() {
			return sName;
		}

		public void setName(String name) {
			sName = name;
		}
	}

	class SearchTask implements Runnable {
		private Result result = null;

		public SearchTask(Result res) {
			result = res;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String name = Thread.currentThread().getName();
			System.out.printf("Thread %s: Start\n", name);
			try {
				doTask();
				result.setName(name);
			} catch (InterruptedException e) {
				System.out.printf("Thread %s: Interrupted\n", name);
				return;
			}
			System.out.printf("Thread %s: End\n", name);
		}

		private void doTask() throws InterruptedException {
			Random random = new Random((new Date()).getTime());
			int value = (int) (random.nextDouble() * 100);
			System.out.printf("Thread %s: %d\n", Thread.currentThread()
					.getName(), value);
			TimeUnit.SECONDS.sleep(value);
		}

	}

	class TaskThrowingUnCaughtExp implements Runnable
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.printf("Culprit intre..");
				e.printStackTrace();
			}
			throw new RuntimeException("I am Uncaught");
		}
		
	}
	
	class MyThreadGroup extends ThreadGroup
	{

		public MyThreadGroup(String arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void uncaughtException(Thread arg0, Throwable arg1) {
			// TODO Auto-generated method stub
			super.uncaughtException(arg0, arg1);
			System.out.println("I have caught the culprit thread.. " + arg0.getName() + " its ID " + arg0.getId());
			System.out.println("Its exception details");
			arg1.printStackTrace();
			interrupt();
		}
		
	}
	public void runUncaughtExpHandlerForThreadGrupExap()
	{
		MyThreadGroup thG = new MyThreadGroup("Maruthi2");
		SearchTask shtask = new SearchTask(new Result());
		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(thG, shtask);
			thread.start();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Adding Culprit..");
		//Add one exception Throwing Task
		Thread thread2 = new Thread(thG, new TaskThrowingUnCaughtExp());
		thread2.start();
		//thG.interrupt();
	}
	public void runThreadsInGroup()
	{
		ThreadGroupExample thExp = new ThreadGroupExample();
		ThreadGroup thG = new ThreadGroup("Maruthi");
		ThreadGroupExample.Result res = thExp.new Result();
		ThreadGroupExample.SearchTask shTaks = thExp.new SearchTask(res);
		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(thG, shTaks);
			thread.start();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Number of Threads : " + thG.activeCount());
		System.out.printf("Information about the Thread Group\n");
		thG.list(); //Prints information about this thread group to the standard output.

		Thread[] threads = new Thread[thG.activeCount()];
		thG.enumerate(threads);
		for (int i = 0; i < thG.activeCount(); i++) {
			System.out.printf("Thread %s: %s\n", threads[i].getName(),
					threads[i].getState());
		}

		while (thG.activeCount() > 4) {
			System.out.println("Number of Threads : " + thG.activeCount());
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Number of Threads : " + thG.activeCount());
		thG.interrupt(); // it interrupts all the threads in the ThreadGroup.
	}
	/**
	 * An interesting functionality offered by the concurrency API of Java is the ability to group the
	 * threads. This allows us to treat the threads of a group as a single unit and provides access to
	 * the Thread objects that belong to a group to do an operation with them. For example, you have
	 * some threads doing the same task and you want to control them, irrespective of how many
	 * threads are still running, the status of each one will interrupt all of them with a single call.
	 * 
	 * Java provides the ThreadGroup class to work with groups of threads. A ThreadGroup object
	 * can be formed by Thread objects and by another ThreadGroup object, generating a tree
	 * structure of threads.
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ThreadGroupExample tgExp =  new ThreadGroupExample();
		tgExp.runUncaughtExpHandlerForThreadGrupExap();
	}

}
