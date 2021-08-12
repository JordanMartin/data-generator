package io.github.jordanmartin.datagenerator.provider.transform;

import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;

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
    public O getOneWithContext(IObjectProviderContext ctx) {
        I input = evaluateProviderWithContext(provider, ctx);
        return map(input);
    }
}
