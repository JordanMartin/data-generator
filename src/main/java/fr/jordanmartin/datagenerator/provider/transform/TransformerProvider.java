package fr.jordanmartin.datagenerator.provider.transform;

import fr.jordanmartin.datagenerator.provider.base.ValueProvider;
import fr.jordanmartin.datagenerator.provider.object.ObjectProviderContext;

/**
 * Transforme une donnée
 *
 * @param <I> Type de donnée en entrée
 * @param <O> Type de la donnée formatté
 */
public abstract class TransformerProvider<I, O> implements ValueProvider<O> {

    private final ValueProvider<I> provider;

    public TransformerProvider(ValueProvider<I> provider) {
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
    public O getOneWithContext(ObjectProviderContext ctx) {
        I input = evaluateProviderWithContext(provider, ctx);
        return map(input);
    }
}
