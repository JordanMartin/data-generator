package io.github.jordanmartin.datagenerator.provider.sequence;


import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;

/**
 * Génère un entier incrémenté à chaque appel.
 * Lorsque la maximum est atteint on revient à la première valeur
 */
public class IntAutoIncrement implements StatelessValueProvider<Integer> {

    /**
     * Valeur initiale
     */
    private final int start;

    /**
     * Incrément à chaque génération
     */
    private final int step;

    /**
     * Valeur max
     */
    private final int max;

    /**
     * Prochaine valeur à renvoyer
     */
    private int nextValue;

    /**
     * AutoIncrement start=0, step=1, max={@link Integer.MAX_VALUE}
     */
    public IntAutoIncrement() {
        this(0, 1, Integer.MAX_VALUE);
    }

    public IntAutoIncrement(int start, int step, int max) {
        this.start = start;
        this.step = step;
        this.max = max;
        nextValue = start;
    }

    @Override
    public Integer getOne() {
        int currentValue = nextValue;
        if (shouldNextValueSetToStart()) {
            this.nextValue = this.start;
        } else {
            this.nextValue = this.nextValue + step;
        }
        return currentValue;
    }

    private boolean shouldNextValueSetToStart() {
        return isNextValueGreaterThatMaximum() || isNextValueLessThanMinimum();
    }

    private boolean isNextValueGreaterThatMaximum() {
        return isPositiveStep() && this.nextValue + step > this.max;
    }

    private boolean isPositiveStep() {
        return step > 0;
    }

    private boolean isNextValueLessThanMinimum() {
        return !isPositiveStep() && this.nextValue + step < this.max;
    }
}