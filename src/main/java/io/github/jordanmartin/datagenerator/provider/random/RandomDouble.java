package io.github.jordanmartin.datagenerator.provider.random;

import io.github.jordanmartin.datagenerator.provider.core.DoubleProvider;
import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;
import io.github.jordanmartin.datagenerator.provider.core.ValueProviderException;

import java.util.Random;

/**
 * Génère un double aléatoire
 */
public class RandomDouble implements DoubleProvider, StatelessValueProvider<Double> {
    private final Random random = new Random();
    private final double min;
    private final double max;

    public RandomDouble(int min, int max) {
        if (min > max) {
            throw new ValueProviderException(this, "La valeur minimum doit être <= à la valeur maximum");
        }
        this.min = min;
        this.max = max;
    }

    public RandomDouble(int max) {
        this(0, max);
    }

    @Override
    public Double getOne() {
        return min + random.nextDouble() * (max - min);
    }

}
