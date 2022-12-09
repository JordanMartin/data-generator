package io.github.datagenerator.domain.providers.object;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AndTest {

    @Test
    void shouldReturnsFalse() {
        And provider = new And(ctx -> true, ctx -> false);
        assertFalse(provider.get());
    }

    @Test
    void shouldReturnsTrue() {
        And provider = new And(ctx -> true, ctx -> true);
        assertTrue(provider.get());
    }

    @Test
    void shouldReturnsTrueWithListOfTrue() {
        And provider = new And(List.of(ctx -> true, ctx -> true));
        assertTrue(provider.get());
    }
}