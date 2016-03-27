package edu.brown.cs.hk125.dijkstra;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * A Dijkstra class which outside of basics (constructors, getters, setters),
 * can do two things:
 *
 * 1. Perform Dijkstra's algorithm until reaching a specified node. 2. Return an
 * ArrayList of 'Links' from the start node to a specified node, where the
 * overall path is the shortest possible per Dijkstra's algorithm.
 *
 * @author sl234
 *
 */
public class Dijkstra {

  /**
   * A Dijkstra object contains a startNode, an infoGetter which retrieves
   * information about a graph, a list of discovered nodes, and a ranked queue
   * of undiscovered neighboring nodes.
   *
   * Dijkstra is able to find the shortest / lightest path to any node from the
   * start node.
   *
   * Users need only specify the startNode and the infoGetter when creating a
   * Dijkstra variable.
   *
   */
  private String startNode;
  private infoGetter ig;
  private HashMap<String, Link> discovered;
  private String newest; // the most recently
                         // discovered node
  private PriorityQueue<Link> closestUndiscovered;

  /**
   * Creates a Dijkstra object with a startNode and an infoGetter for accessing
   * information.
   *
   * @param startNode
   *          , the node which Dijkstra will calculate distances from and to.
   * @param ig
   *          , an infoGetter which retrieves information about neighbors and
   *          their distances
   * @throws IllegalArgumentException
   *           , there is an error if the startNode is not in the graph,
   *           according to the infoGetter!
   * @throws SQLException
   *           , if the infogetter uses querying, and there is an error, an
   *           exception may be thrown.
   */
  public Dijkstra(String startNode, infoGetter ig)
      throws IllegalArgumentException, SQLException {
    if (!ig.isIn(startNode)) {
      throw new IllegalArgumentException();
      // there is an error if the startNode is not in the graph, according to
      // the infoGetter!
    } else {
      this.ig = ig;
      this.setStartNode(startNode);
      this.newest = startNode;
      setDiscovered(new HashMap<String, Link>());
      getDiscovered().put(startNode, new Link(startNode, startNode, 0.0, ""));
      setClosestUndiscovered(new PriorityQueue<Link>());
    }
  }

  /**
   * @return the startNode
   */
  public String getStartNode() {
    return startNode;
  }

  /**
   * @param startNode
   *          the startNode to set
   */
  public void setStartNode(String startNode) {
    this.startNode = startNode;
  }

  /**
   * @return the newest discovered Node
   */
  public String getNewest() {
    return newest;
  }

  /**
   * Sets newest to something else.
   *
   * @param newest
   */
  protected void setNewest(String newest) {
    this.newest = newest;
  }

  /**
   * Returns the HashMap of Discovered Nodes
   *
   * @return
   */
  protected HashMap<String, Link> getDiscovered() {
    return (HashMap<String, Link>) Collections.unmodifiableMap(discovered);
  }

  /**
   * Sets discovered to something else.
   *
   * @param discovered
   */
  protected void setDiscovered(HashMap<String, Link> discovered) {
    this.discovered = discovered;
  }

  // You're not allowed to set the newest node though. Makes no sense in
  // implementation,
  // since then the newest node might be different than actual newest node in
  // the
  // 'discovered' HashMap

  /**
   * @return the closestUndiscovered
   */
  protected PriorityQueue<Link> getClosestUndiscovered() {
    return closestUndiscovered;
  }

  /**
   * @param closestUndiscovered
   *          the closestUndiscovered to set
   */
  protected void setClosestUndiscovered(PriorityQueue<Link> closestUndiscovered) {
    this.closestUndiscovered = closestUndiscovered;
  }

  /**
   * @return the ig, note that infoGetter is immutable; it has no private
   *         fields!
   */
  protected infoGetter getIg() {
    return ig;
  }

  /**
   * Adds the neighbors from "newest" into the closestUndiscovered.
   *
   * @throws SQLException
   *           , if the infogetter uses SQL querying and there is an error in
   *           the querying
   */
  private void addNewestNeighbors() throws SQLException {
    double distOfNewest = getDiscovered().get(newest).getDistance();
    // the distance the newest node is from the start node
    for (Link n : getIg().getNeighbors(newest, getDiscovered(), distOfNewest)) {
      getClosestUndiscovered().add(n);
      // Our closestUndiscovered queue already contains all the neighbors of
      // our
      // discovered nodes... except for the newest node just added!
      // So we use our infogetter to retrieve the list of neighbors from
      // this
      // new node only
      // We pass in discovered to 'getNeighbors', which the getNeighbors
      // function uses to make sure
      // none of the neighbors
      // are already in the discovered node list.
      // We pass in distOfNewest; this will allow us to calculate the
      // distance
      // from
      // each of the neighbors to the start node; i.e.
      // (dist of neighbor to start node) = (dist of neighbor to newest) +
      // (dist of newest to start node)
    }
  }

