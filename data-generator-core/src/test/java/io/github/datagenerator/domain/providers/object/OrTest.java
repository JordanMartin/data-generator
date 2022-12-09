package io.github.datagenerator.domain.providers.object;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrTest {
    @Test
    void shouldReturnsFalse() {
        Or provider = new Or(ctx -> false, ctx -> false);
        assertFalse(provider.get());
    }

    @Test
    void shouldReturnsTrue() {
        Or provider = new Or(ctx -> true, ctx -> false);
        assertTrue(provider.get());
    }

    @Test
    void shouldReturnsTrueWithListOfTrue() {
        Or provider = new Or(List.of(ctx -> true, ctx -> false));
        assertTrue(provider.get());
    }

}