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
import java.util.concurrent.ConcurrentHashMap;

import edu.brown.cs.hk125.dijkstra.InfoGetterAStar;
import edu.brown.cs.hk125.dijkstra.Link;
import edu.brown.cs.hk125.dijkstra.groupLink;
import edu.brown.cs.hk125.latlng.LatLng;
import edu.brown.cs.hk125.trie.Trie;

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
  private Map<String, LatLng> cache = new HashMap<>();
  private Map<Map<LatLng, LatLng>, Double> wayCache = new ConcurrentHashMap<>();

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
    // Map<String, Double> latLng = getLatLng(nodeName);
    // Double lat = latLng.get("Latitude");
    // Double lng = latLng.get("Longitude");

    LatLng latlng = cache.get(nodeName);
    Double lat = latlng.getLat();
    Double lng = latlng.getLng();

    // Makes sure the present node exists in the database!
    if ((lat == null) || (lng == null)) {
      throw new IllegalArgumentException(
          "The node you inputed does not have an associated latitude or "
              + "longitude in the database. Please check if your id is correct!");
    }

    // Here we get the latitude and longitude of the end node.
    // Map<String, Double> endLatLng = getLatLng(endNode);
    // Double endLat = endLatLng.get("Latitude");
    // Double endLng = endLatLng.get("Longitude");
    LatLng endlatlng = cache.get(endNode);
    Double endLat = endlatlng.getLat();
    Double endLng = endlatlng.getLng();

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
      String neighbor = rs.getString(2);
      if (!(hm.containsKey(neighbor))) {
        double neighborLat = rs.getDouble(3);
        double neighborLng = rs.getDouble(4);
        // length between the start and end points of the Way
        double wayLength = LatLng.distance(lat, lng, neighborLat, neighborLng);

        // We could use the heuristicValue method but this is literally the same
        // thing, plus it vastly reduces the amount of querying necessary (i.e.
        // we don't have to query for endNode for EVERY different neighbor)
        double heuristicLength = LatLng.distance(neighborLat, neighborLng,
            endLat, endLng);

        Link toAdd = new Link(nodeName, neighbor, wayLength + heuristicLength
            + extraDist, rs.getString(1)); // make
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
      mapToReturn.put("Latitude", rs.getDouble(1));
      mapToReturn.put("Longitude", rs.getDouble(2));
    } //
    rs.close();
    prep.close();

    return mapToReturn;
  }

  /**
   * Returns a list of all the LatLng points in the database.
   *
   * @return a list of all the LatLng points in the database
   * @throws SQLException
   *           , if there is an error with the query
   */
  public List<LatLng> getLatLngList() throws SQLException {
    String query = "SELECT id, latitude, longitude FROM Node";

    // Create a PreparedStatement
    PreparedStatement prep;
    prep = conn.prepareStatement(query);

    // Execute the query and retrieve a ResultStatement
    ResultSet rs = prep.executeQuery();

    // Add the LatLngs to the elementList;

    List<LatLng> elementList = new ArrayList<>();

    while (rs.next()) {
      double lat = rs.getDouble(2);
      double lng = rs.getDouble(3);
      String id = rs.getString(1);
      LatLng add = new LatLng(lat, lng, id);
      cache.put(id, add);
      elementList.add(add);
    }
    rs.close();
    prep.close();

    return elementList;
  }

  public Trie getWayTrie() throws SQLException {
    String query = "SELECT start, end, name FROM Way";

    // Create a PreparedStatement
    PreparedStatement prep;
    prep = conn.prepareStatement(query);

    // Execute the query and retrieve a ResultStatement
    ResultSet rs = prep.executeQuery();

    // Add the LatLngs to the elementList;
    Trie trie = new Trie();

    while (rs.next()) {
      String startNode = rs.getString(1);
      String endNode = rs.getString(2);

      LatLng start = cache.get(startNode);
      LatLng end = cache.get(endNode);

      Double distance = start.distance(end);

      Map<LatLng, LatLng> hm = new HashMap<>();

      hm.put(start, end);

      wayCache.put(hm, distance);

      trie.insert(rs.getString(3));
    }
    rs.close();
    prep.close();

    return trie;
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
    String query = "SELECT name, start, end FROM Way WHERE (name == ?) OR (name == ?)";

    // Create a PreparedStatement
    PreparedStatement prep;
    prep = conn.prepareStatement(query);
    prep.setString(1, street);
    prep.setString(2, crossStreet);
    // Sets the street names in the query

    // Execute the query and retrieve a ResultStatement
    ResultSet rs = prep.executeQuery();

    // start and end nodes of streets named 'street'
    List<String> streetNodes = new ArrayList<>();

    // start and end nodes of streets named 'crossStreet'
    List<String> crossNodes = new ArrayList<>();

    while (rs.next()) {
      if (rs.getString(1).equals(street)) {
        // is the street named 'street'
        streetNodes.add(rs.getString(2));
        streetNodes.add(rs.getString(3));
      }
      if (rs.getString(1).equals(crossStreet)) {
        // is the street named 'crossStreet?'
        // Note if street and crossStreet have the same name,
        // the nodes will be added to both lists!
        crossNodes.add(rs.getString(2));
        crossNodes.add(rs.getString(3));
      }
    } //
    rs.close();
    prep.close();

    if (streetNodes.isEmpty() || crossNodes.isEmpty()) {
      throw new IllegalArgumentException(
          "One or more of the streets is not in the database!");
    } else {
      // only the common nodes between streetNodes and crossNodes are retained
      streetNodes.retainAll(crossNodes);
      if (streetNodes.isEmpty()) {
        // if it's empty the streets have no nodes in common, and thus don't
        // intersect
        throw new IllegalArgumentException("These streets don't intersect!");
      } else {
        // if it's not empty, the streets have at least one node in common. We
        // simply return the first
        return streetNodes.get(0);
      }
    }
  }

  /**
   * Given a subject node and an end node, heuristicValue node returns the
   * "heuristic" for the subject node. How the heuristic value is calculated
   * will vary between different A* searches, but for ours, it is simply the
   * distance between the subject node and the end node, calculated using the
   * Haversine formula.
   */
  @Override
  public Double heuristicValue(String node, String endNode)
      throws SQLException, IllegalArgumentException {
    // TODO Auto-generated method stub
    Map<String, Double> nodeCoords = getLatLng(node);
    Map<String, Double> endCoords = getLatLng(endNode);

    // if either the node or endNode does not exist in the database, throw an
    // error
    if (nodeCoords.isEmpty() || endCoords.isEmpty()) {
      throw new IllegalArgumentException();
    }
    return LatLng.distance(nodeCoords.get("Latitude"),
        nodeCoords.get("Longitude"), endCoords.get("Latitude"),
        endCoords.get("Longitude"));
  }
}
