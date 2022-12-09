package io.github.datagenerator.domain.providers.base;

import io.github.datagenerator.domain.core.ValueProvider;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ValueProviderTest {

    @Test
    void get() {
        ValueProvider<String> provider = ctx -> "a";
        assertEquals("a", provider.get(null));
    }

    @Test
    void multipleGet() {
        ValueProvider<String> provider = (objectContext) -> UUID.randomUUID().toString();
        String first = provider.get(null);
        String second = provider.get(null);
        assertNotEquals(first, second);
    }
}