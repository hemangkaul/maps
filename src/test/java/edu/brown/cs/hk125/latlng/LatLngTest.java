package edu.brown.cs.hk125.latlng;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LatLngTest {

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
    LatLng latlng1 = new LatLng(10.0, 15.0);
    LatLng latlng2 = new LatLng(6.0, 10.0);
    LatLng latlng3 = new LatLng(6.0, 10.0);
    LatLng latlng4 = new LatLng(6.0, 10.0, "named");
    LatLng latlng5 = new LatLng(-10, 10);

    assertTrue(!latlng1.equals(latlng2));
    assertTrue(latlng1.equals(latlng1));
    assertTrue(latlng2.equals(latlng3));
    assertTrue(latlng3.equals(latlng4));
    assertTrue(latlng5.equals(new LatLng(-10, 10)));
  }

  @Test
  public void haversineDistanceTest() {

  }

}
