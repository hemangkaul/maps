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
   *
   * @param corpus
   *          the words
   * @param single
   *          a boolean which decides whether to look at each word in the corpus
   *          as a full word or as each of its composite parts
   */
  public Trie(List<String> corpus, boolean single) {
    this.root = new Node(null, false);
    setTrie(corpus, single);
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

  /**
   * get the unigrams list.
   *
   * @return the set of unigrams
   */
  public Multiset<String> getUnigrams() {
    return unigrams;
  }

  /**
   * get the bigrams list.
   *
   * @return the set of bigrams
   */
  public Multiset<Bigram> getBigrams() {
    return bigrams;
  }

  /**
   * sets the unigram and bigram sets and inserts all elements into the trie.
   *
   * @param corpus
   *          the list of all the words to be added.
   * @param single
   *          the boolean representing whether to consider multi-words
   */
  private void setTrie(List<String> corpus, boolean single) {
    unigrams = HashMultiset.create();
    bigrams = HashMultiset.create();
    if (corpus.isEmpty()) {
      return;
    }

    int numElements = corpus.size();

    for (int i = 0; i < numElements; i++) {
      String token = corpus.get(i).toLowerCase();
      if (single) {
        unigrams.add(token);
        insert(token);
      } else {
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
        } else {
          unigrams.add(token);
          insert(token);
        }
      }
    }
  }
}
