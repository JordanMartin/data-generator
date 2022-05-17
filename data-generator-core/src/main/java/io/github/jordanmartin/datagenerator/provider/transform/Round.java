package io.github.jordanmartin.datagenerator.provider.transform;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.DoubleProvider;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Générateur pour arrondir un nombre decimal
 */
@Provider(
        name = "Round",
        description = "Arrondie une valeur décimale",
        examples = {
                "Round(Double(10, 20), 2) => 17.22"
        },
        group = "nombre"
)
public class Round implements DoubleProvider {

    private final int precision;
    private final ValueProvider<Double> provider;
    private final RoundingMode roundingMode;

    public Round(ValueProvider<Double> provider, int precision, RoundingMode roundingMode) {
        this.provider = provider;
        this.precision = precision;
        this.roundingMode = roundingMode;
    }

    @ProviderCtor
    public Round(
            @ProviderArg(description = "Un générateur de double")
            ValueProvider<Double> provider,
            @ProviderArg(description = "Nombre de décimales")
            int precision,
            @ProviderArg(
                    description = "Type de l'arrondie",
                    examples = {"UP", "DOWN", "CEILING", "FLOOR", "HALF_UP", "HALF_DOWN", "HALF_EVEN"}
            ) String roundingMode
    ) {
        this(provider, precision, RoundingMode.valueOf(roundingMode.toUpperCase()));
    }

    @ProviderCtor("Arrondie à la décimale supérieur")
    public Round(
            @ProviderArg(description = "Un générateur de double")
            ValueProvider<Double> provider,
            @ProviderArg(description = "Nombre de décimales")
            int precision
    ) {
        this(provider, precision, RoundingMode.UP);
    }

    @Override
    public Double getOneWithContext(IObjectProviderContext ctx) {
        Double value = evaluateProviderWithContext(provider, ctx);
        return BigDecimal.valueOf(value)
                .setScale(precision, roundingMode)
                .doubleValue();
    }
}
