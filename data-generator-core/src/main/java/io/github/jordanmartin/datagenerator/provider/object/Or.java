package io.github.jordanmartin.datagenerator.provider.object;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;

import java.util.List;
import java.util.function.Predicate;

@Provider(
        name = "Or",
        description = "Returns true when at least one parameter are true otherwise returns false",
        returns = Boolean.class,
        group = "boolean"
)
public class Or implements ValueProvider<Boolean> {

    private final List<ValueProvider<Boolean>> values;

    @ProviderCtor
    public Or(@ProviderArg(description = "A boolean provider") ValueProvider<Boolean> a,
              @ProviderArg(description = "A boolean provider") ValueProvider<Boolean> b) {
        this(List.of(a, b));
    }

    @ProviderCtor
    public Or(@ProviderArg(description = "A list of boolean provider") List<ValueProvider<Boolean>> values) {
        this.values = values;
    }

    @Override
    public Boolean getOneWithContext(IObjectProviderContext ctx) {
        return values.stream()
                .map(provider -> provider.getOneWithContext(ctx))
                .anyMatch(Predicate.isEqual(true));
    }
}
