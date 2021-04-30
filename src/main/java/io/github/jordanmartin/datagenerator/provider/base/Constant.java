package io.github.jordanmartin.datagenerator.provider.base;

import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;

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
    public T getOneWithContext(IObjectProviderContext ctx) {
        return value;
    }
}
