package io.github.datagenerator.domain.providers.base;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.ValueProvider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Provider(
        name = "EnumWeight",
        description = "Returns a EnumWeight. Note: should only be used with Enum(...) provider",
        examples = {
                "See examples of Enum(...) provider"
        },
        returns = EnumWeight.class,
        group = "enum"
)
public class EnumWeight {
    private Object item;
    private int weight;

    @ProviderCtor("Constante value")
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
