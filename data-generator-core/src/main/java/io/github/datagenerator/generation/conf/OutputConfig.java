package io.github.datagenerator.generation.conf;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OutputConfig extends IOutputConfig {
    private final Map<String, String> map = new HashMap<>();

    @Override
    public Optional<String> getParam(String name) {
        return Optional.ofNullable(map.get(name));
    }

    public OutputConfig setParam(String name, Object value) {
        String paramValue = Optional.ofNullable(value)
                .map(Object::toString)
                .orElse(null);
        map.put(name, paramValue);
        return this;
    }
}
