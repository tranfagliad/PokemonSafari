package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
    private static final String BACKGROUND_IMAGE_FILENAME = "images/battle_background.png";
    private static final String POKEMON_IMAGE_FILENAME    = "images/pokemon_sprites.png";
    private static final String PLAYER_IMAGE_FILENAME     = "images/trainer_battle_sprites.png";

    private static final double SRC_WILD_POKEMON_IMAGE_SIZE = 100;
    private static final double DEST_WILD_POKEMON_IMAGE_SIZE = SRC_WILD_POKEMON_IMAGE_SIZE * 3;
    private static final double WILD_POKEMON_X = 480;
    private static final double WILD_POKEMON_Y = 20;

    private static final double SRC_PLAYER_IMAGE_SIZE = 70;
    private static final double DEST_PLAYER_IMAGE_SIZE = SRC_PLAYER_IMAGE_SIZE * 3.5;
    private static final double PLAYER_X = 100;
    private static final double PLAYER_Y = 278;

    private Image backgroundImage;
    private Image pokemonImage;
    private Image playerImage;
    private GraphicsContext paintBrush;

    private int wildPokemonSourceX;
    private int wildPokemonSourceY;


    /**
     * BattleScene (Pokemon)
     *
     * Purpose: Creates and initializes the BattleScene for the given Pokemon.
     */
    public BattleScene (Pokemon wildPokemon)
    {
        super();
        this.paintBrush = this.getGraphicsContext2D();
        this.wildPokemonSourceX = (int)((wildPokemon.getID() % 5) * SRC_WILD_POKEMON_IMAGE_SIZE);
        this.wildPokemonSourceY = (int)((wildPokemon.getID() / 5) * SRC_WILD_POKEMON_IMAGE_SIZE);

        try {
            this.backgroundImage = new Image(new FileInputStream(BACKGROUND_IMAGE_FILENAME));
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
        this.paintBrush.drawImage(this.backgroundImage, 0, 0, this.getWidth(), this.getHeight());
        this.paintBrush.drawImage(this.playerImage,
                0, 0,
                SRC_PLAYER_IMAGE_SIZE, SRC_PLAYER_IMAGE_SIZE,
                PLAYER_X, PLAYER_Y,
                DEST_PLAYER_IMAGE_SIZE, DEST_PLAYER_IMAGE_SIZE);
        this.paintBrush.drawImage(this.pokemonImage,
                this.wildPokemonSourceX, wildPokemonSourceY,
                SRC_WILD_POKEMON_IMAGE_SIZE, SRC_WILD_POKEMON_IMAGE_SIZE,
                WILD_POKEMON_X, WILD_POKEMON_Y,
                DEST_WILD_POKEMON_IMAGE_SIZE, DEST_WILD_POKEMON_IMAGE_SIZE);
    } // drawFrame()

} // class BattleScene
