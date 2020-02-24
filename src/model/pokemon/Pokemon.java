package model.pokemon;

/**
 * Pokemon.java
 *
 * Purpose: Model for a Pokemon in the game.
 */
public class Pokemon
{
    private String name;
    private int hp;
    private int catchLikelihood;
    private int runLikelihood;
    private int maxDuration;


    /**
     * Pokemon (String, int, int, int)
     *
     * Purpose: Creates a new Pokemon with the given parameters.
     */
    public Pokemon (String name, int hp, int catchLikelihood, int runLikelihood, int maxDuration)
    {
        this.name = name;
        this.hp = hp;
        this.catchLikelihood = catchLikelihood;
        this.runLikelihood = runLikelihood;
        this.maxDuration = maxDuration;
    } // Pokemon (String, int, int, int)


    /**
     * getName()
     *
     * Purpose: Returns the name of the Pokemon.
     */
    public String getName ()
    {
        return this.name;
    } // getName()


    /**
     * getHp()
     *
     * Purpose: Returns the HP amount of the Pokemon.
     */
    public int getHp ()
    {
        return this.hp;
    } // getHp()


    /**
     * getCatchLikelihood()
     *
     * Purpose: Returns the catch likelihood percentage of the Pokemon.
     */
    public int getCatchLikelihood ()
    {
        return this.catchLikelihood;
    } // getCatchLikelihood()


    /**
     * setCatchLikelihood()
     *
     * Purpose: Sets the catch likelihood percentage to a new value.
     *      The new value must be between 0 and 100.
     */
    public void setCatchLikelihood (int newCatchLikelihood)
    {
        this.catchLikelihood = newCatchLikelihood < 0 ? 0 : newCatchLikelihood > 100 ? 100 : newCatchLikelihood;
    } // setCatchLikelihood()


    /**
     * getRunLikelihood()
     *
     * Purpose: Returns the run likelihood percentage of the Pokemon.
     */
    public int getRunLikelihood ()
    {
        return this.runLikelihood;
    } // getRunLikelihood()


    /**
     * setRunLikelihood()
     *
     * Purpose: Sets the run likelihood percentage to a new value.
     *      The new value must be between 0 and 100.
     */
    public void setRunLikelihood (int newRunLikelihood)
    {
        this.runLikelihood = newRunLikelihood < 0 ? 0 : newRunLikelihood > 100 ? 100 : newRunLikelihood;
    } // setRunLikelihood()


    /**
     * getMaxDuration()
     *
     * Purpose: Returns the maximum turn duration of the Pokemon.
     */
    public int getMaxDuration ()
    {
        return this.maxDuration;
    } // getMaxDuration()


    /**
     * toString()
     *
     * Purpose: Returns the String representation of the Pokemon.
     */
    public String toString ()
    {
        return this.name+": { HP: "+this.hp+", Catch: "+this.catchLikelihood+"%, Run: "+this.runLikelihood+"%, Duration: "+this.maxDuration+" }";
    } // toString()

} // class Pokemon
