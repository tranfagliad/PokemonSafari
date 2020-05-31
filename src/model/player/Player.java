package model.player;

import model.pokemon.Pokemon;

import java.util.ArrayList;
import java.util.List;

/**
 * Player.java
 *
 * Purpose: Represents the Player in the game.
 */
public final class Player
{

    private static final int INITIAL_NUM_SAFARI_BALLS = 30;
    private static final int INITIAL_STEPS_REMAINING = 500;

    private String name;
    private int numSafariBalls;
    private int stepsRemaining;
    private Position2D position;
    private List<Pokemon> pokemonCaught;


    /**
     * Player ()
     *
     * Purpose: Creates and initializes a Player with a the given name,
     * 30 safari balls, 500 steps remaining, an empty Pokemon list.
     * The initial position of the Player is (0,0).
     */
    public Player (final String name)
    {
        this.name = name;
        this.numSafariBalls = INITIAL_NUM_SAFARI_BALLS;
        this.stepsRemaining = INITIAL_STEPS_REMAINING;
        this.position = new Position2D();
        this.pokemonCaught = new ArrayList<>();
    } // Player ()


    /**
     * getName()
     *
     * Purpose: Returns the name of the Player.
     */
    public String getName()
    {
        return this.name;
    } // getName()


    /**
     * getNumSafariBalls()
     *
     * Purpose: Returns the number of safari balls that the player has.
     */
    public int getNumSafariBalls ()
    {
        return this.numSafariBalls;
    } // getNumSafariBalls()


    /**
     * setNumSafariBalls()
     *
     * Purpose: Sets the number of safari balls to a new value. The value
     *      cannot be negative.
     */
    public void setNumSafariBalls (final int newNumSafariBalls)
    {
        this.numSafariBalls = newNumSafariBalls < 0 ? 0 : newNumSafariBalls;
    } // setNumSafariBalls()


    /**
     * getStepsRemaining()
     *
     * Purpose: Returns the number of steps that the player has left.
     */
    public int getStepsRemaining ()
    {
        return this.stepsRemaining;
    } // getStepsRemaining()


    /**
     * setStepsRemaining()
     *
     * Purpose: Sets the number of steps that the player has left to a new
     *      value. The new value cannot be negative.
     */
    public void setStepsRemaining (final int newStepsRemaining)
    {
        this.stepsRemaining = newStepsRemaining < 0 ? 0 : newStepsRemaining;
    } // setStepsRemaining()


    /**
     * getPosition()
     *
     * Purpose: Returns the position of the player.
     */
    public Position2D getPosition ()
    {
        return this.position;
    } // getPosition()


    /**
     * getPokemonCaught()
     *
     * Purpose: Returns the list of Pokemon that the player has caught.
     */
    public List<Pokemon> getPokemonCaught ()
    {
        return this.pokemonCaught;
    } // getPokemonCaught()


    /**
     * toString()
     *
     * Purpose: Returns the String representation of the Player.
     */
    public String toString ()
    {
        return "Player: { Balls: "+this.numSafariBalls+", Steps: "+this.stepsRemaining+", Position: "+this.position.toString()+", Pokemon: "+this.pokemonCaught.toString()+" }";
    } // toString()

} // final class Player
