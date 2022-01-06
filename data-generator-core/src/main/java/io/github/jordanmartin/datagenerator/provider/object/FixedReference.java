package io.github.jordanmartin.datagenerator.provider.object;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.core.ValueProviderException;

/**
 * Récupère une valeur générée pour un autre champ ou par un générateur de reference
 *
 * @param <T> Le type de la valeur
 */
@Provider(
        name = "FixedReference",
        description = "Référence un autre champ. S'il s'agit d'un champ de référence, la générateur associé ne sera appelé qu'une fois",
        examples = {
                "FixedReference(\"champ1\")"
        },
        returns = Object.class,
        group = "reference"
)
public class FixedReference<T> implements ValueProvider<T> {

    /**
     * Nom de la reference
     **/
    private final String refName;

    @ProviderCtor
    public FixedReference(String refName) {
        this.refName = refName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getOneWithContext(IObjectProviderContext ctx) {
        // On cherche dans générateurs de références
        T objectField = (T) ctx.getFixedRefValue(refName);
        if (objectField == null) {
            // Sinon dans les autres champs
            objectField = (T) ctx.getFieldValue(refName);
        }

        if (objectField == null) {
            throw new ValueProviderException(this, "La référence \"" + refName + "\" n'existe pas");
        }

        return objectField;
    }
}
