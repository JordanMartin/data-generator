package fr.jordanmartin.datagenerator.provider.object;

/**
 * Récupère une valeur générée pour un autre champ ou par un générateur de reference
 *
 * @param <T> Le type de la valeur
 */
public class Reference<T> extends ValueProviderWithContext<T> {

    /**
     * Nom de la reference
     **/
    private final String refName;

    public Reference(String refName) {
        this.refName = refName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T evaluate(ObjectProviderContext ctx) {
        // On cherche dans les autres champs de l'objet
        Object objectField = ctx.getFieldValue(refName);
        if (objectField == null) {
            // Sinon dans les générateurs de références
            objectField = ctx.getRefProviderValue(refName);
        }

        if (objectField == null) {
            throw new IllegalArgumentException("La référence \"" + refName + "\" n'existe pas");
        }

        return (T) objectField;
    }
}
