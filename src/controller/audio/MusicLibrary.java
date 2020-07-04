package controller.audio;

/**
 * MusicLibrary.java
 *
 * Purpose: Contains music file names.
 */
public enum MusicLibrary {

    Wild_Area_1 ("wild_area_1"),
    Wild_Area_2 ("wild_area_2"),
    Battle ("battle");


    private String filename;

    MusicLibrary (String filename)
    {
        this.filename = filename;
    }

}
