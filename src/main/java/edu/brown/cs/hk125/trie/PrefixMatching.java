package edu.brown.cs.hk125.trie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * the prefix matching algorithm.
 *
 * @author hk125
 *
 */
public class PrefixMatching {

  /**
   * the prefix matching algorithm.
   *
   * @param word
   *          the word to be matched
   * @param trie
   *          the trie to search in
   * @return a List of the possible matches
   */
  public static List<String> prefix(String word, Trie trie) {
    Node node = trie.getNode(word);
    if (node == null) {
      return new ArrayList<>();
    }
    List<String> words = new ArrayList<>();
    return prefixHelp(node, words, word);
  }

  /**
   * a helper function for the prefix matching algorithm.
   *
   * @param node
   *          the current node
   * @param words
   *          the list of words which match
   * @param word
   *          the word being checked
   * @return a list of the possible matches
   */
  public static List<String> prefixHelp(Node node, List<String> words,
      String word) {
    if (node.isTerminal()) {
      words.add(word);
    }
    if (node.hasChild()) {
      Iterator<Node> children = node.getChildren();
      while (children.hasNext()) {
        Node child = children.next();
        words = prefixHelp(child, words, word + child.getChar());
      }
    }
    return words;
  }
}
