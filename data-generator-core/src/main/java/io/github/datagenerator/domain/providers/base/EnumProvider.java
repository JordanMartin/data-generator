package io.github.datagenerator.domain.providers.base;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderArg;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.ObjectContext;
import io.github.datagenerator.domain.core.ValueProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Génère une valeur aléatoire à partir d'une liste donnée
 * Peut être un générateur
 *
 * @param <T> Le type des éléments de la liste
 */
@Slf4j
@Provider(
        name = "Enum",
        description = "Random element from a predefined list",
        examples = {
                "Enum([\"A\", \"B\", \"C\"]) => Returns A, B or C with the same probability",
                "Enum([EnumWeight(\"A\", 50), EnumWeight(\"B\", 30), EnumWeight(\"C\", 20)]) =>" +
                        " returns \"A\" with a probability of 50, \"B\": 30% ou \"C\": 20% "
        },
        group = "enum"
)
public class EnumProvider<T> implements ValueProvider<T> {

    private final Random random = new Random();
    private final List<Object> items;

    @SafeVarargs
    @ProviderCtor("Returns one of the values with the same probability")
    public EnumProvider(
            @ProviderArg(description = "List of items. Can be a value (string, number, boolean) or a provider ", examples = {"\"A\"", "Integer(1, 10)"}) T... items
    ) {
        this.items = Arrays.asList(items);
    }

    @SafeVarargs
    @ProviderCtor("Changes the probability of selecting a value")
    public EnumProvider(
            @ProviderArg(
                    description = "List of items wrapped with an EnumWeight (the wrapped value can be a string," +
                            " number, boolean or a provider)",
                    examples = {"EnumWeight(\"A\", 10)", "EnumWeight(Integer(1, 10), 90)"})
            ValueProvider<T>... items) {
        this.items = Arrays.asList(items);
    }

    public EnumProvider(List<?> items) {
        this.items = new ArrayList<>();
        for (Object item : items) {
            if (item instanceof EnumWeight) {
                EnumWeight itemWeight = (EnumWeight) item;
                for (int i = 0; i < itemWeight.getWeight(); ++i) {
                    this.items.add(itemWeight.getItem());
                }
            } else {
                this.items.add(item);
            }
        }
    }

    public EnumProvider(EnumWeight... items) {
        this(Arrays.asList(items));
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(ObjectContext ctx) {
        int idx = random.nextInt(items.size());
        Object item = items.get(idx);

        // Autorise l'utilisation d'un autre générateur
        if (item instanceof ValueProvider) {
            return evaluateProvider((ValueProvider<T>) item, ctx);
        }

        return (T) item;
    }
}
