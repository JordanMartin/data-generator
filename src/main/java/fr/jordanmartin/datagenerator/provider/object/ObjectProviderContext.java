package fr.jordanmartin.datagenerator.provider.object;

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

    Object evaluate(Object object);

}
