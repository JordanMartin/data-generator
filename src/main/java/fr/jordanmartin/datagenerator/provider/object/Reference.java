package fr.jordanmartin.datagenerator.provider.object;

import fr.jordanmartin.datagenerator.provider.base.ValueProvider;
import fr.jordanmartin.datagenerator.provider.base.ValueProviderException;

/**
 * Récupère une valeur générée pour un autre champ ou par un générateur de reference
 *
 * @param <T> Le type de la valeur
 */
public class Reference<T> implements ValueProvider<ObjectContextHandler<?>> {

    /**
     * Nom de la reference
     **/
    private final String refName;

    public Reference(String refName) {
        this.refName = refName;
    }

    @Override
    public ObjectContextHandler<?> getOne() {
        return (ctx) -> {
            // On cherche dans les autres champs de l'objet
            Object objectField = ctx.getFieldValue(refName);
            if (objectField == null) {
                // Sinon dans les générateurs de références
                objectField = ctx.getRefProviderValue(refName);
            }

            if (objectField == null) {
                throw new ValueProviderException(this, "La référence \"" + refName + "\" n'existe pas");
            }

            return objectField;
        };
    }
}
