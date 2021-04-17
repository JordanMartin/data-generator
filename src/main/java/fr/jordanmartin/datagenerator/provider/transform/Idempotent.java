package fr.jordanmartin.datagenerator.provider.transform;

import fr.jordanmartin.datagenerator.provider.base.ValueProvider;

/**
 * Génère toujours la même donnée à partir d'un générateur
 * @param <T>
 */
public class Idempotent<T> implements ValueProvider<T> {

    private final ValueProvider<T> provider;
    private T value;

    public Idempotent(ValueProvider<T> provider) {
        this.provider = provider;
    }

    @Override
    public T getOne() {
        if (value == null) {
            value = provider.getOne();
        }

        return value;
    }
}
