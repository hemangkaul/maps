package edu.brown.cs.hk125.maps;

import java.util.HashMap;
import java.util.Map;

import edu.brown.cs.hk125.latlng.LatLng;

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
   * a map of starts to ends, i.e. ways.
   */
  private Map<LatLng, LatLng> tileWays = new HashMap<LatLng, LatLng>();

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
  public Tile(double topLat, double bottomLat, double leftLng, double rightLng) {
    this.tlat = topLat;
    this.blat = bottomLat;
    this.llng = leftLng;
    this.rlng = rightLng;
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
    return (lat < tlat && lat > blat && lng > llng && lng < rlng);
  }

  /**
   * inserts a kv pair of start and end to the tileWays.
   *
   * @param start
   *          the start point
   * @param end
   *          the end point
   */
  public void insertWay(LatLng start, LatLng end) {
    tileWays.put(start, end);
  }

}
