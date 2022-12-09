package io.github.datagenerator.domain.providers.transform;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderArg;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.ObjectContext;
import io.github.datagenerator.domain.core.ValueProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Génère une liste à partir d'un autre générateur
 */
@Provider(
        name = "List",
        description = "Generate a list from multiple providers",
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
    public ListOf(@ProviderArg(description = "The list of providers (they can be of differents type") ValueProvider<?>... providers) {
        this.valueProviders = Arrays.asList(providers);
    }

    @Override
    public List<?> get(ObjectContext ctx) {
        List<Object> array = new ArrayList<>(valueProviders.size());
        for (ValueProvider<?> provider : valueProviders) {
            array.add(evaluateProvider(provider, ctx));
        }
        return array;
    }
}
