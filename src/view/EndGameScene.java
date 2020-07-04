package view;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * EndGameScene.java
 *
 * Purpose: Displays once the player runs out of steps.
 */
public final class EndGameScene extends GameScene
{
    private static final Font BIG_FONT = Font.font("Verdana", 35);


    /**
     * start()
     *
     * Purpose: Starts the EndGameScene.
     */
    @Override
    public void start()
    {
        getPaintBrush().setFont(BIG_FONT);
        getPaintBrush().setTextAlign(TextAlignment.CENTER);
        new EndGameCloseTimer().start();
    } // start()


    @Override
    public void restart() { /* Nothing */ }


    /**
     * EndGameCloseTimer
     *
     * Purpose: Animation timer to close the game.
     */
    private final class EndGameCloseTimer extends AnimationTimer
    {
        private int frames;


        private EndGameCloseTimer ()
        {
            this.frames = 0;
        }


        @Override
        public void handle (final long now)
        {
            this.frames++;
            drawFrame();
            if (this.frames == 300) {
                this.stop();
                System.exit(0);  // Exit with SUCCESS
            }
        }
    } // final class EndGameCloseTimer


    /**
     * drawFrame()
     *
     * Purpose: Draws a single frame of the EndGameScene.
     */
    private void drawFrame ()
    {
        getPaintBrush().setFill(Color.BLACK);
        getPaintBrush().fillRect(0, 0, getWidth(), getHeight());
        getPaintBrush().setFill(Color.YELLOW);
        getPaintBrush().fillText("Thank you for playing!", getWidth()/2, getHeight()/2);
    } // drawFrame()

} // final class EndGameScene
