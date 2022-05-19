package io.github.jordanmartin.datagenerator.provider.random;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Provider(
        name = "EnumWeight",
        description = "Returns a EnumWeight. Note: it can only be used with Enum(...) provider",
        examples = {
                "See examples of Enum(...) provider"
        },
        returns = EnumWeight.class,
        group = "enum"
)
public class EnumWeight {
    private Object item;
    private int weight;

    @ProviderCtor
    public EnumWeight(Object item, int weight) {
        this.item = item;
        this.weight = weight;
    }

    @ProviderCtor("Value from another provider")
    public EnumWeight(ValueProvider<?> item, int weight) {
        this.item = item;
        this.weight = weight;
    }
}
