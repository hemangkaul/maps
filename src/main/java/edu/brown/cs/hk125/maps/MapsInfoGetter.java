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

import edu.brown.cs.hk125.dijkstra.Link;
import edu.brown.cs.hk125.dijkstra.groupLink;
import edu.brown.cs.hk125.dijkstra.infoGetter;

/**
 * MapsInfoGetter's primary role is to return the neighboring nodes given a
 * node_id.
 *
 * It does this by querying the database for Ways starting with the node_id.
 *
 * MapInfoGetter incorporates A* Search directly.
 *
 * @author sl234
 *
 */
public class MapsInfoGetter implements infoGetter {

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
   * plus the distance from the neighbor to the end node.
   *
   * The distance from the neighbor to the end node is added to implement A*
   * search; this optimizes the search algorithm to try and get it to search
   * more in the general direction of the destination.
   *
   * @param nodeName
   *          , the id of the Node
   * @param endNode
   *          , the id of the final destination
   * @param hm
   *          , a hashmap of discovered nodes
   * @param extraDist
   *          , the distance from the node to the start node, added to the
   *          distance value of each neighbor
   * @return a list of all the node's undiscovered neighbors
   *
   * @throws SQLException
   */
  public ArrayList<Link> getNeighborsAStar(String nodeName, String endNode,
      HashMap<String, Link> hm, double extraDist) throws SQLException {
    // Write the query as a string
    String query = "SELECT film, count(film) FROM actor_film WHERE film IN "
        + "(SELECT film FROM actor_film WHERE actor = '" + nodeName
        + "') GROUP BY film ORDER BY count(film) DESC";

    // This will return a list of all the films the actor appeared in,
    // sorted by number of actors in each film

    // Create a PreparedStatement
    PreparedStatement prep;
    prep = conn.prepareStatement(query);

    // Execute the query and retrieve a ResultStatement
    ResultSet rs = prep.executeQuery();

    // Add the results to this list
    List<Link> toReturn = new ArrayList<Link>();
    while (rs.next()) {
      Link toAdd = new groupLink(nodeName, (1 / rs.getDouble(2)) + extraDist,
          rs.getString(1)); // make sure to add extraDist!
      toReturn.add(toAdd);
    }
    // Close the ResultSet and the PreparedStatement
    rs.close();
    prep.close();
    return (ArrayList<Link>) toReturn;
  }

  /**
   * Not needed, so we return null!
   */
  @Override
  public ArrayList<Link> getNeighbors(String nodeName,
      HashMap<String, Link> hm, double extraDist) throws SQLException {
    // Write the query as a string
    return null;
  }

  @Override
  public boolean isIn(String nodeName) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * Not needed, so we return null!
   */
  @Override
  public ArrayList<Link> expandGroupLink(groupLink g, HashMap<String, Link> hm)
      throws SQLException {
    return null;
  }

}
