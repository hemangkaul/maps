package edu.brown.cs.hk125.dijkstra;

/**
 * A groupLink stores a group of links together. A groupLink comes in handy when
 * a bunch of different links have the same distance -- it will save space to
 * leave them as a groupLink and separately expand the groupLink when needed.
 *
 * The method for expanding a groupLink into an ArrayList of Links is determined
 * in the given infoGetter.
 *
 * @author sl234
 *
 */
public class GroupLink extends Link {

  /**
   * the constructor for a grouplink.
   *
   * @param source
   *          the source
   * @param distance
   *          the distance
   * @param name
   *          the name
   */
  public GroupLink(String source, Double distance, String name) {
    super(source, "", distance, name);
  }
}
