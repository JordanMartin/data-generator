package io.github.jordanmartin.datagenerator.provider.base;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;

/**
 * Générateur de valeur constante
 *
 * @param <T>
 */
@Provider(name = "Constant", description = "Retourne toujours la même valeur")
public class Constant<T> implements ValueProvider<T> {

    private final T value;

    @ProviderCtor
    public Constant(
            T value
    ) {
        this.value = value;
    }

    @Override
    public T getOneWithContext(IObjectProviderContext ctx) {
        return value;
    }
}
