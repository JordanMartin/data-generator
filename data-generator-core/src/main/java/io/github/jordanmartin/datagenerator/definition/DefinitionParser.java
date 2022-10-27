package io.github.jordanmartin.datagenerator.definition;

import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * Classe pour l'implémentation d'un parser
 */
public abstract class DefinitionParser {

    private final ProviderRegistry providerRegistry = ProviderRegistry.getInstance();

    public abstract ObjectProvider parse();

    @SneakyThrows
    protected Object createNewProvider(String providerName, Object[] providerParams) {
        Class<?> classProvider = providerRegistry.get(providerName);

        if (classProvider == null) {
            String providers = String.join(", ", providerRegistry.listNames());
            throw new DefinitionException("The provider \"" + providerName + "\" doesn't exists\n" +
                    "Available providers : " + providers);
        }

        Class<?>[] paramsClass = Arrays.stream(providerParams)
                .map(Object::getClass)
                .toArray(Class<?>[]::new);

        // TODO exception détaillée pour chaque cas
        Constructor<?> constructor = Arrays.stream(classProvider.getConstructors())
                .filter(c -> {
                    Class<?>[] types = c.getParameterTypes();
                    // Aucun argument
                    if (types.length == paramsClass.length && types.length == 0) {
                        return true;
                    }

                    // Nombre différent de paramètres
                    if (types.length != paramsClass.length) {
                        return false;
                    }

                    // Vérifie si le type des paramètres correspond
                    for (int i = 0; i < types.length; i++) {
                        if (!isTypeAssignable(types[i], paramsClass[i])) {
                            return false;
                        }
                    }
                    return true;
                })
                .findFirst().orElseThrow(() ->
                        new ProviderDefinitionException(providerName, providerRegistry.getDoc(providerName).orElse(null)));

        return constructor.newInstance(providerParams);
    }

    /**
     * Détermine si des paramètres sont compatibles
     */
    private boolean isTypeAssignable(Class<?> from, Class<?> to) {
        if (from.isAssignableFrom(to)) {
            return true;
        } else if ((from == int.class || from == long.class || from == Integer.class || from == Long.class)
                && (to == int.class || to == long.class || to == Integer.class || to == Long.class)) {
            return true;
        } else if (from == double.class && to == Double.class
                || from == Double.class && to == double.class) {
            return true;
        } else if (from == Boolean.class && to == boolean.class
                || from == boolean.class && to == Boolean.class) {
            return true;
        }

        return false;
    }
}
