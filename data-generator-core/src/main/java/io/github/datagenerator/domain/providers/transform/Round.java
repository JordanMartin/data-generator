package io.github.datagenerator.domain.providers.transform;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderArg;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.ObjectContext;
import io.github.datagenerator.domain.core.ValueProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Générateur pour arrondir un nombre decimal
 */
@Provider(
        name = "Round",
        description = "Round a decimal value",
        examples = {
                "Round(Double(10, 20), 2) => 17.22"
        },
        group = "number"
)
public class Round implements ValueProvider<Double> {

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
            @ProviderArg(description = "A double number provider")
            ValueProvider<Double> provider,
            @ProviderArg(description = "Number of digit for decimal part")
            int precision,
            @ProviderArg(
                    description = "Rounding mode",
                    examples = {"UP", "DOWN", "CEILING", "FLOOR", "HALF_UP", "HALF_DOWN", "HALF_EVEN"}
            ) String roundingMode
    ) {
        this(provider, precision, RoundingMode.valueOf(roundingMode.toUpperCase()));
    }

    @ProviderCtor("Round to the upper decimal number")
    public Round(
            @ProviderArg(description = "A double number provider")
            ValueProvider<Double> provider,
            @ProviderArg(description = "Number of digit for decimal part")
            int precision
    ) {
        this(provider, precision, RoundingMode.UP);
    }

    @Override
    public Double get(ObjectContext ctx) {
        Double value = evaluateProvider(provider, ctx);
        return BigDecimal.valueOf(value)
                .setScale(precision, roundingMode)
                .doubleValue();
    }
}
