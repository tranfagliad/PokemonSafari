package controller.audio;

/**
 *
 */
public enum SfxLibrary
{
    Run ("run"),
    Select ("select");


    private String filename;

    private SfxLibrary (String filename)
    {
        this.filename = filename;
    }

}
