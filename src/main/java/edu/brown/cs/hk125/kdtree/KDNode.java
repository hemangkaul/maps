package edu.brown.cs.hk125.kdtree;

/**
 * KDNode represents a node of the KD Tree.
 *
 * @author hk125
 *
 */
public class KDNode {

  /**
   * the datum held by the Node.
   */
  private KDData datum;

  /**
   * the left Node.
   */
  private KDNode left;

  /**
   * the right Node.
   */
  private KDNode right;

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
  public KDNode(KDData kddata, KDNode leftNode, KDNode rightNode) {
    this.setDatum(checkDatum(kddata));
    this.setLeft(leftNode);
    this.setRight(rightNode);
  }

  /**
   * checks if the datum is valid.
   *
   * @param datum
   *          the datum to be set
   * @return the KDData
   */
  private static KDData checkDatum(KDData datum) {
    if (datum.equals(null)) {
      throw new IllegalArgumentException(datum + "is null");
    } else {
      return datum;
    }
  }

  /**
   * returns the datum.
   *
   * @return the datum
   */
  public KDData getDatum() {
    return datum;
  }

  /**
   * sets the datum.
   *
   * @param kddata
   *          the datum you want to set the node to
   */
  private void setDatum(KDData kddata) {
    this.datum = kddata;
  }

  /**
   * returns the left node.
   *
   * @return the left node
   */
  public KDNode getLeft() {
    return left;
  }

  /**
   * sets the left node.
   *
   * @param leftNode
   *          the left node you want to set left to
   */
  private void setLeft(KDNode leftNode) {
    this.left = leftNode;
  }

  /**
   * returns the right node.
   *
   * @return the right node
   */
  public KDNode getRight() {
    return right;
  }

  /**
   * sets the right node.
   *
   * @param rightNode
   *          the right node you want to set right to
   */
  private void setRight(KDNode rightNode) {
    this.right = rightNode;
  }

  @Override
  public boolean equals(Object o) {
    if (o.equals(null)) {
      return false;
    }
    if (!(o instanceof KDNode)) {
      return false;
    }
    KDNode other = (KDNode) o;
    return ((this.datum.equals(other.datum))
        && (this.right.equals(other.right)) && (this.left.equals(other.left)));
  }

  @Override
  public String toString() {
    return datum.toString();
  }

}
