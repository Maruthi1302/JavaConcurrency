package threadmanagement;

public class ThreadManagement {

	class DeamonThread extends Thread
	{
//		Java has a special kind of thread called daemon thread. These kind of threads have very low
//		priority and normally only executes when no other thread of the same program is running.
//		When daemon threads are the only threads running in a program, the JVM ends the program
//		finishing these threads.
		public DeamonThread()
		{
			setDaemon(true);
		}
		
		public void run()
		{
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("I am Deamon.");
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ThreadManagement tm = new ThreadManagement();
		ThreadManagement.DeamonThread dm = tm.new DeamonThread();
		dm.start();
		
		try {
			Thread.currentThread().sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Main Thread Finished.");
	}

}
