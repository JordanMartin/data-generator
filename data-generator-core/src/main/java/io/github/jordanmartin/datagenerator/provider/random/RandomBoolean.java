package io.github.jordanmartin.datagenerator.provider.random;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;

/**
 * Génère un booléen aléatoire
 */
@Provider(
        name = "Boolean",
        description = "Random boolean",
        group = "boolean",
        examples = "Boolean(0.3) => returns 30% of true"
)
public class RandomBoolean implements StatelessValueProvider<Boolean> {

    public static final double AS_MANY_TRUE_AS_FALSE_PERCENTAGE = 0.5;
    public static final int ONLY_FALSE_PERCENTAGE = 0;
    public static final int ONLY_TRUE_PERCENTAGE = 1;

    private final double percentage;

    /**
     * Génère un booléen en fonction du pourcentage précisé
     *
     * @param percentageOfTrue Ratio de génération d'une valeur {@code true} entre 0 et 1.
     *                         Ex: 0.5: autant de true que de false
     *                         Ex: 0.25: 25% de true, 75% de true
     */
    @ProviderCtor("Define the probability of returning true")
    public RandomBoolean(
            @ProviderArg(description = "The probability between 0 (always false) and 1 (always true)")
                    double percentageOfTrue
    ) {
        this.percentage = percentageOfTrue;
    }

    /**
     * Génère autant de valeur true que de valeur false
     */
    @ProviderCtor("Equal probability of returning true and false")
    public RandomBoolean() {
        this(AS_MANY_TRUE_AS_FALSE_PERCENTAGE);
    }

    @Override
    public Boolean getOne() {
        if (percentage <= ONLY_FALSE_PERCENTAGE) {
            return false;
        } else if (percentage >= ONLY_TRUE_PERCENTAGE) {
            return true;
        }

        return Math.random() < percentage;
    }
}
