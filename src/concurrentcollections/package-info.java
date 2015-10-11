/**
 * Using non-blocking thread-safe lists.
 * Using blocking thread-safe lists
 * Using blocking thread-safe lists ordered by priority
 * Using thread-safe lists with delayed elements
 * Using thread-safe navigable maps
 * Generating concurrent random numbers
 * Using atomic variables
 * Using atomic arrays
 */
/**
 * Java provides two kinds of collections to use in concurrent applications:
 * 
 * 1) Blocking collections: This kind of collection includes operations to add and remove 
 * data. If the operation can't be made immediately, because the collection is full or
 * empty, the thread that makes the call will be blocked until the operation can be made.
 * 
 * 2) Non-blocking collections: This kind of collection also includes operations to add and
 * remove data. If the operation can't be made immediately, the operation returns a 
 * null value or throws an exception, but the thread that makes the call won't be blocked.
 *
 */

/**
 * 1) Non-blocking lists, using the ConcurrentLinkedDeque class.
 * 2) Blocking lists, using the LinkedBlockingDeque class.
 * 3) Blocking lists to be used with producers and consumers of data, using the LinkedTransferQueue class.
 * 4) Blocking lists that order their elements by priority, with the PriorityBlockingQueue.
 * 5) Blocking lists with delayed elements, using the DelayQueue class.
 * 6) Non-blocking navigable maps, using the ConcurrentSkipListMap class.
 * 7) Random numbers, using the ThreadLocalRandom class.
 * 8) Atomic variables, using the AtomicLong and AtomicIntegerArray classes.
 */
package concurrentcollections;