package fr.jordanmartin.datagenerator.provider.transformer;

import fr.jordanmartin.datagenerator.provider.ValueProvider;

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
    public O getOne() {
        I input = provider.getOne();
        return map(input);
    }
}
