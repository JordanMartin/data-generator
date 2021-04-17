package fr.jordanmartin.datagenerator.provider.transform;

import fr.jordanmartin.datagenerator.provider.base.ValueProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Génère une liste à partir d'un autre générateur
 */
public class ListOf implements ValueProvider<List<?>> {

    /**
     * Les générateurs de valeur
     */
    private final List<ValueProvider<?>> valueProviers;

    public ListOf(List<ValueProvider<?>> valueProviers) {
        this.valueProviers = valueProviers;
    }

    public ListOf(ValueProvider<?>... valueProviers) {
        this.valueProviers = Arrays.asList(valueProviers);
    }

    @Override
    public List<Object> getOne() {
        List<Object> array = new ArrayList<>(valueProviers.size());
        for (ValueProvider<?> provider : valueProviers) {
            array.add(provider.getOne());
        }
        return array;
    }
}
