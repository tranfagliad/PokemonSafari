package model.map.tile;

/**
 * TileFactory.java
 *
 * Purpose: Used to create tiles.
 */
public class TileFactory
{
    /**
     * getTile()
     *
     * Purpose: Creates and returns a tile corresponding to the given ID.
     */
    public static AbstractTile getTile (final int id)
    {
        switch (id)
        {
            case 1: return new GrassTile();
            case 2: return new TallGrassTile();
            case 3: /* TreeTile : top-left */
            case 4: /* TreeTile : top-right */
            case 5: /* TreeTile : bottom-left */
            case 6: return new TreeTile(id); /* TreeTile : bottom-right */
            case 7: return new WaterTile();
            case 8: return new WaterBridgeTile();
        }
        return new EmptyTile();
    } // getTile()

} // class TileFactory
