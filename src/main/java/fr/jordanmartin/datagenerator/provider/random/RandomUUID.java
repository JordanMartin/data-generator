package fr.jordanmartin.datagenerator.provider.random;

import fr.jordanmartin.datagenerator.provider.ValueProvider;

import java.util.UUID;

/**
 * Génère un UUID
 */
public class RandomUUID implements ValueProvider<String> {

    @Override
    public String getOne() {
        return UUID.randomUUID().toString();
    }
}
