package edu.brown.cs.hk125.maps;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Tile represents a tile which contains data points.
 *
 * @author hk125
 *
 */
public class Tile {

  /**
   * top latitude.
   */
  private double tlat;

  /**
   * bottom latitude.
   */
  private double blat;

  /**
   * left longitude.
   */
  private double llng;

  /**
   * right longitude.
   */
  private double rlng;

  /**
   * id.
   */
  private String id;

  /**
   * a list of ways.
   */
  private Map<String, Way> tileWays = new HashMap<>();

  /**
   * the constructor for a Tile.
   *
   * @param topLat
   *          the topmost latitude value
   * @param bottomLat
   *          the bottommost latitude value
   * @param leftLng
   *          the leftmost longitude value
   * @param rightLng
   *          the rightmost longitude value
   */
  public Tile(double topLat, double bottomLat, double leftLng,
      double rightLng) {
    this.tlat = topLat;
    this.blat = bottomLat;
    this.llng = leftLng;
    this.rlng = rightLng;
    this.id = Double.toString(bottomLat) + ":" + Double.toString(leftLng);
  }

  /**
   * checks to see if a point is in a tile.
   *
   * @param lat
   *          the latitude of the point to check
   * @param lng
   *          the longitude of the point to check
   * @return true if in the tile and false otherwise
   */
  public boolean inTile(double lat, double lng) {
    return (lat <= tlat && lat > blat && lng >= llng && lng < rlng);
  }

  /**
   * inserts a kv pair of start and end to the tileWays.
   *
   * @param way
   *          the way to be added
   * @param traffic
   *          the traffic of the way
   */
  public void insertWay(Way way, double traffic) {
    if (tileWays.containsKey(way.getId())) {
      tileWays.get(way.getId()).setTraffic(traffic);
    } else {
      tileWays.put(way.getId(), way);
    }
  }

  /**
   * gets the top lat.
   *
   * @return the tlat
   */
  public double getTlat() {
    return tlat;
  }

  /**
   * gets the bottom lat.
   *
   * @return the blat
   */
  public double getBlat() {
    return blat;
  }

  /**
   * gets the left long.
   *
   * @return the llng
   */
  public double getLlng() {
    return llng;
  }

  /**
   * gets the right long.
   *
   * @return the rlng
   */
  public double getRlng() {
    return rlng;
  }

  /**
   * gets the tileWays.
   *
   * @return the tileWays
   */
  public Collection<Way> getTileWays() {
    return tileWays.values();
  }

  /**
   * gets the Tile id.
   *
   * @return the tile ID
   */
  public String getId() {
    return id;
  }

}
