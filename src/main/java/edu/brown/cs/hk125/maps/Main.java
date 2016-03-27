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
import java.util.Map;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.hk125.dijkstra.AStar;
import edu.brown.cs.hk125.kdtree.KDTree;
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

    parser.accepts("gui"); // so gui is accepted!
    parser.accepts("port").withOptionalArg().ofType(Integer.class);
    // accepts a port as well!
    OptionSet options = parser.parse(args); // now we actually parse the
                                            // arguments
    // into the accepted options and their arguments, plus any non-options

    if (args.length > 2) {
      System.out.println("ERROR: Illegal number of arguments");
    } else if (args.length == 2) {
      if (args[0].contains("gui")) {
        db = args[1];
      } else {
        db = args[0];
      }
    }

    MapsInfoGetter ig = null;
    AStar dj = null;

    try {
      ig = new MapsInfoGetter(db);
      dj = new AStar("", ig);
    } catch (SQLException e) {
      System.out.println("ERROR: SQL exception: " + e);
      System.exit(1);
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: ClassNotFound Exception: " + e);
      System.exit(1);
    }

    if (options.has("gui")) {
      if (options.has("port") && options.hasArgument("port")) {
        // set the port if necessary
        int portNum = (int) options.valueOf("port");
        Spark.setPort(portNum);
      }
      runSparkServer();
    } else {
      String command;
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      // TEMP ***************
      KDTree tree = new KDTree(new ArrayList<>());

      try {
        while ((command = br.readLine()) != null) {
          ReadEvaluatePrintLoop.execute(command, tree);
        }
      } catch (IOException e) {
        System.out.println("ERROR: IOException: " + e);
        System.exit(1);
      } catch (IllegalArgumentException e) {
        System.out.println("ERROR: IllegalArgumentException: " + e);
        System.exit(1);
      }

      // ********************
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
  }

  /**
   * Builds the initial html for the gui.
   *
   * @author sl234
   *
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of();
      return new ModelAndView(variables, "main.ftl");
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
