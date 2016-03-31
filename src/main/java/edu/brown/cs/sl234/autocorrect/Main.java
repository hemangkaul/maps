package edu.brown.cs.sl234.autocorrect;

// this is the package we're a part of. We can access all
// other classes in this package

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;

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

import freemarker.template.Configuration;

//packages we're importing

/**
 * The Main class, which will be used to run the program.
 *
 * Takes in one argument, args. When running, first type mvn package into the
 * command prompt and press enter. Then, type "./run [args]" or
 * "./run [args] -gui" the top containing 'StarID', 'ProperName', 'X', 'Y' and
 * 'Z'.
 */
public final class Main {
  /**
   * Your typical main function, this is what is called when the program is ran.
   * If run without the -gui tag, there will be an endless loop in the command
   * prompt, where you can enter inputs of the form 'neighbors [number of stars]
   * [coordinates or star name]' or 'radius [radius length] [coordinates or star
   * name]' If you enter the neighbors command, it will find the closest [number
   * of stars] neighbors from your coordinates or inputted star. If you enter
   * the radius command, it will return all stars within the number given radius
   * length of the inputted star or coordinates. To exit the loop, simply enter
   * nothing, and press enter.
   *
   * @param args
   *          - a CSV file with five columns. The file should have at the top
   *          containing 'StarID', 'ProperName', 'X', 'Y' and 'Z'.
   */
  // A Main.class is NOT required for every Java package. However, ifordList.
  // we want to change the name, for this project we would have to change
  // Main.main(args) in the run file for Maven
  public static void main(String[] args) {
    // All Java packages have a main method.
    // This is what's run when you run the package.
    new Main(args).run();
  }

  // args is the command line argument, returned as an array of strings
  // does not include ./run or ./runCorrect or the actual run text
  private String[]          args;
  // db will be the file that's read in!
  private File              db;
  private ArrayList<String> wordList;
  private Trie              autoCorrectTrie;

  private static final Gson GSON = new Gson();

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    OptionParser parser = new OptionParser();
    // an OptionParser parses through the different options
    // in some list of strings.
    // An option is something that influences the way the package is run
    // For example, -gui will cause the package to run as a Gui file
    // and not just in the command prompt.
    // I believe all options must start with - or --, but am not sure.

    parser.accepts("gui"); // so gui is accepted!
    parser.accepts("led").withOptionalArg().ofType(Integer.class);
    // led can take an optional integer argument
    parser.accepts("prefix");
    parser.accepts("whitespace");
    parser.accepts("smart");
    OptionSpec<File> fileSpec = parser.nonOptions().ofType(File.class);
    // parser.nonOptions() -- returns an object that includes text in the input
    // which isn't accepted as an object
    // ofType specifies what type of text to include -- in this case,
    // only text which resembles a File
    OptionSet options = parser.parse(args); // now we actually parse the
                                            // arguments
    // into the accepted options and their arguments, plus any non-options

    db = options.valueOf(fileSpec); // the file in options
    if (db == null) {
      System.out.println("ERROR: Please specify a valid word list");
      System.exit(1);
    }

    wordList = new ArrayList<String>();

    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(db));
      String line;
      while ((line = br.readLine()) != null) {
        String filteredLine = line.replaceAll("[^a-zA-Z\\s]", " ")
        // replaces all non-alphabet, non-space characters
        // with a whitespace
            .replaceAll("^\\s+", "") // replace any leading white space in the
                                     // line
            .replaceAll("\\s+", " "); // replace any long spaces with a single
                                      // space

        String[] temp = filteredLine.split(" ");
        // Unfortunately, the split function returns an array, not an array list
        for (String item : temp) {
          // Add each of the words to our master Word List; :DDDDD
          wordList.add(item.toLowerCase());
        }
      }
    } catch (FileNotFoundException e) {
      System.out
          .println("ERROR: The file could not be found. Please try again.");
      System.exit(1);
    } catch (NumberFormatException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    autoCorrectTrie = Trie.makeTrie(wordList);
    autoCorrectTrie.condense(); // make into a prefix tree to save space /
                                // time complexity
    if (options.has("gui")) {
      runSparkServer();
    } else {
      System.out.println("Ready");
      Generator g;
      if (options.has("led") && options.hasArgument("led")) {
        g = new Generator(options.has("prefix"),
            options.has("whitespace"), (int) options.valueOf("led"),
            autoCorrectTrie);
      } else {
        g = new Generator(options.has("prefix"),
            options.has("whitespace"), 0, autoCorrectTrie);
      }
      Ranker r = new Ranker(options.has("smart"), autoCorrectTrie,
          wordList);
      autocorrecter(g, r);
    }
  }

  private void autocorrecter(Generator g, Ranker r) {
    BufferedReader brRepl = new BufferedReader(new InputStreamReader(
        System.in));
    String thisLine;
    try {
      while ((thisLine = brRepl.readLine()) != null) {
        if (thisLine.equals("")) {
          break;
        }
        ArrayList<String> topFive = r.topRanked(thisLine, g);
        for (String elem : topFive) {
          System.out.println(elem);
        }
        System.out.println("");
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("Error. Try again");
      autocorrecter(g, r);
    }
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File(
        "src/main/resources/spark/template/freemarker");
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
    Spark.get("/autocorrect", new FrontHandler(), freeMarker);
    Spark.post("/update", new UpdateHandler());
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
   * Whenever the user types a keystroke in the gui, a post request is set. This
   * handles the post request and updates the suggested words.
   *
   * @author sl234
   *
   */
  private class UpdateHandler implements Route {
    public static final int IND_FIVE = 4;
    public static final int IND_FOUR = 3;

    // we need these to avoid Magic Style error

    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      Boolean isPref = Boolean.parseBoolean(qm.value("isPref"));
      Boolean isWhitespace = Boolean.parseBoolean(qm.value("isWS"));
      int led = Integer.parseInt(qm.value("led"));
      String phrase = qm.value("phrase");

      Generator g = new Generator(isPref, isWhitespace, led,
          autoCorrectTrie);
      Ranker r = new Ranker(false, autoCorrectTrie, wordList);

      ArrayList<String> topFive = r.topRanked(phrase, g);

      while (topFive.size() < IND_FIVE + 1) {
        topFive.add("");
      } // make sure we return at five items (no Out of Bound errors)

      Map<String, String> variables = new ImmutableMap.Builder<String, String>()
          .put("first", topFive.get(0)).put("second", topFive.get(1))
          .put("third", topFive.get(2))
          .put("fourth", topFive.get(IND_FOUR))
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
    public static final int STATUS_NUM = 500; // needed to prevent magic number
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
