package edu.brown.cs.hk125.autocorrect;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.cs.hk125.trie.Trie;

public class WhitespaceTest {
  @BeforeClass
  public static void setUpClass() throws Exception {
    // (Optional) Code to run before any tests begin goes here.
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    // (Optional) Code to run after all tests finish goes here.
  }

  @Before
  public void setUp() {
    // (Optional) Code to run before each test case goes here.
  }

  @After
  public void tearDown() {
    // (Optional) Code to run after each test case goes here.
  }

  @Test
  public void whiteSpaceTest() {
    List<String> words = new ArrayList<>();
    words.add("hope");
    words.add("hopes");
    words.add("holy");
    words.add("hola");
    words.add("cops");
    words.add("cope");
    words.add("hole");
    words.add("holes");
    words.add("distinguish");
    Trie trie = new Trie(words, true);
    Set<String> whiteSpaced = Whitespace.whitespace("holacops", trie);
    Set<String> noWhiteSpaces = Whitespace.whitespace("indistinguishable",
        trie);
    System.out.println(whiteSpaced);
    assertTrue(whiteSpaced.remove("hola cops"));
    assertTrue(whiteSpaced.isEmpty());
    assertTrue(noWhiteSpaces.isEmpty());
  }
}
