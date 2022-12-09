package io.github.datagenerator.domain.providers.transform;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderArg;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.ObjectContext;
import io.github.datagenerator.domain.core.ValueProvider;

/**
 * Utilise un autre générateur pour récupérer la valeur et la retourne à chaque appel
 *
 * @param <T> Type de la donnée à générer
 */
@Provider(
        name = "Idempotent",
        description = "Use a provider to compute a value and returns always this value on each use",
        examples = {"Idempotent(UUID()) => returns always the same UUID"}
)
public class Idempotent<T> implements ValueProvider<T> {

    private final ValueProvider<T> provider;
    private T value;

    @ProviderCtor
    public Idempotent(
            @ProviderArg(description = "Provider of any type", examples = {"UUID()", "RandomDate()"}) ValueProvider<T> provider
    ) {
        this.provider = provider;
    }

    @Override
    public T get(ObjectContext ctx) {
        if (this.value == null) {
            this.value = provider.get(ctx);
        }
        return this.value;
    }
}
