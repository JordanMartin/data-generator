package io.github.datagenerator.domain.core;

import java.util.LinkedHashMap;

public class DataDefinition {
    final LinkedHashMap<String, ValueProvider<?>> fields;
    final LinkedHashMap<String, ValueProvider<?>> refFields;

    public DataDefinition() {
        this.fields = new LinkedHashMap<>();
        this.refFields = new LinkedHashMap<>();
    }

    public DataDefinition field(String name, ValueProvider<?> provider) {
        fields.put(name, provider);
        return this;
    }

    public DataDefinition field(String name, StatelessValueProvider<?> provider) {
        fields.put(name, provider);
        return this;
    }

    public DataDefinition refField(String name, ValueProvider<?> provider) {
        refFields.put(name, provider);
        return this;
    }

    public DataDefinition refField(String name, StatelessValueProvider<?> provider) {
        refFields.put(name, provider);
        return this;
    }

    public MapProvider build() {
        return new MapProvider(this);
    }
}
