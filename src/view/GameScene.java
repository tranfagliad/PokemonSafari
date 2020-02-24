package view;

import javafx.scene.canvas.Canvas;

/**
 * GameScene.java
 *
 * Purpose: Represents a scene in the game.
 */
public abstract class GameScene extends Canvas
{
    /**
     * drawFrame()
     *
     * Purpose: Draws a single frame of the scene.
     */
    public abstract void drawFrame ();

} // abstract class GameScene
