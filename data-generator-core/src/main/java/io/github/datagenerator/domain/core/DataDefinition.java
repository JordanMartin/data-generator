package io.github.datagenerator.domain.core;

import java.util.LinkedHashMap;

public class DataDefinition {
    final LinkedHashMap<String, ValueProvider<?>> fields;
    final LinkedHashMap<String, ValueProvider<?>> refFields;

    public DataDefinition() {
        this.fields = new LinkedHashMap<>();
        this.refFields = new LinkedHashMap<>();
    }

    public void field(String name, ValueProvider<?> provider) {
        fields.put(name, provider);
    }

    public void field(String name, StatelessValueProvider<?> provider) {
        fields.put(name, provider);
    }

    public void refField(String name, ValueProvider<?> provider) {
        refFields.put(name, provider);
    }

    public void refField(String name, StatelessValueProvider<?> provider) {
        refFields.put(name, provider);
    }
}
