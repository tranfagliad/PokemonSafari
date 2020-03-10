package view;

import controller.audio.CryPlayer;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import model.player.Player;
import model.pokemon.Pokemon;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * BattleScene.java
 *
 * Purpose: Displays and operates the Battle Scene when a Pokemon
 *      is encountered in the Safari Zone.
 */
public final class BattleScene extends GameScene
{
    private static final String BACKGROUND_IMAGE_FILENAME   = "images/battle/battle_background.png";
    private static final String BATTLE_BOXES_IMAGE_FILENAME = "images/battle/battle_boxes.png";
    private static final String POKEMON_IMAGE_FILENAME      = "images/battle/pokemon_sprites.png";
    private static final String PLAYER_IMAGE_FILENAME       = "images/battle/trainer_battle_sprites.png";
    private static final String BATTLE_ITEMS_IMAGE_FILENAME = "images/battle/battle_items.png";

    private static final double SRC_WILD_POKEMON_IMAGE_SIZE = 100;
    private static final double DEST_WILD_POKEMON_IMAGE_SIZE = SRC_WILD_POKEMON_IMAGE_SIZE * 3;
    private static final double WILD_POKEMON_X = 490;
    private static final double WILD_POKEMON_Y = 20;

    private static final double SRC_PLAYER_IMAGE_SIZE = 70;
    private static final double DEST_PLAYER_IMAGE_SIZE = SRC_PLAYER_IMAGE_SIZE * 3.5;
    private static final double PLAYER_X = 100;
    private static final double PLAYER_Y = 278;

    private static final Font BIG_FONT = Font.font("Verdana", 32);
    private static final Font SMALL_FONT = Font.font("Verdana", 25);

    private Image backgroundImage;
    private Image battleBoxImage;
    private Image pokemonImage;
    private Image playerImage;
    private Image battleItemImage;

    private Player player;
    private Pokemon wildPokemon;

    private int wildPokemonSourceX;
    private int wildPokemonSourceY;

    private double actionArrowX = 475;
    private double actionArrowY = 566;
    private int menuRow = 0;
    private int menuCol = 0;


