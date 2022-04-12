package io.github.jordanmartin.datagenerator.provider.object;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;

@Provider(
        name = "And",
        description = "Retourne vrai si les deux valeurs sont vrais",
        returns = Boolean.class,
        group = "booléen"
)
public class And implements ValueProvider<Boolean> {

    private final ValueProvider<Boolean> a;
    private final ValueProvider<Boolean> b;

    @ProviderCtor
    public And(ValueProvider<Boolean> a, ValueProvider<Boolean> b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Boolean getOneWithContext(IObjectProviderContext ctx) {
        return a.getOneWithContext(ctx) && b.getOneWithContext(ctx);
    }
}
