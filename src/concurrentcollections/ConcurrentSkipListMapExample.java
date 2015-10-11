package concurrentcollections;

import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

class Contact {
	private String name;
	private String phone;

	public Contact(String name, String phone) {
		this.name = name;
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}
}

class Task3 implements Runnable {

	private ConcurrentSkipListMap<String, Contact> map;
	private String id;

	public Task3(ConcurrentSkipListMap<String, Contact> map, String id) {
		this.id = id;
		this.map = map;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 1000; i++) {
			Contact contact = new Contact(id, String.valueOf(i + 1000));
			map.put(id + contact.getPhone(), contact);
		}
	}

}

/**
 * An interesting data structure provided by the Java API that you can use in
 * your concurrent programs is defined by the ConcurrentNavigableMap interface.
 * The classes that implement the ConcurrentNavigableMap interface stores
 * elements within two parts: 1) A key that uniquely identifies an element 2)
 * The rest of the data that defines the element
 * 
 * Each part has to be implemented in different classes.
 * 
 * Java API also provides a class that implements that interface, which is the
 * ConcurrentSkipListMap interface that implements a non-blocking list with the
 * behavior of the ConcurrentNavigableMap interface. Internally, it uses a Skip
 * List to store the data.
 * 
 * A Skip List is a data structure based on parallel lists that allows us to get
 * efficiency similar to a binary tree. With it, you can get a sorted data
 * structure with a better access time to insert
 * 
 * When you insert an element in the map, it uses the key to order them, so all
 * the elements will be ordered. The class also provides methods to obtain a
 * submap of the map, in addition to the ones that return a concrete element
 * 
 */
public class ConcurrentSkipListMapExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConcurrentSkipListMap<String, Contact> map;
		map = new ConcurrentSkipListMap<>();
		Thread threads[] = new Thread[25];
		int counter = 0;
		for (char i = 'A'; i < 'Z'; i++) {
			Task3 task = new Task3(map, String.valueOf(i));
			threads[counter] = new Thread(task);
			threads[counter].start();
			counter++;
		}
		// wait for finalization of threads.
		for (int i = 0; i < 25; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		/**
		 * 1) headMap(K toKey): K is the class of the key values used in the parameterization
		 * of the ConcurrentSkipListMap object. This method returns a submap of the first
		 * elements of the map with the elements that have a key smaller than the one passed as parameter.
		 * 
		 * 2) tailMap(K fromKey): K is the class of the key values used in the parameterization
		 * of the ConcurrentSkipListMap object. This method returns a submap of the last
		 * elements of the map with the elements that have a key greater than the one passed as parameter.
		 * 
		 * 3) putIfAbsent(K key, V Value): This method inserts the value specified as a
		 * parameter with the key specified as parameter if the key doesn't exist in the map.
		 * 
		 * 4) pollLastEntry(): This method returns and removes a Map.Entry object with the last element of the map.
		 * 5) replace(K key, V Value): This method replaces the value associated with the key specified as parameter if this key exists in the map.
		 */
		System.out.printf("Main: Size of the map: %d\n", map.size());
		Map.Entry<String, Contact> element;
		Contact contact;
		element = map.firstEntry();
		contact = element.getValue();
		System.out.printf("Main: First Entry: %s: %s\n", contact.getName(),
				contact.getPhone());
		element = map.lastEntry();
		contact = element.getValue();
		System.out.printf("Main: Last Entry: %s: %s\n", contact.getName(),
				contact.getPhone());
		System.out.printf("Main: Submap from A1996 to B1002: \n");
		ConcurrentNavigableMap<String, Contact> submap = map.subMap("A1996",
				"B1002");
		do {
			element = submap.pollFirstEntry();
			if (element != null) {
				contact = element.getValue();
				System.out.printf("%s: %s\n", contact.getName(),
						contact.getPhone());
			}
		} while (element != null);
	}

}
