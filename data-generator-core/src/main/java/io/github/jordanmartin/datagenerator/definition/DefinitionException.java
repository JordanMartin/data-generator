package io.github.jordanmartin.datagenerator.definition;

public class DefinitionException extends RuntimeException {

    public DefinitionException(String message) {
        super(message);
    }

    public DefinitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
