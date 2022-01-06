package io.github.jordanmartin.datagenerator.provider.random;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Provider(
        name = "EnumWeight", description = "Retourne un objet EnumWeight à utiliser avec Enum(...)",
        examples = {
                "EnumWeight(\"A\", 10) => Valeur \"A\" avec un poids de 10",
                "EnumWeight(Faker(\"Name.firstName\"), 10) => Un prénom aléatoire avec un poids de 10",
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

    @ProviderCtor("Valeur depuis un autre générateur")
    public EnumWeight(ValueProvider<?> item, int weight) {
        this.item = item;
        this.weight = weight;
    }
}
