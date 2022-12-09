package io.github.datagenerator.domain.providers.transform;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionalProviderTest {

    @Test
    void shouldBePresent() {
        OptionalProvider provider = new OptionalProvider(ctx -> "value", ctx -> true);
        assertTrue(provider.get().isPresent());
    }

    @Test
    void shouldBeAbsent() {
        OptionalProvider provider = new OptionalProvider(ctx -> "value", ctx -> false);
        assertTrue(provider.get().isEmpty());
    }
}