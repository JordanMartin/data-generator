package io.github.datagenerator.domain.providers.base;


import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderArg;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.StatelessValueProvider;

/**
 * Génère un entier incrémenté à chaque appel.
 * Lorsque la maximum est atteint on revient à la première valeur
 */
@Provider(
        name = "Increment",
        description = "Generate a value which increment on each use",
        group = "id"
)
public class IntIncrement implements StatelessValueProvider<Integer> {

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
     * AutoIncrement start=0, step=1, max={@link Integer#MAX_VALUE}
     */
    @ProviderCtor("start=0, step=1, max=Integer.MAX_VALUE")
    public IntIncrement() {
        this(0, 1, Integer.MAX_VALUE);
    }

    @ProviderCtor("Custom start, step and max")
    public IntIncrement(
            @ProviderArg(description = "First value") int start,
            @ProviderArg(description = "Amount for each increment") int step,
            @ProviderArg(description = "Maximum value. When reached the value restart from <start>") int max
    ) {
        this.start = start;
        this.step = step;
        this.max = max;
        nextValue = start;
    }

    @Override
    public Integer get() {
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
