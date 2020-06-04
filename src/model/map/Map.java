package model.map;

import model.map.tile.AbstractTile;

import java.util.List;

/**
 * Map.java
 *
 * Purpose: Represents a map of tiles in the game.
 */
public final class Map
{
    private static final int MIN_SIZE = 11;
    private static final String ILLEGAL_SIZE_MSG = "Map must be a square grid, and at least "+MIN_SIZE+"x"+MIN_SIZE+" in size.";

    private List<List<AbstractTile>> tiles;


    /**
     * Map (List<List<AbstractTile>>)
     *
     * Purpose: Creates and initializes a Map with the given grid of tiles.
     *      The width and height of the grid must be the same.
     */
    public Map (final List<List<AbstractTile>> tiles)
    {
        if (tiles.size() < MIN_SIZE)
            throw new IllegalArgumentException(ILLEGAL_SIZE_MSG);
        for (List<AbstractTile> row : tiles)
            if (tiles.size() != row.size())
                throw new IllegalArgumentException(ILLEGAL_SIZE_MSG);
        this.tiles = tiles;
    } // Map (List<List<AbstractTile>>)


    /**
     * getTile()
     *
     * Purpose: Returns the tile at the specified row and column.
     */
    public AbstractTile getTile (final int row, final int column)
    {
        return this.tiles.get(row).get(column);
    } // getTile()


    /**
     * getSize()
     *
     * Purpose: Returns the size of the Map. Since the Map must be a grid,
     *      the width and height are the same size.
     */
    public int getSize ()
    {
        return this.tiles.size();
    } // getSize()


    /**
     * toIDs()
     *
     * Purpose: Returns a String that displays a grid of the tile IDs.
     */
    public String toIDs ()
    {
        String retStr = "";
        for (List<AbstractTile> row : this.tiles)
        {
            for (AbstractTile tile : row)
                retStr += tile.getID()+" ";
            retStr += "\n";
        }
        return retStr;
    } // toIDs()


    /**
     * toString()
     *
     * Purpose: Returns the String representation of the Map.
     */
    public String toString ()
    {
        String retStr = "{ size: "+this.getSize()+"x"+this.getSize()+",\n  tiles: [\n";
        for (List<AbstractTile> row : this.tiles)
        {
            retStr += "   ";
            for (AbstractTile tile : row)
                retStr += " "+tile.getName()+" ";
            retStr += "\n";
        }
        retStr += "  ]\n}";
        return retStr;
    } // toString()

} // final class Map
