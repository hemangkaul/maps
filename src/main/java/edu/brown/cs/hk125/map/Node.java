package edu.brown.cs.hk125.map;

/**
 * This class represents a Node.
 *
 * @author hk125
 *
 */
public class Node {

  /**
   * the latitude.
   */
  private double latitude;

  /**
   * the longitude.
   */
  private double longitude;

  /**
   * the id.
   */
  private String id;

  /**
   * the constructor for the Node class.
   *
   * @param lat
   *          the latitude
   * @param lng
   *          the longitude
   * @param name
   *          the id
   */
  public Node(double lat, double lng, String name) {
    this.latitude = lat;
    this.longitude = lng;
    this.id = name;
  }

  /**
   * gets the latitude.
   *
   * @return the latitude
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * sets the latitude.
   *
   * @param latitude
   *          the latitude to set
   */
  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  /**
   * gets the longitude.
   *
   * @return the longitude
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   * sets the longitude.
   *
   * @param longitude
   *          the longitude to set
   */
  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  /**
   * gets the id.
   *
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * sets the id.
   * 
   * @param id
   *          the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

}
