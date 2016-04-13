package edu.brown.cs.hk125.trie;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Multiset;

public class TrieTest {

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
  public void makeTrieEmptyTest() {
    Trie trie = new Trie(new ArrayList<String>(), true);
    Multiset<String> unigrams = trie.getUnigrams();
    Multiset<Bigram> bigrams = trie.getBigrams();
    assertTrue(unigrams.isEmpty());
    assertTrue(bigrams.isEmpty());
  }

  @Test
  public void makeTrieSingle() {
    List<String> words = new ArrayList<>();
    words.add("artichoke");
    words.add("beets");
    words.add("carrots");
    words.add("donuts");
    Trie trie = new Trie(words, true);
    assertTrue(trie.getUnigrams().containsAll(words));
    assertTrue(trie.getUnigrams().removeAll(words));
    assertTrue(trie.getUnigrams().isEmpty());
  }

  @Test
  public void makeTrieNotSingle() {
    List<String> words = new ArrayList<>();
    words.add("I do the work here");
    words.add("work here");
    words.add("work here");
    Bigram bigram1 = new Bigram("i", "do");
    Bigram bigram2 = new Bigram("do", "the");
    Bigram bigram3 = new Bigram("the", "work");
    Bigram bigram4 = new Bigram("work", "here");

    List<String> correct = new ArrayList<>();
    correct.add("i");
    correct.add("do");
    correct.add("the");
    correct.add("work");
    correct.add("here");
    Trie trie = new Trie(words, false);
    assertTrue(trie.getUnigrams().removeAll(correct));
    assertTrue(trie.getUnigrams().isEmpty());
    assertTrue(trie.getBigrams().remove(bigram1));
    assertTrue(trie.getBigrams().remove(bigram2));
    assertTrue(trie.getBigrams().remove(bigram3));
    assertTrue(trie.getBigrams().remove(bigram4));
    assertTrue(trie.getBigrams().remove(bigram4));
    assertTrue(trie.getBigrams().remove(bigram4));
    assertTrue(trie.getBigrams().isEmpty());
  }

  @Test
  public void getNodeTest() {
    List<String> words = new ArrayList<>();
    words.add("artichoke");
    words.add("beets");
    words.add("carrots");
    words.add("donuts");
    Trie trie = new Trie(words, true);
    Node shouldEqual = new Node('e', true);
    Node shouldEqual2 = new Node('s', true);
    assertTrue(shouldEqual.equals(trie.getNode("artichoke")));
    assertTrue(shouldEqual2.equals(trie.getNode("beets")));
    assertTrue(shouldEqual2.equals(trie.getNode("carrots")));
    assertTrue(shouldEqual2.equals(trie.getNode("donuts")));
  }
}