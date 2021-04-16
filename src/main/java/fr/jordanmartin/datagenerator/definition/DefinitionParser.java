package fr.jordanmartin.datagenerator.definition;

import fr.jordanmartin.datagenerator.provider.IdempotentProvider;
import fr.jordanmartin.datagenerator.provider.ListOf;
import fr.jordanmartin.datagenerator.provider.Repeat;
import fr.jordanmartin.datagenerator.provider.ValueProvider;
import fr.jordanmartin.datagenerator.provider.constant.Constant;
import fr.jordanmartin.datagenerator.provider.object.ComputedProvider;
import fr.jordanmartin.datagenerator.provider.object.Expression;
import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;
import fr.jordanmartin.datagenerator.provider.object.Reference;
import fr.jordanmartin.datagenerator.provider.random.*;
import fr.jordanmartin.datagenerator.provider.sequence.IntAutoIncrement;
import fr.jordanmartin.datagenerator.provider.sequence.SequenceFromList;
import fr.jordanmartin.datagenerator.provider.transformer.AsString;
import fr.jordanmartin.datagenerator.provider.transformer.FormatDate;

import java.util.HashMap;
import java.util.Map;

public abstract class DefinitionParser {

    protected final Map<String, Class<? extends ValueProvider<?>>> defaultProvider = new HashMap<>();
    protected final Map<String, Class<? extends ComputedProvider<?>>> defaultComputedProvider = new HashMap<>();

    public DefinitionParser() {
        registerDefaultProviders();
    }

    private void registerDefaultProviders() {
        registerProvider(Constant.class);
        registerProvider(RandomDate.class);
        registerProvider(RandomFromList.class);
        registerProvider(RandomFromRegex.class);
        registerProvider(RandomInt.class);
        registerProvider(RandomUUID.class);
        registerProvider(IntAutoIncrement.class);
        registerProvider(SequenceFromList.class);
        registerProvider(AsString.class);
        registerProvider(FormatDate.class);
        registerProvider(IdempotentProvider.class);
        registerProvider(ListOf.class);
        registerProvider(Repeat.class);

        registerComputedProvider(Expression.class);
        registerComputedProvider(Reference.class);
    }

    @SuppressWarnings("unchecked")
    public void registerComputedProvider(String name, Class<?> providerClass) {
        if (!ComputedProvider.class.isAssignableFrom(providerClass)) {
            throw new IllegalArgumentException("Le générateur de référence \"" + providerClass + "\" doit implémenter " + ComputedProvider.class);
        }
        Class<? extends ComputedProvider<?>> existingProviderClass = defaultComputedProvider.get(name);
        if (existingProviderClass != null) {
            throw new IllegalArgumentException("Le générateur de référence \"" + existingProviderClass
                    + "\" et \"" + providerClass + "\" ne peuvent pas être enregistrés sous le même nom");
        }
        defaultComputedProvider.put(name, (Class<? extends ComputedProvider<?>>) providerClass);
    }

    public void registerComputedProvider(Class<?> providerClass) {
        registerComputedProvider(providerClass.getSimpleName(), providerClass);
    }

    public void registerProvider(Class<?> providerClass) {
        registerProvider(providerClass.getSimpleName(), providerClass);
    }

    @SuppressWarnings("unchecked")
    public void registerProvider(String name, Class<?> providerClass) {
        if (!ValueProvider.class.isAssignableFrom(providerClass)) {
            throw new IllegalArgumentException("Le générateur \"" + providerClass + "\" doit implémenter " + ValueProvider.class);
        }
        Class<? extends ValueProvider<?>> existingProviderClass = defaultProvider.get(name);
        if (existingProviderClass != null) {
            throw new IllegalArgumentException("Le générateur \"" + existingProviderClass
                    + "\" et \"" + providerClass + "\" ne peuvent pas être enregistrés sous le même nom");
        }
        defaultProvider.put(name, (Class<? extends ValueProvider<?>>) providerClass);
    }

    public abstract ObjectProvider parse();
}
