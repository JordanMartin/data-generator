package fr.jordanmartin.datagenerator.provider.sequence;

import fr.jordanmartin.datagenerator.provider.base.ValueProvider;

/**
 * Génère un entier incrémenté à chaque appel.
 * Lorsque la maximum est atteint on revient à la première valeur
 */
public class IntAutoIncrement implements ValueProvider<Integer> {

    /**
     * Première minmum
     */
    private final int start;

    /**
     * Incrément
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
        if (step < 0 && this.nextValue + step < this.max) {
            this.nextValue = this.start;
        } else if (step > 0 && this.nextValue + step > this.max) {
            this.nextValue = this.start;
        } else {
            this.nextValue = this.nextValue + step;
        }
        return currentValue;
    }
}
