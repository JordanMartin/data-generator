package io.github.jordanmartin.datagenerator.definition;

import io.github.jordanmartin.datagenerator.provider.doc.ProviderDoc;

public class ProviderDefinitionException extends RuntimeException {
    private final String name;
    private final ProviderDoc doc;

    public ProviderDefinitionException(String name, ProviderDoc doc) {
        this.name = name;
        this.doc = doc;
    }

    @Override
    public String getMessage() {
        return String.format("Les paramètres du générateurs \"%s\" sont incorrects", name);
    }
}
