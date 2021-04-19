package fr.jordanmartin.datagenerator.provider.object;

import fr.jordanmartin.datagenerator.provider.base.ValueProvider;
import fr.jordanmartin.datagenerator.provider.base.ValueProviderException;

/**
 * Récupère une valeur générée pour un autre champ ou par un générateur de reference
 *
 * @param <T> Le type de la valeur
 */
public class Reference<T> implements ValueProvider<T> {

    /**
     * Nom de la reference
     **/
    private final String refName;

    public Reference(String refName) {
        this.refName = refName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getOneWithContext(ObjectProviderContext ctx) {
        // On cherche dans générateurs de références
        T objectField = (T) ctx.getRefProviderValue(refName);
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
