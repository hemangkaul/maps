package edu.brown.cs.hk125.trie;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

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

  // TODO add test methods here.
  // The methods must be annotated with annotation @Test. For example:
  //
  // @Test
  // public void hello() {}

  @Test
  public void makeTrieEmptyTest() {
    Trie trie = new Trie(new ArrayList<String>(), true);
    Multiset<String> unigrams = trie.getUnigrams();
    Multiset<Bigram> bigrams = trie.getBigrams();
    assertTrue(unigrams.isEmpty());
    assertTrue(bigrams.isEmpty());
  }
}