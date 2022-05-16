package io.github.jordanmartin.datagenerator.provider.object;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;

import java.util.List;
import java.util.function.Predicate;

@Provider(
        name = "And",
        description = "Retourne vrai tous les paramètres sont vrais",
        returns = Boolean.class,
        group = "booléen"
)
public class And implements ValueProvider<Boolean> {

    private final List<ValueProvider<Boolean>> values;

    @ProviderCtor
    public And(@ProviderArg(description = "Un générateur de booléen") ValueProvider<Boolean> a,
               @ProviderArg(description = "Un générateur de booléen") ValueProvider<Boolean> b) {
        this(List.of(a, b));
    }

    @ProviderCtor
    public And(@ProviderArg(description = "Liste de générateur de booléen") List<ValueProvider<Boolean>> values) {
        this.values = values;
    }

    @Override
    public Boolean getOneWithContext(IObjectProviderContext ctx) {
        return values.stream()
                .map(provider -> provider.getOneWithContext(ctx))
                .allMatch(Predicate.isEqual(true));
    }
}
