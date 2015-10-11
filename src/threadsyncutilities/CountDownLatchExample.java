/**
 * 
 */
package threadsyncutilities;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author nxp69448
 * 
 */
class Videoconference implements Runnable {

	private final CountDownLatch controller;

	public Videoconference(int number) {
		controller = new CountDownLatch(number); // Indicates number events
		// ie 10 calls to countDown api to happen.
	}

	public void arrive(String name) {
		System.out.printf("%s has arrived.", name);
		controller.countDown();
		// CountDownLatchs getCount returns number of events pending.
		System.out.printf("VideoConference: Waiting for %d participants\n",
				controller.getCount());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.printf(
				"VideoConference: Initialization: %d participants.\n",
				controller.getCount());

		try {
			controller.await();
			System.out
					.printf("VideoConference: All the participants have come\n");
			System.out.printf("VideoConference: Let's start...\n");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class Participant implements Runnable {

	private Videoconference conference;
	private String name;

	public Participant(Videoconference conference, String name) {
		this.conference = conference;
		this.name = name;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long duration = (long) (Math.random() * 10);
		try {
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		conference.arrive(name);
	}

}
;
/**
 * There are some differences with respect to other synchronization methods, which are
 * as follows:
 *  The CountDownLatch mechanism is not used to protect a shared resource or a
 *  critical section. It is used to synchronize one or more threads with the execution of
 *  various tasks.
 *  It only admits one use. As we explained earlier, once the counter of
 *  CountDownLatch arrives at 0, all the calls to its methods have no effect.
 *  You have to create a new object if you want to do the same synchronization again.
 * @author nxp69448
 *
 */
public class CountDownLatchExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Videoconference conference = new Videoconference(10);
		Thread threadConference = new Thread(conference);
		threadConference.start();

		for (int i = 0; i < 10; i++) {
			Participant p = new Participant(conference, "Participant " + i);
			Thread t = new Thread(p);
			t.start();
		}
		
		 /*It only admits one use. As we explained earlier, once the counter of
		 *  CountDownLatch arrives at 0, all the calls to its methods have no effect.
		 *  You have to create a new object if you want to do the same synchronization again.*/
		
		
	}

}
