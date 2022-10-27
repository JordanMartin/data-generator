package io.github.jordanmartin.datagenerator.provider.core;

import io.github.jordanmartin.datagenerator.provider.doc.ProviderDoc;
import io.github.jordanmartin.datagenerator.provider.doc.ProviderDocumentationParser;

public class ValueProviderException extends RuntimeException {

    private transient ValueProvider<?> provider;

    public ValueProviderException() {
    }

    public ValueProviderException(ValueProvider<?> provider, String message, Throwable cause) {
        super(message, cause);
        this.provider = provider;
    }

    public ValueProviderException(ValueProvider<?> provider, String message) {
        this(provider, message, null);
    }

    public ValueProviderException(ValueProvider<?> provider, Throwable cause) {
        this(provider, null, cause);
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        if (provider != null) {
            sb.append("[Provider : ").append(getProviderName()).append("] ");
        }
        sb.append(super.getMessage());
        if (getCause() != null && getCause().getMessage() != null) {
            sb.append("\n").append(getCause().getMessage());
        }
        return sb.toString();
    }

    private String getProviderName() {
        return ProviderDocumentationParser.parse(provider.getClass())
                .map(ProviderDoc::getName)
                .orElse(provider.getClass().getSimpleName());
    }
}