  /**
   * Returns the first VALID element in closestUndiscovered. Helper function for
   * findPaths. i.e. retrieves the head of the queue, the smallest distance of
   * all the eligible nodes.
   *
   * What in particular are 'invalid' elements?
   *
   * First, if the first element is a groupLink, we need to expand it.
   *
   * Also, if the head of the queue points to a node that's already been
   * discovered, that's invalid. for example, suppose we add nodes using
   * Dijkstra in this order: start --> n1 --> n2 --> n3 --> ... (undiscovered as
   * of now) ...
   *
   * if n1 is connected to n3, and n2 is connected to n3, but the path through
   * n2 is closer, we will add n3 to our discovered list as linked to n2.
   *
   * but the link from n1 will still exist in closestUndiscovered, and may, in
   * the future, be the closest and added to the discovered group!
   *
   * We could choose to remove all 'repeated' links from "closestUndiscovered"
   * after every loop in findPaths But that would be at minimum an O(n)
   * operation each time. And it may not actually save too much space.
   *
   * @throws SQLException
   *           , if querying is used in the infogetter and there is an error in
   *           the querying
   */
  protected Link topOption() throws SQLException {
    if (getClosestUndiscovered().isEmpty()) {
      return new Link("", "", -1.0, ""); // we will use the -1.0 to tell us
                                         // there are no more nodes to
                                         // discover!
    } else {
      Link curHead = getClosestUndiscovered().poll();
      // poll also removes the link while retrieving it
      if (curHead instanceof groupLink) { // if it's a groupLink, it's time to
                                          // expand!
        getClosestUndiscovered().addAll(
            getIg().expandGroupLink((groupLink) curHead, getDiscovered()));
      } else { // curHead is a normal Link
        if (!(getDiscovered().containsKey(curHead.getEnd()))) {
          return curHead;
        }
      }
      return topOption(); // recur with updated closestDiscovered
    }
  }

  /**
   * Uses Dijkstra's algorithm to find the closest path to every point in the
   * entire graph... until we reach the end point. Returns all discovered nodes.
   *
   * If the end point doesn't exist, goes until there are no more points to go
   * to.
   *
   * If the end point has already been found, simply returns all the discovered
   * nodes.
   *
   * @param stop
   *          , the node to stop the Dijkstra process at
   * @return a HashMap containing all discovered nodes with their associated
   *         links after performing Dijkstra's algorithm continuously until
   *         reaching the end nodes.
   * @throws SQLException
   *           , if querying is used in the infogetter and there is an error in
   *           the querying
   */
  public HashMap<String, Link> findPaths(String stop) throws SQLException {
    HashMap<String, Link> mapToReturn = new HashMap<String, Link>();
    // This is the HashMap we will return. It is intended to be a copy of the
    // private variable 'discovered'.

    while (!(getDiscovered().containsKey(stop))) {
      // if stop has already been discovered, we need do no more!
      addNewestNeighbors();
      Link newAddition = topOption();
      // the new node to add; see topOption comments
      if (newAddition.getDistance().equals(-1.0)) {
        break; // if there are no valid options
      }
      String neighborName = newAddition.getEnd(); // name of this
                                                  // neighbor
      getDiscovered().put(neighborName, newAddition);
      // updating our Hashmap of discovered nodes

      newest = neighborName;
    }
    mapToReturn.putAll(getDiscovered());
    return mapToReturn;
  }

  /**
   * Helpers getPath by using recursion.
   *
   * We could implement recursion directly in getPath, but we would have to
   * perform findPaths repeatedly. Each attempt (except the first) would be in
   * O(n), however, which is non-ideal.
   *
   * @param stop
   *          , the stop node
   * @param hm
   *          , a hashmap of links which contains all the links from start to
   *          stop
   * @throws SQLException
   *           , if querying is used in the infogetter and there is an error in
   *           the querying
   */
  private ArrayList<Link> getPathHelper(String stop, HashMap<String, Link> hm)
      throws SQLException {
    if (startNode.equals(stop)) { // base case
      ArrayList<Link> listToReturn = new ArrayList<Link>();
      listToReturn.add(hm.get(startNode));
      return listToReturn;
    } else {
      String source = hm.get(stop).getSource();
      ArrayList<Link> listToReturn = getPathHelper(source, hm); // key recursive
      // step
      listToReturn.add(hm.get(stop)); // order is important; make sure we add
      // the last one at the very end!
      return listToReturn;
    }
  }

  /**
   * Returns the list of paths to the 'stop' node. In other words, findPaths
   * includes ALL PATHS found along the way to finding the 'stop' node.
   *
   * getPath filters out those which are not part of the actual path to 'stop'
   * node.
   *
   * @param stop
   *          , the node to stop the Dijkstra process at
   * @return the list of paths to the 'stop' node.
   * @throws SQLException
   *           , if querying is used in the infogetter and there is an error in
   *           the querying
   */
  public ArrayList<Link> getPath(String stop) throws SQLException {
    HashMap<String, Link> pathMap = findPaths(stop);
    if (pathMap.containsKey(stop)) {
      return getPathHelper(stop, pathMap);
    } else {
      // if it doesn't contain stop, that means stop is not in any way
      // connected to start
      return null;
    }
  }
}
