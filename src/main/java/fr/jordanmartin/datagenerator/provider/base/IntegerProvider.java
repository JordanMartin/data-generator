package fr.jordanmartin.datagenerator.provider.base;

import fr.jordanmartin.datagenerator.provider.transform.AsString;

public interface IntegerProvider extends ValueProvider<Integer> {

    default AsString<Integer> asString() {
        return new AsString<>(this);
    }

}
