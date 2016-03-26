package edu.brown.cs.hk125.kdtree;

import java.util.Comparator;

/**
 * KDDistanceComparator is a comparator that compares the distance from a target
 * between two KDData.
 *
 * @author hk125
 *
 */
public class KDDistanceComparator implements Comparator<KDData> {

  private KDData target;

  /**
   * This is the constructor for a KDDistanceComparator.
   *
   * @param target
   *          is the KDData we want to find the distance from
   */
  public KDDistanceComparator(KDData target) {
    this.target = target;
  }

  @Override
  public int compare(KDData o1, KDData o2) {
    return Double.compare(o1.distance(target), o2.distance(target));
  }

}
