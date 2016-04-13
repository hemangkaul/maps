package edu.brown.cs.hk125.map;

/**
 * A class for storing ways; simply stores their components.
 *
 * @author hk125
 *
 */
public class Way {

  private double startLatitude;

  private double startLongitude;

  private double endLatitude;

  private double endLongitude;

  private String name;

  private String type;

  private String id;

  private double traffic;

  public Way(double startLat, double startLng, double endLat, double endLng,
      String wayName, String wayType, String wayId, double trafficVal) {
    this.startLatitude = startLat;
    this.startLongitude = startLng;
    this.endLatitude = endLat;
    this.endLongitude = endLng;
    this.name = wayName;
    this.type = wayType;
    this.id = wayId;
    this.traffic = trafficVal;
  }

  /**
   * @return the traffic
   */
  public double getTraffic() {
    return traffic;
  }

  /**
   * @param traffic
   *          the traffic to set
   */
  public void setTraffic(double traffic) {
    this.traffic = traffic;
  }

  /**
   * @return the startLatitude
   */
  public double getStartLatitude() {
    return startLatitude;
  }

  /**
   * @return the startLongitude
   */
  public double getStartLongitude() {
    return startLongitude;
  }

  /**
   * @return the endLatitude
   */
  public double getEndLatitude() {
    return endLatitude;
  }

  /**
   * @return the endLongitude
   */
  public double getEndLongitude() {
    return endLongitude;
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
