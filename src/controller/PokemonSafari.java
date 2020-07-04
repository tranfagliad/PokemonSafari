package controller;

import controller.audio.CryPlayer;
import controller.audio.MusicPlayer;
import controller.audio.SfxPlayer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.player.Player;
import view.GameScene;
import view.NameEntryScene;
import view.OverworldScene;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * PokemonSafari.java
 *
 * Purpose: The central module of the Pokemon Safari game.
 */
public final class PokemonSafari extends Application
{
    private static final String ICON_FILENAME = "images/icons/safari_ball.png";
    private static final String WINDOW_TITLE = "Pokemon Safari";

    private static final double WINDOW_WIDTH = 352 * 2.5;
    private static final double WINDOW_HEIGHT = 288 * 2.5;

    private static Pane root;


    /**
     * main()
     *
     * Purpose: Launches the application.
     */
    public static void main (final String[] args)
    {
        Application.launch();
    } // main()


    /**
     * start()
     *
     * Purpose: Initializes and starts the game.
     */
    @Override
    public void start (final Stage window) throws Exception
    {
        initWindow(window);
        initMediaPlayers();
        window.show();
        PokemonSafari.goToNextScene(new OverworldScene(new Player("you")));
    } // start()


    /**
     * initWindow()
     *
     * Purpose: Initializes the game window.
     */
    private static void initWindow (final Stage window)
    {
        root = new Pane();
        final Scene scene = new Scene(root);
        try { window.getIcons().add(new Image(new FileInputStream(ICON_FILENAME))); }
        catch (IOException e) { e.printStackTrace(); }
        window.setTitle(WINDOW_TITLE);
        window.setWidth(WINDOW_WIDTH);
        window.setHeight(WINDOW_HEIGHT);
        window.initStyle(StageStyle.UNDECORATED);
        window.setScene(scene);
    } // initWindow()


    /**
     * initMediaPlayers()
     *
     * Purpose: Calls getInstance on all media players to initialize them.
     */
    private void initMediaPlayers ()
    {
        CryPlayer.getInstance();
        MusicPlayer.getInstance();
        SfxPlayer.getInstance();
    } // initMediaPlayers()


    /**
     * getWindowWidth()
     *
     * Purpose: Return the window width.
     */
    public static double getWindowWidth ()
    {
        return WINDOW_WIDTH;
    } // getWindowWidth()


    /**
     * getWindowHeight()
     *
     * Purpose: Returns the window height.
     */
    public static double getWindowHeight ()
    {
        return WINDOW_HEIGHT;
    } // getWindowHeight()


    /**
     * goToNextScene()
     *
     * Purpose: Starts the given next scene.
     */
    public static void goToNextScene (final GameScene nextScene)
    {
        GameSceneManager.addScene(nextScene);
        root.getChildren().add(nextScene);
        GameSceneManager.getActiveScene().start();
    } // goToNextScene()


    /**
     * goToPreviousScene()
     *
     * Purpose: Returns back to the previous scene.
     */
    public static void goToPreviousScene ()
    {
        GameSceneManager.removeScene();
        root.getChildren().remove(root.getChildren().size()-1);
        GameSceneManager.getActiveScene().restart();
    } // goToPreviousScene()

} // final class PokemonSafari
