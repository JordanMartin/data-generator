package io.github.jordanmartin.datagenerator.provider.transform;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.base.Constant;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
            @ProviderArg(description = "A provider of any type") ValueProvider<T> valueProvider,
            @ProviderArg(description = "A provider of Integer that define the size of the list")
            ValueProvider<Integer> countProvider) {
        this.countProvider = countProvider;
        this.valueProvider = valueProvider;
    }

    @ProviderCtor
    public Repeat(
            @ProviderArg(description = "A provider of any type") ValueProvider<T> valueProvider,
            @ProviderArg(description = "Size of the list") int count) {
        this(valueProvider, new Constant<>(count));
    }


    @Override
    public List<T> getOneWithContext(IObjectProviderContext ctx) {
        int count = evaluateProviderWithContext(this.countProvider, ctx);
        List<T> array = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            T value = evaluateProviderWithContext(this.valueProvider, ctx);
            array.add(value);
        }
        return array;
    }

    public Stream<T> getAsStream() {
        int count = countProvider.getOne();
        return IntStream.of(0, count)
                .mapToObj(idx -> this.valueProvider.getOne());
    }
}
