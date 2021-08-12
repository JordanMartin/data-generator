package io.github.jordanmartin.datagenerator.provider.random;

import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;

/**
 * Génère un booléen aléatoire
 */
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
    public RandomBoolean(double percentageOfTrue) {
        this.percentage = percentageOfTrue;
    }

    /**
     * Génère autant de valeur true que de valeur false
     */
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
