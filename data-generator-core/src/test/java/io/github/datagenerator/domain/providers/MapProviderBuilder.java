package io.github.datagenerator.domain.providers;

import io.github.datagenerator.domain.core.DataDefinition;
import io.github.datagenerator.domain.core.MapProvider;
import io.github.datagenerator.domain.core.StatelessValueProvider;
import io.github.datagenerator.domain.core.ValueProvider;

public class MapProviderBuilder {
    private final DataDefinition definition = new DataDefinition();

    public MapProviderBuilder providerRef(String name, ValueProvider<?> provider) {
        definition.refField(name, provider);
        return this;
    }

    public MapProviderBuilder providerRef(String name, StatelessValueProvider<?> provider) {
        return providerRef(name, (ValueProvider<?>) provider);
    }

    public MapProviderBuilder field(String name, ValueProvider<?> provider) {
        definition.field(name, provider);
        return this;

    }

    public MapProviderBuilder field(String name, StatelessValueProvider<?> provider) {
        return field(name, (ValueProvider<?>) provider);
    }

    public MapProvider build() {
        return new MapProvider(definition);
    }
}
