package controller.audio;

/**
 * CryPlayer.java
 *
 * Purpose: Media player singleton to play Pokemon cries.
 */
public final class CryPlayer extends AbstractMediaPlayer
{
    private static CryPlayer instance = null;


    /**
     * CryPlayer ()
     *
     * Purpose: Creates and initializes the CryPlayer.
     */
    private CryPlayer ()
    {
        super("audio/cries/");
    } // CryPlayer ()


    /**
     * getInstance()
     *
     * Purpose: Returns the only instance of CryPlayer.
     */
    public static CryPlayer getInstance ()
    {
        return instance != null ? instance : new CryPlayer();
    } // getInstance()

} // final class CryPlayer
