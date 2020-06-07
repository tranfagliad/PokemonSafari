package controller.audio;

/**
 * SfxLibrary.java
 *
 * Purpose: Contains sound effect file names.
 */
public enum SfxLibrary
{
    Run ("run"),
    Select ("select"),
    Pokeball_Open ("pokeball_open"),
    Throw ("throw"),
    Menu ("menu"),
    Pokeball_Contact ("pokeball_contact"),
    Rock ("rock"),
    Save ("save");


    private String filename;

    SfxLibrary (String filename)
    {
        this.filename = filename;
    }

}
