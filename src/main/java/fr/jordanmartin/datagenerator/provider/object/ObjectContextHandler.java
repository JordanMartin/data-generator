package fr.jordanmartin.datagenerator.provider.object;

public interface ObjectContextHandler<T> {

    T evaluate(ObjectProviderContext ctx);

}
