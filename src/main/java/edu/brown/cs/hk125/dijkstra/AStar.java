package edu.brown.cs.hk125.dijkstra;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Performs A* search, an extension of Dijkstra's algorithm.
 *
 * @author sl234
 *
 */
public class AStar extends Dijkstra {

  /**
   * Constructs an AStar. Note that the infoGetter must be an InfoGetterAStar
   * (an extension of infoGetter with additional methods).
   *
   * @param startNode
   *          , the node which the Dijkstra process shall begin with.
   * @param ig
   *          , the infoGetter used to retrieve information, primarily the list
   *          of neighbors for a node.
   * @throws IllegalArgumentException
   *           , if the startNode cannot be found by the infoGetter! 'Doh!
   * @throws SQLException
   *           , Dijkstra declaration makes sure that the startNode is in the
   *           infoGetter; if that process requires querying, an error may be
   *           produced here.
   */
  public AStar(String startNode, InfoGetterAStar ig)
      throws IllegalArgumentException, SQLException {
    super(startNode, ig);
  }

  /**
   * Adds the neighbors from "newest" into the closestUndiscovered.
   *
   * The distances of these neighbors is adjusted to account for A* search
   *
   * @throws SQLException
   *           , if the infogetter uses SQL querying and there is an error in
   *           the querying
   * @throws IllegalArgumentException
   *           , if the stop node isn't in the database
   */
  private void addNewestNeighborsAStar(HashMap<String, Link> discovered,
      String endNode) throws SQLException, IllegalArgumentException {
    String newest = getNewest();
    PriorityQueue<Link> closestUndiscovered = getClosestUndiscovered();
    InfoGetterAStar ig = (InfoGetterAStar) getIg();
    double distOfNewest = discovered.get(newest).getDistance();
    // the distance the newest node is from the start node
    for (Link n : ig.getNeighborsAStar(newest, endNode, discovered,
        distOfNewest)) {
      closestUndiscovered.add(n);
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
    setClosestUndiscovered(closestUndiscovered);
  }

  /**
   * Uses A Star Algorithm to find the closest path to every point in the entire
   * graph... until we reach the end point. Returns all discovered nodes.
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
   * @throws IllegalArgumentException
   *           , if the stopNode is not in the database
   */
  @Override
  public HashMap<String, Link> findPaths(String stop) throws SQLException,
      IllegalArgumentException {

    HashMap<String, Link> discovered = getDiscovered();
    // A copy of our current HashMap of discovered nodes

    while (!(discovered.containsKey(stop))) {
      // if stop has already been discovered, we need do no more!
      addNewestNeighborsAStar(discovered, stop);
      Link newAddition = topOption();
      // the new node to add; see topOption comments
      if (newAddition.getDistance().equals(-1.0)) {
        break; // if there are no valid options
      }
      String neighborName = newAddition.getEnd(); // name of this
                                                  // neighbor

      // When we add a node to our discovered group, we remove the heuristic
      // distance.
      // This is for two reasons. First, it makes it easier to do calculations
      // of distances of future neighbors, as:
      //
      // [Dist. of Neighbor] = [Dist. from start to source] + [Dist. from source
      // to neighbor] + [Dist. from neighbor to end]
      //
      // By removing the heuristic distance, when we search this node's
      // neighbors in the future,
      // we will be able to use the node's distance as [Dist. from start to
      // source]; otherwise
      // we would have to remove the heuristic distance at THAT time, which is
      // annoying.

      // The second reason is because it makes sense practically: if we run a
      // big Dijkstra search,
      // we can ask our Dijkstra program for the distances to any of the nodes
      // we've found
      // and get the actual distances this way, as opposed to getting a skewed
      // version of the actual distance.
      Double newDistance = newAddition.getDistance()
          - ((InfoGetterAStar) getIg()).heuristicValue(neighborName, stop);
      // updating our Hashmap of discovered nodes
      // note we round our distance to six decimals, in case there are any weird
      // symptoms of adding and subtracting a double
      discovered.put(neighborName, new Link(newAddition.getSource(),
          neighborName, Math.round(newDistance * 100000.0) / 100000.0,
          newAddition.getName()));
      setNewest(neighborName);
    }
    setDiscovered(discovered); // updating the field in the parent class
    return discovered;
  }
}
