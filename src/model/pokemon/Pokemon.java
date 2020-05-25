package model.pokemon;

/**
 * Pokemon.java
 *
 * Purpose: Model for a Pokemon in the game.
 */
public final class Pokemon
{
    private static final int MIN_PERCENT = 20;
    private static final int ONE_HUNDRED_PERCENT = 100;

    private int id;
    private String name;
    private int hp;
    private int level;
    private Gender gender;
    private int catchLikelihood;
    private int runLikelihood;
    private int maxDuration;


    /**
     * Pokemon (int, String, int, int, int)
     *
     * Purpose: Creates a new Pokemon with the given parameters.
     */
    public Pokemon(int id, String name, int hp, int level, Gender gender,
                   int catchLikelihood, int runLikelihood, int maxDuration)
    {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.level = level;
        this.gender = gender;
        this.setCatchLikelihood(catchLikelihood);
        this.setRunLikelihood(runLikelihood);
        this.maxDuration = maxDuration;
    } // Pokemon (String, int, int, int)


    /**
     * getID()
     *
     * Purpose: Returns the ID of the Pokemon.
     */
    public int getID()
    {
        return this.id;
    } // getID()


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
     * getLevel()
     *
     * Purpose: Returns the level of the Pokemon.
     */
    public int getLevel ()
    {
        return this.level;
    } // getLevel()


    /**
     * getGender()
     *
     * Purpose: Returns the gender of the Pokemon.
     */
    public Gender getGender ()
    {
        return this.gender;
    } // getGender()


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
        this.catchLikelihood = newCatchLikelihood < MIN_PERCENT ?
                MIN_PERCENT : newCatchLikelihood > ONE_HUNDRED_PERCENT ?
                ONE_HUNDRED_PERCENT : newCatchLikelihood;
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
        this.runLikelihood = newRunLikelihood < MIN_PERCENT ?
                MIN_PERCENT : newRunLikelihood > ONE_HUNDRED_PERCENT ?
                ONE_HUNDRED_PERCENT : newRunLikelihood;
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

} // final class Pokemon
