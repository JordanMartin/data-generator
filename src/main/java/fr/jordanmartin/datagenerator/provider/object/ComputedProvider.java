package fr.jordanmartin.datagenerator.provider.object;

/**
 * Générateur permettant le calcul d'une donnée à partir du context des autres données
 *
 * @param <T> Type de la donnée généré
 */
public interface ComputedProvider<T> {

    T evaluate(ObjectProviderContext ctx);

}
