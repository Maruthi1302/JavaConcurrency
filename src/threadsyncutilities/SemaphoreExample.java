package threadsyncutilities;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* Semaphores: A semaphore is a counter that controls the access to one or
 * more shared resources. This mechanism is one of the basic tools of concurrent
 * programming and is provided by most of the programming languages. 
 */

class PrintQueue {
	private final Semaphore semaphore;

	public PrintQueue() {
		semaphore = new Semaphore(1, true);// passing 1 creates the binary
											// semaphore.
		// Passing true indicates the fairness of semaphore. it gives access to
		// the long waiting thread.
	}

	public void printJob(Object document) {

		try {
			semaphore.acquire();
			long duration = (long) (Math.random() * 10);
			System.out.printf(
					"%s: PrintQueue: Printing a Job during %d seconds\n",
					Thread.currentThread().getName(), duration);
			Thread.currentThread().sleep(duration);
			//Thread.sleep(duration);
			//TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			semaphore.release();
		}

	}
}

class PrintQueueWithManyPrinters extends PrintQueue{
	private final Semaphore semaphore;
	private boolean freePrinters[];
	private Lock lockPrinters;

	public PrintQueueWithManyPrinters() {
		semaphore = new Semaphore(3, true);
		freePrinters = new boolean[3];
		// Passing true indicates the fairness of semaphore. it gives access to
		// the long waiting thread.
		for (int i = 0; i < 3; i++) {
			freePrinters[i] = true;
		}
		lockPrinters = new ReentrantLock();
	}

	public void printJob(Object document) {

		try {
			/**
			 * The acquire(), acquireUninterruptibly(), tryAcquire(), and release()
			 * methods have an additional version which has an int parameter. This parameter represents
			 * the number of permits that the thread that uses them wants to acquire or release, so as to
			 * say, the number of units that this thread wants to delete or to add to the internal counter
			 * of the semaphore. In the case of the acquire(), acquireUninterruptibly(), and
			 * tryAcquire() methods, if the value of this counter is less than this value, the thread will be
			 * blocked until the counter gets this value or a greater one.
			 */
			semaphore.acquire();
			int assignedPrinter=getPrinter();
			long duration=(long)(Math.random()*10);
			System.out.printf("%s: PrintQueue: Printing a Job in Printer %d during %d seconds\n",Thread.currentThread().getName(),
			assignedPrinter,duration);
			TimeUnit.SECONDS.sleep(duration);
			freePrinters[assignedPrinter]=true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			semaphore.release();
		}
	}
	
	private int getPrinter() {
		int ret = -1;
		
		lockPrinters.lock();
		for(int idx=0; idx < freePrinters.length; idx++)
		{
			if(freePrinters[idx])
			{
				freePrinters[idx] = false;
				ret = idx;
				break;
			}
		}
		lockPrinters.unlock();
		return ret;
	}
}

class Job implements Runnable {

	private PrintQueue printQueue;

	public Job(PrintQueue printQueue) {
		this.printQueue = printQueue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.printf("%s: Going to print a job\n", Thread.currentThread()
				.getName());
		printQueue.printJob(new Object());
		System.out.printf("%s: The document has been printed\n", Thread
				.currentThread().getName());
	}

}

public class SemaphoreExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PrintQueue printQueue = new PrintQueue();
		PrintQueueWithManyPrinters printQuee2 = new PrintQueueWithManyPrinters();
		
		Thread thread[] = new Thread[10];
		for (int i = 0; i < 10; i++) {
			thread[i] = new Thread(new Job(printQueue), "Thread" + i);
		}
		for (int i = 0; i < 10; i++) {
			thread[i].start();
		}
	}

}
