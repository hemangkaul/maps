package edu.brown.cs.hk125.maps;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.hk125.dijkstra.AStar;
import edu.brown.cs.hk125.dijkstra.Dijkstra;
import edu.brown.cs.hk125.dijkstra.Link;
import edu.brown.cs.hk125.kdtree.KDTree;
import edu.brown.cs.hk125.latlng.LatLng;

/**
 * The ReadEvaluatePrintLoopClass evaluates and prints the read input, allowing
 * it to loop.
 *
 * @author hk125
 *
 */
class ReadEvaluatePrintLoop {

  /**
   * Execute is a static method which evaluates and prints the results.
   *
   * @param input
   *          is the command the user inputs
   * @param tree
   *          is the tree
   * @param ig
   *          the infogetter for the REPL
   * @throws IllegalArgumentException
   *           if the argument is illegal
   * @throws NumberFormatException
   *           if the user gives an argument that is not a number
   * @throws SQLException
   *           , if querying is used and there is an error with the query
   */
  public static void execute(String input, KDTree<LatLng> tree,
      MapsInfoGetter ig) throws SQLException {
    String command = input.trim();
    List<String> commands = new ArrayList<>();
    boolean latlng = false;
    if (command.startsWith("\"")) {

      String[] splitQuotes = command.split("\"");
      for (String quote : splitQuotes) {
        if (!quote.matches("[ ]+") && !quote.matches("")) {
          commands.add(quote);
          System.out.println(quote);
        }
      }
      if (commands.size() != 4) {
        System.out.println(commands.size());
        throw new IllegalArgumentException("Wrong number of arguments!");
      }
    } else {
      String[] splitSpaces = command.split(" ");
      if (splitSpaces.length != 4) {
        throw new IllegalArgumentException("Wrong number of arguments!");
      }
      for (String number : splitSpaces) {
        commands.add(number);
      }
      latlng = true;
    }
    if (latlng) {
      // the inputed lat/lon values
      Double lat1 = Double.parseDouble(commands.get(0));
      Double lng1 = Double.parseDouble(commands.get(1));
      Double lat2 = Double.parseDouble(commands.get(2));
      Double lng2 = Double.parseDouble(commands.get(3));

      // making LatLng objects that we use in the nearestNeighbor function
      LatLng source = new LatLng(lat1, lng1, "");
      LatLng target = new LatLng(lat2, lng2, "");

      // finding the nearest neighbors!
      LatLng sourcePoint = tree.findNN(source);
      LatLng targetPoint = tree.findNN(target);

      // the ID's of the nearest neighbors, used in the AStar search
      String startNode = sourcePoint.getID();
      String endNode = targetPoint.getID();

      AStar maps = new AStar(startNode, ig);

      List<Link> path = maps.getPath(endNode);
      List<Link> pathWithoutFirst = path.subList(1, path.size());

      printResults(pathWithoutFirst);

    } else {
      // given street names, not lat/lon values
      String startNode = ig.getIntersection(commands.get(0),
          commands.get(1));
      String endNode = ig
          .getIntersection(commands.get(2), commands.get(3));

      Dijkstra maps = new AStar(startNode, ig);

      List<Link> path = maps.getPath(endNode);
      List<Link> pathWithoutFirst = path.subList(1, path.size());
      if (pathWithoutFirst.isEmpty()) {
        System.out.println(startNode + " -/- " + endNode);
      } else {
        printResults(pathWithoutFirst);
      }
    }
  }

  /**
   * Prints the results if there are any.
   *
   * @param results
   *          the results of the repl.
   */
  private static void printResults(List<Link> results) {
    // we don't want to print out the first element of the path,
    // since it's just the start node to itself

    for (Link l : results) {
      System.out.println(l.getSource() + " -> " + l.getEnd() + " : "
          + l.getName());
    }
  }
}
