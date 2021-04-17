package fr.jordanmartin.datagenerator.provider.object;

import fr.jordanmartin.datagenerator.provider.base.ValueProvider;

/**
 * Générateur permettant le calcul d'une donnée à partir du context des autres données
 *
 * @param <T> Type de la donnée généré
 */
public abstract class ValueProviderWithContext<T> implements ValueProvider<T>, ContextAwareProvider<T> {

    protected ObjectProviderContext ctx = null;

    @Override
    public T getOne() {
        return evaluate(ctx);
    }

    public void setCtx(ObjectProviderContext ctx) {
        this.ctx = ctx;
    }
}
