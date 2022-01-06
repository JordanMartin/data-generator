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
        name = "Reference",
        description = "Référence un autre champ. S'il s'agit d'un champ de référence, une nouvelle valeur sera générée à chaque utilisation",
        examples = {
                "Reference(\"champ1\")"
        },
        returns = Object.class,
        group = "reference"
)
public class Reference<T> implements ValueProvider<T> {

    /**
     * Nom de la reference
     **/
    private final String refName;

    @ProviderCtor
    public Reference(String refName) {
        this.refName = refName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getOneWithContext(IObjectProviderContext ctx) {
        // On cherche dans générateurs de références
        T objectField = (T) ctx.getRefValue(refName);
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
