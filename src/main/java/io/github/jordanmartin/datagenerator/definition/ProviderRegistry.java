package io.github.jordanmartin.datagenerator.definition;

import io.github.jordanmartin.datagenerator.provider.base.Constant;
import io.github.jordanmartin.datagenerator.provider.base.CurrentDate;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.object.Expression;
import io.github.jordanmartin.datagenerator.provider.object.FixedReference;
import io.github.jordanmartin.datagenerator.provider.object.Reference;
import io.github.jordanmartin.datagenerator.provider.random.*;
import io.github.jordanmartin.datagenerator.provider.sequence.IntAutoIncrement;
import io.github.jordanmartin.datagenerator.provider.sequence.SequenceFromList;
import io.github.jordanmartin.datagenerator.provider.transform.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// FIXME: refacto à poursuivre
public class ProviderRegistry {

    protected final Map<String, Class<? extends ValueProvider<?>>> defaultProvider = new HashMap<>();

    protected ProviderRegistry() {
        registerDefaultProviders();
    }

    private void registerDefaultProviders() {
        registerProvider(Constant.class);
        registerProvider(CurrentDate.class);
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
        registerProvider(FixedReference.class);
    }

    public void registerProvider(Class<?> providerClass) {
        registerProvider(providerClass.getSimpleName(), providerClass);
    }

    @SuppressWarnings("unchecked")
    public void registerProvider(String name, Class<?> providerClass) {
        // FIXME
//        if (!ValueProvider.class.isAssignableFrom(providerClass)) {
//            throw new IllegalArgumentException("Le générateur \"" + providerClass + "\" doit implémenter " + ValueProvider.class);
//        }
//        Class<? implements ValueProvider<?>> existingProviderClass = defaultProvider.get(name);
//        if (existingProviderClass != null) {
//            throw new IllegalArgumentException("Le générateur \"" + existingProviderClass
//                    + "\" et \"" + providerClass + "\" ne peuvent pas être enregistrés sous le même nom");
//        }
        defaultProvider.put(name, (Class<? extends ValueProvider<?>>) providerClass);
    }

    public Class<?> get(String providerName) {
        return defaultProvider.get(providerName);
    }

    public Set<String> list() {
        return defaultProvider.keySet();
    }
}
