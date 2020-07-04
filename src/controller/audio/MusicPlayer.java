package controller.audio;

/**
 * MusicPlayer.java
 *
 * Purpose: Media player singleton to play background music.
 */
public final class MusicPlayer extends AbstractMediaPlayer
{


    private static MusicPlayer instance = null;


    /**
     * MusicPlayer ()
     *
     * Purpose: Creates and initializes the MusicPlayer.
     */
    private MusicPlayer ()
    {
        super("audio/music/");
    } // MusicPlayer ()


    /**
     * getInstance()
     *
     * Purpose: Returns the only instance of MusicPlayer.
     */
    public static MusicPlayer getInstance ()
    {
        return instance != null ? instance : new MusicPlayer();
    } // getInstance()

} // final class MusicPlayer
