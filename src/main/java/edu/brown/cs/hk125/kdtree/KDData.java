package edu.brown.cs.hk125.kdtree;

/**
 * KDData is a representation of a data with K dimensions.
 *
 * @author hk125
 *
 */
public interface KDData {
  /**
   * dimensions returns the dimensions of the KDData.
   *
   * @return the number of dimensions
   */
  public int dimensions();

  /**
   * compareDimension compares along a dimension.
   *
   * @param dim
   *          represents the dimension you wish to compare on
   * @param other
   *          is the data you wish to compare to
   * @return positive, if current is greater than other on dimension dim,
   *         negative if current is less and 0 if they are equal.
   */
  public double compareDimension(int dim, KDData other);

  /**
   * distance gives the distance between two KDData.
   *
   * @param other
   *          represents the KDData you want to compare to
   * @return a non negative double representing the distance between the two
   *         data
   */
  public double distance(KDData other);

}
