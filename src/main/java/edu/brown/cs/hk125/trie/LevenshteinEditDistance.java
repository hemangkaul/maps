package edu.brown.cs.hk125.trie;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.brown.cs.hk125.trie.Node;
import edu.brown.cs.hk125.trie.Trie;

public class LevenshteinEditDistance {

  public static Set<String> levenshtein(String word, Trie trie, int distance) {
    Set<String> words = new HashSet<>();
    return levenshteinHelp(trie, word, distance, words);
  }

  public static Set<String> levenshteinHelp(Trie trie, String word,
      int distance, Set<String> words) {

    // should we add the word
    if (distance >= 0) {
      Node wordNode = trie.getNode(word);
      if (wordNode != null && wordNode.isTerminal()) {
        words.add(word);
      }
    }

    if (distance > 0) {
      for (int i = 0; i < word.length(); i++) {

        // deletions of i'th letter
        String deletion = word.substring(0, i)
            + word.substring(i + 1, word.length());
        Node delNode = trie.getNode(deletion);
        if (delNode != null) {
          words = levenshteinHelp(trie, deletion, distance - 1, words);
        }

        // substitutions of i'th letter
        Node subNode = trie.getNode(word.substring(0, i));
        if (subNode != null) {
          Iterator<Node> children = subNode.getChildren();
          while (children.hasNext()) {
            Node child = children.next();
            String withSub = word.substring(0, i) + child.getChar()
                + word.substring(i + 1, word.length());
            Node subWord = trie.getNode(withSub);
            if (subWord != null) {
              words = levenshteinHelp(trie, withSub, distance - 1, words);
            }
          }
        }
      }

      // additions of i'th letter
      for (int j = 0; j < word.length() + 1; j++) {
        Node addNode = trie.getNode(word.substring(0, j));
        if (addNode != null) {
          Iterator<Node> children = addNode.getChildren();
          while (children.hasNext()) {
            Node child = children.next();
            String withAdd = word.substring(0, j) + child.getChar()
                + word.substring(j, word.length());
            Node addWord = trie.getNode(withAdd);
            if (addWord != null) {
              words = levenshteinHelp(trie, withAdd, distance - 1, words);
            }
          }
        }
      }
    }

    return words;
  }
}
