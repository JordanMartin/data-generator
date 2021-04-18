package fr.jordanmartin.datagenerator.provider.random;

import fr.jordanmartin.datagenerator.provider.base.ValueProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Génère une valeur aléatoire à partir d'un liste donnée
 * Peut être un générateur
 *
 * @param <T> Le type des éléments de la liste
 */
@Slf4j
public class RandomFromList<T> implements ValueProvider<T> {

    private final Random random = new Random();
    private final List<Object> items;

    @SafeVarargs
    public RandomFromList(T... items) {
        this.items = Arrays.asList(items);
    }

    @SafeVarargs
    public RandomFromList(ValueProvider<T>... items) {
        this.items = Arrays.asList(items);
    }

    public RandomFromList(List<?> items) {
        this.items = new ArrayList<>();
        for (Object item : items) {
            if (item instanceof ItemWeight) {
                ItemWeight<?> itemWeight = (ItemWeight<?>) item;
                for (int i = 0; i < itemWeight.weight; ++i) {
                    this.items.add(itemWeight.item);
                }
            } else {
                this.items.add(item);
            }
        }
    }

    public RandomFromList(ItemWeight<?>... items) {
        this(Arrays.asList(items));
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getOne() {
        int idx = random.nextInt(items.size());
        Object item = items.get(idx);

        // Autorise l'utilisation d'un autre générateur
        if (item instanceof ValueProvider) {
            return ((ValueProvider<T>) item).getOne();
        }

        return (T) item;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ItemWeight<T> {

        private Object item;
        private int weight;

        public ItemWeight(ValueProvider<T> item, int weight) {
            this.item = item;
            this.weight = weight;
        }
    }
}
