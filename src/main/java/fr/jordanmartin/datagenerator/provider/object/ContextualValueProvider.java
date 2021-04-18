package fr.jordanmartin.datagenerator.provider.object;

import fr.jordanmartin.datagenerator.provider.base.ValueProvider;

@FunctionalInterface
public interface ContextualValueProvider<T> extends ValueProvider<ObjectContextHandler<T>> {

    @Override
    default ObjectContextHandler<T> getOne() {
        return this::getOne;
    }

    T getOne(ObjectProviderContext ctx);
}
