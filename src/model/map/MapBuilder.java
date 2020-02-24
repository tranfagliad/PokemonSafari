package model.map;

import model.map.tile.AbstractTile;
import model.map.tile.TileFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * MapBuilder.java
 *
 * Purpose: Used to create Maps for the game.
 */
public class MapBuilder
{
    /**
     * createMap()
     *
     * Purpose: Creates and returns a Map by reading and parsing data from a file.
     */
    public static Map createMap ()
    {
        final String filename = "data/map/map1.txt";
        List<List<AbstractTile>> tiles = new ArrayList<>();

        String line;
        int currRow = -1;
        try {
            BufferedReader bf = new BufferedReader(new FileReader(filename));
            while ((line = bf.readLine()) != null)
            {
                tiles.add(new ArrayList<>());
                currRow++;
                final String[] mapDataStrs = line.split(" ");
                for (int i = 0; i < mapDataStrs.length; i++)
                {
                    final int tileID = Integer.parseInt(mapDataStrs[i]);
                    tiles.get(currRow).add(TileFactory.getTile(tileID));
                }
            }
            bf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Map(tiles);
    } // createMap()

} // class MapBuilder
