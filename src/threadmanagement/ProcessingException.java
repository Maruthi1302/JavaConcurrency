package threadmanagement;

import java.lang.Thread.UncaughtExceptionHandler;

public class ProcessingException {

//	When a checked exception is thrown inside the run() method of a Thread object, we have
//	to catch and treat them, because the run() method doesn't accept a throws clause. When
//	an unchecked exception is thrown inside the run() method of a Thread object, the default
//	behaviour is to write the stack trace in the console and exit the program.
	
	// In order to avoid exiting the program due to unchecked exceptions from Thread
	// We need to implement one UncaughtExceptionHandler.
	
	
	//Flow of Uncaught exception.
//	1) check for handler in current Thread object. if not found
//	2) Look for handler in ThreadGroup if not found
//	3) Look for static setDefaultUncaughtExceptionHandler. if not found
//	4) prints the stack trace in the console. and exit.
	class ExceptionHandler implements UncaughtExceptionHandler
	{

		@Override
		public void uncaughtException(Thread arg0, Throwable arg1) {
			// TODO Auto-generated method stub
			System.out.println("Processing Uncaught exception.");
		}
		
	}
	
	class MyRuntimeExceptionThrowingThread extends Thread
	{
		public void run()
		{
			System.out.println("Throwing Uncaught exception.");
			throw new RuntimeException("Throwing Uncaught exception.");
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProcessingException pe = new ProcessingException();
		MyRuntimeExceptionThrowingThread thread = pe.new MyRuntimeExceptionThrowingThread();
		thread.setUncaughtExceptionHandler(pe.new ExceptionHandler());
		thread.start();
		System.out.println("Main finished.");
		//This below method is static and will takes the handler and calls it for all the
		// Uncaught exceptions from all the threads in this application.
		//thread.setDefaultUncaughtExceptionHandler(arg0);
	}

}
