package io.github.jordanmartin.datagenerator.provider.transform;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;

/**
 * Converti une valeur en String
 */
@Provider(
        name = "Not",
        description = "Inverse the value of a Boolean",
        examples = {
                "Not(true) => false",
                "Not(Boolean()) => false"
        },
        returns = Boolean.class,
        group = "boolean"
)
public class Not extends TransformerProvider<Boolean, Boolean> {

    @ProviderCtor
    public Not(
            @ProviderArg(description = "Provider of Boolean", examples = {"Boolean()", "Reference(\"booleanField\")"})
            ValueProvider<Boolean> provider
    ) {
        super(provider);
    }

    @ProviderCtor
    public Not(
            @ProviderArg(name = "boolean", description = "Boolean value", examples = {"true", "false"}) boolean bool) {
        super(ctx -> bool);
    }

    @Override
    protected Boolean map(Boolean input) {
        return !input;
    }
}
