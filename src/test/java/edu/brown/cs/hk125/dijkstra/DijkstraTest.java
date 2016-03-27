package edu.brown.cs.hk125.dijkstra;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DijkstraTest {

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
  // @Test
  // public void hello() {}

  class testInfoGetter implements infoGetter {
    // a info getter we will be using for testing
    // aka a graph which we will retrieve information from

    private HashMap<String, List<Link>> database;

    private void fillDatabase() {
      // We begin by writing our own small 'graph', for testing
      // in the database, we will store each node and its associated Links
      // there are six nodes in our small 'graph'

      database = new HashMap<String, List<Link>>();

      // LinkList for start
      List<Link> startLinks = Arrays.asList(new Link("start", "n1", 2.0),
          new Link("start", "n2", 2.5));
      database.put("start", startLinks);

      // LinkList for n1
      List<Link> n1Links = Arrays.asList(new Link("n1", "start", 2.0),
          new Link("n1", "n3", 3.5));
      database.put("n1", n1Links);

      // LinkList for n2
      List<Link> n2Links = Arrays.asList(new Link("n2", "start", 2.5),
          new Link("n2", "n3", 1.0), new Link("n2", "n4", 4.0));
      database.put("n2", n2Links);

      // LinkList for n3
      List<Link> n3Links = Arrays.asList(new Link("n3", "n1", 3.5), new Link(
          "n3", "n2", 1.0), new Link("n3", "n5", 2.0));
      database.put("n3", n3Links);

      // LinkList for n4
      List<Link> n4Links = Arrays.asList(new Link("n4", "n2", 4.0), new Link(
          "n4", "n6", 2.5));
      database.put("n4", n4Links);

      // LinkList for n5
      List<Link> n5Links = Arrays.asList(new Link("n5", "n3", 2.0), new Link(
          "n5", "n6", 2.0));
      database.put("n5", n5Links);

      // LinkList for n6
      List<Link> n6Links = Arrays.asList(new Link("n6", "n4", 2.5), new Link(
          "n6", "n5", 2.0));
      database.put("n6", n6Links);

      // LinkList for n7. Notice n7 is not adjacent to anyone!
      List<Link> n7Links = Arrays.asList();
      database.put("n7", n7Links);
    }

    @Override
    public ArrayList<Link> getNeighbors(String nodeName,
        HashMap<String, Link> hm, double extraDist) {
      // This function, given a node, should return a list of its neighbors in
      // the form of links, but only those neighbors which do not appear in hm.

      // an empty arrayList for us to add to later
      ArrayList<Link> neighborList = new ArrayList<Link>();

      // iterate through the neighbors of nodeName. We need to add extraDist to
      // its distance before
      // adding these neighbors to neighborList
      for (Link n : database.get(nodeName)) {
        if (!(hm.containsKey(n.getEnd()))) {
          // if the neighbor does not exist already in hm
          neighborList.add(new Link(n.getSource(), n.getEnd(), n.getDistance()
              + extraDist, n.getName()));
        }
      }
      return neighborList;
    }

    @Override
    public boolean isIn(String nodeName) {
      return database.containsKey(nodeName);
    }

    @Override
    public ArrayList<Link> expandGroupLink(groupLink g, HashMap<String, Link> hm)
        throws SQLException {
      // We will not be testing expandGroupLink here
      return null;
    }

    @Override
    public List<Link> getNeighborsAStar(String nodeName, String endNode,
        HashMap<String, Link> hm, double extraDist) throws SQLException,
        IllegalArgumentException {
      // We will not be using getNeighborsAStar here
      return null;
    }
  }

  @Test
  public void findPathsSimpleTest() throws IllegalArgumentException,
      SQLException {
    // findPaths returns a HashMap of all the discovered paths until the stop
    // node is reached
    // Here we perform a simple test where the stop node is the closest node to
    // the start node.

    HashMap<String, Link> expectedMap = new HashMap<String, Link>();
    expectedMap.put("start", new Link("start", "start", 0.0));
    expectedMap.put("n1", new Link("start", "n1", 2.0));

    testInfoGetter ig = new testInfoGetter();
    ig.fillDatabase();
    Dijkstra simpleDijkstra = new Dijkstra("start", ig);

    assertTrue(simpleDijkstra.findPaths("n1").equals(expectedMap));
  }

  @Test
  public void findPathsEdgeCases() throws IllegalArgumentException,
      SQLException {
    // We test two edge cases.
    // 1. The 'stop' node is the same as the start node. findPaths should then
    // terminate when the
    // stop node is already in the discoveredNodes Hashmap.
    // 2. The start node is not adjacent to any nodes! findPaths should then
    // terminate when
    // closestUndiscovered is empty.

    // expectedMap for Case 1
    HashMap<String, Link> expectedOne = new HashMap<String, Link>();
    expectedOne.put("start", new Link("start", "start", 0.0));

    // expectedMap for Case 2
    HashMap<String, Link> expectedTwo = new HashMap<String, Link>();
    expectedTwo.put("n7", new Link("n7", "n7", 0.0));

    testInfoGetter ig = new testInfoGetter();
    ig.fillDatabase();
    Dijkstra caseOneDijkstra = new Dijkstra("start", ig);
    Dijkstra caseTwoDijkstra = new Dijkstra("n7", ig);

    assertTrue(caseOneDijkstra.findPaths("start").equals(expectedOne));
    assertTrue(caseTwoDijkstra.findPaths("").equals(expectedTwo));
  }

  @Test
  public void findPathsError() throws SQLException {
    // In Dijkstra, an IllegalArgumentException() is thrown when the ig doesn't
    // contain the node!
    // Here we test to make show that this is working correctly.

    String message;
    testInfoGetter ig = new testInfoGetter();
    ig.fillDatabase();
    try {
      @SuppressWarnings("unused")
      Dijkstra errorDijkstra = new Dijkstra("apple", ig);
      message = "Huh. Looks like the error wasn't generated.";
    } catch (IllegalArgumentException e) {
      message = "Great, the error was thrown! Yippee!";
    }

    assertTrue(message.equals("Great, the error was thrown! Yippee!"));
  }

  @Test
  public void findPathsAdvanced() throws IllegalArgumentException, SQLException {
    // Complex test with findPathsAdvanced!

    HashMap<String, Link> expectedMap = new HashMap<String, Link>();
    expectedMap.put("start", new Link("start", "start", 0.0));
    expectedMap.put("n1", new Link("start", "n1", 2.0));
    expectedMap.put("n2", new Link("start", "n2", 2.5));
    expectedMap.put("n3", new Link("n2", "n3", 3.5));
    expectedMap.put("n5", new Link("n3", "n5", 5.5));
    expectedMap.put("n4", new Link("n2", "n4", 6.5));
    expectedMap.put("n6", new Link("n5", "n6", 7.5));

    testInfoGetter ig = new testInfoGetter();
    ig.fillDatabase();

    Dijkstra testDijkstra = new Dijkstra("start", ig);

    assertTrue(testDijkstra.findPaths("n6").equals(expectedMap));
    assertTrue(testDijkstra.findPaths("n1").equals(expectedMap));
    // for the second one, testDijkstra will see that the current list of
    // discovered nodes
    // already contains "n1"... thus it just returns the entire list
    // (technically 'map' ;)).
  }

  @Test
  public void findPathsAll() throws IllegalArgumentException, SQLException {
    // We're going to search the entire graph now, by giving a non-existent
    // end node. This should terminate after closestUndiscovered becomes empty!

    HashMap<String, Link> expectedMap = new HashMap<String, Link>();
    expectedMap.put("start", new Link("start", "start", 0.0));
    expectedMap.put("n1", new Link("start", "n1", 2.0));
    expectedMap.put("n2", new Link("start", "n2", 2.5));
    expectedMap.put("n3", new Link("n2", "n3", 3.5));
    expectedMap.put("n5", new Link("n3", "n5", 5.5));
    expectedMap.put("n4", new Link("n2", "n4", 6.5));
    expectedMap.put("n6", new Link("n5", "n6", 7.5));

    testInfoGetter ig = new testInfoGetter();
    ig.fillDatabase();

    Dijkstra testDijkstra = new Dijkstra("start", ig);

    assertTrue(testDijkstra.findPaths("nonexistent").equals(expectedMap));
    // expectedMap is the entire graph. nonexistent doesn't exist; if our
    // program
    // works correctly, findPaths should terminate after closestUndiscovered
    // empties out
  }

  @Test
  public void getPathTest() throws IllegalArgumentException, SQLException {
    // Test to make sure getPath works!

    ArrayList<Link> expectedList = new ArrayList<Link>();
    expectedList.add(new Link("start", "start", 0.0));
    expectedList.add(new Link("start", "n2", 2.5));
    expectedList.add(new Link("n2", "n3", 3.5));
    expectedList.add(new Link("n3", "n5", 5.5));
    expectedList.add(new Link("n5", "n6", 7.5));

    testInfoGetter ig = new testInfoGetter();
    ig.fillDatabase();

    Dijkstra testDijkstra = new Dijkstra("start", ig);

    assertTrue(testDijkstra.getPath("n6").equals(expectedList));
  }
}