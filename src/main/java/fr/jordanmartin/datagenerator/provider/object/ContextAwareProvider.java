package fr.jordanmartin.datagenerator.provider.object;

@FunctionalInterface
public interface ContextAwareProvider<T> {

    T evaluate(ObjectProviderContext ctx);

}
