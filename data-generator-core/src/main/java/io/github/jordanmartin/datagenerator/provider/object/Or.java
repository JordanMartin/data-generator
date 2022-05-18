package io.github.jordanmartin.datagenerator.provider.object;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;

import java.util.List;
import java.util.function.Predicate;

@Provider(
        name = "Or",
        description = "Retourne vrai si l'un des paramètres est vrai",
        returns = Boolean.class,
        group = "boolean"
)
public class Or implements ValueProvider<Boolean> {

    private final List<ValueProvider<Boolean>> values;

    @ProviderCtor
    public Or(@ProviderArg(description = "Un générateur de booléen") ValueProvider<Boolean> a,
              @ProviderArg(description = "Un générateur de booléen") ValueProvider<Boolean> b) {
        this(List.of(a, b));
    }

    @ProviderCtor
    public Or(@ProviderArg(description = "Liste de générateur de booléen") List<ValueProvider<Boolean>> values) {
        this.values = values;
    }

    @Override
    public Boolean getOneWithContext(IObjectProviderContext ctx) {
        return values.stream()
                .map(provider -> provider.getOneWithContext(ctx))
                .anyMatch(Predicate.isEqual(true));
    }
}
