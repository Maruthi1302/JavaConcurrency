package threadmanagement;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ThreadLocalVariablesTest {

	ThreadLocalVariables tLocalVariables = new ThreadLocalVariables();
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRunUnsafeTask() {
		tLocalVariables.runUnsafeTask();
	}

	@Test
	public void testRunSafeTask() {
		tLocalVariables.runSafeTask();
	}

}
