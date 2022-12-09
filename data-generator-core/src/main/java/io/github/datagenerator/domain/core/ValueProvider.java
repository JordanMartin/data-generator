package io.github.datagenerator.domain.core;

@FunctionalInterface
public interface ValueProvider<T> {
    T get(ObjectContext objectContext);

    default T get() {
        return get(null);
    }

    default <P> P evaluateProvider(ValueProvider<P> provider, ObjectContext context) {
        if (context == null) {
            return provider.get();
        }
        return context.evaluateProvider(provider);
    }
}
