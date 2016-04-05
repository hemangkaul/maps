package edu.brown.cs.hk125.maps;

import java.sql.SQLException;

public interface Tiler {

  public void setTiles() throws SQLException;

  public Tile getTile() throws SQLException;

}
