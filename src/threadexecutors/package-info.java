/**
 * Executor framework and is around the Executor
 * interface, its subinterface ExecutorService, and the ThreadPoolExecutor class that
 * implements both interfaces
 * This mechanism separates the task creation and its execution. With an executor, you only
 * have to implement the Runnable objects and send them to the executor. It is responsible for
 * their execution, instantiation, and running with necessary threads. But it goes beyond that and
 * improves performance using a pool of threads. When you send a task to the executor, it tries to
 * use a pooled thread for the execution of this task, to avoid continuous spawning of threads
 * 
 * Another important advantage of the Executor framework is the Callable interface. It's
 * similar to the Runnable interface, but offers two improvements, which are as follows:
 * 
 * The main method of this interface, named call(), may return a result.
 * When you send a Callable object to an executor, you get an object that implements
 * the Future interface. You can use this object to control the status and the result of
 * the Callable object.

 *	Creating a thread executor
 *	Creating a fixed-size thread executor
 *	Executing tasks in an executor that returns a result
 *	Running multiple tasks and processing the first result
 *	Running multiple tasks and processing all the results
 *	Running a task in an executor after a delay
 *	Running a task in an executor periodically
 *	Canceling a task in an executor
 *	Controlling a task finishing in an executor
 *	Separating the launching of tasks and the processing of their results in an executor
 *	Controlling the rejected tasks of an executor
 */
/**
 * @author nxp69448
 *
 */
package threadexecutors;