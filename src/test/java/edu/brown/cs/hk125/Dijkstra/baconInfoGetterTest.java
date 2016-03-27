package edu.brown.cs.sl234.bacon;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.cs.sl234.dijkstra.Link;
import edu.brown.cs.sl234.dijkstra.groupLink;

public class baconInfoGetterTest {

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
	private baconInfoGetter big;

	@Test
	public void isInTest() throws SQLException, ClassNotFoundException {
		big = new baconInfoGetter("/course/cs032/data/bacon/smallBacon.sqlite3");
		assertTrue(!big.isIn("Samuel L. Jackson"));
	}

	@Test
	public void convertingTests() throws ClassNotFoundException, SQLException {
		// Tests convertToID, convertToName, convertFilmIDtoName,
		// convertFilmNametoID
		big = new baconInfoGetter("/course/cs032/data/bacon/smallBacon.sqlite3");
		assertTrue(big.convertToID("Will Smith").equals("/m/0147dk"));
		assertTrue(big.convertToName("/m/0147dk").equals("Will Smith"));
		assertTrue(big.convertFilmNametoID("Men in Black").equals("/m/016dj8"));
		assertTrue(big.convertFilmIDtoName("/m/016dj8").equals("Men in Black"));
	}

	@Test
	public void getNeighborsTest() throws ClassNotFoundException, SQLException {
		big = new baconInfoGetter("/course/cs032/data/bacon/smallBacon.sqlite3");
		ArrayList<Link> films = big.getNeighbors("/m/0147dk",
				new HashMap<String, Link>(), 2.5);
		assertTrue(films.get(0).getSource().equals("/m/0147dk"));
		assertTrue(films.get(0).getDistance().equals(3.0));
	}

	@Test
	public void expandGroupLinkTest() throws ClassNotFoundException, SQLException {
		big = new baconInfoGetter("/course/cs032/data/bacon/smallBacon.sqlite3");
		ArrayList<Link> films = big.getNeighbors("/m/0147dk",
				new HashMap<String, Link>(), 2.5);
		groupLink g = (groupLink) films.get(0);
		ArrayList<Link> expandedG = big.expandGroupLink(g,
				new HashMap<String, Link>());
		System.out.println(expandedG.size());
		assertTrue(!(expandedG.get(0).getEnd().equals(g.getEnd())));
	}
}
