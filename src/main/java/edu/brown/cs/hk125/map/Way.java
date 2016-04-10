package edu.brown.cs.hk125.map;

/**
 * A class for storing ways; simply stores their components.
 *
 * @author hk125
 *
 */
public class Way {

  private String start;

  private String end;

  private String name;

  private String type;

  private String id;

  public Way(String startNode, String endNode, String wayName, String wayType,
      String wayId) {
    this.start = startNode;
    this.end = endNode;
    this.name = wayName;
    this.type = wayType;
    this.id = wayId;
  }

  /**
   * @return the start, the start node
   */
  public String getStart() {
    return start;
  }

  /**
   * @param start
   *          the start to set
   */
  public void setStart(String start) {
    this.start = start;
  }

  /**
   * @return the end
   */
  public String getEnd() {
    return end;
  }

  /**
   * @param end
   *          the end to set
   */
  public void setEnd(String end) {
    this.end = end;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @param type
   *          the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

}
