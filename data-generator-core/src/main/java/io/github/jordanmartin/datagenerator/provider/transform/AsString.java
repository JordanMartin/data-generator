package io.github.jordanmartin.datagenerator.provider.transform;

import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;

/**
 * Converti une valeur en String
 */
public class AsString<I> extends TransformerProvider<I, String> {

    public AsString(ValueProvider<I> provider) {
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
