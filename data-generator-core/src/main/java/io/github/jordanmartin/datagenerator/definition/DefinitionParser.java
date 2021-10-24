package io.github.jordanmartin.datagenerator.definition;

import io.github.jordanmartin.datagenerator.provider.object.ObjectProvider;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public abstract class DefinitionParser {

    private final ProviderRegistry defaultProvider = ProviderRegistry.getInstance();

    public abstract ObjectProvider parse();

    @SneakyThrows
    protected Object createNewProvider(String providerName, Object[] providerParams) {
        Class<?> classProvider = defaultProvider.get(providerName);

        if (classProvider == null) {
            String providers = String.join(", ", defaultProvider.listNames());
            throw new DefinitionException("Le générateur \"" + providerName + "\" n'existe pas\n" +
                    "Générateur disponibles: " + providers);
        }

        Class<?>[] paramsClass = Arrays.stream(providerParams)
                .map(Object::getClass)
                .toArray(Class<?>[]::new);

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
                        new DefinitionException("Les paramètres du générateur \"" + providerName + "\" sont incorrects"));

        return constructor.newInstance(providerParams);
    }

    /**
     * Détermine si des paramètres sont compatibles
     */
    private boolean isTypeAssignable(Class<?> from, Class<?> to) {
        if (from.isAssignableFrom(to)) {
            return true;
        } else if (from == int.class && to == Integer.class
                || from == Integer.class && to == int.class) {
            return true;
        } else {
            return from == double.class && to == Double.class
                    || from == Double.class && to == double.class;
        }
    }
}
