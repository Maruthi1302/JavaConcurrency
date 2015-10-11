package threadsyncutilities;

import java.util.Date;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

class Student implements Runnable {

	private Phaser phaser;

	public Student(Phaser phaser) {
		this.phaser = phaser;
	}

	private void doExercise1() {
		try {
			long duration = (long) (Math.random() * 10);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void doExercise3() {
		try {
			long duration = (long) (Math.random() * 10);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void doExercise2() {
		try {
			long duration = (long) (Math.random() * 10);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.printf("%s: Has arrived to do the exam.%s\n", Thread
				.currentThread().getName(), new Date());
		phaser.arriveAndAwaitAdvance();
		System.out.printf("%s: Is going to do the first exercise.%s\n", Thread
				.currentThread().getName(), new Date());
		doExercise1();
		System.out.printf("%s: Has done the first exercise.%s\n", Thread
				.currentThread().getName(), new Date());
		phaser.arriveAndAwaitAdvance();
		System.out.printf("%s: Is going to do the second exercise.%s\n", Thread
				.currentThread().getName(), new Date());
		doExercise2();
		System.out.printf("%s: Has done the second exercise.%s\n", Thread
				.currentThread().getName(), new Date());
		phaser.arriveAndAwaitAdvance();
		System.out.printf("%s: Is going to do the third exercise.%s\n", Thread
				.currentThread().getName(), new Date());
		doExercise3();
		System.out.printf("%s: Has finished the exam. %s\n", Thread
				.currentThread().getName(), new Date());
		phaser.arriveAndAwaitAdvance();
	}

}

class MyPhaser extends Phaser {

	private boolean studentsArrived() {
		System.out
				.printf("Phaser: The exam are going to start. The students are ready.\n");
		System.out.printf("Phaser: We have %d students.\n",
				getRegisteredParties());
		return false;
	}

	private boolean finishFirstExercise() {
		System.out
				.printf("Phaser: All the students have finished the first exercise.\n");
		System.out.printf("Phaser: It's time for the second one.\n");
		return false;
	}

	private boolean finishSecondExercise() {
		System.out
				.printf("Phaser: All the students have finished the second exercise.\n");
		System.out.printf("Phaser: It's time for the third one.\n");
		return false;
	}

	private boolean finishExam() {
		System.out.printf("Phaser: All the students have finished the exam.\n");
		System.out.printf("Phaser: Thank you for your time.\n");
		return true;
	}

	// Each time all the threads finished calling arriveAndAwaitAdvance method
	// Causes the phaser to advance phase with incrementing number.
	@Override
	protected boolean onAdvance(int phase, int registeredParties) {
		switch (phase) {
		case 0:
			return studentsArrived();
		case 1:
			return finishFirstExercise();
		case 2:
			return finishSecondExercise();
		case 3:
			return finishExam();
		default:
			return true;
		}
	}
}

/**
 * @author nxp69448 The Phaser class provides a method that is executed each
 *         time the phaser changes the phase. It's the onAdvance() method. It
 *         receives two parameters: the number of the current phase and the
 *         number of registered participants; it returns a Boolean value, false
 *         if the phaser continues its execution, or true if the phaser has
 *         finished and has to enter into the termination state.
 * 
 *         Default Implementation: The default implementation of this method
 *         returns true if the number of registered participants is zero, and
 *         false otherwise.
 */
/**
 * @author nxp69448
 *
 *Very Important.
 *In the Core class, when you created the MyPhaser object, you didn't specify the number of
 *participants in the phaser. You made a call to the register() method for every Student
 *object created to register a participant in the phaser. This calling doesn't establish a relation
 *between the Student object or the thread that executes it and the phaser. Really, the
 *number of participants in a phaser is only a number. There is no relationship between the
 *phaser and the participants.
 */
public class PhaserControllingExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyPhaser phaser = new MyPhaser();

		Student students[] = new Student[5];
		for (int i = 0; i < students.length; i++) {
			students[i] = new Student(phaser);
			phaser.register();
		}
		Thread threads[] = new Thread[students.length];
		for (int i = 0; i < students.length; i++) {
			threads[i] = new Thread(students[i], "Student " + i);
			threads[i].start();
		}

		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("Main: The phaser has finished: %s.\n",phaser.isTerminated());
	}

}
