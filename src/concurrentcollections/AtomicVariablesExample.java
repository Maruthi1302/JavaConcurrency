package concurrentcollections;

import java.util.concurrent.atomic.AtomicLong;

class Account {
	private AtomicLong balance;

	public Account() {
		balance = new AtomicLong();
	}

	public long getBalance() {
		return balance.get();
	}

	public void setBalance(long balance) {
		this.balance.set(balance);
	}

	public void addAmount(long amount) {
		this.balance.getAndAdd(amount);
	}

	public void subtractAmount(long amount) {
		this.balance.getAndAdd(-amount);
	}
}

class Company implements Runnable {

	private Account account;

	public Company(Account account) {
		this.account = account;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 10; i++) {
			account.addAmount(1000);
		}
	}

}

class Bank implements Runnable {
	private Account account;

	public Bank(Account account) {
		this.account = account;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 10; i++) {
			account.subtractAmount(1000);
		}
	}

}

/**
 * Java introduced the atomic variables.
 * 
 * WORKING OF ATOMIC VARIABLE [ COMPARE AND SET ] When a thread is doing an
 * operation with an atomic variable, if other threads want to do an operation
 * with the same variable, the implementation of the class includes a mechanism
 * to check that the operation is done in one step. Basically, the operation
 * gets the value of the variable, changes the value in a local variable, and
 * then tries to change the old value for the new one. If the old value is still
 * the same, it does the change. If not, the method begins the operation again.
 * This operation is called Compare and Set.
 * 
 * Atomic variables don't use locks or other synchronization mechanisms to
 * protect the access to their values. All their operations are based on the
 * Compare and Set operation. It's guaranteed that several threads can work with
 * an atomic variable at a time without generating data inconsistency errors and
 * its performance is better than using a normal variable protected by a
 * synchronization mechanism.
 * 
 */
public class AtomicVariablesExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Account account = new Account();
		account.setBalance(1000);
		Company company = new Company(account);
		Thread companyThread = new Thread(company);
		Bank bank = new Bank(account);
		Thread bankThread = new Thread(bank);
		System.out.printf("Account : Initial Balance: %d\n",
				account.getBalance());
		companyThread.start();
		bankThread.start();
		// Wait for finalization.
		try {
			companyThread.join();
			bankThread.join();
			System.out.printf("Account : Final Balance: %d\n",
					account.getBalance());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * As we mentioned in the introduction, there are other atomic classes in Java.
	 * AtomicBoolean, AtomicInteger, and AtomicReference are other examples
	 * of atomic classes.
	 */
}
