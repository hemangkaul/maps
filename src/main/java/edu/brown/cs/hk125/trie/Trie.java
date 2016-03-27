package edu.brown.cs.hk125.trie;

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
   * the constructor of the Trie.
   */
  public Trie() {
    this.root = new Node(null, false);
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
}
