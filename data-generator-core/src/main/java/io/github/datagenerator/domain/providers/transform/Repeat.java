package io.github.datagenerator.domain.providers.transform;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderArg;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.ObjectContext;
import io.github.datagenerator.domain.core.ValueProvider;
import io.github.datagenerator.domain.providers.base.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Répète un généteur pour construire une liste
 *
 * @param <T> Le type de la donnée de la liste
 */
@Provider(
        name = "Repeat",
        description = "Generate a list by repeating a provider",
        examples = {
                "Repeat(Faker(\"Name.firstName\"), Integer(0, 3)) => [ \"Zoe\", \"Sacha\", \"Rayan\" ] ou [ \"Benjamin\", \"Laura\" ]",
                "Repeat(UUID(), 2) => [ \"d9443c7c-81c9-4188-869f-f9145a298837\", \"578b2d27-f7ca-4da0-9849-22fc5b7e31fe\" ]"
        }
)
public class Repeat<T> implements ValueProvider<List<T>> {

    /**
     * Le générateur qui alimentera la liste
     **/
    private final ValueProvider<T> valueProvider;

    /**
     * Nombre de répétition
     **/
    private final ValueProvider<Integer> countProvider;

    @ProviderCtor
    public Repeat(
            @ProviderArg(description = "Provider of any type") ValueProvider<T> valueProvider,
            @ProviderArg(description = "Provider of Integer that define the size of the list")
            ValueProvider<Integer> countProvider) {
        this.countProvider = countProvider;
        this.valueProvider = valueProvider;
    }

    @ProviderCtor
    public Repeat(
            @ProviderArg(description = "Provider of any type") ValueProvider<T> valueProvider,
            @ProviderArg(description = "Size of the list") int count) {
        this(valueProvider, new Constant<>(count));
    }


    @Override
    public List<T> get(ObjectContext ctx) {
        int count = evaluateProvider(this.countProvider, ctx);
        List<T> array = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            T value = evaluateProvider(this.valueProvider, ctx);
            array.add(value);
        }
        return array;
    }
}
