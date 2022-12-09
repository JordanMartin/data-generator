package io.github.datagenerator.domain.providers.transform;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderArg;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.ObjectContext;
import io.github.datagenerator.domain.core.ValueProvider;

import java.util.Optional;

/**
 * Converti une valeur en String
 */
@Provider(
        name = "Optional",
        description = "Optionally generates a field based on a condition",
        examples = {
                "Optional(\"second-first-name\", Boolean(0.1)) the field has a 10% chance of being present with the value \"second-first-name\"",
                "Optional(Integer(0, 10), Boolean(0.5)) the field has a 50% chance of being present with a integer between 1 and 10 as the value"
        },
        returns = Optional.class
)
public class OptionalProvider implements ValueProvider<Optional<?>> {

    private final ValueProvider<?> provider;
    private final ValueProvider<Boolean> condition;

    @ProviderCtor
    public OptionalProvider(
            @ProviderArg(description = "Provider of any type", examples = "IntIncrement()")
            ValueProvider<?> provider,
            @ProviderArg(description = "Provider of Boolean", examples = "Boolean(0.9) => generates 10% of null value")
            ValueProvider<Boolean> condition) {
        this.provider = provider;
        this.condition = condition;
    }

    @ProviderCtor
    public OptionalProvider(
            @ProviderArg(description = "The value to return", examples = "\"test\"")
            Object value,
            @ProviderArg(description = "Provider of Boolean", examples = "Boolean(0.9) => generates 10% of null value")
            ValueProvider<Boolean> condition) {
        this.provider = (ctx) -> value;
        this.condition = condition;
    }

    @Override
    public Optional<?> get(ObjectContext ctx) {
        Object value = evaluateProvider(provider, ctx);
        Boolean condition = evaluateProvider(this.condition, ctx);

        if (condition) {
            return Optional.ofNullable(value);
        }
        return Optional.empty();
    }
}
