package edu.brown.cs.hk125.dijkstra;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Retrieves information for a node; intended for use in Dijkstra's algorithm.
 *
 * @author sl234
 *
 */
public interface infoGetter {

  /**
   * Returns a list of the given node's undiscovered neighbors.
   *
   * @param nodeName
   *          , the name of the node
   * @param hm
   *          , a hashMap containing neighbors which are 'not allowed.' I.e. we
   *          want to find the neighbors NOT in this hashmap!
   * @param extraDist
   *          , when we return our neighbors, we add 'extraDist' to the distance
   *          variable of the neighbor.
   * @return a list of the given node's undiscovered neighbors.
   * @throws SQLException
   *           , if SQL querying is used and there is an issue with the query
   */
  public List<Link> getNeighbors(String nodeName, HashMap<String, Link> hm,
      double extraDist) throws SQLException;

  /**
   * Returns a list of the given node's undiscovered neighbors, where the
   * distances of those neighbors are altered by the specifications of A*
   * search.
   *
   * @param nodeName
   *          , name of the node whose neighbors we are searching for
   * @param endNode
   *          , name of the node we are searching for in the overall dijkstra
   *          process
   * @param hm
   *          , a hashMap containing neighbors which are 'not allowed.' I.e. we
   *          want to find the neighbors NOT in this hashmap!
   * @param extraDist
   *          , when we return our neighbors, we add 'extraDist' to the distance
   *          variable of the neighbor.
   * @return a list of the given node's undiscovered neighbors, where the
   *         distances of those neighbors are altered by the specifications of
   *         A* search.
   * @throws SQLException
   *           , if SQL querying is used and there is an issue with the query
   *
   * @throws IllegalArgumentException
   *           , if the end Node is not in the database
   */
  public List<Link> getNeighborsAStar(String nodeName, String endNode,
      HashMap<String, Link> hm, double extraDist) throws SQLException,
      IllegalArgumentException;

  /**
   * Specifies if nodeName is in the system / graph.
   *
   * @param nodeName
   *          , the name of the node
   * @return true if the node is in the database and false if not
   * @throws SQLException
   *           if SQL querying is used and there is an issue with the query
   */
  public boolean isIn(String nodeName) throws SQLException;

  /**
   * Expands a groupLink into an array of links.
   *
   * @param g
   *          the groupLink to be expanded
   * @param hm
   *          the map of discovered nodes
   * @return an ArrayList of all the links which make up the groupLink
   * @throws SQLException
   *           if SQL querying is used and ther is an issue with the query
   */
  public List<Link> expandGroupLink(groupLink g, HashMap<String, Link> hm)
      throws SQLException;
}
