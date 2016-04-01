package edu.brown.cs.hk125.trie;

import com.google.common.base.Objects;

/**
 * a representation of Bigrams.
 *
 * @author hk125
 *
 */
public class Bigram {

  /**
   * the previous word.
   */
  public String prev;

  /**
   * the word itself.
   */
  public String word;

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
