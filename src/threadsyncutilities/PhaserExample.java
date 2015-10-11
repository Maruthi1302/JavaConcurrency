package threadsyncutilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

class FileSearch implements Runnable {

	private String initPath;
	private String end;
	private List<String> results;
	private Phaser phaser;

	public FileSearch(String initPath, String end, Phaser phaser) {
		this.initPath = initPath;
		this.end = end;
		this.phaser = phaser;
		results = new ArrayList<>();
	}

	private void directoryProcess(File file) {
		File list[] = file.listFiles();
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				if (list[i].isDirectory()) {
					directoryProcess(list[i]);
				} else {
					fileProcess(list[i]);
				}
			}
		}
	}

	private void filterResults() {
		List<String> newResults = new ArrayList<>();
		long actualDate = new Date().getTime();

		for (int i = 0; i < results.size(); i++) {
			File file = new File(results.get(i));
			long fileDate = file.lastModified();
			if (actualDate - fileDate < TimeUnit.MILLISECONDS.convert(1,
					TimeUnit.DAYS)) {
				newResults.add(results.get(i));
			}
		}
		results = newResults;
	}

	private void fileProcess(File file) {
		if (file.getName().endsWith(end)) {
			results.add(file.getAbsolutePath());
		}
	}

	private boolean checkResults() {
		if (results.isEmpty()) {
			System.out.printf("%s: Phase %d: 0 results.\n", Thread
					.currentThread().getName(), phaser.getPhase());
			System.out.printf("%s: Phase %d: End.\n", Thread.currentThread()
					.getName(), phaser.getPhase());
			// Meaning, Thread is finished its this checkResult phase and dont
			// wanting to participate in future phases. and hence deregister it self, causing
			// Registered count to decrement.
			phaser.arriveAndDeregister();
			return false;
		} else {
			System.out.printf("%s: Phase %d: %d results.\n", Thread
					.currentThread().getName(), phaser.getPhase(), results
					.size());
			// Meaning, Thread is finished its this checkResult phase and
			// wanting to participate in future phases.
			phaser.arriveAndAwaitAdvance();
			return true;
		}
	}

	private void showInfo() {
		for (int i = 0; i < results.size(); i++) {
			File file = new File(results.get(i));
			System.out.printf("%s: %s\n", Thread.currentThread().getName(),
					file.getAbsolutePath());
		}
		phaser.arriveAndAwaitAdvance();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// Wait till all the threads start running.
		phaser.arriveAndAwaitAdvance();
		// Once all the thread finished calling this method... control passes
		// further down for all threads.
		System.out.printf("%s: Starting.\n", Thread.currentThread().getName());

		File file = new File(initPath);
		if (file.isDirectory()) {
			directoryProcess(file);
		}
		if (!checkResults()) {
			return;
		}
		filterResults();
		if (!checkResults()) {
			return;
		}
		showInfo();
		phaser.arriveAndDeregister();
		System.out.printf("%s: Work completed.\n", Thread.currentThread()
				.getName());
	}

}


/**
 * One of the most complex and powerful functionalities offered by the Java concurrency API is
 * the ability to execute concurrent-phased tasks using the Phaser class. This mechanism is
 * useful when we have some concurrent tasks divided into steps. The Phaser class provides us
 * with the mechanism to synchronize the threads at the end of each step, so no thread starts
 * its second step until all the threads have finished the first one.
 */

/**
 * A Phaser object can be in two states:
 * Active:
 * Phaser enters this state when it accepts the registration of new participants 
 * and its synchronization at the end of each phase
 * 
 * Termination: By default, Phaser enters in this state when all the participants in 
 * Phaser have been deregistered, so Phaser has zero participants. More in detail,
 * Phaser is in the termination state when the method onAdvance() returns the
 * true value. If you override that method, you can change the default behavior. When
 * Phaser is on this state, the synchronization method arriveAndAwaitAdvance()
 * returns immediately without doing any synchronization operation.
 * 
 * A notable feature of the Phaser class is that you haven't had to control any exception
 * from the methods related with the phaser. Unlike other synchronization utilities, threads
 * that are sleeping in a phaser don't respond to interruption events and don't throw an
 * InterruptedException exception.
 * 
 * @author nxp69448
 *
 */

/** Other Important Phaser Methods.
 * arrive(): This method notifies the phaser that one participant has finished the
 * actual phase, but it should not wait for the rest of the participants to continue
 * with its execution. Be careful with the utilization of this method, because it doesn't
 * synchronize with other threads.
 * 
 * awaitAdvance(int phase)
 * Awaits the phase of this phaser to advance from the given phase value,
 * returning immediately if the current phase is not equal to the given phase value or this phaser is terminated.
 * 
 * awaitAdvanceInterruptibly(int phase)
 * Awaits the phase of this phaser to advance from the given phase value, throwing InterruptedException 
 * if interrupted while waiting, or returning immediately if the current phase is not equal to the given phase value or this phaser is terminated.
 * 
 * onAdvance(int phase, int registeredParties)
 * Overridable method to perform an action upon impending phase advance, and to control termination.
 * 
 * getPhase()
 * Returns the current phase number.
 * 
 * getArrivedParties()
 * Returns the number of registered parties that have arrived at the current phase of this phaser.
 * 
 * getUnarrivedParties()
 * Returns the number of registered parties that have not yet arrived at the current phase of this phaser.
 * 
 * getRegisteredParties()
 * Returns the number of parties registered at this phaser.
 * 
 * forceTermination()
 * Forces this phaser to enter termination state.
 * 
 * @author nxp69448
 *
 */
public class PhaserExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Phaser has three participants. This number indicates to Phaser
		//the number of threads that have to execute an arriveAndAwaitAdvance() method
		//before Phaser changes the phase and wakes up the threads that were sleeping.
		
		Phaser phaser = new Phaser(3); // Three Participants.
		
		// This method is used to add the participant dynamically.
		//phaser.register();
		
		// This method is used to add the no of participants dynamically.
		//phaser.bulkRegister(10);
		
		

		FileSearch system = new FileSearch("C:\\Windows", "log", phaser);
		FileSearch apps = new FileSearch("C:\\Program Files", "log", phaser);
		FileSearch documents = new FileSearch("C:\\Documents And Settings",
				"log", phaser);

		Thread systemThread = new Thread(system, "System");
		systemThread.start();
		Thread appsThread = new Thread(apps, "Apps");
		appsThread.start();
		Thread documentsThread = new Thread(documents, "Documents");
		documentsThread.start();
		
		try {
			systemThread.join();
			appsThread.join();
			documentsThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Terminated: " + phaser.isTerminated());

	}

}
