package fr.jordanmartin.datagenerator.provider.constant;

import fr.jordanmartin.datagenerator.provider.ValueProvider;

/**
 * Générateur de valeur constante
 *
 * @param <T>
 */
public class Constant<T> implements ValueProvider<T> {

    private final T value;

    public Constant(T value) {
        this.value = value;
    }

    @Override
    public T getOne() {
        return value;
    }
}
