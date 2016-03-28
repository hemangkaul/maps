package edu.brown.cs.hk125.maps;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MapsInfoGetterTest {

  private MapsInfoGetter smallMapsMig;

  @BeforeClass
  public static void setUpClass() throws Exception {
    // (Optional) Code to run before any tests begin goes here.
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    // (Optional) Code to run after all tests finish goes here.
  }

  @Before
  public void setUp() throws ClassNotFoundException, SQLException {
    smallMapsMig = new MapsInfoGetter(
        "/course/cs032/data/maps/smallMaps.sqlite3");
  }

  @After
  public void tearDown() {
    // (Optional) Code to run after each test case goes here.
  }

  // TODO add test methods here.
  // The methods must be annotated with annotation @Test. For example:
  //

  @Test
  public void isInTest() throws SQLException {
    // tests to see if the isIn method can properly tell if an element is in the
    // database
    assertTrue(smallMapsMig.isIn("/n/2"));
    assertTrue(smallMapsMig.isIn("/n/5"));
    assertTrue(!(smallMapsMig.isIn("/n/7")));
  }

  @Test
  public void getNeighborsAStarTest() throws ClassNotFoundException,
      SQLException {
  }

  @Test
  public void getLatLngTest() throws SQLException {
    // tests if getLatLng returns the correct latitude and longitude for a node.
    // Two tests:
    // 1. A node that's in the database
    // 2. A node not in the database.

    Map<String, Double> expectedMapOne = new HashMap<String, Double>();
    Map<String, Double> expectedMapTwo = new HashMap<String, Double>();

    expectedMapOne.put("Latitude", 41.8206);
    expectedMapOne.put("Longitude", -71.4);

    assertTrue(smallMapsMig.getLatLng("/n/2").equals(expectedMapOne));
    assertTrue(smallMapsMig.getLatLng("duck").equals(expectedMapTwo));
  }

  @Test
  public void getIntersectionTest() throws SQLException,
      IllegalArgumentException {
    // Tests getIntersection, which gives the node which is the intersection of
    // two ways

    // The first two test two ways with the same name... making sure our program
    // checks both
    assertTrue(smallMapsMig
        .getIntersection("Radish Spirit Blvd", "Chihiro Ave").equals("/n/0"));
    assertTrue(smallMapsMig.getIntersection("Kamaji Pl", "Chihiro Ave").equals(
        "/n/2"));

    // /n/1 appears in both ways called "Chihiro Ave"... so there shouldn't be
    // an error here.
    assertTrue(smallMapsMig.getIntersection("Sootball Ln", "Chihiro Ave")
        .equals("/n/1"));

    // street and crossStreet have the same name...
    assertTrue(smallMapsMig.getIntersection("Kamaji Pl", "Kamaji Pl").equals(
        "/n/2"));

    // street and crossStreet have the same name, and appear twice...
    // Our program does not distinguish between different the two different
    // "Chihiro Ave"'s here, which practically speaking is fine. After all, if
    // someone asked,
    // "Where does Chihiro Avenue intersect with Chihiro Avenue?"
    // The question really makes no sense, and they intersect everywhere on the
    // street
    assertTrue(smallMapsMig.getIntersection("Chihiro Ave", "Chihiro Ave")
        .equals("/n/0"));
  }

  @Test
  public void heuristicValueTest() throws SQLException {
    // Given a subject node and an end node, heuristicValue node returns the
    // "heuristic" for the subject node. How the heuristic value is calculated
    // will vary between different A* searches, but for ours, it is simply the
    // distance between the subject node and the end node, calculated using the
    // Haversine formula.

    // This is a pretty basic arithmetic equation. We do a few simple tests.

    // Since heuristicValue does not round, we simply make sure we are within a
    // certain interval of our expected distance
    assertTrue(smallMapsMig.heuristicValue("/n/0", "/n/3") - 0.02486 < 0.0001);
    assertTrue(smallMapsMig.heuristicValue("/n/1", "/n/2") - 0.03336 < 0.0001);
  }
}
