package io.github.jordanmartin.datagenerator.provider.random;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.IntegerProvider;
import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;
import io.github.jordanmartin.datagenerator.provider.core.ValueProviderException;

import java.util.Random;

/**
 * Génère un entier aléatoire
 */
@Provider(
        name = "Integer",
        description = "Retourne un entier aléatoire",
        examples = {"Integer()", "Integer(10)", "Integer(10, 10)"},
        groupe = "nombre"
)
public class RandomInt implements IntegerProvider, StatelessValueProvider<Integer> {

    private final Random random = new Random();
    private final int min;
    private final int max;

    @ProviderCtor("Entier entre min et max")
    public RandomInt(int min, int max) {
        if (min > max) {
            throw new ValueProviderException(this, "La valeur minimum doit être <= à la valeur maximum");
        }
        this.min = min;
        this.max = max;
    }

    @ProviderCtor("Entier entre 0 et Integer.MAX_VALUE - 1")
    public RandomInt() {
        this(0, Integer.MAX_VALUE - 1);
    }

    @ProviderCtor("Entier entre 0 et max")
    public RandomInt(int max) {
        this(0, max);
    }

    @Override
    public Integer getOne() {
        return random.nextInt(max - min + 1) + min;
    }

}