    /**
     * BattleScene (Pokemon)
     *
     * Purpose: Creates and initializes the BattleScene for the given Pokemon.
     */
    public BattleScene (Player player, Pokemon wildPokemon)
    {
        super();

        this.player = player;
        this.wildPokemon = wildPokemon;

        this.wildPokemonSourceX = (int)((wildPokemon.getID() % 5) * SRC_WILD_POKEMON_IMAGE_SIZE);
        this.wildPokemonSourceY = (int)((wildPokemon.getID() / 5) * SRC_WILD_POKEMON_IMAGE_SIZE);
        this.getPaintBrush().setFont(BIG_FONT);

        try {
            this.backgroundImage = new Image(new FileInputStream(BACKGROUND_IMAGE_FILENAME));
            this.battleBoxImage = new Image(new FileInputStream(BATTLE_BOXES_IMAGE_FILENAME));
            this.pokemonImage = new Image(new FileInputStream(POKEMON_IMAGE_FILENAME));
            this.playerImage = new Image(new FileInputStream(PLAYER_IMAGE_FILENAME));
            this.battleItemImage = new Image(new FileInputStream(BATTLE_ITEMS_IMAGE_FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // BattleScene (Pokemon)


    /**
     * start()
     *
     * Purpose: Defines how the BattleScene starts.
     */
    @Override
    public void start ()
    {
        transition();
    } // start()


    /**
     * transition()
     *
     * Purpose: Runs the transition animation when a battle starts.
     */
    private void transition ()
    {
        this.getPaintBrush().setFill(Color.BLACK);
        final AnimationTimer transition = new TransitionAnimationTimer();
        transition.start();
    } // transition()


    /**
     * TransitionAnimationTimer
     *
     * Purpose: Defines and handles the transition animation that plays when a battle starts.
     */
    private final class TransitionAnimationTimer extends AnimationTimer
    {
        private static final double MIN_ANGLE = -360.0;
        private static final double MAX_ANGLE =  360.0;
        private static final double ANIMATION_SPEED = 3;
        private static final double POKEMON_APPEAR_SPEED = 0.05;

        private double arcExtent = -1.0;
        private double pokemonBrightness = -1.0;
        private ColorAdjust colorAdjust = new ColorAdjust();


        /**
         * handle()
         *
         * Purpose: Defines how every frame in the transition is drawn.
         */
        @Override
        public void handle (long now)
        {
            if (arcExtent == 0.0)
            {
                if (pokemonBrightness >= 0.0)
                {
                    colorAdjust.setBrightness(0.0);
                    getPaintBrush().setEffect(colorAdjust);
                    CryPlayer.getInstance().play(wildPokemon.getName());
                    getPaintBrush().setFill(Color.WHITE);
                    this.stop();
                    standby();
                    return;
                }
                pokemonBrightness += POKEMON_APPEAR_SPEED;
                //System.out.println(pokemonBrightness);
                colorAdjust.setBrightness(pokemonBrightness);
                getPaintBrush().setEffect(colorAdjust);
                getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X, WILD_POKEMON_Y, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);
                return;
            }
            else if (arcExtent < MIN_ANGLE)
                arcExtent = MAX_ANGLE;
            //System.out.println(arcExtent);
            if (arcExtent >= 0.0)
            {
                getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
                getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
                colorAdjust.setBrightness(-1.0);
                getPaintBrush().setEffect(colorAdjust);
                getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X, WILD_POKEMON_Y, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);
                colorAdjust.setBrightness(0.0);
                getPaintBrush().setEffect(colorAdjust);
            }
            arcExtent -= ANIMATION_SPEED;
            getPaintBrush().fillArc(-180,-180,getWidth()+360,getHeight()+360,90, arcExtent, ArcType.ROUND);
        } // handle()

    } // final class TransitionAnimationTimer


    /**
     *
     */
    private void standby ()
    {
        final AnimationTimer standby = new StandbyAnimationTimer();
        this.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event)
            {
                if (event.getCode() == KeyCode.SPACE)
                {
                    standby.stop();
                    getPaintBrush().setFill(Color.BLACK);
                    enterBattlePhase();
                }
            }
        });
        standby.start();
    } // standby()


    /**
     *
     */
    private final class StandbyAnimationTimer extends AnimationTimer
    {
        private static final double MIN_ARROW_HEIGHT = 650.0;
        private static final double MAX_ARROW_HEIGHT = 655.0;
        private static final double ARROW_SPEED = 0.2;

        private double arrowHeight = MIN_ARROW_HEIGHT;
        private boolean arrowGoingUp = false;


        /**
         *
         */
        @Override
        public void handle (long now)
        {
            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X, WILD_POKEMON_Y, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);
            getPaintBrush().fillText("A wild "+wildPokemon.getName()+" appeared!", 40, 600);
            getPaintBrush().drawImage(battleBoxImage, 120, 0, 32, 32, 800, this.arrowHeight, 32, 32);
            if (this.arrowHeight > MAX_ARROW_HEIGHT)
                this.arrowGoingUp = true;
            else if (this.arrowHeight < MIN_ARROW_HEIGHT)
                this.arrowGoingUp = false;
            if (this.arrowGoingUp)
                this.arrowHeight -= ARROW_SPEED;
            else
                this.arrowHeight += ARROW_SPEED;
        } // handle()

    } //


    /**
     *
     */
    private void enterBattlePhase ()
    {
        getPaintBrush().setFont(SMALL_FONT);
        final AnimationTimer enterBattlePhase = new EnterBattlePhaseAnimationTimer();
        this.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) { /* Do nothing */ }
        });
        enterBattlePhase.start();
    }


    /**
     *
     */
    private final class EnterBattlePhaseAnimationTimer extends AnimationTimer
    {
        private static final double PLAYER_BATTLE_BOX_FINAL_X = 440;
        private static final double POKEMON_BATTLE_BOX_FINAL_X = 40;
        private static final double ANIMATION_SPEED = 20;

        private double playerBattleBoxX = 880;
        private double pokemonBattleBoxX = -400;


        @Override
        public void handle (long now)
        {
            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X, WILD_POKEMON_Y, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);

            getPaintBrush().drawImage(battleBoxImage, 0, 0, 103, 36, playerBattleBoxX, 360, 103<<2, 36<<2);
            getPaintBrush().fillText("Safari Balls", playerBattleBoxX+80, 410);
            getPaintBrush().fillText("Left: " + player.getNumSafariBalls(), playerBattleBoxX+120, 460);
            getPaintBrush().drawImage(battleBoxImage, 0, 175, 18, 18, playerBattleBoxX+280, 410, 18<<1, 18<<1);

            getPaintBrush().drawImage(battleBoxImage, 0, 40, 100, 28, pokemonBattleBoxX, 60, 100<<2, 28<<2);
            getPaintBrush().fillText(wildPokemon.getName(), pokemonBattleBoxX+20, 105);
            getPaintBrush().fillText("Lv" + wildPokemon.getLevel(), pokemonBattleBoxX+280, 105);

            if (playerBattleBoxX > PLAYER_BATTLE_BOX_FINAL_X)
                playerBattleBoxX -= ANIMATION_SPEED;
            if (pokemonBattleBoxX < POKEMON_BATTLE_BOX_FINAL_X)
                pokemonBattleBoxX += ANIMATION_SPEED;

            if (playerBattleBoxX == PLAYER_BATTLE_BOX_FINAL_X && pokemonBattleBoxX == POKEMON_BATTLE_BOX_FINAL_X)
            {
                this.stop();
                battlePhase();
            }

        }

    }


    /**
     *
     */
    private void battlePhase ()
    {
        final AnimationTimer battlePhase = new BattlePhaseAnimationTimer();
        this.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event)
            {
                switch (event.getCode())
                {
                    case W:
                        menuRow = 0;
                        actionArrowY = 566;
                        break;
                    case A:
                        menuCol = 0;
                        actionArrowX = 475;
                        break;
                    case S:
                        menuRow = 1;
                        actionArrowY = 636;
                        break;
                    case D:
                        menuCol = 1;
                        actionArrowX = 665;
                        break;
                    case SPACE:
                        if (menuRow == 0)
                            if (menuCol == 0) System.out.println("Throw Ball!");
                            else System.out.println("Throw Bait!");
                        else
                            if (menuCol == 0) System.out.println("Throw Rock!");
                            else System.out.println("Run!");
                        break;
                }
            }
        });
        battlePhase.start();
    }


    /**
     *
     */



    private final class BattlePhaseAnimationTimer extends AnimationTimer
    {


        @Override
        public void handle (long now)
        {
            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X, WILD_POKEMON_Y, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);

            getPaintBrush().setFont(SMALL_FONT);
            getPaintBrush().setFill(Color.BLACK);
            getPaintBrush().drawImage(battleBoxImage, 0, 0, 103, 36, 440, 360, 103<<2, 36<<2);
            getPaintBrush().fillText("Safari Balls", 520, 410);
            getPaintBrush().fillText("Left: " + player.getNumSafariBalls(), 560, 460);
            getPaintBrush().drawImage(battleBoxImage, 0, 175, 18, 18, 720, 410, 18<<1, 18<<1);

            getPaintBrush().drawImage(battleBoxImage, 0, 40, 100, 28, 40, 60, 100<<2, 28<<2);
            getPaintBrush().fillText(wildPokemon.getName(), 60, 105);
            getPaintBrush().fillText("Lv" + wildPokemon.getLevel(), 320, 105);

            getPaintBrush().setFont(BIG_FONT);
            getPaintBrush().drawImage(battleBoxImage, 0, 200, 176, 78, 440, 523, 176*2.5, 78*2.5);
            getPaintBrush().fillText("Ball", 520, 595);
            getPaintBrush().fillText("Rock", 520, 665);
            getPaintBrush().fillText("Bait", 710, 595);
            getPaintBrush().fillText("Run", 710, 665);

            getPaintBrush().setFill(Color.WHITE);
            getPaintBrush().fillText("What will", 40, 600);
            getPaintBrush().fillText(player.getName()+" throw?", 40, 650);

            getPaintBrush().drawImage(battleBoxImage, 50, 120, 32, 32, actionArrowX, actionArrowY, 32, 32);
        }

    }

        /*this.getPaintBrush().drawImage(this.battleBoxImage,
                this.wildPokemonGenderX, this.wildPokemonGenderY,
                32, 32,
                (this.wildPokemon.getName().length()*21)+60, 76,
                32, 32);
         */


        /*this.getPaintBrush().drawImage(this.battleBoxImage,
                110, 0,
                32, 32,
                470, 566,
                32, 32);
         */

} // final class BattleScene
