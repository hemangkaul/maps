package edu.brown.cs.hk125.trie;

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * a representation of Bigrams.
 *
 * @author hk125
 *
 */
public class Bigram implements Serializable {

  /**
   * the default serialVersion UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * the previous word.
   */
  private String prev;

  /**
   * the word itself.
   */
  private String word;

  /**
   * constructor for Bigram class.
   *
   * @param previous
   *          the previous word
   * @param current
   *          the current word
   */
  public Bigram(String previous, String current) {
    this.prev = previous;
    this.word = current;
  }

  /**
   * gets the previous.
   *
   * @return the prev
   */
  public String getPrev() {
    return prev;
  }

  /**
   * gets the word.
   *
   * @return the word
   */
  public String getWord() {
    return word;
  }

  @Override
  public String toString() {
    return (prev + " " + word);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(prev, word);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (!(o instanceof Bigram)) {
      return false;
    }
    Bigram other = (Bigram) o;
    return ((this.prev.equals(other.prev)) && (this.word.equals(other.word)));
  }
}
