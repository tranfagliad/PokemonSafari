package view;

import javafx.event.EventHandler;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * NameEntryScene.java
 *
 * Purpose: First scene in the game to allow the player to enter a name.
 */
public final class NameEntryScene extends GameScene
{
    private static final Font SMALLER_FONT = Font.font("Verdana", 15);
    private static final Font SMALL_FONT = Font.font("Verdana", 20);
    private static final Font MEDIUM_FONT = Font.font("Verdana", 28);
    private static final Font BIG_FONT = Font.font("Verdana", 35);

    private static final int MAX_CHARS = 10;


    @Override
    public void start ()
    {
        getPaintBrush().setStroke(Color.WHITE);
        drawFrame();
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Pokemon Safari");
        textInputDialog.setContentText("Enter your name:");
        textInputDialog.setHeaderText("New Game");
        textInputDialog.setOnCloseRequest(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent event) {
                System.exit(0);
            }
        });

        Optional<String> name = textInputDialog.showAndWait();
        System.out.println(name);
    }

    @Override
    public void restart () { /* Nothing */ }


    /**
     *
     */
    private void drawFrame ()
    {
        getPaintBrush().setFill(Color.BLACK);
        getPaintBrush().fillRect(0, 0, getWidth(), getHeight());
        getPaintBrush().setFill(Color.WHITE);
        getPaintBrush().setFont(BIG_FONT);
        getPaintBrush().setTextAlign(TextAlignment.CENTER);
        getPaintBrush().fillText("Controls", getWidth()/2, 80);

        getPaintBrush().setFont(MEDIUM_FONT);
        getPaintBrush().fillText("Overworld", 125, 200);
        getPaintBrush().fillText("Battle", 425, 200);
        getPaintBrush().fillText("Collection View", 725, 200);

        getPaintBrush().strokeRect(100, 250, 50, 50);
        getPaintBrush().strokeRect(50, 300, 50, 50);
        getPaintBrush().strokeRect(100, 300, 50, 50);
        getPaintBrush().strokeRect(150, 300, 50, 50);

        getPaintBrush().strokeRect(400, 250, 50, 50);
        getPaintBrush().strokeRect(350, 300, 50, 50);
        getPaintBrush().strokeRect(400, 300, 50, 50);
        getPaintBrush().strokeRect(450, 300, 50, 50);

        getPaintBrush().strokeRect(700, 250, 50, 50);
        getPaintBrush().strokeRect(700, 300, 50, 50);

        getPaintBrush().strokeRect(50, 450, 150, 50);
        getPaintBrush().strokeRect(50, 600, 150, 50);
        getPaintBrush().strokeRect(350, 450, 150, 50);
        getPaintBrush().strokeRect(700, 450, 50, 50);

        getPaintBrush().strokeLine(270, 160, 270, 680);
        getPaintBrush().strokeLine(560, 160, 560, 680);

        getPaintBrush().setFont(SMALL_FONT);
        getPaintBrush().fillText("W", 125, 280);
        getPaintBrush().fillText("A", 75, 330);
        getPaintBrush().fillText("S", 125, 330);
        getPaintBrush().fillText("D", 175, 330);
        getPaintBrush().fillText("Enter", 125, 480);
        getPaintBrush().fillText("Space", 125, 630);

        getPaintBrush().fillText("W", 425, 280);
        getPaintBrush().fillText("A", 375, 330);
        getPaintBrush().fillText("S", 425, 330);
        getPaintBrush().fillText("D", 475, 330);
        getPaintBrush().fillText("Space", 425, 480);

        getPaintBrush().fillText("W", 725, 280);
        getPaintBrush().fillText("S", 725, 330);
        getPaintBrush().fillText("Z", 725, 480);

        getPaintBrush().setFont(SMALLER_FONT);
        getPaintBrush().fillText("North", 125, 240);
        getPaintBrush().fillText("West", 60, 370);
        getPaintBrush().fillText("South", 125, 370);
        getPaintBrush().fillText("East", 190, 370);

        getPaintBrush().fillText("Up", 425, 240);
        getPaintBrush().fillText("Left", 360, 370);
        getPaintBrush().fillText("Down", 425, 370);
        getPaintBrush().fillText("Right", 490, 370);

        getPaintBrush().fillText("Up", 725, 240);
        getPaintBrush().fillText("Down", 725, 370);

        getPaintBrush().fillText("Open Menu", 125, 520);
        getPaintBrush().fillText("Select (In Menu)", 125, 670);
        getPaintBrush().fillText("Select", 425, 520);
        getPaintBrush().fillText("Back", 725, 520);
    } // drawFrame()

} // final class NameEntryScene
