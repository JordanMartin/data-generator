package fr.jordanmartin.datagenerator.provider.transform;

import fr.jordanmartin.datagenerator.provider.core.ValueProvider;
import fr.jordanmartin.datagenerator.provider.object.ObjectProviderContext;

/**
 * Utilise un autre générateur pour récupérer la valeur et la retourne à chaque appel
 *
 * @param <T> Type de la donnée à générer
 */
public class Idempotent<T> implements ValueProvider<T> {

    private final ValueProvider<T> provider;
    private T value;

    public Idempotent(ValueProvider<T> provider) {
        this.provider = provider;
    }

    @Override
    public T getOneWithContext(ObjectProviderContext ctx) {
        if (this.value == null) {
            this.value = provider.getOneWithContext(ctx);
        }
        return this.value;
    }
}
