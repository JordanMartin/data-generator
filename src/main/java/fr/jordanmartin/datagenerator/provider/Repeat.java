package fr.jordanmartin.datagenerator.provider;

import fr.jordanmartin.datagenerator.provider.constant.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Génère une liste à partir d'un autre générateur
 *
 * @param <T> Le type de la donnée de la liste
 */
public class Repeat<T> implements ValueProvider<List<T>> {

    /**
     * Le générateur qui alimentera la liste
     */
    private final ValueProvider<T> valueProvier;

    /**
     * Nombre de répétition
     */
    private final ValueProvider<Integer> countProvider;

    public Repeat(ValueProvider<T> valueProvier, int count) {
        this.valueProvier = valueProvier;
        this.countProvider = new Constant<>(count);
    }

    public Repeat(ValueProvider<T> valueProvider, ValueProvider<Integer> countProvider) {
        this.valueProvier = valueProvider;
        this.countProvider = countProvider;
    }

    @Override
    public List<T> getOne() {
        int count = countProvider.getOne();
        List<T> array = new ArrayList<T>(count);
        for (int i = 0; i < count; i++) {
            T value = this.valueProvier.getOne();
            array.add(value);
        }
        return array;
    }

    public Stream<T> getAsStream() {
        int count = countProvider.getOne();
        return IntStream.of(0, count)
                .mapToObj(idx -> this.valueProvier.getOne());
    }

}
