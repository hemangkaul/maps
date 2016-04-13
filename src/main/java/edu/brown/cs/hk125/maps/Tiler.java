package edu.brown.cs.hk125.maps;

import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * Tiler is an interface which models tiling.
 *
 * @author hk125
 *
 */
public interface Tiler {

  /**
   * sets the tiles.
   *
   * @throws SQLException
   *           if there is an issue with the query
   */
  void setTiles() throws SQLException;

  /**
   * given a point return the tile it is in.
   *
   * @param lat
   *          the latitude of the point
   * @param lng
   *          the longitude of the point
   * @return the Tile it is in
   * @throws NoSuchElementException
   *           if no such tile exists
   * @throws SQLException
   *           if there is an issue with the query
   */
  Tile getTile(double lat, double lng) throws SQLException;

}
