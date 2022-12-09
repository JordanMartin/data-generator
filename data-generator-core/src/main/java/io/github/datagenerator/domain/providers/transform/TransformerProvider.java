package io.github.datagenerator.domain.providers.transform;

import io.github.datagenerator.domain.core.ObjectContext;
import io.github.datagenerator.domain.core.ValueProvider;

/**
 * Transforme une donnée
 *
 * @param <I> Type de donnée en entrée
 * @param <O> Type de la donnée formatté
 */
public abstract class TransformerProvider<I, O> implements ValueProvider<O> {

    /**
     * Générateur à transformer
     */
    private final ValueProvider<I> provider;

    protected TransformerProvider(ValueProvider<I> provider) {
        this.provider = provider;
    }

    /**
     * Fonction de transformation
     *
     * @param input Donnée entrée
     * @return Donnée transformé
     */
    protected abstract O map(I input);

    @Override
    public O get(ObjectContext ctx) {
        I input = evaluateProvider(provider, ctx);
        return map(input);
    }
}
