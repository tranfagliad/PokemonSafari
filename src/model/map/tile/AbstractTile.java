package model.map.tile;

/**
 * AbstractTile.java
 *
 * Purpose: Represents a tile in the game. Top of the tile
 *      hierarchy, constructors of subclasses must be invoked.
 */
public abstract class AbstractTile
{
    private int id;
    private boolean isWalkable;
    private boolean canEncounterPokemon;


    /**
     * AbstractTile (int, boolean, boolean)
     *
     * Purpose: Creates and initializes a tile with the given specifications.
     *      This constructor is invoked by constructors of subclasses, and cannot
     *      be invoked directly.
     */
    protected AbstractTile (final int id, final boolean isWalkable, final boolean canEncounterPokemon)
    {
        this.id = id;
        this.isWalkable = isWalkable;
        this.canEncounterPokemon = canEncounterPokemon;
    } // AbstractTile (int, boolean, boolean)


    /**
     * getName()
     *
     * Purpose: Returns the name of the tile.
     */
    public String getName ()
    {
        return this.getClass().getSimpleName();
    } // getName()


    /**
     * getID()
     *
     * Purpose: Returns the ID of the tile.
     */
    public int getID ()
    {
        return this.id;
    } // getID()


    /**
     * isWalkable()
     *
     * Purpose: Returns the condition of whether the tile can be walked on or not.
     */
    public boolean isWalkable ()
    {
        return this.isWalkable;
    } // isWalkable()


    /**
     * canEncounterPokemon()
     *
     * Purpose: Returns the condition of whether Pokemon can be encountered or not.
     */
    public boolean canEncounterPokemon ()
    {
        return this.canEncounterPokemon;
    } // canEncounterPokemon()


    /**
     * toString()
     *
     * Purpose: Returns the String representation of the tile.
     */
    public String toString ()
    {
        return this.getClass().getSimpleName()+": { id: "+this.id+", isWalkable: "+this.isWalkable+", canEncounterPokemon: "+this.canEncounterPokemon+" }";
    } // toString()

} // abstract class AbstractTile
