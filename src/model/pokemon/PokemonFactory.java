package model.pokemon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

/**
 * PokemonFactory.java
 *
 * Purpose: Used to create Pokemon.
 */
public final class PokemonFactory
{
    private static final int NUM_COMMON   = 6;
    private static final int NUM_UNCOMMON = 3;
    private static final int NUM_RARE     = 1;

    private static final int MIN_LEVEL = 25;
    private static final int MAX_LEVEL = 30;


    /**
     * getPokemon()
     *
     * Purpose: Given the rarity, a random Pokemon is created and returned.
     */
    public static Pokemon getPokemon (final Rarity rarity)
    {
        final String filename = "data/pokemon/" + (rarity == Rarity.Common ? "Common" : rarity == Rarity.Uncommon ? "Uncommon" : "Rare") + ".txt";
        final int maxLines = (rarity == Rarity.Common ? NUM_COMMON : rarity == Rarity.Uncommon ? NUM_UNCOMMON : NUM_RARE);
        final String pokemonLine = getPokemonLine(filename, maxLines);
        return createPokemon(pokemonLine.split(", "), rarity);
    } // getPokemon


    /**
     * getPokemonLine()
     *
     * Purpose: Given the file name and maximum number of lines in the file,
     *      a random line of Pokemon data from the file is returned.
     */
    private static String getPokemonLine (final String filename, final int maxLines)
    {
        final Random r = new Random();
        final int selectedPokemonID = r.nextInt(maxLines);
        //System.out.print(selectedPokemonID);
        String selectedPokemonLine = "";
        try {
            BufferedReader bf = new BufferedReader(new FileReader(filename));
            for (int i = 0; i <= selectedPokemonID; i++)
                selectedPokemonLine = bf.readLine();
            bf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectedPokemonLine;
    } // getPokemonLine()


    /**
     * createPokemon()
     *
     * Purpose: Given an array of Pokemon data, the data is parsed, and a Pokemon
     *      object is created with a random level and gender, then returned.
     */
    private static Pokemon createPokemon (final String[] pokemonInfo, final Rarity rarity)
    {
        final Random r = new Random();
        final int id = Integer.parseInt(pokemonInfo[0]);
        final String name = pokemonInfo[1];
        final int level = r.nextInt(MAX_LEVEL-MIN_LEVEL+1)+MIN_LEVEL;
        final int hp = calcHP(level, Integer.parseInt(pokemonInfo[2]));
        final int catchPercent = Integer.parseInt(pokemonInfo[3]);
        final int runPercent = Integer.parseInt(pokemonInfo[4]);
        final int maxDuration = Integer.parseInt(pokemonInfo[5]);
        final Gender gender = r.nextInt(2) == 0 ? Gender.Male : Gender.Female;
        return new Pokemon(id, name, hp, level, gender, catchPercent, runPercent, maxDuration, rarity);
    } // createPokemon()


    /**
     * calcHP()
     *
     * Purpose: Calculates the actual HP stat of a Pokemon using the level
     *      and base HP stats.
     */
    private static int calcHP (final int level, final int baseHP)
    {
        return ((2 * baseHP * level) / 100) + level + 10;
    } // calcHP()

} // final class PokemonFactory
