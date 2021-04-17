package fr.jordanmartin.datagenerator.provider.transform;

import fr.jordanmartin.datagenerator.provider.base.Constant;
import fr.jordanmartin.datagenerator.provider.base.ValueProvider;
import fr.jordanmartin.datagenerator.provider.object.ObjectProviderContext;
import fr.jordanmartin.datagenerator.provider.object.ValueProviderWithContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Génère une liste à partir d'un autre générateur
 *
 * @param <T> Le type de la donnée de la liste
 */
public class ListByRepeat<T> extends ValueProviderWithContext<List<T>> {

    /**
     * Le générateur qui alimentera la liste
     **/
    private final ValueProvider<T> valueProvider;
    private final ValueProviderWithContext<T> valueProvierWithContext;

    /**
     * Nombre de répétition
     **/
    private final ValueProvider<Integer> countProvider;

    public ListByRepeat(ValueProvider<T> valueProvider, ValueProvider<Integer> countProvider) {
        this.countProvider = countProvider;
        if (valueProvider instanceof ValueProviderWithContext) {
            this.valueProvierWithContext = (ValueProviderWithContext<T>) valueProvider;
            this.valueProvider = null;
        } else {
            this.valueProvider = valueProvider;
            this.valueProvierWithContext = null;
        }
    }

    public ListByRepeat(ValueProvider<T> valueProvier, int count) {
        this(valueProvier, new Constant<>(count));
    }

    @Override
    public List<T> getOne() {
        return evaluate(null);
    }

    public Stream<T> getAsStream() {
        int count = countProvider.getOne();
        return IntStream.of(0, count)
                .mapToObj(idx -> this.valueProvider.getOne());
    }

    @Override
    public List<T> evaluate(ObjectProviderContext ctx) {
        int count = countProvider.getOne();
        List<T> array = new ArrayList<T>(count);
        for (int i = 0; i < count; i++) {
            T value = getOneOrEvaluate(ctx);
            array.add(value);
        }
        return array;
    }

    private T getOneOrEvaluate(ObjectProviderContext ctx) {
        if (this.valueProvider != null) {
            return valueProvider.getOne();
        } else if (this.valueProvierWithContext != null) {
            return valueProvierWithContext.evaluate(ctx);
        }
        return null;
    }
}
