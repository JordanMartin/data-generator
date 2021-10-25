package io.github.jordanmartin.datagenerator.provider.random;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;
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
@Provider(
        name = "Enum",
        description = "Retourne un élément aléatoire à partir d'une liste de valeur",
        examples = {
                "Enum([\"A\", \"B\", \"C\"]) => retourne l'un des éléments avec la même probabilité",
                "Enum([EnumWeight(\"A\", 50), EnumWeight(\"B\", 30), EnumWeight(\"C\", 20)]) =>" +
                        " retourne \"A\" avec un probabilité de 50%, \"B\": 30% ou \"C\": 20% "
        }
)
public class EnumProvider<T> implements ValueProvider<T> {

    private final Random random = new Random();
    private final List<Object> items;

    @SafeVarargs
    @ProviderCtor("Chaque valeur à la même probabilité d'être sélectionner. Pour changer cette probabilité, utiliser l'objet Enum")
    public EnumProvider(
            @ProviderArg(examples = {"\"A\"", "EnumWeight(\"A\", 10)"})
                    T... items
    ) {
        this.items = Arrays.asList(items);
    }

    @SafeVarargs
    @ProviderCtor("Utilise d'autres générateurs pour retourner les valeurs. Chaque valeur à la même probabilité d'être choisi")
    public EnumProvider(ValueProvider<T>... items) {
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
    public T getOneWithContext(IObjectProviderContext ctx) {
        int idx = random.nextInt(items.size());
        Object item = items.get(idx);

        // Autorise l'utilisation d'un autre générateur
        if (item instanceof ValueProvider) {
            return evaluateProviderWithContext((ValueProvider<T>) item, ctx);
        }

        return (T) item;
    }
}
