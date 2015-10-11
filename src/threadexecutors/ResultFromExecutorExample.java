package threadexecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class FactorialCalculator implements Callable<Integer> {

	private Integer number;

	public FactorialCalculator(Integer number) {
		this.number = number;
	}

	@Override
	public Integer call() throws Exception {
		// TODO Auto-generated method stub
		int result = 1;
		if ((number == 0) || (number == 1)) {
			result = 1;
		} else {
			for (int i = 2; i <= number; i++) {
				result *= i;
				TimeUnit.MILLISECONDS.sleep(20);
			}
		}
		System.out.printf("%s: %d\n", Thread.currentThread().getName(), result);
		return result;
	}

}

/**
 * One of the advantages of the Executor framework is that you can run
 * concurrent tasks that return a result. The Java Concurrency API achieves this
 * with the following two interfaces:
 * 
 * Callable: This interface has the call() method. In this method, you have to
 * implement the logic of a task. The Callable interface is a parameterized
 * interface, meaning you have to indicate the type of data the call() method
 * will return.
 * 
 * Future: This interface has some methods to obtain the result generated by a
 * Callable object and to manage its state.
 * 
 */
public class ResultFromExecutorExample {

	/**
	 * The other critical point of this example is in the Main class. You send a
	 * Callable object to be executed in an executor using the submit() method.
	 * This method receives a Callable object as a parameter and returns a
	 * Future object that you can use with two main objectives:
	 * 
	 * 1) You can control the status of the task: you can cancel the task and
	 * check if it has finished. For this purpose, you have used the isDone()
	 * method to check if the tasks had finished.
	 * 
	 * 2) You can get the result returned by the call() method. For this
	 * purpose, you have used the get() method. This method waits until the
	 * Callable object has finished the execution of the call() method and has
	 * returned its result. If the thread is interrupted while the get() method
	 * is waiting for the result, it throws an InterruptedException exception.
	 * If the call() method throws an exception, this method throws an
	 * ExecutionException exception.
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(2);
		List<Future<Integer>> resultList = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			Integer number = random.nextInt(10);
			FactorialCalculator calculator = new FactorialCalculator(number);
			Future<Integer> result = executor.submit(calculator);
			resultList.add(result);
		}

		// Create a do loop to monitor the status of the executor.
		do {
			System.out.printf("Main: Number of Completed Tasks:%d\n",
					executor.getCompletedTaskCount());
			for (int i = 0; i < resultList.size(); i++) {
				Future<Integer> result = resultList.get(i);
				System.out.printf("Main: Task %d: %s\n", i, result.isDone());
			}
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (executor.getCompletedTaskCount() < resultList.size());

		System.out.printf("Main: Results\n");
		for (int i = 0; i < resultList.size(); i++) {
			Future<Integer> result = resultList.get(i);
			Integer number = null;
			try {
				// This Method wait for the callable object to finish the call
				// Method if its not finished
				// that you can check using isDone method of Future.
				// if any Exception occurs while waiting we get
				// InterruptedException.
				// if any Exception occurs in call Method we get
				// ExecutionException.
				number = result.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			System.out.printf("Main: Task %d: %d\n", i, number);
		}
		executor.shutdown();

	}
}