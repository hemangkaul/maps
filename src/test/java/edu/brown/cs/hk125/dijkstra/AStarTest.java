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

public class AStarTest {
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

  class testInfoGetter implements InfoGetterAStar {
    // a info getter we will be using for testing
    // aka a graph which we will retrieve information from

    private HashMap<String, List<Link>> database;
    private HashMap<String, Double> xCoordinates;
    private HashMap<String, Double> yCoordinates;

    private void fillDatabase() {
      // We begin by writing our own small 'graph.'
      // There are seven nodes in our small 'graph.'
      // We will store the information in the database.

      // These correspond with the following coordinates:
      // start (0, 0)
      // N1 (2, 0)
      // N2 (0, 2.5)
      // N3 (0, 4)
      // N4 (4, 2.5)
      // N5 (2, 5)
      // N6 (4, 4)
      // N7 (0, -3)

      // The coordinates rae stored in the xCoordinates and yCoordinates
      // HashMaps.

      database = new HashMap<String, List<Link>>();
      xCoordinates = new HashMap<String, Double>();
      yCoordinates = new HashMap<String, Double>();

      // LinkList for start
      List<Link> startLinks = Arrays.asList(new Link("start", "n1", 2.0),
          new Link("start", "n2", 2.5));
      database.put("start", startLinks);
      xCoordinates.put("start", 0.0);
      yCoordinates.put("start", 0.0);

      // LinkList for n1
      List<Link> n1Links = Arrays.asList(new Link("n1", "start", 2.0),
          new Link("n1", "n3", Math.sqrt(20)));
      database.put("n1", n1Links);
      xCoordinates.put("n1", 2.0);
      yCoordinates.put("n1", 0.0);

      // LinkList for n2
      List<Link> n2Links = Arrays.asList(new Link("n2", "start", 2.5),
          new Link("n2", "n3", 1.5), new Link("n2", "n4", 4.0));
      database.put("n2", n2Links);
      xCoordinates.put("n2", 0.0);
      yCoordinates.put("n2", 2.5);

      // LinkList for n3
      List<Link> n3Links = Arrays.asList(new Link("n3", "n1", Math.sqrt(20)),
          new Link("n3", "n2", 1.5), new Link("n3", "n5", Math.sqrt(5)));
      database.put("n3", n3Links);
      xCoordinates.put("n3", 0.0);
      yCoordinates.put("n3", 4.0);

      // LinkList for n4
      List<Link> n4Links = Arrays.asList(new Link("n4", "n2", 4.0), new Link(
          "n4", "n6", 1.5));
      database.put("n4", n4Links);
      xCoordinates.put("n4", 4.0);
      yCoordinates.put("n4", 2.5);

      // LinkList for n5
      List<Link> n5Links = Arrays.asList(new Link("n5", "n3", Math.sqrt(5)),
          new Link("n5", "n6", Math.sqrt(5)));
      database.put("n5", n5Links);
      xCoordinates.put("n5", 2.0);
      yCoordinates.put("n5", 5.0);

      // LinkList for n6
      List<Link> n6Links = Arrays.asList(new Link("n6", "n4", 1.5), new Link(
          "n6", "n5", Math.sqrt(5)));
      database.put("n6", n6Links);
      xCoordinates.put("n6", 4.0);
      yCoordinates.put("n6", 4.0);

      // LinkList for n7. Notice n7 is not adjacent to anyone!
      List<Link> n7Links = Arrays.asList();
      database.put("n7", n7Links);
      xCoordinates.put("n7", 0.0);
      yCoordinates.put("n7", -3.0);
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
      // We will not be using expandGroupLink here
      return null;
    }

