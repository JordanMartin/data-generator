package fr.jordanmartin.datagenerator.provider.base;

/**
 * Générateur de valeur constante
 *
 * @param <T>
 */
public class Constant<T> implements ValueProvider<T> {

    private final T value;

    public Constant(T value) {
        this.value = value;
    }

    @Override
    public T getOne() {
        return value;
    }
}
