package fr.jordanmartin.datagenerator.provider.core;

import fr.jordanmartin.datagenerator.provider.transform.AsString;

public interface IntegerProvider extends ValueProvider<Integer> {

    default AsString<Integer> asString() {
        return new AsString<>(this);
    }

}
