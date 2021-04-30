package io.github.jordanmartin.datagenerator.provider.transform;

import io.github.jordanmartin.datagenerator.provider.base.Constant;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Répète un généteur pour construire une liste
 *
 * @param <T> Le type de la donnée de la liste
 */
public class ListByRepeat<T> implements ValueProvider<List<T>> {

    /**
     * Le générateur qui alimentera la liste
     **/
    private final ValueProvider<T> valueProvider;

    /**
     * Nombre de répétition
     **/
    private final ValueProvider<Integer> countProvider;

    public ListByRepeat(ValueProvider<T> valueProvider, ValueProvider<Integer> countProvider) {
        this.countProvider = countProvider;
        this.valueProvider = valueProvider;
    }

    public ListByRepeat(ValueProvider<T> valueProvier, int count) {
        this(valueProvier, new Constant<>(count));
    }


    @Override
    public List<T> getOneWithContext(IObjectProviderContext ctx) {
        int count = evaluateProviderWithContext(this.countProvider, ctx);
        List<T> array = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            T value = evaluateProviderWithContext(this.valueProvider, ctx);
            array.add(value);
        }
        return array;
    }

    public Stream<T> getAsStream() {
        int count = countProvider.getOne();
        return IntStream.of(0, count)
                .mapToObj(idx -> this.valueProvider.getOne());
    }
}
