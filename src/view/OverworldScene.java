package view;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import model.map.Map;
import model.map.MapBuilder;
import model.player.Player;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * OverworldScene.java
 *
 * Purpose: Displays and operates the overworld scene that
 *      the player walks around in.
 */
public final class OverworldScene extends GameScene
{
    private static final String PLAYER_IMAGE_FILENAME   = "images/overworld/trainer_sprites.png";
    private static final String TILE_IMAGE_FILENAME = "images/overworld/tile_sprites.png";

    private static final int CAMERA_X_RANGE = 11;
    private static final int CAMERA_Y_RANGE = 9;


    private Image playerImages;
    private Image tileImages;

    private Map map;
    private Player player;

    private int cameraX;
    private int cameraY;


    public OverworldScene (final Player player)
    {
        super();
        this.map = MapBuilder.createMap();

        this.player = player;
        this.player.getPosition().setX(5);
        this.player.getPosition().setY(5);

        this.cameraX = this.player.getPosition().getX()-5;
        this.cameraY = this.player.getPosition().getY()-4;

        try {
            this.playerImages = new Image(new FileInputStream(PLAYER_IMAGE_FILENAME));
            this.tileImages = new Image(new FileInputStream(TILE_IMAGE_FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * start()
     *
     * Purpose: Defines how the OverworldScene starts.
     */
    @Override
    public void start ()
    {
        //System.out.println("Camera: (x="+cameraX+", y="+cameraY+")");
        //System.out.println("Player: (x="+player.getPosition().getX()+", y="+player.getPosition().getY()+")");
        drawFrame();

        setupControls();
    } // start()



    private void drawFrame ()
    {
        for (int y = 0; y < CAMERA_Y_RANGE; y++)
        {
            for (int x = 0; x < CAMERA_X_RANGE; x++)
            {
                //System.out.print(map.getTile(y+cameraY, x+cameraX).getID());
                getPaintBrush().drawImage(tileImages,
                        (map.getTile(y+cameraY, x+cameraX).getID()%3)*32,
                        (map.getTile(y+cameraY, x+cameraX).getID()/3)*32,
                        32, 32, x*80, y*80, 80, 80);
            }
            //System.out.println();
        }
        getPaintBrush().drawImage(playerImages,
                0*32, 2*32, 32,32, 5*80, 4*80, 80, 80);
        //System.out.println();
    }


    private void setupControls ()
    {
        this.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent event)
            {
                switch (event.getCode())
                {
                    case W:
                        if (map.getTile(player.getPosition().getY()-1, player.getPosition().getX()).isWalkable())
                        {
                            cameraY--;
                            player.getPosition().setY(player.getPosition().getY()-1);
                        }
                        break;
                    case A:
                        if (map.getTile(player.getPosition().getY(), player.getPosition().getX()-1).isWalkable())
                        {
                            cameraX--;
                            player.getPosition().setX(player.getPosition().getX()-1);
                        }
                        break;
                    case S:
                        if (map.getTile(player.getPosition().getY()+1, player.getPosition().getX()).isWalkable())
                        {
                            cameraY++;
                            player.getPosition().setY(player.getPosition().getY()+1);
                        }
                        break;
                    case D:
                        if (map.getTile(player.getPosition().getY(), player.getPosition().getX()+1).isWalkable())
                        {
                            cameraX++;
                            player.getPosition().setX(player.getPosition().getX()+1);
                        }
                        break;
                }

                drawFrame();
            }
        });
    }


} // final class OverworldScene
