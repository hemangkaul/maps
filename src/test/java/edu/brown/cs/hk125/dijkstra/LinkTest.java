package edu.brown.cs.hk125.dijkstra;

import static org.junit.Assert.assertTrue;

import java.util.PriorityQueue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.cs.hk125.dijkstra.Link;

public class LinkTest {

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

  @Test
  public void linkCompareToTest() {
    // We test our Comparable implementation
    // Neighbors should be sorted according to their distance
    // Notice we do not specific a link name, as this is optional when
    // constructing a neighbor
    Link n1 = new Link("source", "neighbor1", 15.0);
    Link n2 = new Link("source", "neighbor2", 10.0);
    PriorityQueue<Link> testQueue = new PriorityQueue<Link>();
    testQueue.add(n1);
    testQueue.add(n2);

    assertTrue(testQueue.peek().equals(n2));
    // first element of Queue is n2
  }
}