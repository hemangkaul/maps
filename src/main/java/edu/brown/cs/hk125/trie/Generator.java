package edu.brown.cs.hk125.trie;

import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/**
 * Generates a list of suggestions for a entered phrase or word, based on a
 * given Trie as a wordList.
 *
 * @author sl234
 *
 */
public class Generator {

  /**
   * edit distance set to 2.
   */
  private static final int LED = 2;

  /**
   * the trie being used for generating suggestions.
   */
  private Trie trie;

  /**
   * Returns a Generator with sorting properties (prefix, whitespace, led, etc.)
   * that will be able to generate suggestions for inputed words.
   *
   * @param trie
   *          , the trie for the Generator to access for information
   */
  public Generator(Trie inputTrie) {
    this.trie = inputTrie;
  }

  /**
   * @return the trie
   */
  public Trie getTrie() {
    return trie;
  }

  /**
   * @param trie
   *          the trie to setimport com.google.common.collect.Multimap; import
   *          com.google.common.collect.TreeMultimap;
   */
  public void setTrie(Trie inputTrie) {
    this.trie = inputTrie;
  }

  /**
   * Generates a list of suggestions for the phrase, based on the given
   * specifications of the Generator.
   *
   *
   *
   * @param word
   *          , a word (not a phrase) to be autocorrected
   *
   * @return a set of suggestions for the phrase, based on the given
   *         specifications of the Generator.
   */
  public Multiset<String> generate(String word) {
    Multiset<String> matched = HashMultiset.create();
    // This is going to be the set of all generated words.
    // We use a set, because it's possible some Levenshtein and Prefix
    // suggestions will be
    // duplicates. Sets remove duplicates.

    Set<String> ledMatched = LevenshteinEditDistance.levenshtein(word, trie,
        LED);
    matched.addAll(ledMatched);

    List<String> prefixMatched = PrefixMatching.prefix(word, trie);
    matched.addAll(prefixMatched);

    Set<String> whitespaceMatched = Whitespace.whitespace(word, trie);
    matched.addAll(whitespaceMatched);

    return matched;
  }
}
