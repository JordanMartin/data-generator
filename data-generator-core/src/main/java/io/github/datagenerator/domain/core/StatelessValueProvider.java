package io.github.datagenerator.domain.core;

public interface StatelessValueProvider<T> extends ValueProvider<T> {

    T get();

    @Override
    default T get(ObjectContext objectContext) {
        return get();
    }

}
