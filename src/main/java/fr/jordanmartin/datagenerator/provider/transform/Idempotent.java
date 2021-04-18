package fr.jordanmartin.datagenerator.provider.transform;

import fr.jordanmartin.datagenerator.provider.base.ValueProvider;

/**
 * Utilise un autre générateur pour récupérer la valeur et la retourne à chaque appel
 *
 * @param <T> Type de la donnée à générer
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
