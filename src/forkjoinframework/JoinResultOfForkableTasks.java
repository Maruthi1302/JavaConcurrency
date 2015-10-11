package forkjoinframework;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

class DocumentMock {
	private String words[] = { "the", "hello", "goodbye", "packt", "java",
			"thread", "pool", "random", "class", "main" };

	public String[][] generateDocument(int numLines, int numWords, String word) {
		int counter = 0;
		String document[][] = new String[numLines][numWords];
		Random random = new Random();
		for (int i = 0; i < numLines; i++) {
			for (int j = 0; j < numWords; j++) {
				int index = random.nextInt(words.length);
				document[i][j] = words[index];
				if (document[i][j].equals(word)) {
					counter++;
				}
			}
		}
		System.out.println("DocumentMock: The word appears " + counter
				+ " times in the document");
		return document;
	}
}

class DocumentTask extends RecursiveTask<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String document[][];
	private int start, end;
	private String word;

	public DocumentTask(String document[][], int start, int end, String word) {
		this.document = document;
		this.start = start;
		this.end = end;
		this.word = word;
	}

	private Integer processLines(String[][] document, int start, int end,
			String word) {
		List<LineTask> tasks = new ArrayList<LineTask>();
		for (int i = start; i < end; i++) {
			LineTask task = new LineTask(document[i], 0, document[i].length,
					word);
			tasks.add(task);
		}
		invokeAll(tasks);
		int result = 0;
		for (int i = 0; i < tasks.size(); i++) {
			LineTask task = tasks.get(i);
			try {
				result = result + task.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new Integer(result);
	}

	private Integer groupResults(Integer number1, Integer number2) {
		Integer result;
		result = number1 + number2;
		return result;
	}

	@Override
	protected Integer compute() {
		// TODO Auto-generated method stub
		int result = 0;
		if (end - start < 10) {
			result = processLines(document, start, end, word);
		} else {
			int mid = (start + end) / 2;
			DocumentTask task1 = new DocumentTask(document, start, mid, word);
			DocumentTask task2 = new DocumentTask(document, mid, end, word);
			invokeAll(task1, task2);
			try {
				result = groupResults(task1.get(), task2.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}

class LineTask extends RecursiveTask<Integer> {
	private static final long serialVersionUID = 1L;
	private String line[];
	private int start, end;
	private String word;

	public LineTask(String line[], int start, int end, String word) {
		this.line = line;
		this.start = start;
		this.end = end;
		this.word = word;
	}

	@Override
	protected Integer compute() {
		// TODO Auto-generated method stub
		Integer result = null;
		if (end - start < 100) {
			result = count(line, start, end, word);
		} else {
			int mid = (start + end) / 2;
			LineTask task1 = new LineTask(line, start, mid, word);
			/**
			 * The ForkJoinTask class provides another method to finish execution of a task and returns
			 * a result, that is, the complete() method. This method accepts an object of the type used
			 * in the parameterization of the RecursiveTask class and returns that object as a result of
			 * the task when the join() method is called. It's use is recommended to provide results for
			 * asynchronous tasks.
			 */
			//task1.complete(arg0);
			LineTask task2 = new LineTask(line, mid, end, word);
			
			invokeAll(task1, task2);
			try {
				result = groupResults(task1.get(), task2.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private Integer count(String[] line, int start, int end, String word) {
		int counter;
		counter = 0;
		for (int i = start; i < end; i++) {
			if (line[i].equals(word)) {
				counter++;
			}
		}
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return counter;
	}

	private Integer groupResults(Integer number1, Integer number2) {
		Integer result;
		result = number1 + number2;
		return result;
	}
}

/**
 * The Fork/Join framework provides the ability of executing tasks that return a
 * result. These kinds of tasks are implemented by the RecursiveTask class. This
 * class extends the ForkJoinTask class and implements the Future interface
 * provided by the Executor framework.
 */
public class JoinResultOfForkableTasks {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Create Document with 100 lines and 1,000 words per line using the
		// DocumentMock class.

		DocumentMock mock = new DocumentMock();
		String[][] document = mock.generateDocument(100, 1000, "the");
		DocumentTask task = new DocumentTask(document, 0, 100, "the");
		ForkJoinPool fjpool = new ForkJoinPool();
		fjpool.execute(task);

		do {
			System.out.printf("******************************************\n");
			System.out.printf("Main: Parallelism: %d\n",
					fjpool.getParallelism());
			System.out.printf("Main: Active Threads: %d\n",
					fjpool.getActiveThreadCount());
			System.out.printf("Main: Task Count: %d\n",
					fjpool.getQueuedTaskCount());
			System.out
					.printf("Main: Steal Count: %d\n", fjpool.getStealCount());
			System.out.printf("******************************************\n");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!task.isDone());

		fjpool.shutdown();
		try {
			fjpool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			System.out.printf("Main: The word appears %d in the document",
					task.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}
