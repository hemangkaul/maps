package edu.brown.cs.hk125.kdtree;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Sorts the KDData based on the current dimension.
 *
 * @author hk125
 *
 */
public class KDComparator implements Comparator<KDData>, Serializable {

  /**
   * the default serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * the current dimension.
   */
  private int currDim;

  /**
   * the constructor for the KDComparator.
   *
   * @param dim
   *          the dimension to be sorted on
   */
  public KDComparator(int dim) {
    this.currDim = dim;
  }

  @Override
  public int compare(KDData o1, KDData o2) {
    int result = 0;
    Double comparison = o1.compareDimension(currDim, o2);
    if (comparison > 0) {
      result = 1;
    } else if (comparison < 0) {
      result = -1;
    } else if (comparison == 0) {
      result = 0;
    }
    return result;
  }

}
