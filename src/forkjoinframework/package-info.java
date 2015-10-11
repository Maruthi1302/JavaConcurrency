/**
 * 1) This framework is designed to solve problems that can be broken into smaller tasks using the divide and conquer technique.
 * 2) Inside a task, you check the size of the problem you want to resolve and, if it's bigger than an established size, 
 * you divide it in smaller tasks that are executed using the framework.
 * 3) The established size is implementation dependent.
 * 
 * The framework is based on the following two operations:
 * 1) The fork operation: When you divide a task into smaller tasks and execute them using the framework.
 * 2) The join operation: When a task waits for the finalization of the tasks it has created.
 */

/**
 * The Executor framework separates the task creation and its execution. With it, you only have
 * to implement the Runnable objects and use an Executor object. You send the Runnable
 * tasks to the executor and it creates, manages, and finalizes the necessary threads to execute
 * those tasks.
 * Java 7 goes a step further and includes an additional implementation of the
 * ExecutorService interface oriented to a specific kind of problem. It's the
 * Fork/Join framework.
 */
/**
 * VERY VERY IMPORTANT.
 * The main difference between the Fork/Join and the Executor frameworks is the work-stealing
 * algorithm. Unlike the Executor framework, when a task is waiting for the finalization of the
 * subtasks it has created using the join operation, the thread that is executing that task (called
 * worker thread) looks for other tasks that have not been executed yet and begins its execution.
 * By this way, the threads take full advantage of their running time, thereby improving the
 * performance of the application.
 * 
 * To achieve this goal, the tasks executed by the Fork/Join framework have the following limitations:
 * 1) Tasks can only use the fork() and join() operations as synchronization mechanisms. If they use other synchronization mechanisms, 
 * the worker threads can't execute other tasks when they are in the synchronization operation.
 * 2) Tasks should not perform I/O operations, such as read or write data in a file.
 * 3) Tasks can't throw checked exceptions. It has to include the code necessary to process them.
 */
package forkjoinframework;