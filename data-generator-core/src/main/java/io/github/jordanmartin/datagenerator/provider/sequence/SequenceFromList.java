package io.github.jordanmartin.datagenerator.provider.sequence;

import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.ProviderCtor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Génère une valeur à partir d'une liste données. A chaque appel, on renvoi la valeur suivante de la liste
 *
 * @param <T> Le type des valeurs
 */
@Provider(name = "Sequence", description = "Retourne successivement et dans l'ordre tous les éléments de la liste. " +
        "Retourne au début de la liste quand tous les éléments ont été parcourus")
public class SequenceFromList<T> implements StatelessValueProvider<T> {

    private final List<T> items;
    private int nextIdx;

    @SafeVarargs
    @ProviderCtor
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
