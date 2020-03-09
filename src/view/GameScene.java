package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * GameScene.java
 *
 * Purpose: Represents a scene in the game.
 */
public abstract class GameScene extends Canvas
{
    private GraphicsContext paintBrush;


    /**
     * GameScene ()
     *
     * Purpose: Super-constructor that creates and initializes a GameScene.
     */
    protected GameScene ()
    {
        this.paintBrush = this.getGraphicsContext2D();
    }


    /**
     * start()
     *
     * Purpose: Defines what will happen when this GameScene is started.
     */
    public abstract void start ();


    /**
     * getPaintBrush()
     *
     * Purpose: Returns the GraphicsContext of the canvas to allow drawing.
     */
    public GraphicsContext getPaintBrush ()
    {
        return this.paintBrush;
    } // getPaintBrush()

} // abstract class GameScene
