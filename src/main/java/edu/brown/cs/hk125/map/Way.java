package edu.brown.cs.hk125.map;

/**
 * A class for storing ways; simply stores their components.
 *
 * @author hk125
 *
 */
public class Way {

  /**
   * the start lat.
   */
  private double startLatitude;

  /**
   * the start lng.
   */
  private double startLongitude;

  /**
   * the end lat.
   */
  private double endLatitude;

  /**
   * the end lng.
   */
  private double endLongitude;

  /**
   * the name.
   */
  private String name;

  /**
   * the type.
   */
  private String type;

  /**
   * the id.
   */
  private String id;

  /**
   * the traffic value.
   */
  private double traffic;

  /**
   * the constructor for a way.
   *
   * @param startLat
   *          the start lat
   * @param startLng
   *          the start lng
   * @param endLat
   *          the end lat
   * @param endLng
   *          the end lng
   * @param wayName
   *          the name of the way
   * @param wayType
   *          the type of the way
   * @param wayId
   *          the id of the way
   * @param trafficVal
   *          the value of the traffic
   */
  public Way(double startLat, double startLng, double endLat,
      double endLng, String wayName, String wayType, String wayId,
      double trafficVal) {
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
   * gets the traffic.
   *
   * @return the traffic
   */
  public double getTraffic() {
    return traffic;
  }

  /**
   * sets the traffic.
   *
   * @param trafficVal
   *          the traffic to set
   */
  public void setTraffic(double trafficVal) {
    this.traffic = trafficVal;
  }

  /**
   * gets the start latitude.
   *
   * @return the startLatitude
   */
  public double getStartLatitude() {
    return startLatitude;
  }

  /**
   * gets the start longitude.
   *
   * @return the startLongitude
   */
  public double getStartLongitude() {
    return startLongitude;
  }

  /**
   * gets the end latitude.
   *
   * @return the endLatitude
   */
  public double getEndLatitude() {
    return endLatitude;
  }

  /**
   * gets the end longitude.
   *
   * @return the endLongitude
   */
  public double getEndLongitude() {
    return endLongitude;
  }

  /**
   * gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * gets the type.
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * gets the id.
   *
   * @return the id
   */
  public String getId() {
    return id;
  }

}
