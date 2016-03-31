package edu.brown.cs.hk125.kdtree;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.cs.hk125.point.Point;

public class KDTreeTest {

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
  public void equalsTest() {
    Point latlng1 = new Point(10.0, 15.0);
    Point latlng2 = new Point(6.0, 10.0);
    Point latlng3 = new Point(6.0, 10.0);

    assertTrue(!latlng1.equals(latlng2));
    assertTrue(latlng1.equals(latlng1));
    assertTrue(latlng2.equals(latlng3));
  }

  @Test
  public void findNNTest() {
    List<Point> elementList = new ArrayList<>();
    for (int i = 0; i < 7; i++) {
      elementList.add(new Point(i, i));
    }
    KDTree<Point> kd = new KDTree<Point>(elementList);
    Point target = new Point(6, 6);
    Point target2 = new Point(5, 7);
    Point target3 = new Point(8, 8);
    Point target4 = new Point(0, 0);
    Point target5 = new Point(-1, -1);
    Point result = kd.findNN(target);
    Point result2 = kd.findNN(target2);
    Point result3 = kd.findNN(target3);
    Point result4 = kd.findNN(target5);
    assertTrue(target.equals(result));
    assertTrue(target.equals(result2));
    assertTrue(target.equals(result3));
    assertTrue(target4.equals(result4));
  }

  @Test
  public void findNNMediumTest() {
    List<Point> elementList = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      elementList.add(new Point(i, i));
    }
    KDTree<Point> kd = new KDTree<Point>(elementList);
    Point target = new Point(6, 6);
    Point target2 = new Point(5, 7);
    Point target3 = new Point(999, 999);
    Point target4 = new Point(1001, 1001);
    Point result = kd.findNN(target);
    Point result2 = kd.findNN(target2);
    Point result3 = kd.findNN(target4);
    assertTrue(target.equals(result));
    assertTrue(target.equals(result2));
    assertTrue(target3.equals(result3));
  }

  @Test
  public void findNNBigTest() {
    List<Point> elementList = new ArrayList<>();
    for (int i = 0; i < 100000; i++) {
      elementList.add(new Point(i, i));
    }
    KDTree<Point> kd = new KDTree<Point>(elementList);
    Point target = new Point(6, 6);
    Point target2 = new Point(5, 7);
    Point target3 = new Point(999, 999);
    Point target4 = new Point(998, 1000);
    Point result = kd.findNN(target);
    Point result2 = kd.findNN(target2);
    Point result3 = kd.findNN(target4);
    assertTrue(target.equals(result));
    assertTrue(target.equals(result2));
    assertTrue(target3.equals(result3));
  }

  @Test
  public void findNNNegativeTest() {
    List<Point> elementList = new ArrayList<>();
    for (int i = -49; i < 50; i++) {
      elementList.add(new Point(i, i));
    }
    KDTree<Point> kd = new KDTree<Point>(elementList);
    Point target = new Point(6, 6);
    Point target2 = new Point(5, 7);
    Point target3 = new Point(-10, -8);
    Point target4 = new Point(-9, -9);
    Point result = kd.findNN(target);
    Point result2 = kd.findNN(target2);
    Point result3 = kd.findNN(target3);
    assertTrue(target.equals(result));
    assertTrue(target.equals(result2));
    assertTrue(target4.equals(result3));
  }
}
