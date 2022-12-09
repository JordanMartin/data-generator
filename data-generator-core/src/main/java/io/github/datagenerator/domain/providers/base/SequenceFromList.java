package io.github.datagenerator.domain.providers.base;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderArg;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.StatelessValueProvider;
import io.github.datagenerator.domain.core.ValueProviderException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Génère une valeur à partir d'une liste données. A chaque appel, on renvoi la valeur suivante de la liste
 *
 * @param <T> Le type des valeurs
 */
@Provider(
        name = "Sequence",
        description = "Returns sequentially and in order all the values from a list" +
                "When all elements browsed, restarts from beginning",
        examples = {
                "Sequence([\"a\", \"b\", \"c\"]) => \"a\" then \"b\" then \"c\" then \"a\" ..."
        }
)
public class SequenceFromList<T> implements StatelessValueProvider<T> {

    private final List<T> items;
    private int nextIdx;

    @SafeVarargs
    @ProviderCtor
    public SequenceFromList(
            @ProviderArg(description = "List of values of any types") T... items
    ) {
        this.items = Arrays.asList(items);
        this.nextIdx = 0;
        throwOnEmptyItems();
    }

    public SequenceFromList(
            List<T> items
    ) {
        this.items = new ArrayList<>(items);
        this.nextIdx = 0;
        throwOnEmptyItems();
    }

    @Override
    public T get() {
        int currentIdx = this.nextIdx % items.size();
        ++this.nextIdx;
        return items.get(currentIdx);
    }

    private void throwOnEmptyItems() {
        if (items.isEmpty()) {
            throw new ValueProviderException(this, "You must provide at least one item in the sequence");
        }
    }
}
