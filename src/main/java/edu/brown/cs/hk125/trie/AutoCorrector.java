package edu.brown.cs.hk125.trie;

import java.util.List;

import com.google.common.collect.Multiset;

public class AutoCorrector {

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
  public AutoCorrector(Trie trie) {
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
  public List<String> suggestions(String input) {
    input = input.toLowerCase();
    String[] words = input.split("\\P{L}+");

    String word = null;

    if (words.length > 0) {
      word = words[words.length - 1];
    }

    String previous = null;

    if (words.length > 1) {
      previous = words[words.length - 2];
    }

    Multiset<String> matches = g.generate(word);

    Ranker r = new Ranker(t.getUnigrams(), t.getBigrams(), matches, previous);

    return r.results();
  }
}
