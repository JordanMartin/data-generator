package fr.jordanmartin.datagenerator.provider;

/**
 * Génère toujours la même donnée à partir d'un générateur
 * @param <T>
 */
public class IdempotentProvider<T> implements ValueProvider<T> {

    private final ValueProvider<T> provider;
    private T value;

    public IdempotentProvider(ValueProvider<T> provider) {
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
