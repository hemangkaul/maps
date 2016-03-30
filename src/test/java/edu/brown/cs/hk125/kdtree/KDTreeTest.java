package edu.brown.cs.hk125.kdtree;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.cs.hk125.latlng.LatLng;

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
    // We test our Comparable implementation
    // Neighbors should be sorted according to their distance
    // Notice we do not specific a link name, as this is optional when
    // constructing a neighbor
    LatLng latlng1 = new LatLng(10.0, 15.0);
    LatLng latlng2 = new LatLng(6.0, 10.0);
    LatLng latlng3 = new LatLng(6.0, 10.0);

    assertTrue(!latlng1.equals(latlng2));
    assertTrue(latlng1.equals(latlng1));
    assertTrue(latlng2.equals(latlng3));
  }

  @Test
  public void findNNTest() {
    List<LatLng> elementList = new ArrayList<>();
    for (int i = 0; i < 7; i++) {
      elementList.add(new LatLng(i, i));
    }
    KDTree<LatLng> kd = new KDTree<LatLng>(elementList);
    LatLng target = new LatLng(6, 6);
    LatLng target2 = new LatLng(5, 7);
    LatLng target3 = new LatLng(8, 8);
    LatLng target4 = new LatLng(0, 0);
    LatLng target5 = new LatLng(-1, -1);
    LatLng result = kd.findNN(target);
    LatLng result2 = kd.findNN(target2);
    LatLng result3 = kd.findNN(target3);
    LatLng result4 = kd.findNN(target5);
    assertTrue(target.equals(result));
    assertTrue(target.equals(result2));
    assertTrue(target.equals(result3));
    assertTrue(target4.equals(result4));
  }

  @Test
  public void findNNMediumTest() {
    List<LatLng> elementList = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      elementList.add(new LatLng(i, i));
    }
    KDTree<LatLng> kd = new KDTree<LatLng>(elementList);
    LatLng target = new LatLng(6, 6);
    LatLng target2 = new LatLng(5, 7);
    LatLng target3 = new LatLng(999, 999);
    LatLng target4 = new LatLng(1001, 1001);
    LatLng result = kd.findNN(target);
    LatLng result2 = kd.findNN(target2);
    LatLng result3 = kd.findNN(target4);
    assertTrue(target.equals(result));
    assertTrue(target.equals(result2));
    assertTrue(target3.equals(result3));
  }

  @Test
  public void findNNBigTest() {
    List<LatLng> elementList = new ArrayList<>();
    for (int i = 0; i < 100000; i++) {
      elementList.add(new LatLng(i, i));
    }
    KDTree<LatLng> kd = new KDTree<LatLng>(elementList);
    LatLng target = new LatLng(6, 6);
    LatLng target2 = new LatLng(5, 7);
    LatLng target3 = new LatLng(998, 998);
    LatLng target4 = new LatLng(998, 1000);
    LatLng result = kd.findNN(target);
    LatLng result2 = kd.findNN(target2);
    LatLng result3 = kd.findNN(target4);
    assertTrue(target.equals(result));
    assertTrue(target.equals(result2));
    System.out.println(result3);
    assertTrue(target3.equals(result3));
  }

  @Test
  public void findNNNegativeTest() {
    List<LatLng> elementList = new ArrayList<>();
    for (int i = -49; i < 50; i++) {
      elementList.add(new LatLng(i, i));
    }
    KDTree<LatLng> kd = new KDTree<LatLng>(elementList);
    LatLng target = new LatLng(6, 6);
    LatLng target2 = new LatLng(5, 7);
    LatLng target3 = new LatLng(-10, -8);
    LatLng target4 = new LatLng(-9, -9);
    LatLng result = kd.findNN(target);
    LatLng result2 = kd.findNN(target2);
    LatLng result3 = kd.findNN(target3);
    assertTrue(target.equals(result));
    assertTrue(target.equals(result2));
    assertTrue(target4.equals(result3));
  }
}
