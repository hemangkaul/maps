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
public interface InfoGetterAStar extends InfoGetter {

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
   * @param endNode
   *          , the id of the end Node
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
  List<Link> getNeighborsAStar(String nodeName, String endNode,
      HashMap<String, Link> hm, double extraDist) throws SQLException;

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
  Double heuristicValue(String node, String endNode) throws SQLException;
}
