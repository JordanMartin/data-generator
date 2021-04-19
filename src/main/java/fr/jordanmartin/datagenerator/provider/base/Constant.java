package fr.jordanmartin.datagenerator.provider.base;

import fr.jordanmartin.datagenerator.provider.core.ValueProvider;
import fr.jordanmartin.datagenerator.provider.object.ObjectProviderContext;

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
    public T getOneWithContext(ObjectProviderContext ctx) {
        return value;
    }
}
