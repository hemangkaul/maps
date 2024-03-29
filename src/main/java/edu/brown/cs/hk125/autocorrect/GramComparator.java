package edu.brown.cs.hk125.autocorrect;

import java.io.Serializable;
import java.util.Comparator;

import com.google.common.collect.Multiset;

import edu.brown.cs.hk125.trie.Bigram;

/**
 * GramComparator compares grams.
 *
 * @author hk125
 *
 */
public class GramComparator implements Comparator<String>, Serializable {

  /**
   * default serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * the set of unigrams.
   */
  private Multiset<String> unigrams;

  /**
   * the set of bigrams.
   */
  private Multiset<Bigram> bigrams;

  /**
   * the previous word.
   */
  private String previous;

  /**
   * the constructor for GramComparator.
   *
   * @param unis
   *          the set of unigrams
   * @param bis
   *          the set of bigrams
   * @param prev
   *          the previous string
   */
  public GramComparator(Multiset<String> unis, Multiset<Bigram> bis, String prev) {
    this.unigrams = unis;
    this.bigrams = bis;
    this.previous = prev;
  }

  @Override
  public int compare(String s1, String s2) {
    if (previous != null && bigrams != null) {
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

  /**
   * a helper function for the compare method.
   *
   * @param s1
   *          the first string to be compared
   * @param s2
   *          the second string to be compared
   * @return positive if a word has occurred less than, negative if more than,
   *         and the lexigraphical difference if else
   */
  public int compareHelp(String s1, String s2) {
    if (bigrams != null) {
      if (s1.contains(" ")) {
        s1 = s1.split(" ")[0];
      }
      if (s2.contains(" ")) {
        s2 = s2.split(" ")[0];
      }
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
