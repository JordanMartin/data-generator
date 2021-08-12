package io.github.jordanmartin.datagenerator.provider.random;

import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;

import java.util.UUID;

/**
 * Génère un UUID
 */
public class RandomUUID implements StatelessValueProvider<String> {

    @Override
    public String getOne() {
        return UUID.randomUUID().toString();
    }
}
