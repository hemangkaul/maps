package edu.brown.cs.hk125.maps;

public class Tile {

  private double tlat;
  private double blat;
  private double llng;
  private double rlng;

  public Tile(double topLat, double bottomLat, double leftLng, double rightLng) {
    this.tlat = topLat;
    this.blat = bottomLat;
    this.llng = leftLng;
    this.rlng = rightLng;
  }

  public boolean inTile(double lat, double lng) {
    return (lat < tlat && lat > blat && lng > llng && lng < rlng);
  }

}
