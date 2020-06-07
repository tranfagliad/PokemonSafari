package view;

import controller.PokemonSafari;
import controller.audio.SfxLibrary;
import controller.audio.SfxPlayer;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
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
    private static final String OVERWORLD_IMAGE_FILENAME = "images/overworld/overworld_sprites.png";

    private static final Font SMALL_FONT = Font.font("Verdana", 20);
    private static final Font MEDIUM_FONT = Font.font("Verdana", 28);
    private static final Font BIG_FONT = Font.font("Verdana", 35);

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
    private Image overworldImages;

    private Map map;
    private Player player;

    private int cameraX;
    private int cameraY;

    private int menuItemID;

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

        this.cameraX = this.player.getPosition().getX()-PLAYER_X_OFFSET;
        this.cameraY = this.player.getPosition().getY()-PLAYER_Y_OFFSET;

        getPaintBrush().setLineWidth(3);
        getPaintBrush().setStroke(Color.BLACK);

        try {
            this.playerImages = new Image(new FileInputStream(PLAYER_IMAGE_FILENAME));
            this.tileImages = new Image(new FileInputStream(TILE_IMAGE_FILENAME));
            this.overworldImages = new Image(new FileInputStream(OVERWORLD_IMAGE_FILENAME));
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
        overworldControls();
    } // start()



    private void drawFrame ()
    {
        for (int y = 0; y < CAMERA_Y_RANGE; y++)
        {
            for (int x = 0; x < CAMERA_X_RANGE; x++)
            {
                getPaintBrush().drawImage(tileImages,
                        (double)(map.getTile(y+cameraY, x+cameraX).getID()%3)*64.0,
                        (double)(map.getTile(y+cameraY, x+cameraX).getID()/3)*64.0,
                        32.0, 32.0, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE+1, TILE_SIZE+1);
            }
        }
        getPaintBrush().drawImage(playerImages,
                this.playerX*32, this.playerY*32,
                32,32, PLAYER_X_OFFSET*TILE_SIZE, PLAYER_Y_OFFSET*TILE_SIZE,
                TILE_SIZE, TILE_SIZE);
        if (player.getStepsRemaining() == 0)
            System.exit(0);
    }


    private void overworldControls ()
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
                        if (map.getTile(player.getPosition().getY()-1, player.getPosition().getX()).isWalkable()) {
                            player.setStepsRemaining(player.getStepsRemaining()-1);
                            new WalkNorthAnimation().start();
                        }
                        drawFrame();
                        break;
                    case A:
                        playerX = 0;
                        playerY = 1;
                        if (map.getTile(player.getPosition().getY(), player.getPosition().getX()-1).isWalkable()) {
                            player.setStepsRemaining(player.getStepsRemaining()-1);
                            new WalkWestAnimation().start();
                        }
                        drawFrame();
                        break;
                    case S:
                        playerX = 0;
                        playerY = 2;
                        if (map.getTile(player.getPosition().getY()+1, player.getPosition().getX()).isWalkable()) {
                            player.setStepsRemaining(player.getStepsRemaining()-1);
                            new WalkSouthAnimation().start();
                        }
                        drawFrame();
                        break;
                    case D:
                        playerX = 0;
                        playerY = 3;
                        if (map.getTile(player.getPosition().getY(), player.getPosition().getX()+1).isWalkable()) {
                            player.setStepsRemaining(player.getStepsRemaining()-1);
                            new WalkEastAnimation().start();
                        }
                        drawFrame();
                        break;
                    case ENTER:
                        SfxPlayer.getInstance().play(SfxLibrary.Menu.name());
                        menuControls();
                        break;
                }
            }
        });
    }


    private void menuControls ()
    {
        final MenuArrowAnimation menuArrowAnimation = new MenuArrowAnimation();
        menuArrowAnimation.start();
        this.menuItemID = 0;
        this.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent event)
            {
                switch (event.getCode())
                {
                    case W:
                        if (menuItemID == 1) {
                            menuItemID = 0;
                            SfxPlayer.getInstance().play(SfxLibrary.Select.name());
                        }
                        break;
                    case S:
                        if (menuItemID == 0) {
                            menuItemID = 1;
                            SfxPlayer.getInstance().play(SfxLibrary.Select.name());
                        }
                        break;
                    case SPACE:
                        if (menuItemID == 1)
                        {
                            SfxPlayer.getInstance().play(SfxLibrary.Select.name());
                            drawFrame();
                            menuArrowAnimation.stop();
                            overworldControls();
                        }
                        break;
                    case ENTER:
                        SfxPlayer.getInstance().play(SfxLibrary.Menu.name());
                        drawFrame();
                        menuArrowAnimation.stop();
                        overworldControls();
                        break;
                }
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
                overworldControls();
        }
        else
            overworldControls();
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
                            (map.getTile(cameraYSnapshot+y, cameraXSnapshot+x).getID()%3)*64,
                            (map.getTile(cameraYSnapshot+y, cameraXSnapshot+x).getID()/3)*64,
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
                            (map.getTile(cameraYSnapshot+y, cameraXSnapshot+x).getID()%3)*64,
                            (map.getTile(cameraYSnapshot+y, cameraXSnapshot+x).getID()/3)*64,
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
                            (map.getTile(cameraYSnapshot+y, cameraXSnapshot+x).getID()%3)*64,
                            (map.getTile(cameraYSnapshot+y, cameraXSnapshot+x).getID()/3)*64,
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
                            (map.getTile(cameraYSnapshot+y, cameraXSnapshot+x).getID()%3)*64,
                            (map.getTile(cameraYSnapshot+y, cameraXSnapshot+x).getID()/3)*64,
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


    private void drawMenu ()
    {
        getPaintBrush().setFill(Color.WHITE);
        getPaintBrush().fillRect(550, 20, 300, 80);
        getPaintBrush().strokeRect(550, 20, 300, 80);
        getPaintBrush().setFill(Color.BLACK);
        getPaintBrush().setFont(SMALL_FONT);
        getPaintBrush().setTextAlign(TextAlignment.LEFT);
        getPaintBrush().fillText("Steps Remaining", 560, 40);
        getPaintBrush().setFont(BIG_FONT);
        getPaintBrush().setTextAlign(TextAlignment.CENTER);
        getPaintBrush().fillText(""+this.player.getStepsRemaining(), 700, 80);

        getPaintBrush().setTextAlign(TextAlignment.LEFT);
        getPaintBrush().setFill(Color.WHITE);
        getPaintBrush().fillRect(550, 120, 300, 120);
        getPaintBrush().strokeRect(550, 120, 300, 120);
        getPaintBrush().setFill(Color.BLACK);
        getPaintBrush().setFont(SMALL_FONT);
        getPaintBrush().fillText("Menu", 560, 140);

        getPaintBrush().setFont(MEDIUM_FONT);
        getPaintBrush().fillText("See Collection", 610, 180);
        getPaintBrush().fillText("Close", 610, 220);
    }


    private final class MenuArrowAnimation extends AnimationTimer
    {
        private double arrowXPush = 0;
        private boolean arrowGoingRight;

        @Override
        public void handle (long now)
        {
            drawFrame();
            drawMenu();
            if (this.arrowXPush > 10)
                this.arrowGoingRight = false;
            else if (this.arrowXPush < 0)
                this.arrowGoingRight = true;
            if (this.arrowGoingRight)
                this.arrowXPush += 0.2;
            else
                this.arrowXPush -= 0.2;
            getPaintBrush().drawImage(overworldImages, 0, 0, 32, 32,
                    560+this.arrowXPush, 155+(menuItemID*40), 32, 32);
        }
    }

} // final class OverworldScene
