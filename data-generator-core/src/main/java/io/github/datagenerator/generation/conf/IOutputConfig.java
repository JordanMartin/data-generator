package io.github.datagenerator.generation.conf;

import java.util.Optional;

public abstract class IOutputConfig {
    public Optional<String> getString(String name) {
        return getParam(name);
    }

    public String getRequiredString(String name) {
        return getString(name)
                .orElseThrow(() -> new MissingRequiredConfigException(name));
    }

    public int getRequiredInteger(String name) {
        return getInteger(name)
                .orElseThrow(() -> new MissingRequiredConfigException(name));
    }
    public Optional<Integer> getInteger(String name) {
        return getParam(name).map(Integer::parseInt);
    }

    public Optional<Boolean> getBoolean(String name) {
        return getParam(name).map(Boolean::parseBoolean);
    }

    public boolean getRequiredBoolean(String name) {
        return getBoolean(name)
                .orElseThrow(() -> new MissingRequiredConfigException(name));
    }

    protected abstract Optional<String> getParam(String name);

}
