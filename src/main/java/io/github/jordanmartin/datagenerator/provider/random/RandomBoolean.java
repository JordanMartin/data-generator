package io.github.jordanmartin.datagenerator.provider.random;

import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;

/**
 * Génère un booléen aléatoire
 */
public class RandomBoolean implements StatelessValueProvider<Boolean> {

    private final double threshold;

    /**
     * @param threshold Ratio de génération d'une valeur {@code true} entre 0 et 1.
     *                  Ex: 0.5: autant de true que de false
     *                  Ex: 0.25: 25% de true, 75% de true
     */
    public RandomBoolean(double threshold) {
        this.threshold = threshold;
    }

    /**
     * Génère autant de valeur true que de valeur false
     */
    public RandomBoolean() {
        this(0.5);
    }

    @Override
    public Boolean getOne() {
        if (threshold <= 0) {
            return false;
        } else if (threshold >= 1) {
            return true;
        }

        return Math.random() < threshold;
    }
}
