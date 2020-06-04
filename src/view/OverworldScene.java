package view;

import controller.PokemonSafari;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import model.map.Map;
import model.map.MapBuilder;
import model.player.Player;
import model.pokemon.PokemonFactory;
import model.pokemon.Rarity;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

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

    private static final double MOVEMENT_SPEED = 0.05;

    private static final int ONE_HUNDRED_PERCENT = 100;
    private static final int WILD_ENCOUNTER_CHANCE = 15;

    private static final double TILE_SIZE = 80.0;

    private static final int CAMERA_X_RANGE = 11;
    private static final int CAMERA_Y_RANGE = 9;
    private static final int PLAYER_X_OFFSET = 5;
    private static final int PLAYER_Y_OFFSET = 4;


    private Image playerImages;
    private Image tileImages;

    private Map map;
    private Player player;

    private int cameraX;
    private int cameraY;

    private double playerX;
    private double playerY;


    public OverworldScene (final Player player)
    {
        super();
        this.map = MapBuilder.createMap();

        this.player = player;
        this.player.getPosition().setX(8);
        this.player.getPosition().setY(16);
        this.playerX = 0;
        this.playerY = 2;

        this.cameraX = this.player.getPosition().getX()-PLAYER_X_OFFSET;
        this.cameraY = this.player.getPosition().getY()-PLAYER_Y_OFFSET;

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
        drawFrame();

        setupControls();
    } // start()



    private void drawFrame ()
    {
        for (int y = 0; y < CAMERA_Y_RANGE; y++)
        {
            for (int x = 0; x < CAMERA_X_RANGE; x++)
            {
                getPaintBrush().drawImage(tileImages,
                        (double)(map.getTile(y+(int)cameraY, x+(int)cameraX).getID()%3)*32.0,
                        (double)(map.getTile(y+(int)cameraY, x+(int)cameraX).getID()/3)*32.0,
                        32, 32, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
        getPaintBrush().drawImage(playerImages,
                this.playerX*32, this.playerY*32,
                32,32, PLAYER_X_OFFSET*TILE_SIZE, PLAYER_Y_OFFSET*TILE_SIZE,
                TILE_SIZE, TILE_SIZE);
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
                        playerX = 0;
                        playerY = 0;
                        if (map.getTile(player.getPosition().getY()-1, player.getPosition().getX()).isWalkable())
                            new WalkNorthAnimation().start();
                        break;
                    case A:
                        playerX = 0;
                        playerY = 1;
                        if (map.getTile(player.getPosition().getY(), player.getPosition().getX()-1).isWalkable())
                            new WalkWestAnimation().start();
                        break;
                    case S:
                        playerX = 0;
                        playerY = 2;
                        if (map.getTile(player.getPosition().getY()+1, player.getPosition().getX()).isWalkable())
                            new WalkSouthAnimation().start();
                        break;
                    case D:
                        playerX = 0;
                        playerY = 3;
                        if (map.getTile(player.getPosition().getY(), player.getPosition().getX()+1).isWalkable())
                            new WalkEastAnimation().start();
                        break;
                }
                drawFrame();
            }
        });
    }



    private void checkForWildEncounter ()
    {
        if (this.map.getTile(player.getPosition().getY(), player.getPosition().getX()).canEncounterPokemon())
        {
            final Random random = new Random();
            final int encounterChance = random.nextInt(ONE_HUNDRED_PERCENT);
            if (encounterChance < WILD_ENCOUNTER_CHANCE)
            {
                this.getScene().setOnKeyPressed(null);
                final int rarityChance = random.nextInt(ONE_HUNDRED_PERCENT);
                Rarity selectedRarity;
                if (rarityChance < 70)
                    selectedRarity = Rarity.Common;
                else if (rarityChance < 95)
                    selectedRarity = Rarity.Uncommon;
                else
                    selectedRarity = Rarity.Rare;
                PokemonSafari.goToNextScene(new BattleScene(this.player, PokemonFactory.getPokemon(selectedRarity)));
            }
            else
                setupControls();
        }
        else
            setupControls();
    }







    private final class WalkNorthAnimation extends AnimationTimer
    {
        private double yChange;

        private int frames;
        private int cameraXSnapshot;
        private int cameraYSnapshot;


        private WalkNorthAnimation ()
        {
            getScene().setOnKeyPressed(null);
            this.frames = 0;
            this.yChange = 0.0;
            this.cameraXSnapshot = cameraX;
            this.cameraYSnapshot = cameraY;
        }


        @Override
        public void handle (long now)
        {
            this.frames++;
            if (this.yChange < 1.0)
                this.yChange += MOVEMENT_SPEED;
            else
            {
                this.stop();
                cameraY--;
                player.getPosition().setY(player.getPosition().getY()-1);
                checkForWildEncounter();
            }
            for (int y = -1; y < CAMERA_Y_RANGE; y++)
            {
                for (int x = 0; x < CAMERA_X_RANGE; x++)
                {
                    getPaintBrush().drawImage(tileImages,
                            (map.getTile(cameraYSnapshot+y, cameraXSnapshot+x).getID()%3)*32,
                            (map.getTile(cameraYSnapshot+y, cameraXSnapshot+x).getID()/3)*32,
                            32, 32,
                            x*TILE_SIZE, (y*TILE_SIZE)+(yChange*TILE_SIZE), TILE_SIZE, TILE_SIZE);
                }
            }
            playerX = this.frames < 11 ? 1 : this.frames < 21 ? 2 : 0;
            getPaintBrush().drawImage(playerImages,
                    playerX*32, playerY*32,
                    32,32, PLAYER_X_OFFSET*TILE_SIZE, PLAYER_Y_OFFSET*TILE_SIZE,
                    TILE_SIZE, TILE_SIZE);
        }
    }







    private final class WalkEastAnimation extends AnimationTimer
    {
        private double xChange;

        private int frames;
        private int cameraXSnapshot;
        private int cameraYSnapshot;


        private WalkEastAnimation ()
        {
            getScene().setOnKeyPressed(null);
            this.frames = 0;
            this.xChange = 0.0;
            this.cameraXSnapshot = cameraX;
            this.cameraYSnapshot = cameraY;
        }


        @Override
        public void handle (long now)
        {
            this.frames++;
            if (this.xChange < 1.0)
                this.xChange += MOVEMENT_SPEED;
            else
            {
                this.stop();
                cameraX++;
                player.getPosition().setX(player.getPosition().getX()+1);
                checkForWildEncounter();
            }
            for (int y = 0; y < CAMERA_Y_RANGE; y++)
            {
                for (int x = 0; x < CAMERA_X_RANGE+1; x++)
                {
                    getPaintBrush().drawImage(tileImages,
                            (map.getTile(cameraYSnapshot+y, cameraXSnapshot+x).getID()%3)*32,
                            (map.getTile(cameraYSnapshot+y, cameraXSnapshot+x).getID()/3)*32,
                            32, 32,
                            (x*TILE_SIZE)-(xChange*TILE_SIZE), y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
            playerX = this.frames < 11 ? 1 : this.frames < 21 ? 2 : 0;
            getPaintBrush().drawImage(playerImages,
                    playerX*32, playerY*32,
                    32,32, PLAYER_X_OFFSET*TILE_SIZE, PLAYER_Y_OFFSET*TILE_SIZE,
                    TILE_SIZE, TILE_SIZE);

        }
    }










    private final class WalkSouthAnimation extends AnimationTimer
    {
        private double yChange;

        private int frames;
        private int cameraXSnapshot;
        private int cameraYSnapshot;


        private WalkSouthAnimation ()
        {
            getScene().setOnKeyPressed(null);
            this.frames = 0;
            this.yChange = 0.0;
            this.cameraXSnapshot = cameraX;
            this.cameraYSnapshot = cameraY;
        }


        @Override
        public void handle (long now)
        {
            this.frames++;
            if (this.yChange < 1.0)
                this.yChange += MOVEMENT_SPEED;
            else
            {
                this.stop();
                cameraY++;
                player.getPosition().setY(player.getPosition().getY()+1);
                checkForWildEncounter();
            }
            for (int y = 0; y < CAMERA_Y_RANGE+1; y++)
            {
                for (int x = 0; x < CAMERA_X_RANGE; x++)
                {
                    getPaintBrush().drawImage(tileImages,
                            (map.getTile(cameraYSnapshot+y, cameraXSnapshot+x).getID()%3)*32,
                            (map.getTile(cameraYSnapshot+y, cameraXSnapshot+x).getID()/3)*32,
                            32, 32,
                            x*TILE_SIZE, (y*TILE_SIZE)-(yChange*TILE_SIZE), TILE_SIZE, TILE_SIZE);
                }
            }
            playerX = this.frames < 11 ? 1 : this.frames < 21 ? 2 : 0;
            getPaintBrush().drawImage(playerImages,
                    playerX*32, playerY*32,
                    32,32, PLAYER_X_OFFSET*TILE_SIZE, PLAYER_Y_OFFSET*TILE_SIZE,
                    TILE_SIZE, TILE_SIZE);

        }
    }






    private final class WalkWestAnimation extends AnimationTimer
    {
        private double xChange;

        private int frames;
        private int cameraXSnapshot;
        private int cameraYSnapshot;


        private WalkWestAnimation ()
        {
            getScene().setOnKeyPressed(null);
            this.frames = 0;
            this.xChange = 0.0;
            this.cameraXSnapshot = cameraX;
            this.cameraYSnapshot = cameraY;
        }


        @Override
        public void handle (long now)
        {
            this.frames++;
            if (this.xChange < 1.0)
                this.xChange += MOVEMENT_SPEED;
            else
            {
                this.stop();
                cameraX--;
                player.getPosition().setX(player.getPosition().getX()-1);
                checkForWildEncounter();
            }
            for (int y = 0; y < CAMERA_Y_RANGE; y++)
            {
                for (int x = -1; x < CAMERA_X_RANGE; x++)
                {
                    getPaintBrush().drawImage(tileImages,
                            (map.getTile(cameraYSnapshot+y, cameraXSnapshot+x).getID()%3)*32,
                            (map.getTile(cameraYSnapshot+y, cameraXSnapshot+x).getID()/3)*32,
                            32, 32,
                            (x*TILE_SIZE)+(xChange*TILE_SIZE), y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
            playerX = this.frames < 11 ? 1 : this.frames < 21 ? 2 : 0;
            getPaintBrush().drawImage(playerImages,
                    playerX*32, playerY*32,
                    32,32, PLAYER_X_OFFSET*TILE_SIZE, PLAYER_Y_OFFSET*TILE_SIZE,
                    TILE_SIZE, TILE_SIZE);

        }
    }

} // final class OverworldScene
