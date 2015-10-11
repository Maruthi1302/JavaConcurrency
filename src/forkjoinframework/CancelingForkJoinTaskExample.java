package forkjoinframework;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

class ArrayGenerator {
	public int[] generateArray(int size) {
		int array[] = new int[size];
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			array[i] = random.nextInt(10);
		}
		return array;
	}
}

class TaskManager {
	private List<ForkJoinTask<Integer>> tasks;

	public TaskManager() {
		tasks = new ArrayList<>();
	}

	public void addTask(ForkJoinTask<Integer> task) {
		tasks.add(task);
	}

	public void cancelTasks(ForkJoinTask<Integer> cancelTask) {
		for (ForkJoinTask<Integer> task : tasks) {
			if (task != cancelTask) {
				//The ForkJoinTask class provides the cancel() method that allows you to cancel a
				//task if it hasn't been executed yet. This is a very important point.
				//If the task has begun its execution, a call to the cancel() method has no effect.
				
				//Very important.
				/**
				 * The method receives a parameter	as a Boolean value called mayInterruptIfRunning. This name may make you think that,
				 * if you pass the true value to the method, the task will be canceled even if it is running. The
				 * Java API documentation specifies that, in the default implementation of the ForkJoinTask
				 * class, this attribute has no effect. The tasks are only canceled if they haven't started their
				 * execution. The cancellation of a task has no effect over the tasks that this task sent to the
				 * pool. They continue with their execution.
				 */
				task.cancel(true);
				((SearchNumberTask) task).writeCancelMessage();
			}
		}
	}
}

class SearchNumberTask extends RecursiveTask<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int numbers[];
	private int start, end;
	private int number;
	private TaskManager manager;
	private final static int NOT_FOUND = -1;

	public SearchNumberTask(int numbers[], int start, int end, int number,
			TaskManager manager) {
		this.numbers = numbers;
		this.start = start;
		this.end = end;
		this.number = number;
		this.manager = manager;
	}

	@Override
	protected Integer compute() {
		// TODO Auto-generated method stub
		System.out.println("Task: " + start + ":" + end);
		int ret;
		if (end - start > 10) {
			ret = launchTasks();
		} else {
			ret = lookForNumber();
		}
		return ret;
	}

	private int lookForNumber() {
		for (int i = start; i < end; i++) {
			if (numbers[i] == number) {
				System.out.printf("Task: Number %d found in position%d\n",
						number, i);
				manager.cancelTasks(this);
				return i;
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return NOT_FOUND;
	}

	private int launchTasks() {
		int mid = (start + end) / 2;
		SearchNumberTask task1 = new SearchNumberTask(numbers, start, mid,
				number, manager);
		SearchNumberTask task2 = new SearchNumberTask(numbers, mid, end,
				number, manager);
		manager.addTask(task1);
		manager.addTask(task2);
		task1.fork();
		task2.fork();
		int returnValue;
		returnValue = task1.join();
		if (returnValue != -1) {
			return returnValue;
		}
		returnValue = task2.join();
		return returnValue;
	}

	public void writeCancelMessage() {
		System.out.printf("Task: Canceled task from %d to %d", start, end);
	}
}

/**
 * Important:
 * 
 * 1) When you execute the ForkJoinTask objects in a ForkJoinPool class, you can
 * cancel them before they start their execution. 2) The ForkJoinPool class
 * doesn't provide any method to cancel all the tasks it has running or waiting
 * in the pool. 3) When you cancel a task, you don't cancel the tasks this task
 * has executed.
 * 
 */

/**
 * 
 * In this recipe, you will implement an example of cancelation of ForkJoinTask
 * objects. You will look for the position of a number in an array. The first
 * task that finds the number will cancel the remaining tasks. As that
 * functionality is not provided by the Fork/Join framework, you will implement
 * an auxiliary class to do this cancelation.
 * 
 */
public class CancelingForkJoinTaskExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayGenerator generator = new ArrayGenerator();
		int array[] = generator.generateArray(1000);
		TaskManager manager = new TaskManager();
		ForkJoinPool pool = new ForkJoinPool();
		SearchNumberTask task = new SearchNumberTask(array, 0, 1000, 5, manager);
		pool.execute(task);
		pool.shutdown();
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Main: The program has finished\n");
	}
}
