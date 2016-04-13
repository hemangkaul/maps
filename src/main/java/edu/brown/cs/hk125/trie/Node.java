package edu.brown.cs.hk125.trie;

import java.util.HashMap;
import java.util.Iterator;

import com.google.common.base.Objects;

/**
 * Node class for Trie.
 *
 * @author hk125
 *
 */
public class Node {

  /**
   * character held by node.
   */
  private Character c;

  /**
   * whether this is a terminal node.
   */
  private boolean terminal;

  /**
   * the children of this node.
   */
  private HashMap<Character, Node> children;

  /**
   * a constructor for the node.
   *
   * @param charac
   *          the character held here
   * @param word
   *          whether this is a word
   */
  public Node(Character charac, boolean word) {
    this.c = charac;
    this.terminal = word;
    this.children = new HashMap<Character, Node>();
  }

  /**
   * returns the character held by the node.
   *
   * @return the character
   */
  public Character getChar() {
    return c;
  }

  /**
   * returns the children of the node in an iterator.
   *
   * @return an iterator of the children
   */
  public Iterator<Node> getChildren() {
    return children.values().iterator();
  }

  /**
   * checks to see if the node has children.
   *
   * @return true if the node has children, false otherwise
   */
  public boolean hasChild() {
    return (!children.isEmpty());
  }

  /**
   * gets a specific child.
   *
   * @param ch
   *          the value of the child
   * @return the node of the children which holds the value of c
   */
  public Node getChild(Character ch) {
    return children.get(ch);
  }

  /**
   * adds a new child to a node.
   *
   * @param cha
   *          the character of the child node
   * @param term
   *          true if the node a word
   * @return the child Node which is created
   */
  public Node setChild(Character cha, boolean term) {
    Node child = new Node(cha, term);
    children.put(cha, child);
    return child;
  }

  /**
   * is the node representative of the end of a word.
   *
   * @return true if the node represents the end of a word
   */
  public boolean isTerminal() {
    return terminal;
  }

  /**
   * set the node as the end of a word.
   *
   * @param isWord
   *          is the node a word
   */
  public void setTerminal(boolean isWord) {
    this.terminal = isWord;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (!(o instanceof Node)) {
      return false;
    }
    Node other = (Node) o;
    boolean isEqual = (this.c.equals(other.c))
        && (this.terminal == other.terminal)
        && (this.children.equals(other.children));
    return isEqual;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.c, this.terminal, this.children);
  }

  @Override
  public String toString() {
    return c.toString();
  }
}
