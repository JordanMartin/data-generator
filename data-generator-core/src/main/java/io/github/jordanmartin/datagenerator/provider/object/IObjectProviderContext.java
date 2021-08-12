package io.github.jordanmartin.datagenerator.provider.object;

import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;

/**
 * Contexte utilisé lors de l'évaluation des champs calculés d'un objet
 */
public interface IObjectProviderContext {

    /**
     * Renvoi la valeur d'un autre champ
     *
     * @param fieldName Nom du champ
     * @param clazz     Classe représentant le type de champ
     * @param <T>       Type du champ
     */
    <T> T getFieldValue(String fieldName, Class<T> clazz);

    default Object getFieldValue(String name) {
        return getFieldValue(name, Object.class);
    }

    /**
     * Renvoi la valeur d'un champ de référence. Le générateur associé au champ est evalué à chaque appel.
     *
     * @param refName Nom de la référence
     * @param clazz   Classe représentant le type de la référence
     * @param <T>     Type de la référence
     */
    <T> T getRefValue(String refName, Class<T> clazz);

    default Object getRefValue(String name) {
        return getRefValue(name, Object.class);
    }

    /**
     * Renvoi la valeur d'un champ de référence.
     * Le générateur associé au champ est evalué une seule fois par objet.
     *
     * @param refName Nom de la référence
     * @param clazz   Classe représentant le type de la référence
     * @param <T>     Type de la référence
     */
    <T> T getFixedRefValue(String refName, Class<T> clazz);

    default Object getFixedRefValue(String refName) {
        return getFixedRefValue(refName, Object.class);
    }


    /**
     * Calcul la valeur d'un générateur. Lorsqu'un générateur en renvoi un autre, l'évaluation est recusive
     *
     * @param provider Le provider a evaluer
     * @param <T>      Type de valeur à retourner
     */
    @SuppressWarnings("unchecked")
    default <T> T evaluateProvider(Object provider) {
        if (provider instanceof ValueProvider<?>) {
            ValueProvider<?> valueProvider = (ValueProvider<?>) provider;
            provider = valueProvider.getOneWithContext(this);
            return evaluateProvider(provider);
        }

        return (T) provider;
    }
}
