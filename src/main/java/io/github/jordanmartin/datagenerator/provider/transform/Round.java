package io.github.jordanmartin.datagenerator.provider.transform;

import io.github.jordanmartin.datagenerator.provider.core.DoubleProvider;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Générateur pour arrondir un nombre decimal
 */
public class Round implements DoubleProvider {

    private final int precision;
    private final ValueProvider<Double> provider;
    private final RoundingMode roundingMode;

    public Round(ValueProvider<Double> provider, int precision, RoundingMode roundingMode) {
        this.provider = provider;
        this.precision = precision;
        this.roundingMode = roundingMode;
    }

    public Round(ValueProvider<Double> provider, int precision, String roundingMode) {
        this(provider, precision, RoundingMode.valueOf(roundingMode.toUpperCase()));
    }

    @Override
    public Double getOneWithContext(IObjectProviderContext ctx) {
        Double value = evaluateProviderWithContext(provider, ctx);
        return BigDecimal.valueOf(value)
                .setScale(precision, roundingMode)
                .doubleValue();
    }

    public Round(ValueProvider<Double> provider, int precision) {
        this(provider, precision, RoundingMode.UP);
    }
}
