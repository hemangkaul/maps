package edu.brown.cs.sl234.bacon;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ActorListMakerTest {
	@BeforeClass
	public static void setUpClass() throws Exception {
		// (Optional) Code to run before any tests begin goes here.
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		// (Optional) Code to run after all tests finish goes here.
	}

	@Before
	public void setUp() {
		// (Optional) Code to run before each test case goes here.
	}

	@After
	public void tearDown() {
		// (Optional) Code to run after each test case goes here.
	}

	// TODO add test methods here.
	// The methods must be annotated with annotation @Test. For example:
	//

	@Test
	public void actorListMakerTest() throws ClassNotFoundException, SQLException {
		ActorListMaker a = new ActorListMaker(
				"/course/cs032/data/bacon/smallBacon.sqlite3");
		List<String> actors = a.getActors();
		assertTrue(actors.contains("will smith"));
		assertTrue(actors.contains("will"));
		assertTrue(actors.contains("blanchett"));
		assertTrue(actors.contains("cate blanchett"));
	}
}
