package view;

import controller.PokemonSafari;
import controller.audio.CryPlayer;
import controller.audio.SfxLibrary;
import controller.audio.SfxPlayer;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.pokemon.Pokemon;
import model.pokemon.PokemonFactory;
import model.pokemon.Rarity;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * CollectionScene.java
 *
 * Purpose: Displays and operates the collection scene when
 *      the player wants to see theor caught Pokemon.
 */
public class CollectionScene extends GameScene
{
    private static final String POKEMON_IMAGE_FILENAME = "images/battle/pokemon_sprites.png";
    private static final String COLLECTION_IMAGE_FILENAME = "images/collection/collection_sprites.png";
    private static final String SMALL_POKEMON_IMAGE_FILENAME = "images/collection/pokemon_sprites.png";

    private static final Font SMALL_FONT = Font.font("Verdana", 20);
    private static final Font MEDIUM_FONT = Font.font("Verdana", 28);
    private static final Font BIG_FONT = Font.font("Verdana", 35);

    private static final double DEFAULT_BRIGHTNESS = 0.0;
    private static final double BLACK_SCREEN_BRIGHTNESS = -1.0;

    private static final double MAX_BAR_HEIGHT = 416.0;

    private Image pokemonImage;
    private Image collectionImage;
    private Image smallPokemonImage;

    private List<Pokemon> caughtList;
    private int selectedPokemon;

    private double barHeightPercent;
    private double barHeight;

    private int topPokemon;
    private int cursorPosition;


