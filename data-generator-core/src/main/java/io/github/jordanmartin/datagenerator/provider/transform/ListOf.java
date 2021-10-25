package io.github.jordanmartin.datagenerator.provider.transform;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Génère une liste à partir d'un autre générateur
 */
@Provider(
        name = "List",
        description = "Retourne une liste avec les valeurs obtenus par d'autres générateurs",
        examples = {
                "List([UUID(), Constant(42), Integer(0, 100)]) => [ \"2c2094e2-36cd-4d17-9c68-311368cbd3f8\", 42, 94 ]"
        }
)
public class ListOf implements ValueProvider<List<?>> {

    /**
     * Les générateurs de valeur
     */
    private final List<ValueProvider<?>> valueProviders;

    public ListOf(List<ValueProvider<?>> valueProviders) {
        this.valueProviders = valueProviders;
    }

    @ProviderCtor
    public ListOf(ValueProvider<?>... valueProviders) {
        this.valueProviders = Arrays.asList(valueProviders);
    }

    @Override
    public List<?> getOneWithContext(IObjectProviderContext ctx) {
        List<Object> array = new ArrayList<>(valueProviders.size());
        for (ValueProvider<?> provider : valueProviders) {
            array.add(evaluateProviderWithContext(provider, ctx));
        }
        return array;
    }
}
