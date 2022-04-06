package io.github.jordanmartin.datagenerator.provider.object;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;

@Provider(
        name = "Or",
        description = "Retourne vrai si l'un des deux valeurs est vrai",
        returns = Boolean.class,
        group = "reference"
)
public class Or implements ValueProvider<Boolean> {

    private final ValueProvider<Boolean> a;
    private final ValueProvider<Boolean> b;

    @ProviderCtor
    public Or(ValueProvider<Boolean> a, ValueProvider<Boolean> b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Boolean getOneWithContext(IObjectProviderContext ctx) {
        Boolean aBool = a.getOneWithContext(ctx);
        Boolean bBool = b.getOneWithContext(ctx);
        return aBool || bBool;
    }
}
