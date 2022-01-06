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
        description = "Inverse un booléen",
        examples = {
                "Not(true) => false",
                "Not(Boolean()) => false"
        },
        returns = Boolean.class,
        group = "number"
)
public class Not extends TransformerProvider<Boolean, Boolean> {

    @ProviderCtor
    public Not(
            @ProviderArg(
                    name = "generateur",
                    description = "Un générateur de booléen ou une référence vers un booléen",
                    examples = "Boolean()"
            ) ValueProvider<Boolean> provider) {
        super(provider);
    }

    @ProviderCtor
    public Not(
            @ProviderArg(
                    name = "booleen",
                    description = "Une valeur booléen",
                    examples = "true"
            ) boolean bool) {
        super(ctx -> bool);
    }

    @Override
    protected Boolean map(Boolean input) {
        return !input;
    }
}
