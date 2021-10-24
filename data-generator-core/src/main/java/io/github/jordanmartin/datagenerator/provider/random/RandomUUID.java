package io.github.jordanmartin.datagenerator.provider.random;

import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.Provider;

import java.util.UUID;

/**
 * Génère un UUID
 */
@Provider(
        name = "UUID",
        description = "Retourne un identifiant unique aléatoire",
        examples = "UUID() => dc16324a-9d7f-4454-ab82-92f6092a7918"
)
public class RandomUUID implements StatelessValueProvider<String> {

    @Override
    public String getOne() {
        return UUID.randomUUID().toString();
    }
}
