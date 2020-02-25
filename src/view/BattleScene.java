package view;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
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
public class BattleScene extends GameScene
{
    private static final String BACKGROUND_IMAGE_FILENAME   = "images/battle/battle_background.png";
    private static final String BATTLE_BOXES_IMAGE_FILENAME = "images/battle/battle_boxes.png";
    private static final String POKEMON_IMAGE_FILENAME      = "images/battle/pokemon_sprites.png";
    private static final String PLAYER_IMAGE_FILENAME       = "images/battle/trainer_battle_sprites.png";

    private static final double SRC_WILD_POKEMON_IMAGE_SIZE = 100;
    private static final double DEST_WILD_POKEMON_IMAGE_SIZE = SRC_WILD_POKEMON_IMAGE_SIZE * 3;
    private static final double WILD_POKEMON_X = 490;
    private static final double WILD_POKEMON_Y = 20;

    private static final double SRC_PLAYER_IMAGE_SIZE = 70;
    private static final double DEST_PLAYER_IMAGE_SIZE = SRC_PLAYER_IMAGE_SIZE * 3.5;
    private static final double PLAYER_X = 100;
    private static final double PLAYER_Y = 278;

    private Image backgroundImage;
    private Image battleBoxes;
    private Image pokemonImage;
    private Image playerImage;

    private int wildPokemonSourceX;
    private int wildPokemonSourceY;

    private Player player;
    private Pokemon wildPokemon;


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

        this.getPaintBrush().setFont(Font.font("Verdana", 32));

        this.wildPokemonSourceX = (int)((wildPokemon.getID() % 5) * SRC_WILD_POKEMON_IMAGE_SIZE);
        this.wildPokemonSourceY = (int)((wildPokemon.getID() / 5) * SRC_WILD_POKEMON_IMAGE_SIZE);

        try {
            this.backgroundImage = new Image(new FileInputStream(BACKGROUND_IMAGE_FILENAME));
            this.battleBoxes = new Image(new FileInputStream(BATTLE_BOXES_IMAGE_FILENAME));
            this.pokemonImage = new Image(new FileInputStream(POKEMON_IMAGE_FILENAME));
            this.playerImage = new Image(new FileInputStream(PLAYER_IMAGE_FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // BattleScene (Pokemon)


    /**
     * drawFrame()
     *
     * Purpose: Draws a single frame of the BattleScene.
     */
    @Override
    public void drawFrame ()
    {
        this.getPaintBrush().drawImage(this.backgroundImage, 0, 0, this.getWidth(), this.getHeight());
        this.getPaintBrush().drawImage(this.playerImage,
                0, 0,
                SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE,
                PLAYER_X, PLAYER_Y,
                DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
        this.getPaintBrush().drawImage(this.pokemonImage,
                this.wildPokemonSourceX, wildPokemonSourceY,
                SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE,
                WILD_POKEMON_X, WILD_POKEMON_Y,
                DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);
        this.getPaintBrush().drawImage(this.battleBoxes,
                0, 0,
                105, 38,
                390
                , 345,
                105*4.5, 38*4.5);
        this.getPaintBrush().drawImage(this.battleBoxes,
                0, 38,
                105, 30,
                20, 50,
                105*4.5, 30*4.5);
        this.getPaintBrush().drawImage(this.battleBoxes,
                0, 68,
                176, 78,
                440, 523,
                176*2.5, 78*2.5);
        this.getPaintBrush().setFill(Color.BLACK);
        this.getPaintBrush().fillText(this.wildPokemon.getName(), 60, 105);
        this.getPaintBrush().fillText("Safari Balls", 500, 400);
        this.getPaintBrush().fillText("Left: "+this.player.getNumSafariBalls(), 600, 480);
        this.getPaintBrush().fillText("Ball", 510, 595);
        this.getPaintBrush().fillText("Rock", 510, 665);
        this.getPaintBrush().fillText("Bait", 700, 595);
        this.getPaintBrush().fillText("Run", 700, 665);
        this.getPaintBrush().drawImage(this.battleBoxes,
                120, 0,
                32, 32,
                470, 566,
                32, 32);
        this.getPaintBrush().setFill(Color.WHITE);
        this.getPaintBrush().fillText("What will "+this.player.getName(), 40, 600);
        this.getPaintBrush().fillText("throw?", 40, 650);
    } // drawFrame()

} // class BattleScene
