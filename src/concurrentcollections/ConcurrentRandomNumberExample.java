package concurrentcollections;

import java.util.concurrent.ThreadLocalRandom;

class TaskLocalRandom implements Runnable {

	/**
	 * The key of this example is in the TaskLocalRandom class. In the
	 * constructor of the class, we make a call to the current() method of the
	 * ThreadLocalRandom class. This is a static method that returns the
	 * ThreadLocalRandom object associated with the current thread, so you can
	 * generate random numbers using that object.
	 * 
	 * If the thread that makes the call does not have any object associated
	 * yet, the class creates a new one.
	 */
	public TaskLocalRandom() {
		// initialize the random-number
		// generator to the actual thread using the current() method.
		ThreadLocalRandom.current();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String name = Thread.currentThread().getName();
		for (int i = 0; i < 10; i++) {
			// In the run() method of the TaskLocalRandom class, make a call to
			// the current()
			// method to get the random generator associated with this thread
			System.out.printf("%s: %d\n", name, ThreadLocalRandom.current()
					.nextInt(10));
		}
	}

}

/**
 * 1) The Java concurrency API provides a specific class to generate
 * pseudo-random numbers in concurrent applications. 2) It's the
 * ThreadLocalRandom class and it's new in the Java 7 Version 3) It works as the
 * thread local variables. Every thread that wants to generate random numbers
 * has a different generator, but all of them are managed from the same class.
 * 4) With this mechanism, you will get a better performance than using a shared
 * Random object to generate the random numbers of all the threads
 * 
 */
public class ConcurrentRandomNumberExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread threads[] = new Thread[3];
		for (int i = 0; i < 3; i++) {
			TaskLocalRandom task = new TaskLocalRandom();
			threads[i] = new Thread(task);
			threads[i].start();
		}
	}

}
