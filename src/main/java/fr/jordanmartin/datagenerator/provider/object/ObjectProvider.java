package fr.jordanmartin.datagenerator.provider.object;

import fr.jordanmartin.datagenerator.provider.ValueProvider;

import java.util.*;

/**
 * Génère un objet à partir d'autre générateurs
 */
public class ObjectProvider implements ValueProvider<Map<String, ?>> {

    private final boolean preserveFieldOrder;

    public ObjectProvider(boolean preserveFieldOrder) {
        this.preserveFieldOrder = preserveFieldOrder;
    }

    public ObjectProvider() {
        this(true);
    }

    /**
     * Champs à générer
     */
    private final List<Field> fields = new ArrayList<>();
    private final List<String> fieldsOrder = new ArrayList<>();

    /**
     * Champ dynamique (calculés à partir d'une expression par exemple)
     */
    private final Map<String, ComputedProvider<?>> computedFields = new HashMap<>();

    /**
     * Liste des générateurs de référence dont la valeur est calculée une fois
     * pour chaque génération d'objet via {@link #getOne()}
     */
    private final Map<String, ValueProvider<?>> refProviders = new HashMap<>();

    /**
     * Enregistre un nouveau champ
     *
     * @param name     Nom du champ
     * @param provider Le générateur de valeur
     */
    public ObjectProvider field(String name, ValueProvider<?> provider) {
        if (provider == this) {
            throw new IllegalArgumentException(String.format("Le champ \"%s\" est récursif", name));
        }
        fields.add(new Field(name, provider));
        fieldsOrder.add(name);
        return this;
    }

    /**
     * Enregistre un nouveau champ
     *
     * @param name     Nom du champ
     * @param provider Le générateur de valeur
     */
    public ObjectProvider field(String name, ComputedProvider<?> provider) {
        computedFields.put(name, provider);
        fieldsOrder.add(name);
        return this;
    }

    /**
     * Enregistre un générateur. Ce générateur est appelé une seul fois pour chaque création d'objet lors de l'appel
     * à la méthode {@link #getOne()}.
     * Ce générateur ne créé pas de nouveau champs mais la valeur générée peut être utilisée avec un  {@link ComputedProvider}
     *
     * @param name     Nom de la référence
     * @param provider Le générateur
     */
    public ObjectProvider providerRef(String name, ValueProvider<?> provider) {
        refProviders.put(name, provider);
        return this;
    }

    @Override
    public Map<String, ?> getOne() {
        // Génération des valeurs pour les générateurs de référence
        Map<String, Object> providersSnapshot = new HashMap<>();
        for (Map.Entry<String, ValueProvider<?>> entry : refProviders.entrySet()) {
            Object value = entry.getValue().getOne();
            String name = entry.getKey();
            providersSnapshot.put(name, value);
        }

        // Génération des champs de type "ValueProvider"
        Map<String, Object> object = new HashMap<>();
        for (Field field : fields) {
            object.put(field.name, field.provider.getOne());
        }

        ObjectProviderContext ctx = new ObjectProviderContext() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> T getFieldValue(String name, Class<T> clazz) {
                return (T) object.get(name);
            }

            @SuppressWarnings("unchecked")
            @Override
            public <T> T getRefProviderValue(String name, Class<T> clazz) {
                return (T) providersSnapshot.get(name);
            }
        };

        // Génération des champs calculés de type "ComputedProvider"
        for (Map.Entry<String, ComputedProvider<?>> entry : computedFields.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue().evaluate(ctx);
            object.put(name, value);
        }

        if (preserveFieldOrder) {
            Map<String, Object> orderedObject = new LinkedHashMap<>();
            for (String fieldName : fieldsOrder) {
                orderedObject.put(fieldName, object.get(fieldName));
            }
            return orderedObject;
        }

        return object;
    }

    /**
     * Représente un champ avec son générateur de valeur
     */
    private static class Field {
        private final String name;
        private final ValueProvider<?> provider;

        public Field(String name, ValueProvider<?> provider) {
            this.name = name;
            this.provider = provider;
        }
    }
}
