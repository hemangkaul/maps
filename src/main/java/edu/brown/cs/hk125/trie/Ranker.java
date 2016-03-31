package edu.brown.cs.hk125.trie;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Multiset;

/**
 * Ranker ranks a list of words for the autocorrect program according to either
 * the Bigram Unigram algorithm or a smart algorithm.
 *
 * @author sl234
 *
 */
public class Ranker {
  private Multiset<String> unigrams;
  private Multiset<Bigram> bigrams;
  private Multiset<String> matchList;
  private String previous;

  private static final int TOTAL = 10;

  public Ranker(Multiset<String> unigrams, Multiset<Bigram> bigrams,
      Multiset<String> matchList, String previous) {
    this.unigrams = unigrams;
    this.bigrams = bigrams;
    this.matchList = matchList;
    this.previous = previous;
  }

  public List<String> results() {
    GramComparator gc = new GramComparator(unigrams, bigrams, previous);
    Set<String> sorted = new HashSet<>();
    sorted.addAll(matchList);
    List<String> sorted2 = new ArrayList<>();
    sorted2.addAll(sorted);
    sorted2.sort(gc);
    if (sorted.size() > TOTAL) {
      return sorted2.subList(0, TOTAL);
    } else {
      return sorted2;
    }
  }
}