    @Override
    public List<Link> getNeighborsAStar(String nodeName, String endNode,
        HashMap<String, Link> hm, double extraDist) throws SQLException,
        IllegalArgumentException {
      // Our heuristic is the Cartesian distance between points.

      // This function, given a node, should return a list of its neighbors (who
      // are not already in hm!) in the form of links, where its distance
      // includes the heuristic distance.

      // an empty arrayList for us to add to later
      ArrayList<Link> neighborList = new ArrayList<Link>();

      // iterate through the neighbors of nodeName. We need to add extraDist to
      // its distance before
      // adding these neighbors to neighborList
      for (Link n : database.get(nodeName)) {
        // if the neighbor does not exist already in hm
        if (!(hm.containsKey(n.getEnd()))) {
          neighborList.add(new Link(n.getSource(), n.getEnd(), n.getDistance()
              + extraDist + heuristicValue(n.getEnd(), endNode), n.getName()));
        }
      }
      return neighborList;
    }

    private double cartesianDistance(double xOne, double yOne, double xTwo,
        double yTwo) {
      return Math.sqrt(Math.pow(xTwo - xOne, 2) + Math.pow(yTwo - yOne, 2));
    }

    @Override
    public Double heuristicValue(String node, String endNode)
        throws SQLException, IllegalArgumentException {
      // coordinates of our node...
      Double x = xCoordinates.get(node);
      Double y = yCoordinates.get(node);

      // coordinates of our end node ... kitten

      Double endX = xCoordinates.get(endNode);
      Double endY = yCoordinates.get(endNode);

      if ((endX == null) || (endY == null)) {
        throw new IllegalArgumentException();
      }

      return cartesianDistance(x, y, endX, endY);
    }
  }

  @Test
  public void findPathsSimpleTest() throws IllegalArgumentException,
      SQLException {
    // findPaths returns a HashMap of all the discovered paths until the stop
    // node is reached
    // Here we perform a simple test where the stop node is the closest node to
    // the start node.

    // the only difference between this test and the one in Dijkstra is the
    // changing declarations of Dijkstra to declarations of AStar!

    HashMap<String, Link> expectedMap = new HashMap<String, Link>();
    expectedMap.put("start", new Link("start", "start", 0.0));
    expectedMap.put("n1", new Link("start", "n1", 2.0));

    testInfoGetter ig = new testInfoGetter();
    ig.fillDatabase();
    Dijkstra simpleAStar = new AStar("start", ig);

    assertTrue(simpleAStar.findPaths("n1").equals(expectedMap));
  }

  @Test
  public void findPathsEdgeCases() throws IllegalArgumentException,
      SQLException {
    // We test two edge cases.
    // 1. The 'stop' node is the same as the start node. findPaths should then
    // terminate immediately.
    // 2. The start node is not adjacent to any nodes! findPaths should then
    // terminate when
    // closestUndiscovered is empty.

    // This test is the same as the test in Dijkstra, except for replacing
    // constructors of Dijkstra with constructors of AStar

    // expectedMap for Case 1
    HashMap<String, Link> expectedOne = new HashMap<String, Link>();
    expectedOne.put("start", new Link("start", "start", 0.0));

    // expectedMap for Case 2
    HashMap<String, Link> expectedTwo = new HashMap<String, Link>();
    expectedTwo.put("n7", new Link("n7", "n7", 0.0));

    testInfoGetter ig = new testInfoGetter();
    ig.fillDatabase();
    Dijkstra caseOneAStar = new AStar("start", ig);
    Dijkstra caseTwoAStar = new AStar("n7", ig);

    assertTrue(caseOneAStar.findPaths("start").equals(expectedOne));
    assertTrue(caseTwoAStar.findPaths("").equals(expectedTwo));
  }

