package fr.jordanmartin.datagenerator.definition;

import fr.jordanmartin.datagenerator.provider.base.Constant;
import fr.jordanmartin.datagenerator.provider.base.ValueProvider;
import fr.jordanmartin.datagenerator.provider.object.Expression;
import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;
import fr.jordanmartin.datagenerator.provider.object.Reference;
import fr.jordanmartin.datagenerator.provider.random.*;
import fr.jordanmartin.datagenerator.provider.sequence.IntAutoIncrement;
import fr.jordanmartin.datagenerator.provider.sequence.SequenceFromList;
import fr.jordanmartin.datagenerator.provider.transform.*;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class DefinitionParser {

    // TODO déplacer dans un package/class dédié
    protected final Map<String, Class<? extends ValueProvider<?>>> defaultProvider = new HashMap<>();

    // TODO déplacer dans un package/class dédié
    public DefinitionParser() {
        registerDefaultProviders();
    }

    // TODO déplacer dans un package/class dédié
    private void registerDefaultProviders() {
        registerProvider(Constant.class);
        registerProvider(RandomDate.class);
        registerProvider(RandomFromList.class);
        registerProvider(RandomFromRegex.class);
        registerProvider(RandomInt.class);
        registerProvider(RandomDouble.class);
        registerProvider(Round.class);
        registerProvider(RandomUUID.class);
        registerProvider(IntAutoIncrement.class);
        registerProvider(SequenceFromList.class);
        registerProvider(AsString.class);
        registerProvider(FormatDate.class);
        registerProvider(Idempotent.class);
        registerProvider(ListOf.class);
        registerProvider(ListByRepeat.class);
        registerProvider(Sample.class);


        // FIXME
        registerProvider("ItemWeight", RandomFromList.ItemWeight.class);

        registerProvider(Expression.class);
        registerProvider(Reference.class);
    }

    // FIXME
    @SneakyThrows
    protected Object createNewProvider(String providerName, Object[] providerParams) {
        Class<?> classProvider = defaultProvider.get(providerName);

        if (classProvider == null) {
            // TODO écrire la liste des générateurs dispo
            throw new DefinitionException("Le générateur \"" + providerName + "\" n'existe pas");
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

                    // FIXME refacto pour plus de clareté
                    // Vérifie si le type des paramètres correspond
                    for (int i = 0; i < types.length; i++) {
                        if (types[i].isAssignableFrom(paramsClass[i])) {
                        } else if (types[i] == int.class && paramsClass[i] == Integer.class
                                || types[i] == Integer.class && paramsClass[i] == int.class) {
                        } else if (types[i] == double.class && paramsClass[i] == Double.class
                                || types[i] == Double.class && paramsClass[i] == double.class) {
                        } else {
                            return false;
                        }
                    }
                    return true;
                })
                .findFirst().orElseThrow(() ->
                        new DefinitionException("Les paramètres du générateur \"" + providerName + "\" sont incorrectes"));

        return constructor.newInstance(providerParams);
    }

    @SuppressWarnings("unchecked")
//    public void registerComputedProvider(String name, Class<?> providerClass) {
//        if (!ValueProviderWithContext.class.isAssignableFrom(providerClass)) {
//            throw new IllegalArgumentException("Le générateur de référence \"" + providerClass + "\" doit implémenter " + ValueProviderWithContext.class);
//        }
//        Class<? extends ValueProviderWithContext<?>> existingProviderClass = defaultComputedProvider.get(name);
//        if (existingProviderClass != null) {
//            throw new IllegalArgumentException("Le générateur de référence \"" + existingProviderClass
//                    + "\" et \"" + providerClass + "\" ne peuvent pas être enregistrés sous le même nom");
//        }
//        defaultComputedProvider.put(name, (Class<? extends ValueProviderWithContext<?>>) providerClass);
//    }

//    public void registerComputedProvider(Class<?> providerClass) {
//        registerComputedProvider(providerClass.getSimpleName(), providerClass);
//    }

    public void registerProvider(Class<?> providerClass) {
        registerProvider(providerClass.getSimpleName(), providerClass);
    }

    @SuppressWarnings("unchecked")
    public void registerProvider(String name, Class<?> providerClass) {
        // FIXME
//        if (!ValueProvider.class.isAssignableFrom(providerClass)) {
//            throw new IllegalArgumentException("Le générateur \"" + providerClass + "\" doit implémenter " + ValueProvider.class);
//        }
//        Class<? extends ValueProvider<?>> existingProviderClass = defaultProvider.get(name);
//        if (existingProviderClass != null) {
//            throw new IllegalArgumentException("Le générateur \"" + existingProviderClass
//                    + "\" et \"" + providerClass + "\" ne peuvent pas être enregistrés sous le même nom");
//        }
        defaultProvider.put(name, (Class<? extends ValueProvider<?>>) providerClass);
    }

    public abstract ObjectProvider parse();
}
