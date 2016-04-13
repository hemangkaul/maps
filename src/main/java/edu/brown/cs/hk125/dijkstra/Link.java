package edu.brown.cs.hk125.dijkstra;

import com.google.common.base.Objects;

/**
 * A Link represents an edge connecting, or 'linking' two nodes. A Link contains
 * a source, an end, a distance, and a name.
 *
 * Notice a link can also represent a neighbor. All the links connected to a
 * source node represent its neighbors.
 *
 * @author sl234
 *
 */
public class Link implements Comparable<Link> {

  /**
   * the source of the link.
   */
  private String source;

  /**
   * the end of the link.
   */
  private String end;

  /**
   * the distance of the link.
   */
  private Double distance;
  // distance refers to the distance from the end node to the start node, NOT
  // the distance from the end node to the source node.

  /**
   * the name of the link.
   */
  private String name;

  /**
   * Constructs a link; sets source, end, distance, and link.
   *
   * @param start
   *          , the name of the source of the link
   * @param last
   *          , the name of the end of the link
   * @param dist
   *          , the distance between the source and the end
   * @param id
   *          , the name of the link
   */
  public Link(String start, String last, Double dist, String id) {
    this.setSource(start);
    this.setEnd(last);
    this.setDistance(dist);
    this.setName(id);
  }

  /**
   * Another constructor; sometimes link isn't needed.
   *
   * @param start
   *          , the name of the source of the link
   * @param last
   *          , the name of the end of the link
   * @param dist
   *          , the distance between the source and the end
   */
  public Link(String start, String last, Double dist) {
    this.setSource(start);
    this.setEnd(last);
    this.setDistance(dist);
    this.setName("");
  }

  /**
   * @return the source
   */
  public String getSource() {
    return source;
  }

  /**
   * @param start
   *          the source to set
   */
  public void setSource(String start) {
    this.source = start;
  }

  /**
   * @return the neighborName
   */
  public String getEnd() {
    return end;
  }

  /**
   * @param last
   *          the neighborName to set
   */
  public void setEnd(String last) {
    this.end = last;
  }

  /**
   * @return the distance
   */
  public Double getDistance() {
    return distance;
  }

  /**
   * @param dist
   *          the distance to set
   */
  public void setDistance(Double dist) {
    this.distance = dist;
  }

  /**
   * @return the name of the Link
   */
  public String getName() {
    return name;
  }

  /**
   * @param id
   *          the name of the link to set
   */
  public void setName(String id) {
    this.name = id;
  }

  /**
   * Allows us to sort neighbors according to their distance values.
   *
   * @param l
   *          , the link we are comparing to
   * @return 1 if the compared link is has smaller distance, -1 if the compared
   *         link has greater distance, and 0 if the distances are the same
   */
  @Override
  public int compareTo(Link l) {
    // TODO Auto-generated method stub
    double distDiff = distance - l.getDistance();
    if (distDiff < 0) {
      return -1;
    } else if (distDiff > 0) {
      return 1;
    } else {
      return 0;
    }
  }

  @Override
  /**
   * Simply checking for equality by checking each of the four
   * components are equal.
   */
  public boolean equals(Object other) {
    if (!(other instanceof Link)) {
      return false;
    }
    Link l = (Link) other;

    if (!(this.getSource().equals(l.getSource()))) {
      return false;
    }
    if (!(this.getEnd().equals(l.getEnd()))) {
      return false;
    }
    if (!(this.getDistance().equals(l.getDistance()))) {
      return false;
    }
    if (!(this.getName().equals(l.getName()))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getSource(), this.getEnd(),
        this.getDistance(), this.getName());
  }
}
