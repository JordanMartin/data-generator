package io.github.jordanmartin.datagenerator.provider.random;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.DoubleProvider;
import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;
import io.github.jordanmartin.datagenerator.provider.core.ValueProviderException;

import java.util.Random;

/**
 * Génère un double aléatoire
 */
@Provider(
        name = "Double",
        description = "Retourne un nombre décimale aléatoire",
        examples = {
                "Double(0, 10) => 5.494573180435739"
        }
)
public class RandomDouble implements DoubleProvider, StatelessValueProvider<Double> {
    private final Random random = new Random();
    private final double min;
    private final double max;

    @ProviderCtor("Décimale entre min et max")
    public RandomDouble(double min, double max) {
        if (min > max) {
            throw new ValueProviderException(this, "La valeur minimum doit être <= à la valeur maximum");
        }
        this.min = min;
        this.max = max;
    }

    public RandomDouble(int min, int max) {
        this((double) min, (double) max);
    }

    @ProviderCtor("Décimale entre 0 et max")
    public RandomDouble(double max) {
        this(0, max);
    }

    public RandomDouble(int max) {
        this(0, (double) max);
    }

    @ProviderCtor("Décimale entre 0 et Double.MAX_VALUE")
    public RandomDouble() {
        this(0, Double.MAX_VALUE);
    }

    @Override
    public Double getOne() {
        return min + random.nextDouble() * (max - min);
    }

}
