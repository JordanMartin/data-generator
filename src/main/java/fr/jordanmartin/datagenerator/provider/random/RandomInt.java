package fr.jordanmartin.datagenerator.provider.random;

import fr.jordanmartin.datagenerator.provider.ValueProvider;
import fr.jordanmartin.datagenerator.provider.transformer.AsString;

import java.util.Random;

/**
 * Génère un entier aléatoire
 */
public class RandomInt implements ValueProvider<Integer> {
    private final Random random = new Random();
    private int min;
    private int max;

    public RandomInt(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Integer getOne() {
        return random.nextInt(max - min + 1) + min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public AsString<Integer> asString() {
        return new AsString<>(this);
    }
}
