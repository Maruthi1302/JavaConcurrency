/**
 * Notes:
 * 1) you cant set the id and status of the thread, if thread name is not given JVM allocates Thread-XX
 * 2) Thread can be interrupted by calling threadObject.interrupt()... and in the run method of the thread you can 
 * 	check weather thread has intterupted or not by calling isInterrupted(). if intterrupted its up to you to throw Interrupted checked
 * 	exception.As example sleep method of thread throws Intterupted exception when it is intterupted.
 * 3) You can use recursive calls with synchronized methods. As the thread has access to the synchronized methods of an object, 
 * you can call other synchronized methods of that object, including the method that is executing. It won't have to get access to the
 * synchronized methods again.
 * 
 * 4) When you use the synchronized keyword to protect a block of code, you must pass an object reference as a parameter. Normally, 
 * you will use the this keyword to reference the object that executes the method, but you can use other object references. Normally, these
 * objects will be created exclusively with this purpose. For example, if you have two independent attributes in a class shared 
 * by multiple threads, you must synchronize the access to each variable, but there is no problem if there is one thread accessing 
 * one of the attributes and another thread accessing the other at the same time.
 * 
 * 5) Java provides the wait(), notify(), and notifyAll() methods implemented in the Object class. A thread can call the wait() method inside a
 * synchronized block of code. If it calls the wait() method outside a synchronized block of code, the JVM throws an IllegalMonitorStateException 
 * exception. When the thread calls the wait() method, the JVM puts the thread to sleep and releases the object that controls 
 * the synchronized block of code that it's executing and allows the other threads to execute other blocks of synchronized code protected 
 * by that object. To wake up the thread, you must call the notify() or notifyAll() method inside a block of code protected by the same object.
 * 
 * 6)  Difference between notify and notifyAll in Java:
 * Java provides two methods notify and notifyAll for waking up threads waiting on some condition and you can use any of them but there 
 * is subtle difference between notify and notifyAll in Java which makes it one of the popular multi-threading interview question in Java. 
 * When you call notify only one of waiting thread will be woken and its not guaranteed which thread will be woken, 
 * it depends upon Thread scheduler. While if you call notifyAll method, all threads waiting on that lock will be woken up,
 *  but again all woken thread will fight for lock before executing remaining code and that's why wait is called on loop because 
 *  if multiple threads are woken up, the thread which will get lock will first execute and it may reset waiting condition, 
 *  which will force subsequent threads to wait. So key difference between notify and notifyAll is that notify() 
 *  will cause only one thread to wake up while notifyAll method will make all thread to wake up.
 *  
 * 7) This is the follow-up question if you get pass the earlier one Difference between notifyAll and notify in Java. 
 * If you understand notify vs notifyAll then you can answer this by applying little common sense. 
 * You can use notify over notifyAll if all thread are waiting for same condition and only one Thread at a time can benefit 
 * from condition becoming true. In this case notify is optimized call over notifyAll because waking up all of them because 
 * we know that only one thread will benefit and all other will wait again, so calling notifyAll method is just waste of cpu cycles. 
 * Though this looks quite reasonable there is still a caveat that unintended recipient swallowing critical notification. 
 * by using notifyAll we ensure that all recipient will get notify. Josh bloach has explained this in good detail in his book Effective Java , 
 * I highly recommend this book if you haven't read them already. Another one you can try is Concurrency Practice in Java and Java Thread, 
 * which discusses wait and notify methods in good details.
 * 
 * 8) The Lock interfaces provide additional functionalities over the synchronized keyword. 
 * One of the new functionalities is implemented by the tryLock() method. This method tries to get the control of the lock and if it can't,
 * because it's used by other thread, it returns the lock. With the synchronized keyword, when a thread (A) tries to execute a synchronized 
 * block of code, if there is another thread (B) executing it, the thread (A) is suspended until the thread (B) finishes the execution of the 
 * synchronized block. With locks, you can execute the tryLock() method. This method returns a Boolean value indicating if there is 
 * another thread running the code protected by this lock.
 * 	a) The Lock interfaces allow a separation of read and write operations having multiple readers and only one modifier.
 * 	b) The Lock interfaces offer better performance than the synchronized keyword.
 * 
 * 9) The Lock interface (and the ReentrantLock class) includes another method to get the control of the lock. It's the tryLock() method. 
 * The biggest difference with the lock() method is that this method, if the thread that uses it can't get the control of the Lock
 * interface, returns immediately and doesn't put the thread to sleep. This method returns a boolean value, true if the thread gets the 
 * control of the lock, and false if not.
 * 
 * 10) The ReentrantLock class also allows the use of recursive calls. When a thread has the control of a lock and makes a recursive call, 
 * it continues with the control of the lock, so the calling to the lock() method will return immediately and the thread will continue with the
 * execution of the recursive call. Moreover, we can also call other methods.
 */
/**
 * @author nxp69448
 *
 */
package threadmanagement;