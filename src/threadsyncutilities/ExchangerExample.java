package threadsyncutilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

class Producer implements Runnable {

	private List<String> buffer;
	private final Exchanger<List<String>> exchanger;

	public Producer(List<String> buffer, Exchanger<List<String>> exchanger) {
		this.buffer = buffer;
		this.exchanger = exchanger;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int cycle = 1;
		for (int i = 0; i < 10; i++) {
			System.out.printf("Producer: Cycle %d\n", cycle);
			// Each cycle add 10 Messages.
			for (int j = 0; j < 10; j++) {
				String message = "Event " + ((i * 10) + j);
				System.out.printf("Producer: %s\n", message);
				buffer.add(message);
			}

			// After each cycle exchange the data with the consumer.
			try {
				// retuned buffer will be from consumer.
				buffer = exchanger.exchange(buffer);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Producer: " + buffer.size());
			cycle++;
		}
	}
}

class Consumer implements Runnable {

	private List<String> buffer;
	private final Exchanger<List<String>> exchanger;

	public Consumer(List<String> buffer, Exchanger<List<String>> exchanger) {
		this.buffer = buffer;
		this.exchanger = exchanger;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int cycle = 1;
		for (int i = 0; i < 10; i++) {
			System.out.printf("Consumer: Cycle %d\n", cycle);
			// Get the data from [ exchange data with the producer. ]
			try {
				// Consumer waits here until producer call exchange on
				// the same exchanger object.
				// Only Two threads can use Exchanger.
				buffer = exchanger.exchange(buffer);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("Consumer: " + buffer.size());
			for (int j = 0; j < 10; j++) {
				String message = buffer.get(0);
				System.out.println("Consumer: " + message);
				buffer.remove(0);
			}
			cycle++;
		}
	}
}

/**
 * Exchanger class allows the definition of a synchronization point between two
 * threads. When the two threads arrive to this point, they interchange a data
 * structure so the data structure of the first thread goes to the second one
 * and the data structure of the second thread goes to the first one.
 * 
 * This class may be very useful in a situation similar to the producer-consumer
 * problem.
 * 
 * As the Exchanger class only synchronizes two threads, you can use it if you
 * have a producer-consumer problem with one producer and one consumer.
 */
public class ExchangerExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> buffer1=new ArrayList<>();
		List<String> buffer2=new ArrayList<>();
		Exchanger<List<String>> exchanger=new Exchanger<>();
		Producer producer=new Producer(buffer1, exchanger);
		Consumer consumer=new Consumer(buffer2, exchanger);
		
		Thread threadProducer=new Thread(producer);
		Thread threadConsumer=new Thread(consumer);
		threadProducer.start();
		threadConsumer.start();
	}

}
