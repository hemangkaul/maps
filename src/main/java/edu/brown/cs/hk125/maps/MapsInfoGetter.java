package edu.brown.cs.hk125.maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;

import edu.brown.cs.hk125.autocorrect.AutoCorrector;
import edu.brown.cs.hk125.dijkstra.InfoGetterAStar;
import edu.brown.cs.hk125.dijkstra.Link;
import edu.brown.cs.hk125.dijkstra.groupLink;
import edu.brown.cs.hk125.kdtree.KDTree;
import edu.brown.cs.hk125.latlng.LatLng;
import edu.brown.cs.hk125.map.Way;
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
public class MapsInfoGetter implements InfoGetterAStar, Tiler {

  private Connection conn;
  private Map<String, LatLng> pointCache = new HashMap<>();
  private List<Tile> tileCache = new ArrayList<>();
  private Map<String, Double> trafficCache = new ConcurrentHashMap<>();
  private Map<String, Way> wayCache = new HashMap<>();
  private boolean trafficOn;
  private static final double TILESIZE = 0.01;

  public MapsInfoGetter(String db, boolean trafficServerRunning)
      throws ClassNotFoundException, SQLException {
    this.trafficOn = trafficServerRunning;
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

    Double lat;
    Double lng;

    if (pointCache.containsKey(nodeName)) {
      LatLng latlng = pointCache.get(nodeName);
      lat = latlng.getLat();
      lng = latlng.getLng();
    } else {
      // only make call to database if the cache doesn't contain
      Map<String, Double> latLng = getLatLng(nodeName);
      lat = latLng.get("Latitude");
      lng = latLng.get("Longitude");
    }

    // Makes sure the present node exists in the database!
    if ((lat == null) || (lng == null)) {
      throw new IllegalArgumentException(
          "The node you inputed does not have an associated latitude or "
              + "longitude in the database. Please check if your id is correct!");
    }

    // Here we get the latitude and longitude of the end node.

    Double endLat;
    Double endLng;

    if (pointCache.containsKey(endNode)) {
      LatLng endlatlng = pointCache.get(endNode);
      endLat = endlatlng.getLat();
      endLng = endlatlng.getLng();
    } else {
      // only make call to database if the cache doesn't contain
      Map<String, Double> endLatLng = getLatLng(endNode);
      endLat = endLatLng.get("Latitude");
      endLng = endLatLng.get("Longitude");
    }

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
        String wayID = rs.getString(1);
        double neighborLat = rs.getDouble(3);
        double neighborLng = rs.getDouble(4);
        // length between the start and end points of the Way
        double wayLength = LatLng.distance(lat, lng, neighborLat, neighborLng);

        // We could use the heuristicValue method but this is literally the same
        // thing, plus it vastly reduces the amount of querying necessary (i.e.
        // we don't have to query for endNode for EVERY different neighbor)
        double heuristicLength = LatLng.distance(neighborLat, neighborLng,
            endLat, endLng);

        Link toAdd;
        if (trafficOn) {
          double traffic = wayLength * trafficCache.get(wayID);
          toAdd = new Link(nodeName, neighbor, traffic + heuristicLength
              + extraDist, wayID);
        } else {
          toAdd = new Link(nodeName, neighbor, wayLength + heuristicLength
              + extraDist, wayID);
        }
        // make

        // sure
        // to
        // add
        // extraDist!;
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
   * Returns a list of all the LatLng points in the database and adds them to
   * the cache.
   *
   * @return a list of all the LatLng points in the database
   * @throws SQLException
   *           , if there is an error with the query
   */
  public KDTree<LatLng> getKDTree() throws SQLException {
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
      pointCache.put(id, add);
      elementList.add(add);
    }
    rs.close();
    prep.close();

    KDTree<LatLng> tree = new KDTree<LatLng>(elementList);
    return tree;
  }

