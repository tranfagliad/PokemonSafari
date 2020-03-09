package model.map.tile;

/**
 * TreeTile.java
 *
 * Purpose: Represents a tile with a piece of a tree.
 *      The piece corresponds to the ID value:
 *          ID == 3: Top-Left
 *          ID == 4: Top-Right
 *          ID == 5: Bottom-Left
 *          ID == 6: Bottom-Right
 */
public final class TreeTile extends AbstractTile
{
    private static final String ILLEGAL_ID_MSG = "ID must be between 3 and 6, inclusive.";


    /**
     * TreeTile (int)
     *
     * Purpose:
     */
    public TreeTile (final int id)
    {
        super(id, false, false);
        if (id < 3 || id > 6)
            throw new IllegalArgumentException(ILLEGAL_ID_MSG);
    } // TreeTile (int)


    /**
     * toString()
     *
     * Purpose: Returns the String representation of the TreeTile.
     */
    public String toString ()
    {
        String retStr = this.getClass().getSimpleName()+": { id: "+super.getID()+", isWalkable: "+super.isWalkable()+", canEncounterPokemon: "+super.canEncounterPokemon();
        retStr += ", piece: "+(super.getID() == 3 ? "top-left" : super.getID() == 4 ? "top-right" : super.getID() == 5 ? "bottom-left" : "bottom-right")+" }";
        return retStr;
    } // toString()

} // final TreeTile (int)