  @Test
  public void findPathsError() throws SQLException {
    // Here we test two errors:
    // 1. The start node doesn't exist!
    // 2. The end node doesn't exist!

    // Technically it's possible for AStar to not produce an error when the end
    // node doesn't exist,
    // since the class itself does not call an error, but it is expected an
    // error will be passed by the
    // infoGetter, since, well, it's impossible to perform A* search without
    // knowing what the end node is!

    // *Also, getNeighborAStar does throw IllegalArgumentExceptions per its
    // signature.

    // So alternatively we could test the error in the individual infoGetters.
    // But for the sake of completeness we'll do the tests in both.

    // Note a nonexistent end node does not produce an error in Dijkstra, only
    // in AStar.
    // In Dijkstra, the entire graph is found and things end there!

    testInfoGetter ig = new testInfoGetter();
    ig.fillDatabase();
    // Error 1
    String message;
    try {
      @SuppressWarnings("unused")
      Dijkstra errorAStar = new AStar("apple", ig);
      message = "Huh. Looks like the error wasn't generated.";
    } catch (IllegalArgumentException e) {
      message = "Great, the error was thrown! Yippee!";
    }

    // Error 2
    String messageTwo;
    try {
      @SuppressWarnings("unused")
      Dijkstra errorAStarTwo = new AStar("start", ig);
      messageTwo = "Whattt? The error wasn't generated, OMG!";
    } catch (IllegalArgumentException e) {
      messageTwo = "Great, the error was thrown! Cool beans!";
    }

    assertTrue(message.equals("Great, the error was thrown! Yippee!"));
    assertTrue(messageTwo.equals("Great, the error was thrown! Cool beans!"));
  }

  @Test
  public void findPathsAdvanced() throws IllegalArgumentException, SQLException {
    // Complex test with findPathsAdvanced!

    HashMap<String, Link> expectedMap = new HashMap<String, Link>();
    expectedMap.put("start", new Link("start", "start", 0.0));
    expectedMap.put("n1", new Link("start", "n1", 2.0));
    expectedMap.put("n2", new Link("start", "n2", 2.5));
    expectedMap.put("n3", new Link("n2", "n3", 4.0));
    // In normal Dijkstra's we would also have:
    // expectedMap.put("n5", new Link("n3", "n5", 6.0));
    expectedMap.put("n4", new Link("n2", "n4", 6.5));
    expectedMap.put("n6", new Link("n4", "n6", 8.0));

    testInfoGetter ig = new testInfoGetter();
    ig.fillDatabase();

    Dijkstra testDijkstra = new AStar("start", ig);

    assertTrue(testDijkstra.findPaths("n6").equals(expectedMap));
    assertTrue(testDijkstra.findPaths("n1").equals(expectedMap));
    // for the second one, testDijkstra will see that the current list of
    // discovered nodes
    // already contains "n1"... thus it just returns the entire list
    // (technically 'map' ;)).
  }

  @Test
  public void findPathsAll() throws IllegalArgumentException, SQLException {
    // We're going to search the entire graph now, by giving n7, a node which
    // isn't connected to the others.
    // This should terminate after closestUndiscovered becomes empty!

    // Note in DijkstraTest, we give an end node which doesn't exist, but that
    // isn't allowed in AStar!

    HashMap<String, Link> expectedMap = new HashMap<String, Link>();
    expectedMap.put("start", new Link("start", "start", 0.0));
    expectedMap.put("n1", new Link("start", "n1", 2.0));
    expectedMap.put("n2", new Link("start", "n2", 2.5));
    expectedMap.put("n3", new Link("n2", "n3", 4.0));
    expectedMap.put("n4", new Link("n2", "n4", 6.5));
    // round the decimals!
    expectedMap.put("n5", new Link("n3", "n5",
        Math.round(4.0 + Math.sqrt(5)) * 100000.0 / 100000.0));
    expectedMap.put("n6", new Link("n5", "n6", 8.0));

    testInfoGetter ig = new testInfoGetter();
    ig.fillDatabase();

    Dijkstra testDijkstra = new AStar("start", ig);

    HashMap<String, Link> results = testDijkstra.findPaths("n7");
    for (String s : results.keySet()) {
      System.out.println(s);
      System.out.println(results.get(s).getSource());
      System.out.println(results.get(s).getEnd());
      System.out.println(results.get(s).getDistance());
      System.out.println("");
    }

    assertTrue(testDijkstra.findPaths("n7").equals(expectedMap));
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
    expectedList.add(new Link("n2", "n4", 6.5));
    expectedList.add(new Link("n4", "n6", 8.0));

    testInfoGetter ig = new testInfoGetter();
    ig.fillDatabase();

    Dijkstra testDijkstra = new AStar("start", ig);

    assertTrue(testDijkstra.getPath("n6").equals(expectedList));
  }
}
