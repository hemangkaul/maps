package edu.brown.cs.hk125.autocorrect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Multiset;

import edu.brown.cs.hk125.trie.Bigram;

/**
 * Ranker ranks a list of words for the autocorrect program according to either
 * the Bigram Unigram algorithm or a smart algorithm.
 *
 * @author sl234
 *
 */
public class Ranker {

  /**
   * a set of the unigrams.
   */
  private Multiset<String> unigrams;

  /**
   * a set of the bigrams.
   */
  private Multiset<Bigram> bigrams;

  /**
   * a set of the matches.
   */
  private Multiset<String> matchList;

  /**
   * the previous word.
   */
  private String previous;

  /**
   * the max matches
   */
  private static final int MAX = 10;

  /**
   * the constructor for Ranker.
   *
   * @param unis
   *          the set of unigrams
   * @param bis
   *          the set of bigrams
   * @param matches
   *          the set of matches
   * @param prev
   *          the previous string
   */
  public Ranker(Multiset<String> unis, Multiset<Bigram> bis,
      Multiset<String> matches, String prev) {
    this.unigrams = unis;
    this.bigrams = bis;
    this.matchList = matches;
    this.previous = prev;
  }

  public Ranker(Multiset<String> unis, Multiset<String> matches) {
    this.unigrams = unis;
    this.bigrams = null;
    this.matchList = matches;
    this.previous = null;
  }

  /**
   * results ranks the results.
   *
   * @return a list of the results ranked
   */
  public List<String> results() {
    GramComparator gc = new GramComparator(unigrams, bigrams, previous);
    Set<String> sorted = new HashSet<>();
    sorted.addAll(matchList);
    List<String> sorted2 = new ArrayList<>();
    sorted2.addAll(sorted);
    sorted2.sort(gc);
    if (sorted.size() > MAX) {
      return sorted2.subList(0, MAX);
    } else {
      return sorted2;
    }
  }
}
