package io.github.jordanmartin.datagenerator.provider.object;

import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.core.ValueProviderException;

/**
 * Récupère une valeur générée pour un autre champ ou par un générateur de reference
 *
 * @param <T> Le type de la valeur
 */
public class FixedReference<T> implements ValueProvider<T> {

    /**
     * Nom de la reference
     **/
    private final String refName;

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
