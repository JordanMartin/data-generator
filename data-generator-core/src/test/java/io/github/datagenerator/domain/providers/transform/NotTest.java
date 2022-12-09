package io.github.datagenerator.domain.providers.transform;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotTest {

    @Test
    void not() {
        assertEquals(true, new Not(false).get());
        assertEquals(false, new Not(true).get());
        assertEquals(true, new Not(ctx -> false).get());
        assertEquals(false, new Not(ctx -> true).get());
    }
}