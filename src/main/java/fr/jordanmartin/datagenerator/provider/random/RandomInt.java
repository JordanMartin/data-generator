package fr.jordanmartin.datagenerator.provider.random;

import fr.jordanmartin.datagenerator.provider.base.IntegerProvider;
import fr.jordanmartin.datagenerator.provider.base.ValueProviderException;

import java.util.Random;

/**
 * Génère un entier aléatoire
 */
public class RandomInt implements IntegerProvider {

    private final Random random = new Random();
    private final int min;
    private final int max;

    public RandomInt(int min, int max) {
        if (min > max) {
            throw new ValueProviderException(this, "La valeur minimum doit être <= à la valeur maximum");
        }
        this.min = min;
        this.max = max;
    }

    public RandomInt(int max) {
        this(0, max);
    }

    @Override
    public Integer getOne() {
        return random.nextInt(max - min + 1) + min;
    }

}
