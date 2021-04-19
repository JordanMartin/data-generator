package fr.jordanmartin.datagenerator.provider.object;

import fr.jordanmartin.datagenerator.provider.base.StatelessValueProvider;
import fr.jordanmartin.datagenerator.provider.base.ValueProvider;
import fr.jordanmartin.datagenerator.provider.base.ValueProviderException;

import java.util.*;

/**
 * Génère un objet à partir d'autre générateurs
 */
public class ObjectProvider implements ValueProvider<Map<String, ?>> {

    /**
     * Champs à générer
     */
    private final List<Field> fields = new ArrayList<>();

    /**
     * Liste des générateurs de référence dont la valeur est calculée une fois
     * pour chaque génération d'objet
     */
    private final Map<String, ValueProvider<?>> refProviders = new LinkedHashMap<>();

    /**
     * Conserve l'ordre des champs à depuis l'ordre d'ajout
     */
    private final boolean preserveFieldOrder;
    /**
     * Ordre des champs en sortie
     */
    private final List<String> fieldsOrder = new ArrayList<>();

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
    public ObjectProvider field(String name, ValueProvider<?> provider) {
        if (provider == this) {
            throw new ValueProviderException(this, String.format("Le champ \"%s\" est récursif", name));
        }
        fields.add(new Field(name, provider));
        fieldsOrder.add(name);
        return this;
    }

    public ObjectProvider field(String name, StatelessValueProvider<?> provider) {
        return field(name, (ValueProvider<?>) provider);
    }

    /**
     * Enregistre un générateur. Ce générateur est appelé une seul fois pour chaque création d'objet
     * Ce générateur ne créé pas de nouveau champs mais la valeur générée peut être utilisée avec une {@link Reference}
     * ou {@link Expression}
     *
     * @param name     Nom de la référence
     * @param provider Le générateur
     */
    public ObjectProvider providerRef(String name, ValueProvider<?> provider) {
        refProviders.put(name, provider);
        return this;
    }

    public ObjectProvider providerRef(String name, StatelessValueProvider<?> provider) {
        return providerRef(name, (ValueProvider<?>) provider);
    }

    @Override
    public Map<String, ?> getOne() {
        return getOneWithContext(null);
    }

    @Override
    public Map<String, ?> getOneWithContext(ObjectProviderContext ctx) {

        Map<String, Object> object = new HashMap<>();
        Map<Object, Object> refProvidersSnapshot = new HashMap<>();
        ObjectProviderContext context = new ObjectProviderContext() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> T getFieldValue(String name, Class<T> clazz) {
                T ref = (T) object.get(name);
                if (ref == null && ctx != null) {
                    return ctx.getFieldValue(name, clazz);
                } else {
                    return ref;
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public <T> T getRefProviderValue(String name, Class<T> clazz) {
                T ref = (T) refProvidersSnapshot.get(name);
                if (ref == null && ctx != null) {
                    return ctx.getRefProviderValue(name, clazz);
                } else {
                    return ref;
                }
            }
        };

        // Génération des valeurs pour les générateurs de référence
        for (Map.Entry<String, ValueProvider<?>> entry : refProviders.entrySet()) {
            ValueProvider<?> provider = entry.getValue();
            Object value = provider.getOneWithContext(context);
            String name = entry.getKey();
            refProvidersSnapshot.put(name, value);
        }

        // Génère une valeur pour chaque champ
        for (Field field : fields) {
            Object value = context.evaluate(field.provider);
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
