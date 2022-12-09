package io.github.datagenerator.domain.providers.transform;

import io.github.datagenerator.domain.core.ValueProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AsStringTest {

    @Test
    void get() {
        ValueProvider<Integer> provider = (ctx) -> 1;
        AsString<Integer> asStringProvider = new AsString<>(provider);
        Assertions.assertEquals("1", asStringProvider.get(null));
    }
}