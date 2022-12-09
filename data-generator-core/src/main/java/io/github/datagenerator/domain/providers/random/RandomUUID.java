package io.github.datagenerator.domain.providers.random;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.core.StatelessValueProvider;

import java.util.UUID;

/**
 * Génère un UUID
 */
@Provider(
        name = "UUID",
        description = "Random UUID",
        examples = "UUID() => dc16324a-9d7f-4454-ab82-92f6092a7918",
        group = "id"
)
public class RandomUUID implements StatelessValueProvider<String> {

    @Override
    public String get() {
        return UUID.randomUUID().toString();
    }
}
