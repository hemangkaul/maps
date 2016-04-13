package edu.brown.cs.hk125.autocorrect;

import java.util.HashSet;
import java.util.Set;

import edu.brown.cs.hk125.trie.Node;
import edu.brown.cs.hk125.trie.Trie;

/**
 * Represents the whitespace algorithm.
 *
 * @author hk125
 *
 */
class Whitespace {
  /**
   * Generates a list of suggested words under the presumption that the user
   * forgot to type a space. Returns all the possible splits of the word such
   * that both of the split output words are in the Trie.
   *
   * @param word
   *          , any String
   *
   * @param trie
   *          the trie to be searched
   * @return a set of words which appear in the Trie which are edited versions
   *         of the inputed word where a space is added
   */
  public static Set<String> whitespace(String word, Trie trie) {
    Set<String> wordSet = new HashSet<String>();
    // an empty list to which we will add words later
    for (int i = 1; i < word.length(); i++) {
      String output1 = word.substring(0, i);
      String output2 = word.substring(i);
      Node wordOne = trie.getNode(output1);
      Node wordTwo = trie.getNode(output2);
      boolean areWords = false;
      if (wordOne != null && wordTwo != null) {
        areWords = wordOne.isTerminal() && wordTwo.isTerminal();
      }

      if (areWords) {
        wordSet.add(output1 + " " + output2);
      }
    }
    return wordSet;
  }

}
