package fr.jordanmartin.datagenerator.provider.random;

import fr.jordanmartin.datagenerator.provider.core.StatelessValueProvider;

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
