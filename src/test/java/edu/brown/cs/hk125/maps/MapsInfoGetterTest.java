package edu.brown.cs.hk125.maps;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MapsInfoGetterTest {

  private MapsInfoGetter mig;

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
    mig = new MapsInfoGetter("/course/cs032/data/maps/smallMaps.sqlite3");
  }

  @After
  public void tearDown() {
    // (Optional) Code to run after each test case goes here.
  }

  // TODO add test methods here.
  // The methods must be annotated with annotation @Test. For example:
  //

  @Test
  public void isInTest() throws SQLException, ClassNotFoundException {
    // tests to see if the isIn method can properly tell if an element is in the
    // database
    assertTrue(mig.isIn("/n/2"));
    assertTrue(mig.isIn("/n/5"));
    assertTrue(!(mig.isIn("/n/7")));
  }

  @Test
  public void getNeighborsAStarTest() throws ClassNotFoundException,
      SQLException {
  }

  @Test
  public void getLatLngTest() throws SQLException {
  }

  @Test
  public void getIntersectionTest() throws SQLException {
  }

  @Test
  public void heuristicValueTest() throws SQLException {
  }
}
