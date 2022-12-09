package io.github.datagenerator.domain.definition;


import io.github.datagenerator.domain.doc.ProviderDoc;

public class ProviderDefinitionException extends RuntimeException {
    private final String name;
    private final ProviderDoc doc;

    public ProviderDefinitionException(String name, ProviderDoc doc) {
        this.name = name;
        this.doc = doc;
    }

    @Override
    public String getMessage() {
        return String.format("The parameters of the provider \"%s\" are not valid", name);
    }
}
