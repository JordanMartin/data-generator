package io.github.datagenerator.generation.conf;

public class MissingRequiredConfigException extends RuntimeException {
    private final String name;

    public MissingRequiredConfigException(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return "The required configuration '" + name + "' is missing";
    }
}
