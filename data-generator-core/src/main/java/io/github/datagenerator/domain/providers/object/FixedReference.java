package io.github.datagenerator.domain.providers.object;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderArg;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.FieldNotFoundInContext;
import io.github.datagenerator.domain.core.ObjectContext;
import io.github.datagenerator.domain.core.ValueProvider;
import io.github.datagenerator.domain.core.ValueProviderException;

/**
 * Récupère une valeur générée pour un autre champ ou par un générateur de reference
 *
 * @param <T> Le type de la valeur
 */
@Provider(
        name = "FixedReference",
        description = "Reference another field and use his provider",
        examples = {
                "FixedReference(\"myField\")",
                "$$myField"
        },
        returns = Object.class,
        group = "reference"
)
public class FixedReference<T> implements ValueProvider<T> {

    private final String refName;

    @ProviderCtor
    public FixedReference(@ProviderArg(description = "Name of the field to reference. When the field " +
            "is declared from 'references' part of the definition then it generate a value from this" +
            " provider and returns always this value") String refName) {
        this.refName = refName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(ObjectContext ctx) {
        // On cherche dans les générateurs de références
        T objectField = (T) ctx.getFixedRefValue(refName, Object.class);
        if (objectField == null) {
            // Sinon dans les autres champs
            objectField = (T) ctx.getFieldValue(refName, Object.class);
        }

        if (objectField == null) {
            throw new FieldNotFoundInContext(refName, ctx);
        }

        return objectField;
    }
}
