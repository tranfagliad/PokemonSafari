package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 */
public class PokemonSafariMain extends Application
{
    private static final String WINDOW_TITLE = "Pokemon Safari";
    private static final int WINDOW_WIDTH = 352 * 2;
    private static final int WINDOW_HEIGHT = 288 * 2;


    /**
     *
     */
    public static void main (String[] args)
    {
        Application.launch();
    } // main()


    /**
     *
     */
    @Override
    public void start (Stage window) throws Exception
    {
        initWindow(window);
        window.show();
    } // start()


    /**
     *
     */
    private static void initWindow (Stage window)
    {
        Pane root = new Pane();
        Scene scene = new Scene(root);
        window.setTitle(WINDOW_TITLE);
        window.setWidth(WINDOW_WIDTH);
        window.setHeight(WINDOW_HEIGHT);
        window.setScene(scene);
    } // initWindow()

} // class PokemonSafariMain
