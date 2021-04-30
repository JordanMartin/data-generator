package io.github.jordanmartin.datagenerator.provider.random;

import io.github.jordanmartin.datagenerator.provider.core.IntegerProvider;
import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;
import io.github.jordanmartin.datagenerator.provider.core.ValueProviderException;

import java.util.Random;

/**
 * Génère un entier aléatoire
 */
public class RandomInt implements IntegerProvider, StatelessValueProvider<Integer> {

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
