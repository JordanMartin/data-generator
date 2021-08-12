package io.github.jordanmartin.datagenerator.provider.core;

import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;

public interface StatelessValueProvider<T> extends ValueProvider<T> {

    @Override
    T getOne();

    @Override
    default T getOneWithContext(IObjectProviderContext ctx) {
        return getOne();
    }
}
