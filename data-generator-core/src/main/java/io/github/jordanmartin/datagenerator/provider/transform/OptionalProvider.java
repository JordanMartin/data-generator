package io.github.jordanmartin.datagenerator.provider.transform;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;

import java.util.Optional;

/**
 * Converti une valeur en String
 */
@Provider(
        name = "Optional",
        description = "Retourne la valeur de façon optionnel",
        examples = {
        },
        returns = Optional.class
)
public class OptionalProvider implements ValueProvider<Optional<?>> {

    private final ValueProvider<?> provider;
    private final ValueProvider<Boolean> condition;

    @ProviderCtor
    public OptionalProvider(
            @ProviderArg(
                    name = "generateur",
                    description = "Un autre générateur",
                    examples = "IntIncrement()"
            ) ValueProvider<?> provider, ValueProvider<Boolean> condition) {
        this.provider = provider;
        this.condition = condition;
    }

    @ProviderCtor
    public OptionalProvider(
            @ProviderArg(
                    name = "generateur",
                    description = "Un autre générateur",
                    examples = "IntIncrement()"
            ) Object value, ValueProvider<Boolean> condition) {
        this.provider = (ctx) -> value;
        this.condition = condition;
    }

    @Override
    public Optional<?> getOneWithContext(IObjectProviderContext ctx) {
        Object value = evaluateProviderWithContext(provider, ctx);
        Boolean condition = evaluateProviderWithContext(this.condition, ctx);

        if (condition) {
            return Optional.ofNullable(value);
        }
        return Optional.empty();
    }
}
