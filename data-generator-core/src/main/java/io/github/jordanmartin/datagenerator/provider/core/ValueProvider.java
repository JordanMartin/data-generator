package io.github.jordanmartin.datagenerator.provider.core;

import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;
import io.github.jordanmartin.datagenerator.provider.transform.Idempotent;
import io.github.jordanmartin.datagenerator.provider.transform.Repeat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Générateur d'une donnée
 *
 * @param <T> Le type de la donnée à générer
 */
@FunctionalInterface
public interface ValueProvider<T> {

    /**
     * Génère une liste
     *
     * @param count Nombre d'éléments à générer
     */
    default List<T> getList(int count) {
        return getStream(count).collect(Collectors.toList());
    }

    default List<T> getList(ValueProvider<Integer> countProvider) {
        return getList(countProvider.getOne());
    }

    /**
     * Génère une liste sous forme de Stream
     *
     * @param count Nombre d'éléments à générer
     */
    default Stream<T> getStream(int count) {
        return IntStream.range(0, count)
                .mapToObj(idx -> getOne());
    }

    default Stream<T> getStream(ValueProvider<Integer> countProvider) {
        return getStream(countProvider.getOne());
    }

    /**
     * Retourne un nouveau générateur qui retournera toujours la même valeur
     * Le générateur d'origine ne sera donc appelé qu'une seul fois
     */
    default Idempotent<T> idempotent() {
        return new Idempotent<>(this);
    }

    default Repeat<T> repeat(int count) {
        return new Repeat<>(this, count);
    }

    default Repeat<T> repeat(ValueProvider<Integer> count) {
        return new Repeat<>(this, count);
    }

    T getOneWithContext(IObjectProviderContext ctx);

    default T getOne() {
        return getOneWithContext(null);
    }

    default <R> R evaluateProviderWithContext(ValueProvider<R> provider, IObjectProviderContext ctx) {
        if (ctx == null) {
            return provider.getOne();
        }
        return ctx.evaluateProvider(provider);
    }

}