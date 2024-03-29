package io.github.datagenerator.domain.providers.random;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.StatelessValueProvider;
import io.github.datagenerator.domain.core.ValueProviderException;

import java.util.Random;

/**
 * Génère un double aléatoire
 */
@Provider(
        name = "Double",
        description = "Random decimal number",
        examples = {
                "Double(0, 10) => 5.494573180435739"
        },
        group = "number"
)
public class RandomDouble implements StatelessValueProvider<Double> {
    private final Random random = new Random();
    private final double min;
    private final double max;

    @ProviderCtor("Decimal number between min and max")
    public RandomDouble(double min, double max) {
        if (min > max) {
            throw new ValueProviderException(this, "Min value must be less than the max");
        }
        this.min = min;
        this.max = max;
    }

    @ProviderCtor("Decimal number between 0 and max")
    public RandomDouble(double max) {
        this(0, max);
    }

    public RandomDouble(int min, int max) {
        this(min, (double) max);
    }

    public RandomDouble(int max) {
        this(0, (double) max);
    }

    @ProviderCtor("Decimal value between 0 and Double.MAX_VALUE (1.7976931348623157e+308)")
    public RandomDouble() {
        this(0, Double.MAX_VALUE);
    }

    @Override
    public Double get() {
        return min + random.nextDouble() * (max - min);
    }

}
