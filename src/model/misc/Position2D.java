package model.misc;

/**
 * Position2D.java
 *
 * Purpose: Represents a position on a 2D grid.
 */
public class Position2D
{
    private int x;
    private int y;


    /**
     * Position2D ()
     *
     * Purpose: Creates a new Position2D where the x and y coordinates
     *      are both initialized to 0.
     */
    public Position2D ()
    {
        this.x = 0;
        this.y = 0;
    } // Position2D ()


    /**
     * Position2D (int, int)
     *
     *  Purpose: Creates a new Position2D with the given parameters.
     */
    public Position2D (final int x, final int y)
    {
        this.x = x;
        this.y = y;
    } // Position2D (int, int)


    /**
     * getX()
     *
     * Purpose: Returns the x coordinate.
     */
    public int getX ()
    {
        return this.x;
    } // getX()


    /**
     * setX()
     *
     * Purpose: Sets the x coordinate to a new value.
     */
    public void setX (final int newX)
    {
        this.x = newX;
    } // setX()


    /**
     * getY()
     *
     * Purpose: Returns the y coordinate.
     */
    public int getY ()
    {
        return this.y;
    } // getY()


    /**
     * setY()
     *
     * Purpose: Sets the y coordinate to a new value.
     */
    public void setY (final int newY)
    {
        this.y = newY;
    } // setY()


    /**
     * toString()
     *
     * Purpose: Returns the String representation of the Position2D.
     */
    public String toString ()
    {
        return "{ x: "+this.x+", y: "+this.y+" }";
    } // toString()

} // class Position2D
