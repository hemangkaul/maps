package edu.brown.cs.hk125.autocorrect;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.cs.hk125.trie.Trie;

public class PrefixMatchingTest {
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
  public void prefixMatchingTest() {
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
    List<String> prefixes = PrefixMatching.prefix("ho", trie);
    assertTrue(words.containsAll(prefixes));
    assertTrue(words.removeAll(prefixes));
    List<String> notIn = new ArrayList<>();
    notIn.add("distinguish");
    notIn.add("cops");
    notIn.add("cope");
    assertTrue(words.containsAll(notIn));
    assertTrue(words.removeAll(notIn));
    assertTrue(words.isEmpty());
  }
}
