package edu.brown.cs.sl234.autocorrect;

import java.util.HashSet;
import java.util.Set;

/**
 * Generates a list of suggestions for a entered phrase or word, based on a
 * given Trie as a wordList.
 *
 * @author sl234
 *
 */
public class Generator {

  private Boolean isPrefix;
  private Boolean isWhitespace;
  private int     led;         // set to 0 if Levenshtein suggestions aren't
                                // asked for
  private Trie    trie;

  /**
   * Returns a Generator with sorting properties (prefix, whitespace, led, etc.)
   * that will be able to generate suggestions for inputed words.
   *
   * @param isPrefix
   *          , whether or not to activate prefix search
   * @param isWhitespace
   *          , whether or not to activate whitespace search
   * @param led
   *          , whether or not to activate Levenshtein search. If 0 or less,
   *          Levenshtein is inactive; otherwise it will do levenshtein search
   *          on the given distance
   * @param trie
   *          , the trie for the Generator to access for information
   */
  public Generator(Boolean isPrefix, Boolean isWhitespace, int led,
      Trie trie) {
    this.isPrefix = isPrefix;
    this.isWhitespace = isWhitespace;
    this.led = Math.max(0, led); // 0 if the Led is negative or 0...
    this.trie = trie;
  }

  /**
   * @return the isPrefix
   */
  public Boolean getIsPrefix() {
    return isPrefix;
  }

  /**
   * @param isPrefix
   *          the isPrefix to set
   */
  public void setIsPrefix(Boolean isPrefix) {
    this.isPrefix = isPrefix;
  }

  /**
   * @return led
   */
  public int getLed() {
    return led;
  }

  /**
   * @param led
   *          the isLed to set
   */
  public void setLed(int led) {
    this.led = led;
  }

  /**
   * @return the isWhitespace
   */
  public Boolean getIsWhitespace() {
    return isWhitespace;
  }

  /**
   * @param isWhitespace
   *          the isWhitespace to set
   */
  public void setIsWhitespace(Boolean isWhitespace) {
    this.isWhitespace = isWhitespace;
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
  public void setTrie(Trie trie) {
    this.trie = trie;
  }

  /**
   * Generates a list of suggestions for the phrase, based on the given
   * specifications of the Generator.
   *
   * (Specifically values for isPrefix, isLed, and isWhiteSpace
   *
   * @param word
   *          , a word (not a phrase) to be autocorrected
   *
   * @return a set of suggestions for the phrase, based on the given
   *         specifications of the Generator.
   */
  public Set<String> generate(String word) {
    Set<String> masterSet = new HashSet<String>();
    // This is going to be the set of all generated words.
    // We use a set, because it's possible some Levenshtein and Prefix
    // suggestions will be
    // duplicates. Sets remove duplicates.

    // default behavior: if prefix, led, and whitespace are all turned off,
    // we still want to be able to suggest exact match words
    if (trie.inTrie(word)) {
      masterSet.add(word);
    }

    if (isPrefix) {
      masterSet.addAll(trie.prefixSuggestions(word));
    }

    if (led != 0) { // led is either 0 or positive
      masterSet.addAll(trie.levSuggestions(word, led));
    }

    if (isWhitespace) {
      masterSet.addAll(trie.whiteSpaceSuggestions(word));
    }
    return masterSet;
  }
}
