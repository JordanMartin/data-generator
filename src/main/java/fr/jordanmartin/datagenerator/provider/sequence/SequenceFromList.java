package fr.jordanmartin.datagenerator.provider.sequence;

import fr.jordanmartin.datagenerator.provider.base.ValueProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Génère une valeur à partir d'une liste données. A chaque appel, on renvoi la valeur suivante de la liste
 *
 * @param <T> Le type des valeurs
 */
public class SequenceFromList<T> implements ValueProvider<T> {

    private final List<T> items;
    private int nextIdx;

    @SafeVarargs
    public SequenceFromList(T... items) {
        this.items = Arrays.asList(items);
        this.nextIdx = 0;
    }

    public SequenceFromList(List<T> items) {
        this.items = new ArrayList<>(items);
        this.nextIdx = 0;
    }

    @Override
    public T getOne() {
        int currentIdx = this.nextIdx % items.size();
        ++this.nextIdx;
        return items.get(currentIdx);
    }
}
