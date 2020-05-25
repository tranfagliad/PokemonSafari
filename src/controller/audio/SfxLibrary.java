package controller.audio;

/**
 *
 */
public enum SfxLibrary
{
    Run ("run"),
    Select ("select"),
    Pokeball_Open ("pokeball_open"),
    Throw ("throw");


    private String filename;

    SfxLibrary (String filename)
    {
        this.filename = filename;
    }

}
