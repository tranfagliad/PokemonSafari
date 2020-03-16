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
import model.pokemon.Gender;
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

    private static final double SRC_WILD_POKEMON_IMAGE_SIZE = 100.0;
    private static final double DEST_WILD_POKEMON_IMAGE_SIZE = SRC_WILD_POKEMON_IMAGE_SIZE * 3;
    private static final double WILD_POKEMON_X = 490.0;
    private static final double WILD_POKEMON_Y = 20.0;

    private static final double SRC_PLAYER_IMAGE_SIZE = 70.0;
    private static final double DEST_PLAYER_IMAGE_SIZE = SRC_PLAYER_IMAGE_SIZE * 3.5;
    private static final double PLAYER_X = 100.0;
    private static final double PLAYER_Y = 278.0;

    private static final double MALE_GENDER_Y = 70.0;
    private static final double FEMALE_GENDER_Y = 120.0;

    private static final Font BIG_FONT = Font.font("Verdana", 32);
    private static final Font SMALL_FONT = Font.font("Verdana", 25);

    private static double wildPokemonSourceX;
    private static double wildPokemonSourceY;

    private static double genderSourceX;
    private static double genderSourceY;
    private static double genderDestX;

    private static double actionArrowX = 475;
    private static double actionArrowY = 566;
    private static int menuRow = 0;
    private static int menuCol = 0;
    private static final double ARROW_TOP = 566.0;
    private static final double ARROW_BOTTOM = 636.0;
    private static final double ARROW_LEFT = 475.0;
    private static final double ARROW_RIGHT = 665.0;
    private static double currArrowX = ARROW_LEFT;

    private Image backgroundImage;
    private Image battleBoxImage;
    private Image pokemonImage;
    private Image playerImage;
    private Image battleItemImage;

    private Player player;
    private Pokemon wildPokemon;


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
        public void handle (final long now)
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

    } // final class TransitionAnimationTimer


    /**
     *
     */
    private void standby ()
    {
        final AnimationTimer standby = new StandbyAnimationTimer();
        this.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent event)
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
        final AnimationTimer enterBattlePhase = new EnterBattlePhaseAnimationTimer();
        this.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent event) { /* Do nothing */ }
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
    private static double startNanoTime;

    private void battlePhase ()
    {
        final AnimationTimer battlePhase = new BattlePhaseAnimationTimer();
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
                        }
                        break;
                    case A:
                        if (menuCol != 0) {
                            menuCol = 0;
                            actionArrowX = ARROW_LEFT;
                            currArrowX = ARROW_LEFT;
                        }
                        break;
                    case S:
                        if (menuRow != 1) {
                            menuRow = 1;
                            actionArrowY = ARROW_BOTTOM;
                        }
                        break;
                    case D:
                        if (menuCol != 1) {
                            menuCol = 1;
                            actionArrowX = ARROW_RIGHT;
                            currArrowX = ARROW_RIGHT;
                        }
                        break;
                    case SPACE:
                        battlePhase.stop();
                        startNanoTime = System.nanoTime();
                        if (menuRow == 0)
                            if (menuCol == 0) new ThrowSafariBallAnimationTimer().start();
                            else new ThrowBaitAnimationTimer().start();
                        else
                            if (menuCol == 0) new ThrowRockAnimationTimer().start();
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
    private final class ThrowSafariBallAnimationTimer extends AnimationTimer
    {
        private static final double ITEM_START_X = 450;
        private static final double ITEM_START_Y = 350;
        private static final double START_ANGLE = 135.2;
        private static final double FINAL_ANGLE = 137.65;
        private static final double ITEM_SPEED = 0.05;

        private double itemX = ITEM_START_X;
        private double itemY = ITEM_START_Y;
        private double angle = START_ANGLE;


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
                    System.out.println(angle);
                    getPaintBrush().drawImage(battleItemImage, 0, 20, 16, 16, itemX, itemY, 40, 40);
                    getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
                }
                else {
                    getPaintBrush().drawImage(battleItemImage, 0, 20, 16, 16, itemX, itemY, 40, 40);
                    getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
                    this.stop();
                }
            }
            else if (currTime < 2) {
                getPaintBrush().drawImage(playerImage, 0, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            }
            else if (currTime == 2) {
                getPaintBrush().drawImage(battleItemImage, 0, 20, 16, 16, 115, 415, 40, 40);
                getPaintBrush().drawImage(playerImage, 70, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            }
            else if (currTime == 3) {
                getPaintBrush().drawImage(battleItemImage, 0, 20, 16, 16, 115, 355, 40, 40);
                getPaintBrush().drawImage(playerImage, 140, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            }
            else { /* currTime == 4 */
                itemX = (200 * Math.cos(this.angle)) + ITEM_START_X;
                itemY = (200 * Math.sin(this.angle)) + ITEM_START_Y;
                this.angle += ITEM_SPEED;
                getPaintBrush().drawImage(battleItemImage, 0, 20, 16, 16, itemX, itemY, 40, 40);
                getPaintBrush().drawImage(playerImage, 210, 0, SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE, PLAYER_X + 40, PLAYER_Y, DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
            }
        }

    }


    private final class ThrowBaitAnimationTimer extends AnimationTimer
    {
        @Override
        public void handle (final long now)
        {

        }
    }


    private final class ThrowRockAnimationTimer extends AnimationTimer
    {
        @Override
        public void handle (final long now)
        {

        }
    }

} // final class BattleScene
