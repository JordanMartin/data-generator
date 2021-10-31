package io.github.jordanmartin.datagenerator.provider.transform;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;

/**
 * Converti une valeur en String
 */
@Provider(
        name = "AsString",
        description = "Transforme le résultat d'un générateur en chaine de caractère",
        examples = {
                "AsString(Integer()) => \"5\"",
                "AsString(Double(0, 10)) => \"6.19938784958678\"",
                "AsString(Boolean()) => \"true\""
        },
        returns = String.class
)
public class AsString<I> extends TransformerProvider<I, String> {

    @ProviderCtor
    public AsString(
            @ProviderArg(
                    name = "generateur",
                    description = "Un autre générateur",
                    examples = "IntIncrement()"
            ) ValueProvider<I> provider) {
        super(provider);
    }

    @Override
    protected String map(I input) {
        if (input == null) {
            return null;
        }
        return String.valueOf(input);
    }
}
