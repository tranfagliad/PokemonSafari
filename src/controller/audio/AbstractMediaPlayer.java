package controller.audio;

import javafx.scene.media.AudioClip;

import java.nio.file.Paths;

/**
 * AbstractMediaPlayer.java
 *
 * Purpose: Represents an media player that can play and loop audio files.
 *      Subclasses: CryPlayer, MusicPlayer, SfxPlayer.
 */
public abstract class AbstractMediaPlayer
{
    private static final String FILE_FORMAT = ".wav";

    private String filepath;


    /**
     * AbstractMediaPlayer (String)
     *
     * Purpose: Super-constructor that creates and initializes a media player with the
     *      given filepath to a folder of audio files.
     */
    protected AbstractMediaPlayer (final String filepath)
    {
        this.filepath = filepath;
    }


    /**
     * play()
     *
     * Purpose: Plays the given audio file one time.
     */
    public void play (final String audioFilename)
    {
        final AudioClip audioClip = new AudioClip(Paths.get(this.filepath+audioFilename+FILE_FORMAT).toUri().toString());
        audioClip.setCycleCount(1);
        audioClip.play();
    } // play()


    /**
     * loop()
     *
     * Purpose: Plays the given audio file in a loop.
     */
    public void loop (final String audioFilename)
    {
        final AudioClip audioClip = new AudioClip(Paths.get(this.filepath+audioFilename+FILE_FORMAT).toUri().toString());
        audioClip.setCycleCount(AudioClip.INDEFINITE);
        audioClip.play();
    } // loop()

} // abstract class AbstractMediaPlayer
