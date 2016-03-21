package edu.brown.cs.hk125.kdtree;

public class KDNode {

  private KDData datum;
  private KDNode left;
  private KDNode right;

  public KDNode(KDData datum, KDNode left, KDNode right) {
    this.setDatum(checkDatum(datum));
    this.setLeft(left);
    this.setRight(right);
  }

  private static KDData checkDatum(KDData datum) {
    if (datum.equals(null)) {
      throw new IllegalArgumentException(datum + "is null");
    } else
      return datum;
  }

  // CHANGE - accesses private instance of mutable variable
  public KDData getDatum() {
    return datum;
  }

  public void setDatum(KDData datum) {
    this.datum = datum;
  }

  // CHANGE - accesses private instance of mutable variable
  public KDNode getLeft() {
    return left;
  }

  public void setLeft(KDNode left) {
    this.left = left;
  }

  // CHANGE - accesses private instance of mutable variable
  public KDNode getRight() {
    return right;
  }

  public void setRight(KDNode right) {
    this.right = right;
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
