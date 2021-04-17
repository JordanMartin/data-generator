package fr.jordanmartin.datagenerator.provider.object;

import fr.jordanmartin.datagenerator.provider.base.ValueProvider;

import java.util.*;

/**
 * Génère un objet à partir d'autre générateurs
 */
public class ObjectProvider extends ValueProviderWithContext<Map<String, ?>> {

    /* Conserve l'ordre des champs à depuis l'ordre d'ajout */
    private final boolean preserveFieldOrder;

    /* Liste des paramètres */
    private Map<Object, Object> refProvidersSnapshot;

    /* Objet résultat */
    private Map<String, Object> resultObject;

    public ObjectProvider(boolean preserveFieldOrder) {
        this.preserveFieldOrder = preserveFieldOrder;
        setCtx(new ObjectProviderContext() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> T getFieldValue(String name, Class<T> clazz) {
                return (T) resultObject.get(name);
            }

            @SuppressWarnings("unchecked")
            @Override
            public <T> T getRefProviderValue(String name, Class<T> clazz) {
                return (T) refProvidersSnapshot.get(name);
            }
        });
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
    private final Map<String, ContextAwareProvider<?>> fieldsWithContext = new HashMap<>();

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

        if (provider instanceof ValueProviderWithContext) {
            fieldsWithContext.put(name, (ValueProviderWithContext<?>) provider);
        } else {
            fields.add(new Field(name, provider));
        }
        fieldsOrder.add(name);
        return this;
    }

    /**
     * Enregistre un nouveau champ
     *
     * @param name     Nom du champ
     * @param provider Le générateur de valeur
     */
    public ObjectProvider field(String name, ContextAwareProvider<?> provider) {
        if (provider == this) {
            throw new IllegalArgumentException(String.format("Le champ \"%s\" est récursif", name));
        }
        fieldsWithContext.put(name, provider);
        fieldsOrder.add(name);
        return this;
    }

    /**
     * Enregistre un générateur. Ce générateur est appelé une seul fois pour chaque création d'objet lors de l'appel
     * à la méthode {@link #getOne()}.
     * Ce générateur ne créé pas de nouveau champs mais la valeur générée peut être utilisée avec un  {@link ValueProviderWithContext}
     *
     * @param name     Nom de la référence
     * @param provider Le générateur
     */
    public ObjectProvider providerRef(String name, ValueProvider<?> provider) {
        refProviders.put(name, provider);
        return this;
    }

    @Override
    public Map<String, ?> evaluate(ObjectProviderContext ctx) {

        // Génération des valeurs pour les générateurs de référence
        this.refProvidersSnapshot = new HashMap<>();
        for (Map.Entry<String, ValueProvider<?>> entry : refProviders.entrySet()) {
            ValueProvider<?> provider = entry.getValue();
            if (provider instanceof ValueProviderWithContext) {
                ((ValueProviderWithContext<?>) provider).setCtx(ctx);
            }
            Object value = provider.getOne();
            String name = entry.getKey();
            refProvidersSnapshot.put(name, value);
        }

        // Génération des champs de type "ValueProvider"
        this.resultObject = new HashMap<>();
        for (Field field : fields) {
            resultObject.put(field.name, field.provider.getOne());
        }

        // Générateurs ayant besoin du context
        for (Map.Entry<String, ContextAwareProvider<?>> entry : fieldsWithContext.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue().evaluate(ctx);
            resultObject.put(name, value);
        }

        if (preserveFieldOrder) {
            Map<String, Object> orderedObject = new LinkedHashMap<>();
            for (String fieldName : fieldsOrder) {
                orderedObject.put(fieldName, resultObject.get(fieldName));
            }
            return orderedObject;
        }

        return resultObject;
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
