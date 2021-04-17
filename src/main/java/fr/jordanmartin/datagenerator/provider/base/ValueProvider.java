package fr.jordanmartin.datagenerator.provider.base;

import fr.jordanmartin.datagenerator.provider.transform.Idempotent;
import fr.jordanmartin.datagenerator.provider.transform.ListByRepeat;

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
     * Génère la donnée
     */
    T getOne();

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
    default ValueProvider<T> idempotent() {
        return new Idempotent<>(this);
    }

    default ListByRepeat<?> repeat(int count) {
        return new ListByRepeat<>(this, count);
    }

    default ListByRepeat<?> repeat(ValueProvider<Integer> count) {
        return new ListByRepeat<>(this, count);
    }
}