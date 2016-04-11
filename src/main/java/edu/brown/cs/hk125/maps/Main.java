package edu.brown.cs.hk125.maps;

//this is the package we're a part of. We can access all
//other classes in this package

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.hk125.autocorrect.AutoCorrector;
import edu.brown.cs.hk125.kdtree.KDTree;
import edu.brown.cs.hk125.latlng.LatLng;
import edu.brown.cs.hk125.map.Way;
import freemarker.template.Configuration;

/**
 * The Main class, which will be used to run the program. Can launch a REPL or a
 * Maps web server.
 *
 * To launch the repl, run in the command line: maps maps.sqlite3. Accepted REPL
 * commands:
 *
 * Given two points in the form of
 * "[latitude 1] [longitude 1] [latitude 2] [longitude 2]", gets the best path
 * from the point closest to ([latitude 1], [longitude 1]) and the point closest
 * to ([latitude 2], [longitude 2]).
 *
 * Given two intersections of form: "Street 1" "Cross-street 1" "Street 2"
 * "Cross-street 2", gets the best path from the intersection of Street 1 and
 * Cross-street 1 to the intersection of Street 2 and Cross-street 2
 *
 * To launch the gui, run in the command line: maps --gui maps.sqlite3
 */
public final class Main {

  /**
   * Your typical main function, this is what is called when the program is ran.
   *
   * @param args
   *          args is anything else you enter when calling the program in the
   *          command prompt. i.e. options like -gui, file paths, etc. ./run is
   *          ignored in args, if present
   */
  // A Main.class is NOT required for every Java package. However, if
  // we want to change "Main" to something else for this project, we would have
  // to change
  // Main.main(args) in the run file for Maven
  public static void main(String[] args) {
    // All Java packages which can run have a main method.
    // This is what's run when you run the package.
    new Main(args).run();
  }

  // args is the command line argument, returned as an array of strings
  // does not include ./run or ./runCorrect if present
  private String[] args;

  private String db;

  private MapsInfoGetter ig;

  private AutoCorrector mapsAC;

  private static final Gson GSON = new Gson();

  // GSOn used to handle Json translations between backend / frontend

  private Main(String[] args) {
    this.args = args;
  }

  /**
   * This is what is run in the command line.
   */
  private void run() {
    OptionParser parser = new OptionParser();
    // an OptionParser parses through the different options
    // in some list of strings.
    // An option is something that influences the way the package is run
    // For example, -gui will cause the package to run as a Gui file
    // and not just in the command prompt.
    // I believe all options must start with - or --, but am not sure.
    // accepts a port as well!
    // now we parse the
    // arguments
    // into the accepted options and their arguments, plus any non-options

    parser.accepts("gui"); // so gui is accepted!
    parser.accepts("port").withOptionalArg().ofType(Integer.class);
    OptionSpec<File> database = parser.nonOptions().ofType(File.class);
    OptionSet options = parser.parse(args);

    // get the value of the database
    db = options.valueOf(database).toString();

    // Set it to null in case if we don't there's an error
    ig = null;
    KDTree<LatLng> tree = null;

    try {
      ig = new MapsInfoGetter(db);
      List<LatLng> elementList = ig.getLatLngList();
      tree = new KDTree<>(elementList);
      mapsAC = ig.getMapsAutoCorrector();

      if (options.has("gui")) {

        if (options.has("port") && options.hasArgument("port")) {

          // set the port if necessary
          int portNum = (int) options.valueOf("port");
          Spark.setPort(portNum);

        }

        ig.setTiles();

        runSparkServer();

      } else {

        String command;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Ready");

        while ((command = br.readLine()) != null) {
          // String[] commands = command.split(" ");
          // LatLng latlng = new LatLng(Double.parseDouble(commands[0]),
          // Double.parseDouble(commands[1]));
          //
          // System.out.println(tree.findNN(latlng));

          // AutoCorrector ac = ig.getAutoCorrector();
          //
          // System.out.println(ac.suggestions(command));
          ReadEvaluatePrintLoop.execute(command, tree, ig);
          System.out.println("Ready");
        }
      }

    } catch (SQLException e) {
      System.out.println("ERROR: SQL exception: " + e);
      System.exit(1);
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: ClassNotFound Exception: " + e);
      System.exit(1);
    } catch (IOException e) {
      System.out.println("ERROR: IOException: " + e);
      System.exit(1);
    } catch (IllegalArgumentException e) {
      System.out.println("ERROR: IllegalArgumentException: " + e);
      System.exit(1);
    } catch (NoSuchElementException e) {
      System.out.println("ERROR: NoSuchElementException: " + e);
      System.exit(1);
    }
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.\n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer() {
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/home", new FrontHandler(), freeMarker);
    Spark.post("/autocorrect", new AutoCorrectHandler());

    Spark.post("/tile", new TileHandler());

  }

  /**
   * Builds the initial html for the gui.
   *
   * @author sl234
   *
   */
  private static class FrontHandler implements TemplateViewRoute {
    // @Override[INFO] Finished at: 2016-04-11T00:49:58-04:00

    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Maps");
      return new ModelAndView(variables, "main.ftl");
    }
  }

