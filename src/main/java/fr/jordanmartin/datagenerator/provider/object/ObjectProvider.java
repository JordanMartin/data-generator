package fr.jordanmartin.datagenerator.provider.object;

import fr.jordanmartin.datagenerator.provider.base.ValueProvider;
import fr.jordanmartin.datagenerator.provider.base.ValueProviderException;

import java.util.*;

/**
 * Génère un objet à partir d'autre générateurs
 */
public class ObjectProvider implements ValueProvider<Map<String, ?>> {

    /* Conserve l'ordre des champs à depuis l'ordre d'ajout */
    private final boolean preserveFieldOrder;
    /**
     * Champs à générer
     */
    private final List<Field> fields = new ArrayList<>();
    private final List<String> fieldsOrder = new ArrayList<>();
    /**
     * Liste des générateurs de référence dont la valeur est calculée une fois
     * pour chaque génération d'objet via {@link #getOne()}
     */
    private final Map<String, ValueProvider<?>> refProviders = new HashMap<>();

    public ObjectProvider(boolean preserveFieldOrder) {
        this.preserveFieldOrder = preserveFieldOrder;
    }

    public ObjectProvider() {
        this(true);
    }

    /**
     * Enregistre un nouveau champ
     *
     * @param name     Nom du champ
     * @param provider Le générateur de valeur
     */
    public ObjectProvider field(String name, ObjectContextHandler<?> provider) {
        return field(name, () -> provider);
    }

    /**
     * Enregistre un nouveau champ
     *
     * @param name     Nom du champ
     * @param provider Le générateur de valeur
     */
    public ObjectProvider field(String name, ValueProvider<?> provider) {
        if (provider == this) {
            throw new ValueProviderException(this, String.format("Le champ \"%s\" est récursif", name));
        }
        fields.add(new Field(name, provider));
        fieldsOrder.add(name);
        return this;
    }

    /**
     * Enregistre un générateur. Ce générateur est appelé une seul fois pour chaque création d'objet lors de l'appel
     * à la méthode {@link #getOne()}.
     * Ce générateur ne créé pas de nouveau champs mais la valeur générée peut être utilisée avec une  {@link Reference}
     * ou {@link Expression}
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
        Map<Object, Object> refProvidersSnapshot = new HashMap<>();
        for (Map.Entry<String, ValueProvider<?>> entry : refProviders.entrySet()) {
            ValueProvider<?> provider = entry.getValue();
            Object value = provider.getOne();
            String name = entry.getKey();
            refProvidersSnapshot.put(name, value);
        }

        Map<String, Object> object = new HashMap<>();
        ObjectProviderContext context = new ObjectProviderContext() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> T getFieldValue(String name, Class<T> clazz) {
                return (T) object.get(name);
            }

            @SuppressWarnings("unchecked")
            @Override
            public <T> T getRefProviderValue(String name, Class<T> clazz) {
                return (T) refProvidersSnapshot.get(name);
            }

            @Override
            public Object evaluate(Object object) {
                return ObjectProvider.this.evaluate(object, this);
            }
        };

        // Généère une valeur pour chaque champ
        for (Field field : fields) {
            Object value = evaluate(field.provider, context);
            object.put(field.name, value);
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

    private Object evaluate(Object value, ObjectProviderContext context) {

        if (value instanceof ObjectProvider) {
            ObjectProvider provider = (ObjectProvider) value;
            // Recopie des references vers un objet enfant si elles n'existent pas
            refProviders.forEach(provider.refProviders::putIfAbsent);
            value = provider.getOne();
            return evaluate(value, context);
        } else if (value instanceof ValueProvider<?>) {
            ValueProvider<?> provider = (ValueProvider<?>) value;
            value = provider.getOne();
            return evaluate(value, context);
        } else if (value instanceof ObjectContextHandler) {
            ObjectContextHandler<?> objectContextHandler = (ObjectContextHandler<?>) value;
            value = objectContextHandler.evaluate(context);
            return evaluate(value, context);
        }

        return value;
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
