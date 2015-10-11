package forkjoinframework;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

class Product {
	private String name;
	private double price;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}

class ProductListGenerator {
	public List<Product> generate(int size) {
		List<Product> ret = new ArrayList<Product>();
		for (int i = 0; i < size; i++) {
			Product product = new Product();
			product.setName("Product " + i);
			product.setPrice(10);
			ret.add(product);
		}
		return ret;
	}
}

// If we dont want any return values from task we willl extend RecursiveAction
// Since the Task class doesn't return a result, it extends the RecursiveAction
// class
class Task extends RecursiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Product> products;
	// These attributes will
	// determine the block of products this task has to process.
	private int first;
	private int last;
	private double increment;

	public Task(List<Product> products, int first, int last, double increment) {
		this.products = products;
		this.first = first;
		this.last = last;
		this.increment = increment;
	}

	private void updatePrices() {
		for (int i = first; i < last; i++) {
			Product product = products.get(i);
			product.setPrice(product.getPrice() * (1 + increment));
		}
	}

	/**
	 * RECOMMENDED Code STRUCTURE:
	 *  Inside the task, you will use the structure
	 * recommended by the Java API documentation: 
	 * If (problem size > defaultsize) { 
	 * tasks=divide(task); execute(tasks); 
	 * } else {
	 *  resolve problem using another algorithm; 
	 * }
	 */
	// When a task executes two or more
	// subtasks, it waits for their finalizations. By this way, the thread that
	// was executing
	// that task (called worker-thread) will look for other tasks to execute,
	// taking full
	// advantage of their execution time.
	@Override
	protected void compute() {
		// TODO Auto-generated method stub
		if (last - first < 10) {
			updatePrices();
		} else {
			int middle = (last + first) / 2;
			// Note how it is recursively called.
			// this where fork happens.
			/**
			 * To execute the subtasks that a task creates, it calls the
			 * invokeAll() method. This is a synchronous call, and the task
			 * waits for the finalization of the subtasks before continuing
			 * (potentially finishing) its execution. While the task is waiting
			 * for its subtasks, the worker thread that was executing it takes
			 * another task that was waiting for execution and executes it. With
			 * this behavior, the Fork/Join framework offers a more efficient
			 * task management than the Runnable and Callable objects
			 * themselves.
			 */
			System.out.printf("Task: Pending tasks:%s\n", getQueuedTaskCount());
			Task t1 = new Task(products, first, middle + 1, increment);
			Task t2 = new Task(products, middle + 1, last, increment);
			invokeAll(t1, t2);
		}
	}
}

/**
 * The core of the Fork/Join framework is formed by the following two classes:
 * 
 * ForkJoinPool: It implements the ExecutorService interface and the
 * workstealing algorithm. It manages the worker threads and offers information
 * about the status of the tasks and their execution.
 * 
 * ForkJoinTask: It's the base class of the tasks that will execute in
 * ForkJoinPool. It provides the mechanisms to execute the fork() and join()
 * operations inside a task and the methods to control the status of the tasks.
 * Usually, to implement your Fork/Join tasks, you will implement a subclass of
 * two subclasses of this class: 1) RecursiveAction for tasks with no return
 * result and 2) RecursiveTask for tasks that return one.
 * 
 */
/**
 * The ForkJoinPool class provides other methods to execute a task in execute
 * (Runnable task):This is another version of the execute() method used in the
 * example. In this case, you send a Runnable task to the ForkJoinPool class.
 * NOTE: ForkJoinPool class doesn't use the work-stealing algorithm with
 * Runnable and Callable objects. It's only used with ForkJoinTask objects, so
 * you'd be better off executing them using an executor.
 * 
 */
public class CreateForkJoinExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProductListGenerator generator = new ProductListGenerator();
		List<Product> products = generator.generate(10000);
		Task task = new Task(products, 0, products.size(), 0.20);
		/**
		 * To create the ForkJoinPool object, you have used the constructor
		 * without arguments, so it will be executed with its default
		 * configuration. It creates a pool with a number of threads equal to
		 * the number of processors of the computer. When the ForkJoinPool
		 * object is created, those threads are created and they wait in the
		 * pool until some tasks arrive for their execution.
		 */
		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(task);

		do {
			System.out.printf("Main: Thread Count: %d\n",
					pool.getActiveThreadCount());
			System.out.printf("Main: Thread Steal: %d\n", pool.getStealCount());
			System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
			try {
				TimeUnit.MILLISECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!task.isDone());
		// Finally, like with the Executor framework, you should finish
		// ForkJoinPool using the
		// shutdown() method.
		pool.shutdown();
		// Check if the task has finished without errors with the
		// isCompletedNormally()
		// method and, in that case, write a message to the console.
		if (task.isCompletedNormally()) {
			System.out.printf("Main: The process has completed normally.\n");
		}
		for (int i = 0; i < products.size(); i++) {
			Product product = products.get(i);
			if (product.getPrice() != 12) {
				System.out.printf("Product %s: %f\n", product.getName(),
						product.getPrice());
			}
		}
		System.out.println("Main: End of the program.\n");
	}
}
