package edu.brown.cs.hk125.autocorrect;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Multiset;

import edu.brown.cs.hk125.trie.Trie;

public class AutoCorrectorTest {

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

  class TestAutoCorrector implements AutoCorrector {

    /**
     * the trie.
     */
    private Trie t;

    /**
     * the generator.
     */
    private Generator g;

    /**
     * constructor for AutoCorrector.
     *
     * @param trie
     *          the trie to use for autocorrecting
     */
    public TestAutoCorrector(Trie trie) {
      this.t = trie;
      this.g = new Generator(trie);
    }

    /**
     * generate suggestions from the trie.
     *
     * @param input
     *          the input to be autocorrected
     * @return a list of the best corrections
     */
    @Override
    public List<String> suggestions(String input) {
      input = input.toLowerCase();

      Multiset<String> matches = g.generate(input);

      Ranker r = new Ranker(t.getUnigrams(), matches);

      return r.results();
    }
  }

  @Test
  public void noSuggestionTest() {
    List<String> words = new ArrayList<>();
    words.add("artichoke");
    words.add("beets");
    words.add("carrots");
    words.add("donuts");
    Trie trie = new Trie(words, true);
    TestAutoCorrector testAC = new TestAutoCorrector(trie);
    assertTrue(testAC.suggestions("hemang").isEmpty());
  }

  @Test
  public void oneSuggestionTest() {
    List<String> words = new ArrayList<>();
    words.add("artichoke");
    words.add("beets");
    words.add("carrots");
    words.add("donuts");
    Trie trie = new Trie(words, true);
    TestAutoCorrector testAC = new TestAutoCorrector(trie);
    List<String> artichoke = new ArrayList<>();
    artichoke.add("artichoke");
    assertTrue(artichoke.equals(testAC.suggestions("articoke")));
  }

  @Test
  public void multipleSuggestionTest() {
    List<String> words = new ArrayList<>();
    words.add("market");
    words.add("marker");
    words.add("mark");
    Trie trie = new Trie(words, true);
    TestAutoCorrector testAC = new TestAutoCorrector(trie);
    assertTrue(words.containsAll(testAC.suggestions("marke")));
  }

  @Test
  public void moreThanTenSuggestionTest() {
    List<String> words = new ArrayList<>();
    words.add("hello");
    words.add("halo");
    words.add("hope");
    words.add("hopes");
    words.add("hole");
    words.add("holes");
    words.add("holy");
    words.add("hopeless");
    words.add("hurt"); // should be first because added twice
    words.add("hurt");
    words.add("hurts");
    words.add("hey"); // should prefer to hurts because e is before u
    words.add("hell");
    Trie trie = new Trie(words, true);
    TestAutoCorrector testAC = new TestAutoCorrector(trie);
    // [hurt, halo, hell, hello, hey, hole, holes, holy, hope, hopeless]
    List<String> correct = new ArrayList<>();
    correct.add("hurt");
    correct.add("halo");
    correct.add("hell");
    correct.add("hello");
    correct.add("hey");
    correct.add("hole");
    correct.add("holes");
    correct.add("holy");
    correct.add("hope");
    correct.add("hopeless");
    assertTrue(correct.containsAll(testAC.suggestions("h")));
    assertTrue(correct.removeAll(testAC.suggestions("h")));
    assertTrue(correct.isEmpty());

  }
}
