package fr.jordanmartin.datagenerator.provider.base;

import fr.jordanmartin.datagenerator.provider.object.ObjectProviderContext;

public interface StatelessValueProvider<T> extends ValueProvider<T> {

    @Override
    T getOne();

    @Override
    default T getOneWithContext(ObjectProviderContext ctx) {
        return getOne();
    }
}
