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
     *
     */
    protected GameScene ()
    {
        this.paintBrush = this.getGraphicsContext2D();
    }


    /**
     * drawFrame()
     *
     * Purpose: Draws a single frame of the scene.
     */
    public abstract void drawFrame ();


    /**
     *
     */
    public GraphicsContext getPaintBrush ()
    {
        return this.paintBrush;
    }

} // abstract class GameScene
