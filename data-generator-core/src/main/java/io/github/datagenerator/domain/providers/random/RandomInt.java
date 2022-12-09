package io.github.datagenerator.domain.providers.random;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.StatelessValueProvider;
import io.github.datagenerator.domain.core.ValueProviderException;

import java.util.Random;

/**
 * Génère un entier aléatoire
 */
@Provider(
        name = "Integer",
            description = "Random number",
        examples = {"Integer()", "Integer(10)", "Integer(10, 10)"},
        group = "number"
)
public class RandomInt implements StatelessValueProvider<Integer> {

    private final Random random = new Random();
    private final int min;
    private final int max;

    @ProviderCtor("Number between min and max")
    public RandomInt(int min, int max) {
        if (min > max) {
            throw new ValueProviderException(this, "Min value must be less than the max");
        }
        this.min = min;
        this.max = max;
    }

    @ProviderCtor("Number between 0 and Integer.MAX_VALUE - 1")
    public RandomInt() {
        this(0, Integer.MAX_VALUE - 1);
    }

    @ProviderCtor("Number between 0 and max")
    public RandomInt(int max) {
        this(0, max);
    }

    @Override
    public Integer get() {
        return random.nextInt(max - min + 1) + min;
    }

}