  /**
   * Tile Handler gets a tile from the back end cache, given a Lat Lng point
   * from the front end.
   *
   * @author sl234
   *
   */
  private class TileHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      Double lat = Double.valueOf(qm.value("lat"));
      Double lng = Double.valueOf(qm.value("lng"));

      Tile tileToReturn = null;
      try {
        tileToReturn = ig.getTile(lat, lng);
      } catch (NoSuchElementException | SQLException e) {
        System.out.println("ERROR: SQLException: " + e);
      }

      // This is the list of ways contained within the tile
      List<Way> wayList = tileToReturn.getTileWays();

      // Here we convert the list of ways from LatLng format to individual
      // latitude / longitude elements
      Map<Map<Double, Double>, Map<Double, Double>> ways = new HashMap<>();
      //
      // System.out.println("aight");

      // iterate through wayList and add converted ways to ways
      for (Way way : wayList) {
        Map<Double, Double> startMap = new HashMap<>();
        Map<Double, Double> endMap = new HashMap<>();
        startMap.put(way.getStartLatitude(), way.getStartLongitude());
        endMap.put(way.getEndLatitude(), way.getEndLongitude());

        ways.put(startMap, endMap);
      }

      // System.out.println("close");

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("l", Double.toString(tileToReturn.getLlng()))
          .put("r", Double.toString(tileToReturn.getRlng()))
          .put("t", Double.toString(tileToReturn.getTlat()))
          .put("b", Double.toString(tileToReturn.getBlat())).put("ways", ways)
          .build();

      System.out.println("okie");
      return GSON.toJson(variables);

    }
  }

  /**
   * Whenever the user types a keystroke in the gui, a post request is set. This
   * handles the post request and updates the suggested words.
   *
   * @author sl234
   *
   */
  private class AutoCorrectHandler implements Route {
    public static final int IND_FIVE = 4;
    public static final int IND_FOUR = 3;

    // we need these to avoid Magic Style error

    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      String street = qm.value("street"); // name of the edited street

      // Generator g = new Generator(true, true, 2, actorTrie);
      // we active WhiteSpace, Prefix, and Led (up to distance of 2) for
      // generating

      // Ranker r = new Ranker(false, actorTrie, (ArrayList<String>) actorList);

      // We will use whiteSpace, prefix, and Levenshtein (up to distance of 2)
      // to generate suggestions

      List<String> topFive = mapsAC.suggestions(street);

      // We don't want suggestions (which will be produced by Levenshtein) if
      // the entry is blank!
      if (!(street.equals(""))) {
        topFive = mapsAC.suggestions(street);
      } else {
        topFive = new ArrayList<>();
      }

      // if either of the entries are blank, we don't want to print out
      // any led suggestions

      while (topFive.size() < IND_FIVE + 1) {
        topFive.add("");
      } // make sure we return at five items (no Out of Bound errors)

      Map<String, String> variables = new ImmutableMap.Builder<String, String>()
          .put("first", topFive.get(0)).put("second", topFive.get(1))
          .put("third", topFive.get(2)).put("fourth", topFive.get(IND_FOUR))
          .put("fifth", topFive.get(IND_FIVE)).build();

      return GSON.toJson(variables);
    }
  }

  /**
   *
   * Exception Printer for the Spark Engine.
   *
   * @author sl234
   *
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    public static final int STATUS_NUM = 500; // needed to prevent magic
                                              // number
                                              // error

    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(STATUS_NUM);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
