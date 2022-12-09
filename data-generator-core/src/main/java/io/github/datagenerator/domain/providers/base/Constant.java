package io.github.datagenerator.domain.providers.base;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderArg;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.ObjectContext;
import io.github.datagenerator.domain.core.ValueProvider;

/**
 * Générateur de valeur constante
 *
 * @param <T>
 */
@Provider(
        name = "Constant",
        description = "Returns always the same value", examples = "Constant(\"Hello World\") => \"Hello World\""
)
public class Constant<T> implements ValueProvider<T> {

    private final T value;

    @ProviderCtor
    public Constant(
            @ProviderArg(description = "The constant value. Can be a string, boolean or number")
            T value
    ) {
        this.value = value;
    }

    @Override
    public T get(ObjectContext ctx) {
        return value;
    }
}
