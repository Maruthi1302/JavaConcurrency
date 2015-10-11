package threadmanagement;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ThreadGroupExampleTest {

	ThreadGroupExample tgExp =  new ThreadGroupExample();
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
	public void testRunUncaughtExpHandlerForThreadGrupExap() {
		tgExp.runUncaughtExpHandlerForThreadGrupExap();
	}

	@Test
	public void testRunThreadsInGroup() {
		tgExp.runThreadsInGroup();
	}

}
