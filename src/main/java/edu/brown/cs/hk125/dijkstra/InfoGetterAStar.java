package edu.brown.cs.hk125.dijkstra;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * An Info Getter designed to be used for AStar search. Has two additional
 * methods: getNeighborsAStar (which takes in an end node needed for AStar
 * search), and heuristicValue, which takes in a node and an endNode and returns
 * the heuristic value added onto its distance.
 *
 * @author sl234
 *
 */
public interface InfoGetterAStar extends infoGetter {

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
   * Returns the heuristic value of a node, given a specified endNode.
   *
   * @param node
   *          , the node whose heuristic value we are looking for.
   * @param endNode
   *          , the node whose aspects shall influence the heuristic value.
   * @return the heurstic value of 'node', given 'endNode.'
   * @throws SQLException
   *           , if querying is involved and there is an error in the query.
   * @throws IllegalArgumentException
   *           , if node or endNode are not in the infoGetter! Wah!
   */
  public Double heuristicValue(String node, String endNode)
      throws SQLException, IllegalArgumentException;
}
