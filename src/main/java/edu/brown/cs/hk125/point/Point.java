package edu.brown.cs.hk125.point;

import edu.brown.cs.hk125.kdtree.KDData;

/**
 * A point in two-d space.
 *
 * @author hk125
 *
 */
public class Point implements KDData {

  /**
   * the x coordinate.
   */
  private double x;

  /**
   * the y coordinate.
   */
  private double y;

  /**
   * the constructor for the point class.
   *
   * @param xval
   *          the x value
   * @param yval
   *          the y value
   */
  public Point(double xval, double yval) {
    this.x = xval;
    this.y = yval;
  }

  @Override
  public int dimensions() {
    return 2;
  }

  @Override
  public double compareDimension(int dim, KDData other) {
    Point point = (Point) other;
    Double comparison = 0.0;
    if (dim == 0) {
      comparison = this.x - point.x;
    } else if (dim == 1) {
      comparison = this.y - point.y;
    }
    return comparison;
  }

  @Override
  public double distance(KDData other) {
    Point point = (Point) other;

    double xdif = this.x - point.x;
    double ydif = this.y - point.y;

    return Math.sqrt(Math.pow(xdif, 2) + Math.pow(ydif, 2));
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (!(o instanceof Point)) {
      return false;
    }
    Point other = (Point) o;
    return ((this.x == other.x) && (this.y == other.y));
  }
}
