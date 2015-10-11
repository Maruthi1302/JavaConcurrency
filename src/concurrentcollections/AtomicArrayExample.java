package concurrentcollections;

import java.util.concurrent.atomic.AtomicIntegerArray;

class Incrementer implements Runnable {

	private AtomicIntegerArray vector;

	public Incrementer(AtomicIntegerArray vector) {
		this.vector = vector;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < vector.length(); i++) {
			vector.getAndIncrement(i);
		}
	}

}

class Decrementer implements Runnable {
	private AtomicIntegerArray vector;

	public Decrementer(AtomicIntegerArray vector) {
		this.vector = vector;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < vector.length(); i++) {
			vector.getAndDecrement(i);
		}
	}

}

/**
 * using a synchronization mechanism as locks or the synchronized keyword to
 * avoid data inconsistency errors has following issues. 1) Deadlock: This
 * situation occurs when a thread is blocked waiting for a lock that is locked
 * by other threads and will never free it. This situation blocks the program,
 * so it will never finish. 2) If only one thread is accessing the shared
 * object, it has to execute the code necessary to get and release the lock.
 * 
 * To provide a better performance to this situation, the compare-and-swap
 * operation was developed. This operation implements the modification of the
 * value of a variable in the following three steps: 1) You get the value of the
 * variable, which is the old value of the variable. 2) You change the value of
 * the variable in a temporal variable, which is the new value of the variable.
 * 3) You substitute the old value with the new value, if the old value is equal
 * to the actual value of the variable. The old value may be different from the
 * actual value if another thread has changed the value of the variable.
 * 
 * With this mechanism, you don't need to use any synchronization mechanism, so
 * you avoid deadlocks and you obtain a better performance. Java implements this
 * mechanism in the atomic variables. These variables provide the
 * compareAndSet() method that is an implementation of the compare-and-swap
 * operation and other methods based on it.
 * 
 */
public class AtomicArrayExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final int THREADS = 100;
		AtomicIntegerArray vector = new AtomicIntegerArray(1000);
		Incrementer incrementer = new Incrementer(vector);
		Decrementer decrementer = new Decrementer(vector);
		Thread threadIncrementer[] = new Thread[THREADS];
		Thread threadDecrementer[] = new Thread[THREADS];
		for (int i = 0; i < THREADS; i++) {
			threadIncrementer[i] = new Thread(incrementer);
			threadDecrementer[i] = new Thread(decrementer);
			threadIncrementer[i].start();
			threadDecrementer[i].start();
		}
		// Wait for finalization.
		for (int i = 0; i < 100; i++) {
			try {
				threadIncrementer[i].join();
				threadDecrementer[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < vector.length(); i++) {
			if (vector.get(i) != 0) {
				System.out.println("Vector[" + i + "] : " + vector.get(i));
			}
		}
		System.out.println("Main: End of the example");
	}

}
