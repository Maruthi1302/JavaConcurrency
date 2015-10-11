package threadmanagement;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockExample {

	static class MyThreadFactory implements ThreadFactory {
		// We cant have static variable in "Non Static inner classes"
		// Because non static inner classes are associated with the instance of
		// the class.
		static int iThreadCreatedCount = 0;

		@Override
		public Thread newThread(Runnable arg0) {
			// TODO Auto-generated method stub
			iThreadCreatedCount++;
			return new Thread(arg0);
		}

		public Thread newThread(ThreadGroup thg , Runnable argo)
		{
			iThreadCreatedCount++;
			return new Thread(thg,argo);
		}
		public int getNoOfThreadsCreatedSoFar() {
			return iThreadCreatedCount;
		}

	}

	class PricesInfo {
		private double price1 = 0.00;
		private double price2 = 0.00;

		ReadWriteLock lokc = null;

		public PricesInfo(boolean fairMode) {
			// Constructor takes the boolean value, its indicates fairness in selecting the
			// long waiting thread, if fairmode is true, while lock is released longest waiting thread
			// is given a chance enter critical section. if fairmode is false random thread is chosen and given a access
			// to critical section.
			lokc = new ReentrantReadWriteLock(fairMode);
		}

		public double getPrice1() {
			double idoubleValue = 0.00;
			lokc.readLock().lock();
			idoubleValue = price1;
			lokc.readLock().unlock();
			return idoubleValue;
		}

		public double getPrice2() {
			double idoubleValue = 0.00;
			lokc.readLock().lock();
			idoubleValue = price2;
			lokc.readLock().unlock();
			return idoubleValue;
		}

		public void setPrices(double dprice1, double dprice2) {
			lokc.writeLock().lock();
			price1 = dprice1;
			price2 = dprice2;
			lokc.writeLock().unlock();
		}
	}

	class ReaderTask implements Runnable {

		// Shared PricesInfo obje
		private PricesInfo pricesInfo;

		public ReaderTask(PricesInfo objPr) {
			pricesInfo = objPr;
		}

		public void run() {
			for (int i = 0; i < 10; i++) {
				System.out.printf("%s: Price 1: %f\n", Thread.currentThread()
						.getName(), pricesInfo.getPrice1());
				System.out.printf("%s: Price 2: %f\n", Thread.currentThread()
						.getName(), pricesInfo.getPrice2());
			}
		}
	}

	class WriterTask implements Runnable {

		// Shared PricesInfo obje
		private PricesInfo pricesInfo;

		public WriterTask(PricesInfo objPr) {
			pricesInfo = objPr;
		}

		public void run() {
			for (int i = 0; i < 3; i++) {
				System.out.printf("Writer: Attempt to modify theprices.\n");
				pricesInfo.setPrices(Math.random() * 10, Math.random() * 8);
				System.out.printf("Writer: Prices have been modified.\n");
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 1) The ReentrantReadWriteLock class has two locks, one for
	 * read operations and one for write operations. The lock used in read operations is obtained
	 * with the readLock() method declared in the ReadWriteLock interface. This lock is an
	 * object that implements the Lock interface, so we can use the lock(), unlock(), and
	 * tryLock() methods. The lock used in write operations is obtained with the writeLock()
	 * method declared in the ReadWriteLock interface. This lock is an object that implements the
	 * Lock interface, so we can use the lock(), unlock(), and tryLock() methods.
	 * 
	 * 2) It is the responsibility of the programmer to ensure the correct use of these locks, using them with the
	 * same purposes for which they were designed.When you get the read lock of a Lock interface,
	 * you can't modify the value of the variable. Otherwise, you probably will have inconsistency
	 * data errors.
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadWriteLockExample rwExp = new ReadWriteLockExample();
		PricesInfo pricesInfo= rwExp.new PricesInfo(false); // shared. Non fair mode.
		
		ThreadGroup readeGroup = new ThreadGroup("Reader Group");
		ThreadGroup writerGroup = new ThreadGroup("Writer Group");
		
		ReadWriteLockExample.MyThreadFactory thFac = new ReadWriteLockExample.MyThreadFactory();
		
		for (int i = 0; i < 5; i++) {
			thFac.newThread(readeGroup, rwExp.new ReaderTask(pricesInfo));
		}
		
		for (int i = 0; i < 2; i++) {
			thFac.newThread(writerGroup, rwExp.new WriterTask(pricesInfo));
		}
	
		// Print the threads count.
		System.out.println("Number of threads created so far:" + thFac.getNoOfThreadsCreatedSoFar());
		// Start the Threads.
		
		Thread[] Writerthreads = new Thread[writerGroup.activeCount()];
		writerGroup.enumerate(Writerthreads);
		for (int i = 0; i < writerGroup.activeCount(); i++) {
			System.out.printf("Writer Details Thread %s: %s\n", Writerthreads[i].getName(),
					Writerthreads[i].getState());
			
		}
		
		Thread[] Readersthreads = new Thread[readeGroup.activeCount()];
		readeGroup.enumerate(Readersthreads);
		for (int i = 0; i < readeGroup.activeCount(); i++) {
			System.out.printf("Reader Details Thread %s: %s\n", Readersthreads[i].getName(),
					Readersthreads[i].getState());
			
		}
		
		
		
	}

}
