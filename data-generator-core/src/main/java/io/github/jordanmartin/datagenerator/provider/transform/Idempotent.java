package io.github.jordanmartin.datagenerator.provider.transform;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;

/**
 * Utilise un autre générateur pour récupérer la valeur et la retourne à chaque appel
 *
 * @param <T> Type de la donnée à générer
 */
@Provider(
        name = "Idempotent",
        description = "Retourne systématiquement la première valeur obtenu à partir d'un autre générateur",
        examples = {"Idempotent(UUID())"}
)
public class Idempotent<T> implements ValueProvider<T> {

    private final ValueProvider<T> provider;
    private T value;

    @ProviderCtor
    public Idempotent(
            @ProviderArg(
                    name = "generateur",
                    description = "Un générateur de n'importe quel type",
                    examples = {"UUID()", "RandomDate()"}
            ) ValueProvider<T> provider
    ) {
        this.provider = provider;
    }

    @Override
    public T getOneWithContext(IObjectProviderContext ctx) {
        if (this.value == null) {
            this.value = provider.getOneWithContext(ctx);
        }
        return this.value;
    }
}
