package edu.brown.cs.hk125.maps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.hk125.dijkstra.InfoGetterAStar;
import edu.brown.cs.hk125.dijkstra.Link;
import edu.brown.cs.hk125.dijkstra.groupLink;
import edu.brown.cs.hk125.latlng.LatLng;

/**
 * MapsInfoGetter's primary role is to return the neighboring nodes given a
 * node_id.
 *
 * It does this by querying the database for Ways starting with the node_id.
 *
 * MapInfoGetter incorporates A* Search.
 *
 * @author sl234
 *
 */
public class MapsInfoGetter implements InfoGetterAStar {

  private Connection conn;

  public MapsInfoGetter(String db) throws ClassNotFoundException, SQLException {
    // Set up a connection
    Class.forName("org.sqlite.JDBC");
    // Store the connection in a field
    String urlToDB = "jdbc:sqlite:" + db;
    conn = DriverManager.getConnection(urlToDB);

    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys = ON;");
  }

  /**
   * Returns a list of all the node's undiscovered neighbors, with one
   * additional consideration: the "distance" value for the neighbor is the
   * normal distance value (distance from neighbor to start node + extraDist)
   * plus the distance from the neighbor to the end node (A* search).
   *
   * The distance from the neighbor to the end node is added to implement A*
   * search; this optimizes the search algorithm to try and get it to search
   * more in the general direction of the destination, thus
   *
   * @param nodeName
   *          , the id of the Node
   * @param endLat
   *          , the latitude of the end node
   * @param endLng
   *          , the longitude of the end node
   * @param hm
   *          , a hashmap of discovered nodes
   * @param extraDist
   *          , the distance from the node to the start node, added to the
   *          distance value of each neighbor
   * @return a list of all the node's undiscovered neighbors
   *
   * @throws SQLException
   *           , if there are errors in the query!
   * @throws IllegalArgumentException
   *           , if there are Illegal arguments given!
   */
  @Override
  public List<Link> getNeighborsAStar(String nodeName, String endNode,
      HashMap<String, Link> hm, double extraDist) throws SQLException,
      IllegalArgumentException {

    // Here we get the latitude and longitude of the present node.
    Map<String, Double> latLng = getLatLng(nodeName);
    Double lat = latLng.get("Latitude");
    Double lng = latLng.get("Longitude");

    // Makes sure the present node exists in the database!
    if ((lat == null) || (lng == null)) {
      throw new IllegalArgumentException(
          "The node you inputed does not have an associated latitude or "
              + "longitude in the database. Please check if your id is correct!");
    }

    // Here we get the latitude and longitude of the end node.
    Map<String, Double> endLatLng = getLatLng(endNode);
    Double endLat = endLatLng.get("Latitude");
    Double endLng = endLatLng.get("Longitude");

    // Makes sure the present node exists in the database!
    if ((endLat == null) || (endLng == null)) {
      throw new IllegalArgumentException(
          "The end node does not exist in the database!");
    }
    // Write the query as a string
    String query = "SELECT Way.id, Way.end, Node.latitude, Node.longitude "
        + "FROM Way INNER JOIN Node ON Way.end == Node.id WHERE start = ?";

    // This will return all the ways which start at our startNode, as well
    // as the latitude and longitude of their end points.

    // Create a PreparedStatement
    PreparedStatement prep;
    prep = conn.prepareStatement(query);
    prep.setString(1, nodeName);

    // Execute the query and retrieve a ResultStatement
    ResultSet rs = prep.executeQuery();

    // Add the results to this list
    List<Link> toReturn = new ArrayList<Link>();
    while (rs.next()) {
      String id = rs.getString(1);
      if (!(hm.containsKey(id))) {
        double neighborLat = rs.getDouble(3);
        double neighborLng = rs.getDouble(4);
        double wayLength = LatLng.distance(lat, lng, neighborLat, neighborLng);
        // length between the start and end points of the Way

        double heuristicLength = LatLng.distance(neighborLat, neighborLng,
            endLat, endLng);

        Link toAdd = new Link(nodeName, rs.getString(2), wayLength
            + heuristicLength + extraDist, id); // make
        // sure
        // to
        // add
        // extraDist!
        toReturn.add(toAdd);
      }
    }
    // Close the ResultSet and the PreparedStatement
    rs.close();
    prep.close();
    return toReturn;
  }

