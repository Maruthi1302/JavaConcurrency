package forkjoinframework;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

class Task2 extends RecursiveTask<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int array[];
	private int start, end;

	public Task2(int array[], int start, int end) {
		this.array = array;
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		// TODO Auto-generated method stub
		System.out.printf("Task: Start from %d to %d\n", start, end);
		if (end - start < 10) {
			if ((3 > start) && (3 < end)) {
				/**
				 * When you throw an unchecked exception in a task, it also affects its parent task (the task
				 * that sent it to the ForkJoinPool class) and the parent task of its parent task, and so on.
				 * If you revise all the output of the program, you'll see that there aren't output messages for
				 * the finalization of some tasks.
				 * 
				 * All of the affected tasks will finish abnormally.
				 */
				throw new RuntimeException("This task throws an"
						+ "Exception: Task from " + start + " to " + end);
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			int mid = (end + start) / 2;
			Task2 task1 = new Task2(array, start, mid);
			Task2 task2 = new Task2(array, mid, end);
			invokeAll(task1, task2);
		}
		System.out.printf("Task: End form %d to %d\n", start, end);
		return 0;
	}
}

/**
 * you can throw (or it can be thrown by any method or object used inside the
 * method) an unchecked exception. The behavior of the ForkJoinTask and
 * ForkJoinPool classes is different from what you may expect. The program
 * doesn't finish execution and you won't see any information about the
 * exception in the console. It's simply swallowed as if it weren't thrown
 * 
 * You can,however, use some methods of the ForkJoinTask class to know if a task
 * threw an exception and what kind of exception it was.
 * 
 */
public class ThrowingExceptionInForkJoinTaskExample {

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int array[]=new int[100];
		Task2 task=new Task2(array,0,100);
		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(task);
		
		pool.shutdown();
		
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(task.isCompletedAbnormally())
		{
			System.out.printf("Main: An exception has ocurred\n");
			System.out.printf("Main: %s\n",task.getException());
		}
		System.out.printf("Main: Result: %d",task.join());
	}
	
	/**
	 * You can obtain the same result obtained in the example, if instead of throwing an exception,
	 * you use the completeExceptionally() method of the ForkJoinTask class. The code
	 * would be like the following:
	 * 
	 * Exception e=new Exception("This task throws an Exception: "+ "Task
	 * from "+start+" to "+end);
	 * completeExceptionally(e);
	 */

}
