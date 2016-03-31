package edu.brown.cs.hk125.latlng;

import com.google.common.base.Objects;

import edu.brown.cs.hk125.kdtree.KDData;

/**
 * LatLng is a representation of a point.
 *
 * @author hk125
 *
 */
public class LatLng implements KDData {

  /**
   * the radius of the earth in miles.
   */
  private static final double RADIUS = 3959;

  /**
   * the latitude.
   */
  private double lat;

  /**
   * the longitude.
   */
  private double lng;

  /**
   * the id of the LatLng.
   */
  private String id;

  /**
   * The constructor method for the LatLng class.
   *
   * @param latitude
   *          is the latitude of the point
   * @param longitude
   *          is the longitude of the point
   */
  public LatLng(double latitude, double longitude, String name) {
    setLat(latitude);
    setLng(longitude);
    this.id = name;
  }

  /**
   * an alternate constructor for the LatLng class.
   *
   * @param latitude
   * @param longitude
   */
  public LatLng(double latitude, double longitude) {
    setLat(latitude);
    setLng(longitude);
    this.id = "";
  }

  @Override
  public int dimensions() {
    return 2;
  }

  @Override
  public double compareDimension(int dim, KDData other) {
    if (!(other instanceof LatLng)) {
      throw new IllegalArgumentException(
          "ERROR: You can only compare two LatLngs");
    }
    LatLng latlng = (LatLng) other;
    Double comparison = 0.0;
    if (dim == 0) {
      comparison = this.lat - latlng.lat;
    } else if (dim == 1) {
      comparison = this.lng - latlng.lng;
    }
    return comparison;
  }

  @Override
  public double distance(KDData other) {
    if (!(other instanceof LatLng)) {
      throw new IllegalArgumentException(
          "ERROR: You can only compare two LatLngs");
    }
    LatLng latlng = (LatLng) other;
    double dLat = Math.toRadians(latlng.lat - lat);
    double dLon = Math.toRadians(latlng.lng - lng);
    double lat1 = Math.toRadians(lat);
    double lat2 = Math.toRadians(latlng.lat);

    double a = Math.pow(Math.sin(dLat / 2), 2)
        + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
    double c = 2 * Math.asin(Math.sqrt(a));
    return RADIUS * c;
  }

  /**
   * Distance is a static method to find the Haversine distance between two
   * points by their latitudes and longitudes.
   *
   * @param lat1
   *          the latitude of the first point
   * @param lng1
   *          the longitude of the first point
   * @param lat2
   *          the latitude of the second point
   * @param lng2
   *          the longitude of the second point
   * @return the distance between two points accounting for the curvature of the
   *         earth
   */
  public static double distance(double lat1, double lng1, double lat2,
      double lng2) {
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lng2 - lng1);
    lat1 = Math.toRadians(lat1);
    lat2 = Math.toRadians(lat2);

    double a = Math.pow(Math.sin(dLat / 2), 2)
        + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
    double c = 2 * Math.asin(Math.sqrt(a));
    return RADIUS * c;
  }

  /**
   * get the lat.
   *
   * @return the lat
   */
  public double getLat() {
    return lat;
  }

  /**
   * set the lat, check for -90 < lat < 90.
   *
   * @param latitude
   */
  private void setLat(double latitude) {
    if (latitude > 90 || latitude < -90) {
      throw new IllegalArgumentException("Latitude must be between 90 and -90");
    }
    this.lat = latitude;
  }

  /**
   * get the lng.
   *
   * @return the lng
   */
  public double getLng() {
    return lng;
  }

  /**
   * set the lng, check for -90 < lng < 90.
   *
   * @param longitude
   */
  private void setLng(double longitude) {
    if (longitude > 180 || longitude < -180) {
      throw new IllegalArgumentException(
          "Longitude must be between 180 and -180");
    }
    this.lng = longitude;
  }

  /**
   * get the id.
   *
   * @return the id
   */
  public String getID() {
    return id;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.lat, this.lng);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (!(o instanceof LatLng)) {
      return false;
    }
    LatLng other = (LatLng) o;
    return ((this.lat == other.lat) && (this.lng == other.lng));
  }

  @Override
  public String toString() {
    return ("(" + lat + ", " + lng + ")");
  }

}
