package threadexecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A common problem in concurrent programming is when you have various
 * concurrent tasks that solve a problem, and you are only interested in the
 * first result of those tasks
 * 
 * This Example uses the invokeAny() method of the ThreadPoolExecutor class to
 * achive the above.
 * 
 * inovkeAny receives a list of tasks, launches them, and returns the result of
 * the first task that finishes without throwing an exception. This method
 * returns the same data type that the call() method of the tasks you launch
 * returns. In this case, it returns a String value.
 * 
 * Here we are using two tasks.. so following 4 possibilities are there.
 * 
 * 1) Both tasks return the true value. The result of the invokeAny() method is
 * the name of the task that finishes in the first place.
 * 
 * 2) The first task returns the true value and the second one throws Exception.
 * The result of the invokeAny() method is the name of the first task.
 * 
 * 3) The first task throws Exception and the second one returns the true value.
 * The result of the invokeAny() method is the name of the second task.
 * 
 * 4) Both tasks throw Exception. In that class, the invokeAny() method throws
 * an ExecutionException exception.
 * 
 */
class UserValidator {
	private String name;

	public UserValidator(String name) {
		this.name = name;
	}

	public boolean validate(String name, String password) {
		Random random = new Random();
		try {
			long duration = (long) (Math.random() * 10);
			System.out.printf(
					"Validator %s: Validating a user during %d seconds\n",
					this.name, duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			return false;
		}
		return random.nextBoolean();
	}

	public String getName() {
		return name;
	}
}

class TaskValidator implements Callable<String> {

	private UserValidator validator;
	private String user;
	private String password;

	public TaskValidator(UserValidator validator, String user, String password) {
		this.validator = validator;
		this.user = user;
		this.password = password;
	}

	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		if (!validator.validate(user, password)) {
			System.out.printf("%s: The user has not been found\n",
					validator.getName());
			throw new Exception("Error validating user");
		}
		System.out.printf("%s: The user has been found\n", validator.getName());
		return validator.getName();
	}
}

public class FirstResultFromExecutor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String username = "test";
		String password = "test";
		UserValidator ldapValidator = new UserValidator("LDAP");
		UserValidator dbValidator = new UserValidator("DataBase");

		TaskValidator ldapTask = new TaskValidator(ldapValidator, username,
				password);
		TaskValidator dbTask = new TaskValidator(dbValidator, username,
				password);
		List<TaskValidator> taskList = new ArrayList<>();
		taskList.add(ldapTask);
		taskList.add(dbTask);
		ExecutorService executor = (ExecutorService) Executors
				.newCachedThreadPool();
		String result;
		try {
			result = executor.invokeAny(taskList);
			System.out.printf("Main: Result: %s\n", result);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		executor.shutdown();
		System.out.printf("Main: End of the Execution\n");
	}

}