    /**
     *
     */
    public CollectionScene (final List<Pokemon> caughtList)
    {
        super();
        this.caughtList = caughtList;

        this.barHeightPercent = this.caughtList.size() <= 5 ? 1.0 : 5.0 / this.caughtList.size();
        this.barHeight = MAX_BAR_HEIGHT * this.barHeightPercent;

        this.selectedPokemon = 0;
        this.topPokemon = 0;
        this.cursorPosition = 0;

        getPaintBrush().setLineWidth(3);
        getPaintBrush().setStroke(Color.BLACK);

        try {
            this.pokemonImage = new Image(new FileInputStream(POKEMON_IMAGE_FILENAME));
            this.collectionImage = new Image(new FileInputStream(COLLECTION_IMAGE_FILENAME));
            this.smallPokemonImage = new Image(new FileInputStream(SMALL_POKEMON_IMAGE_FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     */
    @Override
    public void start()
    {
        new TransitionInAnimation().start();
    }


    /**
     *
     */
    @Override
    public void restart () { /* Nothing */ }


    private final class TransitionInAnimation extends AnimationTimer
    {
        private final ColorAdjust colorAdjust = new ColorAdjust();

        private double screenBrightness;

        private TransitionInAnimation ()
        {
            this.screenBrightness = BLACK_SCREEN_BRIGHTNESS;
        }


        @Override
        public void handle (final long now)
        {
            if (this.screenBrightness < DEFAULT_BRIGHTNESS)
            {
                this.screenBrightness += 0.04;
                this.colorAdjust.setBrightness(this.screenBrightness);
                getPaintBrush().setEffect(this.colorAdjust);
            }
            else {
                this.stop();
                setupControls();
            }

            drawFrame();
        }
    }



    private void setupControls ()
    {
        this.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent event) {
                switch (event.getCode()) {
                    case W:
                        if (selectedPokemon != 0) {
                            if (cursorPosition > 0)
                                cursorPosition--;
                            selectedPokemon--;
                            SfxPlayer.getInstance().play(SfxLibrary.Select.name());
                            drawFrame();
                        }
                        break;
                    case S:
                        if (selectedPokemon < caughtList.size()-1) {
                            if (cursorPosition < 5)
                                cursorPosition++;
                            System.out.println(cursorPosition);
                            selectedPokemon++;
                            SfxPlayer.getInstance().play(SfxLibrary.Select.name());
                            drawFrame();
                        }
                        break;
                    case SPACE:
                        if (!caughtList.isEmpty())
                            CryPlayer.getInstance().play(caughtList.get(selectedPokemon).getName());
                        break;
                    case Z:
                        getScene().setOnKeyPressed(null);
                        SfxPlayer.getInstance().play(SfxLibrary.Select.name());
                        new TransitionOutAnimation().start();
                        break;
                }
            }
        });
    }





    private void drawFrame ()
    {
        getPaintBrush().setFill(Color.DARKBLUE);
        getPaintBrush().fillRect(0,0, PokemonSafari.getWindowWidth(), PokemonSafari.getWindowHeight());

        getPaintBrush().setFill(Color.WHITE);
        getPaintBrush().setFont(BIG_FONT);
        getPaintBrush().setTextAlign(TextAlignment.CENTER);
        getPaintBrush().fillText("Collection", PokemonSafari.getWindowWidth()/2, 80);
        getPaintBrush().setTextAlign(TextAlignment.LEFT);

        getPaintBrush().strokeRect(80,140, 250, 500);
        getPaintBrush().setStroke(Color.GRAY);
        getPaintBrush().strokeRect(333,140, 40, 500);
        getPaintBrush().setStroke(Color.BLACK);
        getPaintBrush().setFill(Color.BLACK);
        getPaintBrush().fillRect(335, 141, 36, 40);
        getPaintBrush().fillRect(335, 599, 36, 40);
        getPaintBrush().setFill(Color.SILVER);
        getPaintBrush().drawImage(collectionImage, 0, 64, 32, 32, 337, 143, 32, 32);
        getPaintBrush().drawImage(collectionImage, 0, 32, 32, 32, 337, 604, 32, 32);

        getPaintBrush().strokeRect(400, 140, 400, 500);

        if (caughtList.isEmpty())
        {
            getPaintBrush().setFont(MEDIUM_FONT);
            getPaintBrush().setTextAlign(TextAlignment.CENTER);
            getPaintBrush().fillText("None", 210, 350);
            getPaintBrush().fillText("Catch Pokemon", 600, 325);
            getPaintBrush().fillText("to view them here!", 600, 375);
            getPaintBrush().setTextAlign(TextAlignment.LEFT);
        }
        else {
            if (caughtList.size() <= 5)
                for (int i = 0; i < caughtList.size(); i++)
                    drawPokemonInfo(i);
            else
            {
                for (int i = 0; i < 5; i++)
                    drawPokemonInfo(i+topPokemon);
            }


            getPaintBrush().fillRect(335, 182, 36, barHeight);

            getPaintBrush().setFont(MEDIUM_FONT);
            getPaintBrush().setFill(Color.WHITE);
            getPaintBrush().fillText(caughtList.get(selectedPokemon).getName(), 470, 190);
            getPaintBrush().fillText("Lv:", 470, 510);
            getPaintBrush().fillText(""+caughtList.get(selectedPokemon).getLevel(), 520, 510);
            getPaintBrush().fillText("HP:", 470, 550);
            getPaintBrush().fillText(""+caughtList.get(selectedPokemon).getHp(), 530, 550);
            getPaintBrush().fillText("Rarity:", 470, 590);

            getPaintBrush().setFill(Color.SKYBLUE);
            getPaintBrush().fillRect(470, 220, 250, 180);
            getPaintBrush().setFill(Color.GREEN);
            getPaintBrush().fillRect(470, 400, 250, 70);

            getPaintBrush().strokeRect(470, 220, 250, 250);
            getPaintBrush().drawImage(pokemonImage, (caughtList.get(selectedPokemon).getID() % 5) * 100, (caughtList.get(selectedPokemon).getID() / 5) * 100,
                    100, 100, 460, 200, 250, 250);

            final int numStars = caughtList.get(selectedPokemon).getRarity() == Rarity.Common ? 1 : caughtList.get(selectedPokemon).getRarity() == Rarity.Uncommon ? 3 : 5;
            for (int i = 0; i < numStars; i++)
                getPaintBrush().drawImage(collectionImage, 0, 0, 32, 32, 580+(40*i), 562, 32, 32);

            getPaintBrush().setStroke(Color.YELLOW);
            getPaintBrush().strokeRect(80, 140 + (100 * selectedPokemon), 250, 100);
            getPaintBrush().setStroke(Color.BLACK);

            getPaintBrush().setFill(Color.BLACK);
            getPaintBrush().fillRect(635, 660, 100, 40);
            getPaintBrush().setFont(SMALL_FONT);
            getPaintBrush().setFill(Color.WHITE);
            getPaintBrush().fillText("Play Cry", 535, 685);
            getPaintBrush().fillText("SPACE", 650, 685);
        }
    }



    private void drawPokemonInfo (final int id)
    {
        getPaintBrush().strokeRect(80, 140 + (100 * id), 250, 100);
        getPaintBrush().setFont(MEDIUM_FONT);
        getPaintBrush().fillText(caughtList.get(id).getName(), 100, 175 + (100 * id));
        getPaintBrush().setFont(SMALL_FONT);
        getPaintBrush().fillText("Lv:", 100, 210 + (100 * id));
        getPaintBrush().fillText("" + caughtList.get(id).getLevel(), 140, 210 + (100 * id));
        getPaintBrush().drawImage(smallPokemonImage,
                (caughtList.get(id).getID()%5)*50, (caughtList.get(id).getID()/5)*50, 50, 50,
                250, 160 + (100 * id), 80, 80);
    }



    private final class TransitionOutAnimation extends AnimationTimer
    {
        private final ColorAdjust colorAdjust = new ColorAdjust();

        private double screenBrightness;

        private TransitionOutAnimation ()
        {
            this.screenBrightness = DEFAULT_BRIGHTNESS;
        }


        @Override
        public void handle (final long now)
        {
            if (this.screenBrightness > BLACK_SCREEN_BRIGHTNESS)
            {
                this.screenBrightness -= 0.04;
                this.colorAdjust.setBrightness(this.screenBrightness);
                getPaintBrush().setEffect(this.colorAdjust);
            }
            else {
                this.stop();
                PokemonSafari.goToPreviousScene();
            }

            drawFrame();
        }
    }


}
