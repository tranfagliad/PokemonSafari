package view;

import controller.GameSceneManager;
import controller.PokemonSafari;
import controller.audio.*;
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
import model.pokemon.Gender;
import model.pokemon.Pokemon;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

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

    private static final double SRC_WILD_POKEMON_IMAGE_SIZE = 100.0;
    private static final double DEST_WILD_POKEMON_IMAGE_SIZE = SRC_WILD_POKEMON_IMAGE_SIZE * 3;
    private static final double WILD_POKEMON_X = 490.0;
    private static final double WILD_POKEMON_Y = 20.0;

    private static final int CATCH_LIKELIHOOD_CHANGE = 10;
    private static final int RUN_LIKELIHOOD_CHANGE = 10;

    private static final int ONE_HUNDRED_PERCENT = 100;

    private static final double SRC_PLAYER_IMAGE_SIZE = 70.0;
    private static final double DEST_PLAYER_IMAGE_SIZE = SRC_PLAYER_IMAGE_SIZE * 3.5;
    private static final double PLAYER_X = 100.0;
    private static final double PLAYER_Y = 278.0;

    private static final double MALE_GENDER_Y = 70.0;
    private static final double FEMALE_GENDER_Y = 120.0;

    private static final double DEFAULT_BRIGHTNESS = 0.0;

    private static final Font BIG_FONT = Font.font("Verdana", 32);
    private static final Font SMALL_FONT = Font.font("Verdana", 25);

    private static double wildPokemonSourceX;
    private static double wildPokemonSourceY;

    private static double genderSourceX;
    private static double genderSourceY;
    private static double genderDestX;

    private static final double ARROW_TOP = 566.0;
    private static final double ARROW_BOTTOM = 636.0;
    private static final double ARROW_LEFT = 475.0;
    private static final double ARROW_RIGHT = 665.0;
    private double currArrowX = ARROW_LEFT;

    private double startNanoTime;

    private Image backgroundImage;
    private Image battleBoxImage;
    private Image pokemonImage;
    private Image playerImage;
    private Image battleItemImage;

    private Player player;
    private Pokemon wildPokemon;
    private int remainingTurns;

    private double actionArrowX;
    private double actionArrowY;
    private int menuRow;
    private int menuCol;



    /**
     * BattleScene (Pokemon)
     *
     * Purpose: Creates and initializes the BattleScene for the given Pokemon.
     */
    public BattleScene (final Player player, final Pokemon wildPokemon)
    {
        super();

        this.player = player;
        this.wildPokemon = wildPokemon;
        this.remainingTurns = this.wildPokemon.getMaxDuration();

        this.actionArrowX = 475.0;
        this.actionArrowY = 566.0;
        this.menuRow = 0;
        this.menuCol = 0;

        wildPokemonSourceX = (int)((wildPokemon.getID() % 5) * SRC_WILD_POKEMON_IMAGE_SIZE);
        wildPokemonSourceY = (int)((wildPokemon.getID() / 5) * SRC_WILD_POKEMON_IMAGE_SIZE);
        genderSourceX = 0;
        genderSourceY = this.wildPokemon.getGender() == Gender.Male ? MALE_GENDER_Y : FEMALE_GENDER_Y;
        genderDestX = (wildPokemon.getName().length()*20)+60;
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
     * restart()
     *
     * Purpose: Not used.
     */
    @Override
    public void restart () { /* Nothing */ };

    /**
     * transition()
     *
     * Purpose: Runs the transition animation when a battle starts.
     */
    private void transition ()
    {
        this.getPaintBrush().setFill(Color.BLACK);
        new TransitionAnimation().start();
    } // transition()


    /**
     * TransitionAnimationTimer
     *
     * Purpose: Defines and handles the transition animation that plays when a battle starts.
     */
    private final class TransitionAnimation extends AnimationTimer
    {
        private final ColorAdjust colorAdjust = new ColorAdjust();

        private static final double MIN_ANGLE = -360.0;
        private static final double MAX_ANGLE =  360.0;
        private static final double ANIMATION_SPEED = 3;
        private static final double POKEMON_APPEAR_SPEED = 0.05;

        private double arcExtent = -1.0;
        private double pokemonBrightness = -1.0;


        /**
         * handle()
         *
         * Purpose: Defines how every frame in the transition is drawn.
         */
        @Override
        public void handle (final long now)
        {
            if (arcExtent == 0.0)
            {
                if (pokemonBrightness >= 0.0)
                {
                    colorAdjust.setBrightness(DEFAULT_BRIGHTNESS);
                    getPaintBrush().setEffect(colorAdjust);
                    CryPlayer.getInstance().play(wildPokemon.getName());
                    getPaintBrush().setFill(Color.WHITE);
                    GameSceneManager.getPreviousScene().getPaintBrush().setFill(Color.BLACK);
                    GameSceneManager.getPreviousScene().getPaintBrush().fillRect(0,0,getWidth(),getHeight());
                    this.stop();
                    standby();
                    return;
                }
                pokemonBrightness += POKEMON_APPEAR_SPEED;
                colorAdjust.setBrightness(pokemonBrightness);
                getPaintBrush().setEffect(colorAdjust);
                getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X, WILD_POKEMON_Y, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);
                return;
            }
            else if (arcExtent < MIN_ANGLE)
                arcExtent = MAX_ANGLE;
            if (arcExtent > 0.0)
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

    } // final class TransitionAnimation


    /**
     *
     */
    private void standby ()
    {
        final AnimationTimer standby = new StandbyAnimation();
        this.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent event)
            {
                if (event.getCode() == KeyCode.SPACE)
                {
                    standby.stop();
                    getPaintBrush().setFill(Color.BLACK);
                    SfxPlayer.getInstance().play(SfxLibrary.Select.name());
                    enterBattlePhase();
                }
            }
        });
        standby.start();
    } // standby()


    /**
     *
     */
    private final class StandbyAnimation extends AnimationTimer
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
        public void handle (final long now)
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
        final AnimationTimer enterBattlePhase = new EnterBattlePhaseAnimation();
        this.getScene().setOnKeyPressed(null);
        enterBattlePhase.start();
    }


    /**
     *
     */
    private final class EnterBattlePhaseAnimation extends AnimationTimer
    {
        private static final double PLAYER_BATTLE_BOX_FINAL_X = 440;
        private static final double POKEMON_BATTLE_BOX_FINAL_X = 40;
        private static final double ANIMATION_SPEED = 20;

        private double playerBattleBoxX = 880;
        private double pokemonBattleBoxX = -400;
        private double genderX = genderDestX - 440;


        @Override
        public void handle (final long now)
        {
            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X, WILD_POKEMON_Y, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);

            getPaintBrush().drawImage(battleBoxImage, 0, 0, 103, 36, this.playerBattleBoxX, 360, 412, 144);
            getPaintBrush().fillText("Safari Balls", this.playerBattleBoxX+80, 410);
            getPaintBrush().fillText("Left: " + player.getNumSafariBalls(), this.playerBattleBoxX+120, 460);
            getPaintBrush().drawImage(battleBoxImage, 0, 175, 18, 18, this.playerBattleBoxX+280, 410, 36, 36);

            getPaintBrush().drawImage(battleBoxImage, 0, 40, 100, 28, this.pokemonBattleBoxX, 60, 400, 112);
            getPaintBrush().fillText(wildPokemon.getName(), this.pokemonBattleBoxX+20, 105);
            getPaintBrush().drawImage(battleBoxImage, genderSourceX, genderSourceY, 32, 32, genderX, 78, 32, 32);
            getPaintBrush().fillText("Lv" + wildPokemon.getLevel(), this.pokemonBattleBoxX+280, 105);

            if (this.playerBattleBoxX > PLAYER_BATTLE_BOX_FINAL_X)
                this.playerBattleBoxX -= ANIMATION_SPEED;
            if (this.pokemonBattleBoxX < POKEMON_BATTLE_BOX_FINAL_X)
            {
                this.pokemonBattleBoxX += ANIMATION_SPEED;
                this.genderX += ANIMATION_SPEED;
            }

            if (this.playerBattleBoxX == PLAYER_BATTLE_BOX_FINAL_X && this.pokemonBattleBoxX == POKEMON_BATTLE_BOX_FINAL_X)
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
        final AnimationTimer battlePhase = new BattlePhaseAnimation();
        this.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent event)
            {
                switch (event.getCode())
                {
                    case W:
                        if (menuRow != 0) {
                            menuRow = 0;
                            actionArrowY = ARROW_TOP;
                            SfxPlayer.getInstance().play(SfxLibrary.Select.name());
                        }
                        break;
                    case A:
                        if (menuCol != 0) {
                            menuCol = 0;
                            actionArrowX = ARROW_LEFT;
                            currArrowX = ARROW_LEFT;
                            SfxPlayer.getInstance().play(SfxLibrary.Select.name());
                        }
                        break;
                    case S:
                        if (menuRow != 1) {
                            menuRow = 1;
                            actionArrowY = ARROW_BOTTOM;
                            SfxPlayer.getInstance().play(SfxLibrary.Select.name());
                        }
                        break;
                    case D:
                        if (menuCol != 1) {
                            menuCol = 1;
                            actionArrowX = ARROW_RIGHT;
                            currArrowX = ARROW_RIGHT;
                            SfxPlayer.getInstance().play(SfxLibrary.Select.name());
                        }
                        break;
                    case SPACE:
                        battlePhase.stop();
                        getScene().setOnKeyPressed(null);
                        startNanoTime = System.nanoTime();
                        if (menuRow == 0)
                            if (menuCol == 0) {
                                if (player.getNumSafariBalls() != 0) {
                                    player.setNumSafariBalls(player.getNumSafariBalls() - 1);
                                    new ThrowSafariBallAnimation().start();
                                }
                                else {
                                    SfxPlayer.getInstance().play(SfxLibrary.Select.name());
                                    new OutOfSafariBallsAnimation().start();
                                }
                            }
                            else new ThrowBaitAnimation().start();
                        else
                            if (menuCol == 0)
                                new ThrowRockAnimation().start();
                            else {
                                battlePhase.stop();
                                SfxPlayer.getInstance().play(SfxLibrary.Run.name());
                                new RunAnimation().start();
                            }
                        break;
                }
            }
        });
        battlePhase.start();
    }



    private void determineTurnResult ()
    {
        this.remainingTurns--;
        if (this.remainingTurns == 0) {
            SfxPlayer.getInstance().play(SfxLibrary.Run.name());
            new PokemonRunAnimation().start();
            return;
        }
        final Random random = new Random();
        if (random.nextInt(ONE_HUNDRED_PERCENT)+1 < wildPokemon.getRunLikelihood()) {
            SfxPlayer.getInstance().play(SfxLibrary.Run.name());
            new PokemonRunAnimation().start();
        }
        else
            battlePhase();
    }




    private final class PokemonRunAnimation extends AnimationTimer
    {
        private final ColorAdjust colorAdjust = new ColorAdjust();

        private int frame = 0;
        private double pokemonX = WILD_POKEMON_X;
        private double screenBrightness = DEFAULT_BRIGHTNESS;

        @Override
        public void handle (final long now)
        {
            frame++;
            getPaintBrush().setEffect(this.colorAdjust);

            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);

            if (this.pokemonX < 1000.0)
                this.pokemonX += 10;
            getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, this.pokemonX, WILD_POKEMON_Y, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);

            getPaintBrush().setFont(BIG_FONT);
            getPaintBrush().setFill(Color.WHITE);
            getPaintBrush().fillText(wildPokemon.getName()+" ran away!", 40, 600);

            if (this.frame > 100) {
                this.screenBrightness -= 0.02;
                this.colorAdjust.setBrightness(this.screenBrightness);

            }
            if (this.screenBrightness < -1.5) {
                //////////////////////////////////////////////////////////////////////////////////////////////
                // EXIT BATTLE HERE AFTER RUNNING
                //////////////////////////////////////////////////////////////////////////////////////////////
                this.stop();
                PokemonSafari.goToPreviousScene();
            }
        }
    }



    /**
     *
     */
    private final class OutOfSafariBallsAnimation extends AnimationTimer
    {
        private int frame = 0;

        @Override
        public void handle (final long now)
        {
            this.frame++;
            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X, WILD_POKEMON_Y, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);

            getPaintBrush().setFont(SMALL_FONT);
            getPaintBrush().setFill(Color.BLACK);
            getPaintBrush().drawImage(battleBoxImage, 0, 0, 103, 36, 440, 360, 412, 144);
            getPaintBrush().fillText("Safari Balls", 520, 410);
            getPaintBrush().fillText("Left: " + player.getNumSafariBalls(), 560, 460);
            getPaintBrush().drawImage(battleBoxImage, 0, 175, 18, 18, 720, 410, 36, 36);

            getPaintBrush().drawImage(battleBoxImage, 0, 40, 100, 28, 40, 60, 400, 112);
            getPaintBrush().fillText(wildPokemon.getName(), 60, 105);
            getPaintBrush().drawImage(battleBoxImage, genderSourceX, genderSourceY, 32, 32, genderDestX, 78, 32, 32);
            getPaintBrush().fillText("Lv" + wildPokemon.getLevel(), 320, 105);

            getPaintBrush().setFont(BIG_FONT);
            getPaintBrush().setFill(Color.WHITE);
            getPaintBrush().fillText("Out of Safari Balls!", 40, 600);

            if (this.frame == 80) {
                this.stop();
                battlePhase();
            }
        }

    }


    /**
     *
     */
    private final class BattlePhaseAnimation extends AnimationTimer
    {
        private static final double ARROW_SPEED = 0.2;
        private boolean arrowGoingLeft = false;

        @Override
        public void handle (final long now)
        {
            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X, WILD_POKEMON_Y, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);

            getPaintBrush().setFont(SMALL_FONT);
            getPaintBrush().setFill(Color.BLACK);
            getPaintBrush().drawImage(battleBoxImage, 0, 0, 103, 36, 440, 360, 412, 144);
            getPaintBrush().fillText("Safari Balls", 520, 410);
            getPaintBrush().fillText("Left: " + player.getNumSafariBalls(), 560, 460);
            getPaintBrush().drawImage(battleBoxImage, 0, 175, 18, 18, 720, 410, 36, 36);

            getPaintBrush().drawImage(battleBoxImage, 0, 40, 100, 28, 40, 60, 400, 112);
            getPaintBrush().fillText(wildPokemon.getName(), 60, 105);
            getPaintBrush().drawImage(battleBoxImage, genderSourceX, genderSourceY, 32, 32, genderDestX, 78, 32, 32);
            getPaintBrush().fillText("Lv" + wildPokemon.getLevel(), 320, 105);

            getPaintBrush().setFont(BIG_FONT);
            getPaintBrush().drawImage(battleBoxImage, 0, 200, 176, 78, 440, 523, 440, 195);
            getPaintBrush().fillText("Ball", 520, 595);
            getPaintBrush().fillText("Rock", 520, 665);
            getPaintBrush().fillText("Bait", 710, 595);
            getPaintBrush().fillText("Run", 710, 665);

            getPaintBrush().setFill(Color.WHITE);
            getPaintBrush().fillText("What will", 40, 600);
            getPaintBrush().fillText(player.getName()+" throw?", 40, 650);

            getPaintBrush().drawImage(battleBoxImage, 50, 120, 32, 32, actionArrowX, actionArrowY, 32, 32);

            if (actionArrowX > currArrowX+5)
                arrowGoingLeft = true;
            else if (actionArrowX < currArrowX)
                arrowGoingLeft = false;
            if (arrowGoingLeft)
                actionArrowX -= ARROW_SPEED;
            else
                actionArrowX += ARROW_SPEED;
        }

    }


    /**
     *
     */
    private class ThrowAnimation extends AnimationTimer
    {
        private static final double ITEM_START_X = 450;
        private static final double ITEM_START_Y = 350;
        private static final double START_ANGLE = 135.3;
        private static final double FINAL_ANGLE = 137.65;
        private static final double ITEM_SPEED = 0.05;

        private double itemX = ITEM_START_X;
        private double itemY = ITEM_START_Y;
        private double angle = START_ANGLE;
        private int rotate = 0;

        private double itemSrcY;
        protected boolean throwComplete = false;


        private ThrowAnimation (final double itemSrcY)
        {
            this.itemSrcY = itemSrcY;
        }


        /**
         *
         */
        @Override
        public void handle (final long now)
        {
            int currTime = (int)((now-startNanoTime)/100_000_000);
            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X, WILD_POKEMON_Y, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);
            if (currTime > 4) {
                if (this.angle < FINAL_ANGLE) {
                    itemX = (200 * Math.cos(this.angle)) + ITEM_START_X;
                    itemY = (200 * Math.sin(this.angle)) + ITEM_START_Y;
                    this.angle += ITEM_SPEED;
                    this.rotate = (this.rotate + 1) % 8;
                    getPaintBrush().drawImage(battleItemImage, this.rotate*16, itemSrcY, 16, 16, itemX, itemY, 40, 40);
                    getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
                }
                else {
                    getPaintBrush().drawImage(battleItemImage, 0, itemSrcY, 16, 16, itemX, itemY, 40, 40);
                    getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
                    this.throwComplete = true;
                    this.stop();
                }
            }
            else if (currTime < 2) {
                getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            }
            else if (currTime == 2) {
                getPaintBrush().drawImage(battleItemImage, 0, itemSrcY, 16, 16, 115, 415, 40, 40);
                getPaintBrush().drawImage(playerImage, 70, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            }
            else if (currTime == 3) {
                SfxPlayer.getInstance().play(SfxLibrary.Throw.name());
                getPaintBrush().drawImage(battleItemImage, 0, itemSrcY, 16, 16, 115, 355, 40, 40);
                getPaintBrush().drawImage(playerImage, 140, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            }
            else { /* currTime == 4 */
                itemX = (200 * Math.cos(this.angle)) + ITEM_START_X;
                itemY = (200 * Math.sin(this.angle)) + ITEM_START_Y;
                this.angle += ITEM_SPEED;
                this.rotate = (this.rotate + 1) % 8;
                getPaintBrush().drawImage(battleItemImage, this.rotate*16, itemSrcY, 16, 16, itemX, itemY, 40, 40);
                getPaintBrush().drawImage(playerImage, 210, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X + 40, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            }
        } // handle()
    }


    /**
     *
     */
    private final class ThrowSafariBallAnimation extends ThrowAnimation
    {
        private ThrowSafariBallAnimation ()
        {
            super(20);
        }

        @Override
        public void handle (final long now)
        {
            super.handle(now);
            if (this.throwComplete) {
                SfxPlayer.getInstance().play(SfxLibrary.Pokeball_Contact.name());
                new CatchPokemonAnimationA(super.itemX, super.itemY).start();
            }
        }

    } // final class ThrowSafariBallAnimation


    /**
     *
     */
    private final class CatchPokemonAnimationA extends AnimationTimer
    {
        private final ColorAdjust colorAdjust = new ColorAdjust();

        private double itemX;
        private double itemY;
        private double pokemonBrightness = 0.0;
        private double pokemonSizeShrink = 0.0;
        private double pokemonXPush = 0.0;
        private double pokemonYPush = 0.0;
        private int pokeballOpen;


        private CatchPokemonAnimationA (final double itemX, final double itemY)
        {
            this.itemX = itemX;
            this.itemY = itemY;
            this.pokeballOpen = 0;
        }


        @Override
        public void handle (final long now)
        {
            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            if (this.itemY > 110) {
                this.itemY -= 5;
                getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X, WILD_POKEMON_Y, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);
                getPaintBrush().drawImage(battleItemImage, 0, 20, 16, 16, this.itemX, this.itemY, 40, 40);
            }
            else {
                if (this.pokemonBrightness < 1.0)
                    this.pokemonBrightness += 0.1;
                else if (this.pokemonSizeShrink != 260.0) {
                    this.pokemonSizeShrink += 5;
                    this.pokemonXPush += 2.3;
                    this.pokemonYPush += 1.3;
                }
                else
                {
                    getPaintBrush().drawImage(battleItemImage, 0, 20, 16, 16, this.itemX, this.itemY, 40, 40);
                    this.stop();
                    new CatchPokemonAnimationB(this.itemX, this.itemY).start();
                    return;
                }
                this.pokeballOpen++;
                if (this.pokeballOpen == 1)
                    SfxPlayer.getInstance().play(SfxLibrary.Pokeball_Open.name());
                getPaintBrush().drawImage(battleItemImage, 128, 20, 12, 16, this.itemX, this.itemY, 40, 40);
                this.colorAdjust.setBrightness(this.pokemonBrightness);
                getPaintBrush().setEffect(this.colorAdjust);
                getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X+this.pokemonXPush, WILD_POKEMON_Y+this.pokemonYPush, DEST_WILD_POKEMON_IMAGE_SIZE-this.pokemonSizeShrink, DEST_WILD_POKEMON_IMAGE_SIZE-this.pokemonSizeShrink);
                this.colorAdjust.setBrightness(DEFAULT_BRIGHTNESS);
                getPaintBrush().setEffect(this.colorAdjust);
            }
        }

    }




    private final class CatchPokemonAnimationB extends AnimationTimer
    {
        private static final double GROUND_Y = 260.0;
        private static final double DOUBLE_GRAVITY_ACCELERATION = 0.098*2;

        private double fallingVelocity = 0;
        private double itemX;
        private double itemY;


        private CatchPokemonAnimationB (final double itemX, final double itemY)
        {
            this.itemX = itemX;
            this.itemY = itemY;
        }


        @Override
        public void handle (final long now)
        {
            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            getPaintBrush().drawImage(battleItemImage, 0, 20, 16, 16, this.itemX, this.itemY, 40, 40);

            if (this.itemY < GROUND_Y)
            {
                this.fallingVelocity += DOUBLE_GRAVITY_ACCELERATION;
                this.itemY += this.fallingVelocity;
            }
            else {
                this.stop();
                SfxPlayer.getInstance().play(SfxLibrary.Pokeball_Contact.name());
                new CatchPokemonAnimationC(this.itemX, this.itemY).start();
            }
        }

    }


    private final class CatchPokemonAnimationC extends AnimationTimer
    {
        private final ColorAdjust colorAdjust = new ColorAdjust();

        private double itemX;
        private double itemY;
        private int frame = 0;
        private Random random;


        private CatchPokemonAnimationC (final double itemX, final double itemY)
        {
            this.itemX = itemX;
            this.itemY = itemY;
            this.random = new Random();
        }


        @Override
        public void handle (final long now)
        {
            this.frame++;
            if (this.frame == 50 || this.frame == 170)
                this.pokeballRollRight();
            if (this.frame == 110)
                this.pokeballRollLeft();
            if (this.frame == 49)
                this.pokeballReset("Oh no! "+wildPokemon.getName()+" broke out!");
            if (this.frame == 60)
                this.pokeballReset(wildPokemon.getName()+" broke free!");
            if (this.frame == 120)
                this.pokeballReset("Almost there!");
            if (this.frame == 180)
                this.pokeballReset("So close!");
            if (this.frame == 220)
                this.pokemonCaught();
        }


        private void pokeballRollRight ()
        {
            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            getPaintBrush().drawImage(battleItemImage, 16, 20, 16, 16, this.itemX+10, this.itemY, 40, 40);
        }


        private void pokeballRollLeft ()
        {
            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            getPaintBrush().drawImage(battleItemImage, 112, 20, 16, 16, this.itemX-10, this.itemY, 40, 40);
        }


        private void pokeballReset (final String breakoutMessage)
        {
            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            getPaintBrush().drawImage(battleItemImage, 0, 20, 16, 16, this.itemX, this.itemY, 40, 40);
            if (random.nextInt(ONE_HUNDRED_PERCENT)+1 > wildPokemon.getCatchLikelihood()) {
                this.stop();
                new PokemonBreakOutAnimation(this.itemX, this.itemY, breakoutMessage).start();
            }
        }

        private void pokemonCaught ()
        {
            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            this.colorAdjust.setBrightness(-0.5);
            getPaintBrush().setEffect(this.colorAdjust);
            getPaintBrush().drawImage(battleItemImage, 0, 20, 16, 16, this.itemX, this.itemY, 40, 40);
            this.colorAdjust.setBrightness(DEFAULT_BRIGHTNESS);
            getPaintBrush().setEffect(this.colorAdjust);
            this.stop();
            player.getPokemonCaught().add(wildPokemon);
            prepareToExitSuccess(this.itemX, this.itemY);
        }

    }




    private final class PokemonBreakOutAnimation extends AnimationTimer
    {
        private final ColorAdjust colorAdjust = new ColorAdjust();

        private int frame;
        private double itemX;
        private double itemY;
        private double pokemonBrightness = 1.0;
        private String message;
        private int pokeballOpen;

        private PokemonBreakOutAnimation (final double itemX, final double itemY, final String message)
        {
            this.message = message;
            this.frame = 0;
            this.itemX = itemX;
            this.itemY = itemY;
            this.pokeballOpen = 0;
        }

        @Override
        public void handle (long now)
        {
            this.frame++;
            if (this.frame < 20)
                return;
            this.pokeballOpen++;
            if (this.pokeballOpen == 1)
                SfxPlayer.getInstance().play(SfxLibrary.Pokeball_Open.name());

            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);

            getPaintBrush().drawImage(battleItemImage, 128, 20, 12, 16, this.itemX, this.itemY, 40, 40);

            if (this.pokemonBrightness > DEFAULT_BRIGHTNESS)
                this.pokemonBrightness -= 0.02;
            else
            {
                getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
                getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
                getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X, WILD_POKEMON_Y, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);

                getPaintBrush().setFont(BIG_FONT);
                getPaintBrush().setFill(Color.WHITE);
                getPaintBrush().fillText(this.message, 40, 600);

                if (this.frame == 160) {
                    this.stop();
                    determineTurnResult();
                }
            }
            this.colorAdjust.setBrightness(this.pokemonBrightness);
            getPaintBrush().setEffect(this.colorAdjust);
            getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X, WILD_POKEMON_Y, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);
            this.colorAdjust.setBrightness(DEFAULT_BRIGHTNESS);
            getPaintBrush().setEffect(this.colorAdjust);
        }
    }





    private void prepareToExitSuccess (final double itemX, final double itemY)
    {
        final PrepareToExitSuccessAnimation prepareToExitAnimation = new PrepareToExitSuccessAnimation(itemX, itemY);
        prepareToExitAnimation.start();
        this.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent event)
            {
                if (event.getCode() == KeyCode.SPACE)
                {
                    getScene().setOnKeyPressed(null);
                    SfxPlayer.getInstance().play(SfxLibrary.Select.name());
                    prepareToExitAnimation.stop();
                    new ExitSuccessAnimation(itemX, itemY).start();
                }
            }
        });

    }


    private final class PrepareToExitSuccessAnimation extends AnimationTimer
    {
        private static final double MIN_ARROW_HEIGHT = 650.0;
        private static final double MAX_ARROW_HEIGHT = 655.0;
        private static final double ARROW_SPEED = 0.2;

        private double itemX;
        private double itemY;

        private double arrowHeight = MIN_ARROW_HEIGHT;
        private boolean arrowGoingUp = false;

        private final ColorAdjust colorAdjust = new ColorAdjust();

        private PrepareToExitSuccessAnimation (final double itemX, final double itemY)
        {
            this.itemX = itemX;
            this.itemY = itemY;
        }

        @Override
        public void handle (final long now)
        {
            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            getPaintBrush().drawImage(battleBoxImage, 120, 0, 32, 32, 800, this.arrowHeight, 32, 32);

            this.colorAdjust.setBrightness(-0.5);
            getPaintBrush().setEffect(this.colorAdjust);
            getPaintBrush().drawImage(battleItemImage, 0, 20, 16, 16, this.itemX, this.itemY, 40, 40);
            this.colorAdjust.setBrightness(DEFAULT_BRIGHTNESS);
            getPaintBrush().setEffect(this.colorAdjust);

            getPaintBrush().setFont(BIG_FONT);
            getPaintBrush().setFill(Color.WHITE);
            this.colorAdjust.setBrightness(DEFAULT_BRIGHTNESS);
            getPaintBrush().setEffect(this.colorAdjust);
            getPaintBrush().fillText(wildPokemon.getName()+" was caught!", 40, 600);

            if (this.arrowHeight > MAX_ARROW_HEIGHT)
                this.arrowGoingUp = true;
            else if (this.arrowHeight < MIN_ARROW_HEIGHT)
                this.arrowGoingUp = false;
            if (this.arrowGoingUp)
                this.arrowHeight -= ARROW_SPEED;
            else
                this.arrowHeight += ARROW_SPEED;
        }
    }



    private final class ExitSuccessAnimation extends AnimationTimer
    {
        private final ColorAdjust colorAdjustSafariBall = new ColorAdjust();
        private final ColorAdjust colorAdjustScreen = new ColorAdjust();

        private double screenBrightness = 0.0;
        private double safariBallBrightness = -0.5;
        private double itemX;
        private double itemY;

        private ExitSuccessAnimation (final double itemX, final double itemY)
        {
            this.itemX = itemX;
            this.itemY = itemY;
            this.colorAdjustSafariBall.setBrightness(-0.5);
        }

        @Override
        public void handle (final long now)
        {
            if (this.screenBrightness > -1.0)
            {
                this.screenBrightness -= 0.02;
                this.colorAdjustScreen.setBrightness(screenBrightness);
                this.safariBallBrightness -= 0.02;
                this.colorAdjustSafariBall.setBrightness(safariBallBrightness);
            }
            else {
                ////////////////////////////////////////////////////////////////////////////////////////////////
                // EXIT BATTLE HERE AFTER CATCHING
                ////////////////////////////////////////////////////////////////////////////////////////////////
                this.stop();
                PokemonSafari.goToPreviousScene();
            }

            getPaintBrush().setEffect(this.colorAdjustScreen);
            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);

            getPaintBrush().setFont(BIG_FONT);
            getPaintBrush().setFill(Color.WHITE);
            getPaintBrush().fillText(wildPokemon.getName()+" was caught!", 40, 600);

            getPaintBrush().setEffect(this.colorAdjustSafariBall);
            getPaintBrush().drawImage(battleItemImage, 0, 20, 16, 16, this.itemX, this.itemY, 40, 40);
        }
    }


    /**
     *
     */
    private final class ThrowBaitAnimation extends ThrowAnimation
    {
        private ThrowBaitAnimation ()
        {
            super(0);
        }

        @Override
        public void handle (final long now)
        {
            super.handle(now);
            if (this.throwComplete)
                new PokemonEatBaitAnimationA().start();
        }

    } // final class ThrowBaitAnimation



    private final class PokemonEatBaitAnimationA extends AnimationTimer
    {
        private static final double POKEMON_JUMP_Y = 0.0;
        private static final int MAX_JUMPS = 6;
        private static final double JUMP_SPEED = 2.5;

        private double pokemonY = WILD_POKEMON_Y;
        private boolean jumpingUp = false;
        private int jumps = 0;

        @Override
        public void handle (long now)
        {

            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);

            getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X, this.pokemonY, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);

            if (this.pokemonY == WILD_POKEMON_Y)
                this.jumps++;
            if (this.pokemonY > WILD_POKEMON_Y)
                this.jumpingUp = true;
            else if (this.pokemonY < POKEMON_JUMP_Y)
                this.jumpingUp = false;
            if (this.jumpingUp)
                this.pokemonY -= JUMP_SPEED;
            else
                this.pokemonY += JUMP_SPEED;

            if (this.jumps == MAX_JUMPS) {
                this.stop();
                new PokemonEatBaitAnimationB().start();
            }
        }
    }


    private final class PokemonEatBaitAnimationB extends AnimationTimer
    {
        private int frames = 0;

        @Override
        public void handle (long now)
        {
            this.frames++;
            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X, WILD_POKEMON_Y, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);

            getPaintBrush().setFont(BIG_FONT);
            getPaintBrush().setFill(Color.WHITE);
            getPaintBrush().fillText(wildPokemon.getName()+" ate the bait", 40, 600);

            if (this.frames == 100) {
                wildPokemon.setRunLikelihood(wildPokemon.getRunLikelihood() - RUN_LIKELIHOOD_CHANGE);
                this.stop();
                determineTurnResult();
            }
        }
    }


    /**
     *
     */
    private final class ThrowRockAnimation extends ThrowAnimation
    {
        private ThrowRockAnimation ()
        {
            super(40);
        }

        @Override
        public void handle (final long now)
        {
            super.handle(now);
            if (this.throwComplete) {
                SfxPlayer.getInstance().play(SfxLibrary.Rock.name());
                new PokemonHitByRockAnimation().start();
            }
        }

    } // final class ThrowRockAnimation



    private final class PokemonHitByRockAnimation extends AnimationTimer
    {
        private int frames = 0;


        @Override
        public void handle (long now)
        {
            this.frames++;

            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X, WILD_POKEMON_Y, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);

            if (this.frames >= 10 && this.frames < 30)
                getPaintBrush().drawImage(battleBoxImage, 200, 0, 32, 32, 550, 150, 32*2, 32*2);
            else if (this.frames >= 30 && this.frames < 50)
                getPaintBrush().drawImage(battleBoxImage, 200, 0, 32, 32, 700, 150, 32*2, 32*2);
            else if (this.frames >= 50 && this.frames < 150)
            {
                getPaintBrush().setFont(BIG_FONT);
                getPaintBrush().setFill(Color.WHITE);
                getPaintBrush().fillText(wildPokemon.getName()+" is angry", 40, 600);
            }
            else if (this.frames == 150)
            {
                wildPokemon.setCatchLikelihood(wildPokemon.getCatchLikelihood()+CATCH_LIKELIHOOD_CHANGE);
                wildPokemon.setRunLikelihood(wildPokemon.getRunLikelihood()+RUN_LIKELIHOOD_CHANGE);
                this.stop();
                determineTurnResult();
            }
        }
    }


    /**
     *
     */
    private final class RunAnimation extends AnimationTimer
    {
        private final ColorAdjust colorAdjust = new ColorAdjust();

        private int frame = 0;
        private double playerX = PLAYER_X;
        private double screenBrightness = DEFAULT_BRIGHTNESS;

        @Override
        public void handle (final long now)
        {
            frame++;
            getPaintBrush().setEffect(this.colorAdjust);

            getPaintBrush().drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
            getPaintBrush().drawImage(pokemonImage, wildPokemonSourceX, wildPokemonSourceY, SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE, WILD_POKEMON_X, WILD_POKEMON_Y, DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);

            if (this.playerX > -220.0)
                this.playerX -= 10;
            getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, this.playerX, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);

            getPaintBrush().setFont(BIG_FONT);
            getPaintBrush().setFill(Color.WHITE);
            getPaintBrush().fillText("Got away safely!", 40, 600);

            if (this.frame > 100) {
                this.screenBrightness -= 0.02;
                this.colorAdjust.setBrightness(this.screenBrightness);

            }
            if (this.screenBrightness < -1.5) {
                //////////////////////////////////////////////////////////////////////////////////////////////
                // EXIT BATTLE HERE AFTER RUNNING
                //////////////////////////////////////////////////////////////////////////////////////////////
                this.stop();
                PokemonSafari.goToPreviousScene();
            }
        }
    }

} // final class BattleScene
