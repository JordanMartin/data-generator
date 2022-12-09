package io.github.datagenerator.domain.providers.object;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderArg;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.ObjectContext;
import io.github.datagenerator.domain.core.ValueProvider;

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
    public Or(@ProviderArg(description = "Boolean provider") ValueProvider<Boolean> a,
              @ProviderArg(description = "Boolean provider") ValueProvider<Boolean> b) {
        this(List.of(a, b));
    }

    @ProviderCtor
    public Or(@ProviderArg(description = "List of boolean provider") List<ValueProvider<Boolean>> values) {
        this.values = values;
    }

    @Override
    public Boolean get(ObjectContext ctx) {
        return values.stream()
                .map(provider -> provider.get(ctx))
                .anyMatch(Predicate.isEqual(true));
    }
}
