package controller.audio;

/**
 * SfxPlayer.java
 *
 * Purpose: Media player singleton to play sound effects.
 */
public final class SfxPlayer extends AbstractMediaPlayer
{
    private static SfxPlayer instance = null;


    /**
     * SfxPlayer ()
     *
     * Purpose: Creates and initializes the SfxPlayer.
     */
    private SfxPlayer ()
    {
        super("audio/sfx/");
    } // SfxPlayer ()


    /**
     * getInstance()
     *
     * Purpose: Returns the only instance of SfxPlayer.
     */
    public static SfxPlayer getInstance ()
    {
        return instance != null ? instance : new SfxPlayer();
    } // getInstance()

} // final class SfxPlayer