  /**
   * gets all the way names and adds them to the trie, while adding all the ways
   * to the wayCache.
   *
   *
   * @return trie with all the names of the ways
   * @throws SQLException
   */
  public AutoCorrector getMapsAutoCorrector() throws SQLException {
    String query = "SELECT start, end, name, type, id FROM Way";

    // Create a PreparedStatement
    PreparedStatement prep;
    prep = conn.prepareStatement(query);

    // Execute the query and retrieve a ResultStatement
    ResultSet rs = prep.executeQuery();

    // Add the LatLngs to the elementList;

    List<String> elementList = new ArrayList<>();

    while (rs.next()) {
      String startNode = rs.getString(1);
      String endNode = rs.getString(2);

      LatLng start = pointCache.get(startNode);
      LatLng end = pointCache.get(endNode);

      String name = rs.getString(3);
      String type = rs.getString(4);
      String id = rs.getString(5);

      Way newWay = new Way(start.getLat(), start.getLng(), end.getLat(),
          end.getLng(), name, type, id, 1.0);

      wayCache.put(id, newWay);

      // 1.0 is the traffic value
      trafficCache.put(id, 1.0);

      elementList.add(name);
    }
    rs.close();
    prep.close();

    Trie trie = new Trie(elementList, true);
    return new MapsAutoCorrector(trie);
  }

  @Override
  public void setTiles() throws SQLException, NoSuchElementException {
    String query = "SELECT MAX(latitude), MAX(longitude), "
        + "MIN(latitude), MIN(longitude) FROM Node";

    // Create a PreparedStatement
    PreparedStatement prep;
    prep = conn.prepareStatement(query);

    // Execute the query and retrieve a ResultStatement
    ResultSet rs = prep.executeQuery();

    double topLat = 0;
    double leftLng = 0;
    double bottomLat = 0;
    double rightLng = 0;

    while (rs.next()) {
      topLat = rs.getDouble(1) + 0.0000001;
      rightLng = rs.getDouble(2) + 0.0000001;
      bottomLat = rs.getDouble(3) - 0.0000001;
      leftLng = rs.getDouble(4) - 0.0000001;
    }
    rs.close();
    prep.close();

    double height = topLat - bottomLat;
    double width = rightLng - leftLng;
    double tileHeight = height * TILESIZE;
    double tileWidth = width * TILESIZE;

    for (int i = 0; (i * tileHeight) < height; i++) {

      double bottomLatitude = bottomLat + (i * tileHeight);
      double topLatitude = bottomLatitude + tileHeight;

      for (int j = 0; (j * tileWidth) < width; j++) {

        double leftLongitude = leftLng + (j * tileWidth);
        double rightLongitude = leftLongitude + tileWidth;

        Tile insert = new Tile(topLatitude, bottomLatitude, leftLongitude,
            rightLongitude);

        tileCache.add(insert);
      }
    }
    setWays();
  }

  /**
   * set ways sets the ways for each tile.
   *
   * @throws SQLException
   *           if it cannot query the tile
   */
  private void setWays() throws SQLException, NoSuchElementException {
    for (Way way : wayCache.values()) {
      getTile(way.getStartLatitude(), way.getStartLongitude()).insertWay(way,
          trafficCache.get(way.getId()));
    }
  }

  /**
   * @throws IOException
   * @throws SQLException
   * @throws NoSuchElementException
   *
   */
  public void updateTraffic(int port) throws IOException,
      NoSuchElementException, SQLException {

    String requestPrefix = "http://localhost:" + port + "?last=";
    long unixTimestamp = Instant.now().getEpochSecond();
    URL url = new URL(requestPrefix + Long.toString(unixTimestamp));
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    BufferedReader br = new BufferedReader(new InputStreamReader(
        conn.getInputStream()));
    String array = "";
    // use parallel threads to fill trafficCache
    while ((array = br.readLine()) != null) {
      List<List<Object>> read = new Gson().fromJson(array, ArrayList.class);
      System.out.println(read);
      System.out.println(array);
    }
    String wayId = null;
    double traffic = 0.0;
    trafficCache.put(wayId, traffic);
    setWays();
  }

  @Override
  public Tile getTile(double lat, double lng) throws NoSuchElementException,
      SQLException {
    if (tileCache.isEmpty()) {
      System.out.println("check");
      setTiles();
    }

    for (Tile tile : tileCache) {
      if (tile.inTile(lat, lng)) {
        return tile;
      }
    }
    throw new NoSuchElementException();
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
