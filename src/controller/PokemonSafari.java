package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.player.Player;
import model.pokemon.PokemonFactory;
import model.pokemon.Rarity;
import view.BattleScene;
import view.GameScene;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * PokemonSafari.java
 *
 * Purpose: The central module of the Pokemon Safari game.
 */
public class PokemonSafari extends Application
{
    private static final String ICON_FILENAME = "images/icons/safari_ball.png";
    private static final String WINDOW_TITLE = "Pokemon Safari";

    private static final double WINDOW_WIDTH = 352 * 2.5;
    private static final double WINDOW_HEIGHT = 288 * 2.5;

    private static Pane root;
    private static Scene scene;


    /**
     * main()
     *
     * Purpose: Launches the application.
     */
    public static void main (String[] args)
    {
        Application.launch();
    } // main()


    /**
     * start()
     *
     * Purpose: Initializes and starts the game.
     */
    @Override
    public void start (Stage window) throws Exception
    {
        initWindow(window);
        PokemonSafari.goToNextScene(new BattleScene(new Player("Daniel"), PokemonFactory.getPokemon(Rarity.Common)));
        window.show();
    } // start()


    /**
     * initWindow()
     *
     * Purpose: Initializes the game window.
     */
    private static void initWindow (Stage window)
    {
        root = new Pane();
        scene = new Scene(root);
        try { window.getIcons().add(new Image(new FileInputStream(ICON_FILENAME))); }
        catch (IOException e) { e.printStackTrace(); }
        window.setTitle(WINDOW_TITLE);
        window.setWidth(WINDOW_WIDTH);
        window.setHeight(WINDOW_HEIGHT);
        window.initStyle(StageStyle.UNDECORATED);
        window.setScene(scene);
    } // initWindow()


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
    public static void goToNextScene (GameScene nextScene)
    {
        GameSceneManager.addScene(nextScene);
        root.getChildren().add(nextScene);
        GameSceneManager.getActiveScene().drawFrame();
    } // goToNextScene()


    /**
     * goToPreviousScene()
     *
     * Purpose: Starts the previous scene.
     */
    public static void goToPreviousScene ()
    {
        root.getChildren().remove(root.getChildren().size()-1);
        GameSceneManager.removeScene();
        GameSceneManager.getActiveScene().drawFrame();
    } // goToPreviousScene()

} // class PokemonSafari
