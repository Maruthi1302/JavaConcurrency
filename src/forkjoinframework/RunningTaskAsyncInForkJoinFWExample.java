package forkjoinframework;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

class FolderProcessor extends RecursiveTask<List<String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String path;
	private String extension;

	public FolderProcessor(String path, String extension) {
		this.path = path;
		this.extension = extension;
	}

	@Override
	protected List<String> compute() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<>();
		List<FolderProcessor> tasks = new ArrayList<>();
		File file = new File(path);
		File content[] = file.listFiles();
		if (content != null) {
			for (int i = 0; i < content.length; i++) {
				if (content[i].isDirectory()) {
					FolderProcessor task = new FolderProcessor(
							content[i].getAbsolutePath(), extension);
					task.fork();
					tasks.add(task);
				} else {
					if (checkFile(content[i].getName())) {
						list.add(content[i].getAbsolutePath());
					}
				}
			}
			if (tasks.size() > 50) {
				System.out.printf("%s: %d tasks ran.\n",
						file.getAbsolutePath(), tasks.size());
			}
			addResultsFromTasks(list, tasks);
			return list;
		}
		return list;
	}

	private boolean checkFile(String name) {
		return name.endsWith(extension);
	}

	/**
	 * Very importatn NOTE:
	 * 
	 *There are two main differences between the get() and the join() methods:
	 *	1) The join() method can't be interrupted. If you interrupt the thread that called 
	 *	the join() method, the method throws an InterruptedException exception.
	 *	
	 *	2)While the get() method will return an ExecutionException exception if 
	 *	the tasks throw any unchecked exception, the join() method will return a
	 *	RuntimeException exception
	 */
	private void addResultsFromTasks(List<String> list,
			List<FolderProcessor> tasks) {

		/**
		 * In this example, you have used the join() method to wait for the finalization of tasks and get
		 * their results. You can also use one of the two versions of the get() method with this purpose
		 * 
		 * get(): This version of the get() method returns the value returned by the
		 * compute() method if ForkJoinTask has finished its execution, or waits
		 * until its finalization.
		 */
		for (FolderProcessor item : tasks) {
			list.addAll(item.join());
		}
	}
}

/**
 * When you use the synchronized methods, the task that calls one of these
 * methods (for example, the invokeAll() method) is suspended until the tasks it
 * sent to the pool finish their execution. This allows the ForkJoinPool class
 * to use the work-stealing algorithm to assign a new task to the worker thread
 * that executed the sleeping task
 * 
 * when you use the asynchronous methods (for example, the fork() method), the
 * task continues with its execution, so the ForkJoinPool class can't use the
 * work-stealing algorithm to increase the performance of the application. In
 * this case, only when you call the join() or get() methods to wait for the
 * finalization of a task, the ForkJoinPool class can use that algorithm.
 * 
 */
public class RunningTaskAsyncInForkJoinFWExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ForkJoinPool pool = new ForkJoinPool();
		FolderProcessor system = new FolderProcessor("C:\\Windows", "log");
		FolderProcessor apps = new FolderProcessor("C:\\Program Files", "log");
		FolderProcessor documents = new FolderProcessor(
				"C:\\Documents And Settings", "log");
		pool.execute(system);
		pool.execute(apps);
		pool.execute(documents);
		do {
			System.out.printf("******************************************\n");
			System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
			System.out.printf("Main: Active Threads: %d\n",
					pool.getActiveThreadCount());
			System.out.printf("Main: Task Count: %d\n",
					pool.getQueuedTaskCount());
			System.out.printf("Main: Steal Count: %d\n", pool.getStealCount());
			System.out.printf("******************************************\n");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while ((!system.isDone()) || (!apps.isDone())
				|| (!documents.isDone()));
		pool.shutdown();

		/**
		 * In this example, you have used the join() method to wait for the finalization of tasks and get
		 * their results. You can also use one of the two versions of the get() method with this purpose
		 * 
		 * get(): This version of the get() method returns the value returned by the
		 * compute() method if ForkJoinTask has finished its execution, or waits
		 * until its finalization.
		 */
		List<String> results;
		results = system.join();
		System.out.printf("System: %d files found.\n", results.size());
		results = apps.join();
		System.out.printf("Apps: %d files found.\n", results.size());
		results = documents.join();
		System.out.printf("Documents: %d files found.\n", results.size());
	}
}
