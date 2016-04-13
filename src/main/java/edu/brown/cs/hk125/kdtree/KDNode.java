package edu.brown.cs.hk125.kdtree;

import com.google.common.base.Objects;

/**
 * KDNode represents a node of the KD Tree.
 *
 * @param <T>
 *          the type of the KDData
 * @author hk125
 *
 */
public class KDNode<T extends KDData> {

  /**
   * the datum held by the Node.
   */
  private T datum;

  /**
   * the left Node.
   */
  private KDNode<T> left;

  /**
   * the right Node.
   */
  private KDNode<T> right;

  /**
   * The public constructor for a KDNode.
   *
   * @param kddata
   *          the datum held by the node
   * @param leftNode
   *          the left node
   * @param rightNode
   *          the right node
   */
  public KDNode(T kddata, KDNode<T> leftNode, KDNode<T> rightNode) {
    this.setDatum(checkDatum(kddata));
    this.setLeft(leftNode);
    this.setRight(rightNode);
  }

  /**
   * checks if the datum is valid.
   *
   * @param data
   *          the datum to be set
   * @return the KDData
   */
  private T checkDatum(T data) {
    if (data.equals(null)) {
      throw new IllegalArgumentException(datum + "is null");
    } else {
      return data;
    }
  }

  /**
   * returns the datum.
   *
   * @return the datum
   */
  public T getDatum() {
    return datum;
  }

  /**
   * sets the datum.
   *
   * @param kddata
   *          the datum you want to set the node to
   */
  private void setDatum(T kddata) {
    this.datum = kddata;
  }

  /**
   * returns the left node.
   *
   * @return the left node
   */
  public KDNode<T> getLeft() {
    return left;
  }

  /**
   * sets the left node.
   *
   * @param leftNode
   *          the left node you want to set left to
   */
  private void setLeft(KDNode<T> leftNode) {
    this.left = leftNode;
  }

  /**
   * returns the right node.
   *
   * @return the right node
   */
  public KDNode<T> getRight() {
    return right;
  }

  /**
   * sets the right node.
   *
   * @param rightNode
   *          the right node you want to set right to
   */
  private void setRight(KDNode<T> rightNode) {
    this.right = rightNode;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(Object o) {
    if (o.equals(null)) {
      return false;
    }
    if (!(o instanceof KDNode)) {
      return false;
    }
    KDNode<T> other = (KDNode<T>) o;
    return ((this.datum.equals(other.datum))
        && (this.right.equals(other.right)) && (this.left
          .equals(other.left)));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.datum, this.right, this.left);
  }

  @Override
  public String toString() {
    return datum.toString();
  }

}
