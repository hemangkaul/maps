package edu.brown.cs.hk125.trie;

import java.util.Comparator;

import com.google.common.collect.Multiset;

public class GramComparator implements Comparator<String> {

  private Multiset<String> unigrams;
  private Multiset<Bigram> bigrams;
  private String previous;

  public GramComparator(Multiset<String> unigrams, Multiset<Bigram> bigrams,
      String previous) {
    this.unigrams = unigrams;
    this.bigrams = bigrams;
    this.previous = previous;
  }

  @Override
  public int compare(String s1, String s2) {
    if (previous != null) {
      Bigram one = new Bigram(previous, s1);
      Bigram two = new Bigram(previous, s2);
      int oneCount = bigrams.count(one);
      int twoCount = bigrams.count(two);
      if (oneCount > twoCount) {
        return -1;
      }
      if (oneCount < twoCount) {
        return 1;
      }
    }
    return compareHelp(s1, s2);
  }

  public int compareHelp(String s1, String s2) {
    if (s1.contains(" ")) {
      s1 = s1.split(" ")[0];
    }
    if (s2.contains(" ")) {
      s2 = s2.split(" ")[0];
    }
    int oneCount = unigrams.count(s1);
    int twoCount = unigrams.count(s2);
    if (oneCount > twoCount) {
      return -1;
    }
    if (oneCount < twoCount) {
      return 1;
    }
    return s1.compareTo(s2);
  }
}
