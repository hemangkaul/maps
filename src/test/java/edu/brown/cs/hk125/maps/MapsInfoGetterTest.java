package edu.brown.cs.hk125.maps;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.cs.hk125.dijkstra.Link;

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
        "/course/cs032/data/maps/smallMaps.sqlite3", false);
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
    // In this test, we find the neighbors of /n/1 which are not already
    // discovered
    // As an end node, we'll use /n/5
    Map<String, Link> discovered = new HashMap<String, Link>();
    discovered.put("/n/0", new Link("/n/0", "/n/0", 0.0, ""));
    // /n/0 is the start node!
    discovered.put("n/1/", new Link("/n/0", "/n/1", 0.03336, "/w/0"));

    // Below (commented out) is what the lists should look like
    // we don't actually use expectedList in our test because the distance
    // values
    // are rounded, but expectedList was helpful to look at when writing the
    // assertion statements

    // List<Link> expectedList = new ArrayList<Link>();
    // expectedList.add(new Link("/n/1", "/n/2", 0.09158, "/w/1"));
    // expectedList.add(new Link("/n/1", "/n/4", 0.09158, "/w/3"));

    List<Link> neighbors = smallMapsMig.getNeighborsAStar("/n/1", "/n/4",
        (HashMap<String, Link>) discovered, 0.03336);

    assertTrue(neighbors.size() == 2); // there should be two links in the
                                       // list...

    // neighbor one
    assertTrue(neighbors.get(0).getSource().equals("/n/1"));
    assertTrue(neighbors.get(0).getEnd().equals("/n/2"));
    assertTrue(neighbors.get(0).getDistance() - 0.09158 < 0.0001);
    assertTrue(neighbors.get(0).getName().equals("/w/1"));

    // neighbor two
    assertTrue(neighbors.get(1).getSource().equals("/n/1"));
    assertTrue(neighbors.get(1).getEnd().equals("/n/4"));
    assertTrue(neighbors.get(1).getDistance() - 0.09158 < 0.0001);
    assertTrue(neighbors.get(1).getName().equals("/w/3"));
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
