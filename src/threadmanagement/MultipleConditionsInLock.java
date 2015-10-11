package threadmanagement;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * When a thread calls the signal() or signallAll() methods of a condition, one
 * or all of the threads that were waiting for that condition are woken up, but
 * this doesn't guarantee that the condition that made them sleep is now true,
 * so you must put the await() calls inside a while loop. You can't leave that
 * loop until the condition is true. While the condition is false, you must call
 * await() again.
 * 
 * @author nxp69448
 * 
 */
class FileMock {
	private String content[];
	private int index;

	public FileMock(int size, int length) {
		content = new String[size];
		for (int i = 0; i < size; i++) {
			StringBuilder buffer = new StringBuilder(length);
			for (int j = 0; j < length; j++) {
				int indice = (int) Math.random() * 255;
				buffer.append((char) indice);
			}
			content[i] = buffer.toString();
		}
		index = 0;
	}

	public boolean hasMoreLines() {
		return index < content.length;
	}

	public String getLine() {
		if (this.hasMoreLines()) {
			System.out.println("Mock: " + (content.length - index));
			return content[index++];
		}
		return null;
	}

}

class Buffer {

	private LinkedList<String> buffer;
	private int maxSize;
	private ReentrantLock lock;
	private Condition lines;
	private Condition space;
	private boolean pendingLines;

	public Buffer(int maxSize) {
		this.maxSize = maxSize;
		buffer = new LinkedList<>();
		lock = new ReentrantLock();
		lines = lock.newCondition();
		space = lock.newCondition();
		pendingLines = true;
	}

	public void insert(String line) {
		lock.lock();
		try {
			// We need to encolose await() call in while loop
			// because if some other thread signals on the condition it wakes up
			// the thread
			// that suspended because of condition being false. signals does not
			// guarantee the
			// condition being true. hence we need to check the actual condition
			// in while loop
			// if still it is false we need to call await() again.
			while (buffer.size() == maxSize) {
				space.await();
			}
			buffer.offer(line);
			System.out.printf("%s: Inserted Line: %d\n", Thread.currentThread()
					.getName(), buffer.size());
			lines.signalAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public String get() {
		String line = null;
		lock.lock();
		try {
			// We need to encolose await() call in while loop
			// because if some other thread signals on the condition it wakes up
			// the thread
			// that suspended because of condition being false. signals does not
			// guarantee the
			// condition being true. hence we need to check the actual condition
			// in while loop
			// if still it is false we need to call await() again.
			while ((buffer.size() == 0) && (hasPendingLines())) {
				lines.await();
			}
			if (hasPendingLines()) {
				line = buffer.poll();
				System.out.printf("%s: Line Readed: %d\n", Thread
						.currentThread().getName(), buffer.size());
				space.signalAll();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return line;
	}

	public void setPendingLines(boolean pendingLines) {
		this.pendingLines = pendingLines;
	}

	public boolean hasPendingLines() {
		return pendingLines || buffer.size() > 0;
	}
}

class Producer implements Runnable {

	private FileMock mock;
	private Buffer buffer;

	public Producer(FileMock mock, Buffer buffer) {
		this.mock = mock;
		this.buffer = buffer;
	}

	public void run() {
		buffer.setPendingLines(true);
		while (mock.hasMoreLines()) {
			String line = mock.getLine();
			buffer.insert(line);
		}
		buffer.setPendingLines(false);
	}
}

class Consumer implements Runnable {

	private Buffer buffer;

	public Consumer(Buffer buffer) {
		this.buffer = buffer;
	}

	public void run() {
		while (buffer.hasPendingLines()) {
			String line = buffer.get();
			processLine(line);
		}
	}

	private void processLine(String line) {
		try {
			Random random = new Random();
			Thread.sleep(random.nextInt(100));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

/**
 * The Condition interface has other versions of the await() method, which are
 * as follows: 1) await(long time, TimeUnit unit): The thread will be sleeping
 * until: a) It's interrupted b) Another thread calls the singal() or
 * signalAll() methods in the condition c) The specified time passes The
 * TimeUnit class is an enumeration with the following constants: DAYS, HOURS,
 * MICROSECONDS, MILLISECONDS, MINUTES, NANOSECONDS,and SECONDS 2)
 * awaitUninterruptibly(): The thread will be sleeping until: a) Another thread
 * calls the signal() or signalAll() methods, which can't be interrupted b)
 * awaitUntil(Date date): The thread will be sleeping until: It's interrupted
 * c) Another thread calls the singal() or signalAll() methods in the
 * condition  The specified date arrives
 */

public class MultipleConditionsInLock {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileMock mock = new FileMock(100, 10);
		Buffer buffer = new Buffer(20);
		Producer producer = new Producer(mock, buffer);
		Thread threadProducer = new Thread(producer, "Producer");
		Consumer consumers[] = new Consumer[3];
		Thread threadConsumers[] = new Thread[3];
		for (int i = 0; i < 3; i++) {
			consumers[i] = new Consumer(buffer);
			threadConsumers[i] = new Thread(consumers[i], "Consumer " + i);
		}

		threadProducer.start();
		for (int i = 0; i < 3; i++) {
			threadConsumers[i].start();
		}
	}

}
