package fr.jordanmartin.datagenerator.provider.transform;

import fr.jordanmartin.datagenerator.provider.base.DoubleProvider;
import fr.jordanmartin.datagenerator.provider.base.ValueProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Générateur pour arrondir un nombre decimal
 */
public class Round implements DoubleProvider {

    private final ValueProvider<Double> provider;
    private final int precision;
    private final RoundingMode roundingMode;

    public Round(ValueProvider<Double> provider, int precision, RoundingMode roundingMode) {
        this.provider = provider;
        this.precision = precision;
        this.roundingMode = roundingMode;
    }

    public Round(ValueProvider<Double> provider, int precision, String roundingMode) {
        this(provider, precision, RoundingMode.valueOf(roundingMode));
    }

    public Round(ValueProvider<Double> provider, int precision) {
        this(provider, precision, RoundingMode.UP);
    }

    @Override
    public Double getOne() {
        return BigDecimal.valueOf(provider.getOne())
                .setScale(precision, roundingMode)
                .doubleValue();
    }
}
