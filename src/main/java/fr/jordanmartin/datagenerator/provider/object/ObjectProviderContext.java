package fr.jordanmartin.datagenerator.provider.object;

import fr.jordanmartin.datagenerator.provider.base.ValueProvider;

/**
 * Contexte utilisé lors de l'évaluation des champs calculés d'un objet
 */
public interface ObjectProviderContext {

    <T> T getFieldValue(String name, Class<T> clazz);

    default Object getFieldValue(String name) {
        return getFieldValue(name, Object.class);
    }

    <T> T getRefProviderValue(String name, Class<T> clazz);

    default Object getRefProviderValue(String name) {
        return getRefProviderValue(name, Object.class);
    }

    @SuppressWarnings("unchecked")
    default <T> T evaluate(Object value) {
        if (value instanceof ValueProvider<?>) {
            ValueProvider<?> provider = (ValueProvider<?>) value;
            value = provider.getOneWithContext(this);
            return evaluate(value);
        }

        return (T) value;
    }
}