  /**
   * Not needed, so we return null!
   */
  @Override
  public List<Link> getNeighbors(String nodeName, HashMap<String, Link> hm,
      double extraDist) throws SQLException {
    // Write the query as a string
    return null;
  }

  @Override
  public boolean isIn(String nodeName) throws SQLException {
    String query = "SELECT id FROM Node WHERE id = ?";

    // Create a PreparedStatement
    PreparedStatement prep;
    prep = conn.prepareStatement(query);
    prep.setString(1, nodeName);
    // Sets "WHERE id = ?" to "WHERE id = nodeName"

    // Execute the query and retrieve a ResultStatement
    ResultSet rs = prep.executeQuery();

    // Add the results to this list
    List<String> toReturn = new ArrayList<String>();
    while (rs.next()) {
      toReturn.add(rs.getString(1));
    } //
    rs.close();
    prep.close();
    return (!toReturn.isEmpty());
    // if the list is empty, then nodeName is NOT in the database!
  }

  /**
   * Not needed, so we return null!
   */
  @Override
  public List<Link> expandGroupLink(groupLink g, HashMap<String, Link> hm)
      throws SQLException {
    return null;
  }

  /**
   * Returns the latitude and longitude of a node given its id, in the form of a
   * HashMap with keys "Latitude" and "Longitude".
   *
   * @param nodeName
   *          , the id of a node
   * @return the latitude and longitude of a node given its id, in the form of a
   *         HashMap with keys "Latitude" and "Longitude"
   * @throws SQLException
   *           , if there is an error with the query
   */
  public Map<String, Double> getLatLng(String nodeName) throws SQLException {
    String query = "SELECT latitude, longitude FROM Node WHERE id = ?";

    // Create a PreparedStatement
    PreparedStatement prep;
    prep = conn.prepareStatement(query);
    prep.setString(1, nodeName);
    // Sets "WHERE id = ?" to "WHERE id = nodeName"

    // Execute the query and retrieve a ResultStatement
    ResultSet rs = prep.executeQuery();

    // Add the results to this list
    Map<String, Double> mapToReturn = new HashMap<String, Double>();
    while (rs.next()) {
      mapToReturn.put("Lat", rs.getDouble(1));
      mapToReturn.put("Long", rs.getDouble(2));
    } //
    rs.close();
    prep.close();

    return mapToReturn;
  }

  /**
   * Given two streets which intersect, returns the Node Id of the intersection.
   *
   * @param street
   *          , the name of the street
   * @param crossStreet
   *          , the name of the street which crosses it
   * @return the Node ID of the intersection of street and crossStreet
   * @throws SQLException
   *           , if there's an error with the query
   * @throws IllegalArgumentException
   *           , if the streets don't intersect, or if the streets don't exist
   */
  public String getIntersection(String street, String crossStreet)
      throws SQLException, IllegalArgumentException {
    String query = "SELECT start, end FROM Way WHERE (name == ?) OR (name == ?)";

    // Create a PreparedStatement
    PreparedStatement prep;
    prep = conn.prepareStatement(query);
    prep.setString(1, street);
    prep.setString(2, crossStreet);
    // Sets the street names in the query

    // Execute the query and retrieve a ResultStatement
    ResultSet rs = prep.executeQuery();

    // Add the results to this list
    List<String> starts = new ArrayList<String>();
    List<String> ends = new ArrayList<String>();
    while (rs.next()) {
      starts.add(rs.getString(1));
      ends.add(rs.getString(2));
    } //
    rs.close();
    prep.close();

    if (starts.size() != 2) {
      throw new IllegalArgumentException(
          "One or more of the streets is not in the database!");
    } else {
      String s1 = starts.get(0);
      String s2 = starts.get(1);
      String e1 = ends.get(0);
      String e2 = ends.get(1);
      if ((s1.equals(s2)) || (s1.equals(e2))) {
        return s1;
      } else if ((e1.equals(s2)) || (e1.equals(e2))) {
        return e1;
      } else {
        throw new IllegalArgumentException("These streets don't intersect!");
      }
    }
  }

  @Override
  public Double heuristicValue(String node, String endNode)
      throws SQLException, IllegalArgumentException {
    // TODO Auto-generated method stub
    Map<String, Double> nodeCoords = getLatLng(node);
    Map<String, Double> endCoords = getLatLng(endNode);
    return LatLng.distance(nodeCoords.get("Latitude"),
        nodeCoords.get("Longitude"), endCoords.get("Latitude"),
        endCoords.get("Longitude"));
  }
}
