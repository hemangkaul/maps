package edu.brown.cs.hk125.trie;

import java.util.List;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/**
 * Representative of the Trie data structure.
 *
 * @author hk125
 *
 */
public class Trie {

  /**
   * the root of the Trie.
   */
  private Node root;

  /**
   * the set of all unigrams.
   */
  private Multiset<String> unigrams;

  /**
   * the set of all bigrams.
   */
  private Multiset<Bigram> bigrams;

  /**
   * the constructor of the Trie.
   */
  public Trie(List<String> corpus) {
    this.root = new Node(null, false);
    setTrie(corpus);
  }

  /**
   * gets the Node associated with a certain word.
   *
   * @param word
   *          the target word
   * @return the Node associated
   */
  public Node getNode(String word) {
    return getNodeHelp(word, root);
  }

  /**
   * a helper function for getting Nodes.
   *
   * @param word
   *          the target word
   * @param curr
   *          the current node
   * @return the correct node, root if given null
   */
  private Node getNodeHelp(String word, Node curr) {
    if (word == null) {
      return curr;
    }
    if (word.length() == 0) {
      return curr;
    }
    Node child = curr.getChild(word.charAt(0));
    if (child == null) {
      return null;
    } else {
      return getNodeHelp(word.substring(1), child);
    }
  }

  /**
   * inserts words into the Trie.
   *
   * @param word
   *          the word to be inserted into the Trie
   */
  public void insert(String word) {
    insertHelp(word, root);
  }

  /**
   * a helper function for insert.
   *
   * @param word
   *          the word to be inserted
   * @param curr
   *          the current node
   */
  private void insertHelp(String word, Node curr) {
    if (word.length() == 0) {
      curr.setTerminal(true);
    } else {
      Node child = curr.getChild(word.charAt(0));
      if (child == null) {
        child = curr.setChild(word.charAt(0), false);
      }
      insertHelp(word.substring(1), child);
    }
  }

  public Multiset<String> getUnigrams() {
    return unigrams;
  }

  public Multiset<Bigram> getBigrams() {
    return bigrams;
  }

  /**
   * sets the unigram and bigram sets and inserts all elements into the trie.
   *
   * @param corpus
   */
  private void setTrie(List<String> corpus) {
    unigrams = HashMultiset.create();
    bigrams = HashMultiset.create();
    if (corpus.isEmpty()) {
      return;
    }

    int numElements = corpus.size();

    for (int i = 0; i < numElements; i++) {
      String token = corpus.get(i).toLowerCase();
      String[] bigramCalc = token.split(" ");
      if (bigramCalc.length > 1) {
        for (int j = 0; j < bigramCalc.length; j++) {
          unigrams.add(bigramCalc[j]);
          insert(bigramCalc[j]);
          if (j != bigramCalc.length - 1) {
            Bigram bigram = new Bigram(bigramCalc[j], bigramCalc[j + 1]);
            bigrams.add(bigram);
          }
        }
      }
    }
    //
    // for (String token : corpus) {
    // token = token.toLowerCase();
    // wordlist.add(token);
    // unigrams.add(token);
    // insert(token);
    // }
    // for (int k = 0; k < wordlist.size() - 1; k++) {
    // Bigram bigram = new Bigram(wordlist.get(k), wordlist.get(k + 1));
    // bigrams.add(bigram);
    // }
  }
}
