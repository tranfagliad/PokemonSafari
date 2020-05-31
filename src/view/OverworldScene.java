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

    private static final int ONE_HUNDRED_PERCENT = 100;
    private static final int WILD_ENCOUNTER_CHANCE = 25;

    private static final double TILE_SIZE = 80.0;

    private static final int CAMERA_X_RANGE = 11;
    private static final int CAMERA_Y_RANGE = 9;


    private Image playerImages;
    private Image tileImages;

    private Map map;
    private Player player;

    private double cameraX;
    private double cameraY;

    private double playerX;
    private double playerY;


    public OverworldScene (final Player player)
    {
        super();
        this.map = MapBuilder.createMap();

        this.player = player;
        this.player.getPosition().setX(8);
        this.player.getPosition().setY(6);
        this.playerX = 0;
        this.playerY = 2;

        this.cameraX = (double)(this.player.getPosition().getX()-5);
        this.cameraY = (double)(this.player.getPosition().getY()-4);

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
                getPaintBrush().drawImage(tileImages,
                        (double)(map.getTile(y+(int)cameraY, x+(int)cameraX).getID()%3)*32.0,
                        (double)(map.getTile(y+(int)cameraY, x+(int)cameraX).getID()/3)*32.0,
                        32, 32, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
        getPaintBrush().drawImage(playerImages,
                this.playerX*32, this.playerY*32,
                32,32, 5*TILE_SIZE, 4*TILE_SIZE, TILE_SIZE, TILE_SIZE);
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
                        {
                            cameraY--;
                            player.getPosition().setY(player.getPosition().getY()-1);
                        }
                        break;
                    case A:
                        playerX = 0;
                        playerY = 1;
                        if (map.getTile(player.getPosition().getY(), player.getPosition().getX()-1).isWalkable())
                        {
                            cameraX--;
                            player.getPosition().setX(player.getPosition().getX()-1);
                        }
                        break;
                    case S:
                        playerX = 0;
                        playerY = 2;
                        if (map.getTile(player.getPosition().getY()+1, player.getPosition().getX()).isWalkable())
                        {
                            cameraY++;
                            player.getPosition().setY(player.getPosition().getY()+1);
                        }
                        break;
                    case D:
                        playerX = 0;
                        playerY = 3;
                        if (map.getTile(player.getPosition().getY(), player.getPosition().getX()+1).isWalkable())
                        {
                            cameraX++;
                            player.getPosition().setX(player.getPosition().getX()+1);
                        }
                        break;
                }

                drawFrame();
                checkForWildEncounter();
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
                if (rarityChance < 60)
                    PokemonSafari.goToNextScene(new BattleScene(this.player, PokemonFactory.getPokemon(Rarity.Common)));
                else if (rarityChance < 90)
                    PokemonSafari.goToNextScene(new BattleScene(this.player, PokemonFactory.getPokemon(Rarity.Uncommon)));
                else
                    PokemonSafari.goToNextScene(new BattleScene(this.player, PokemonFactory.getPokemon(Rarity.Rare)));
            }
        }
    }



} // final class OverworldScene
