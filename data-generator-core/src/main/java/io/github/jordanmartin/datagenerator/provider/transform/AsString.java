package io.github.jordanmartin.datagenerator.provider.transform;

import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.ProviderCtor;

/**
 * Converti une valeur en String
 */
@Provider(
        name = "AsString",
        description = "Transforme le résultat d'un générateur chaine de caractère",
        examples = {"AsString(RandomInt())", "AsString(RandomDouble())"}
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
