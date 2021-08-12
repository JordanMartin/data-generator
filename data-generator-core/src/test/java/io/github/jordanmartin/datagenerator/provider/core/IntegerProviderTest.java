package io.github.jordanmartin.datagenerator.provider.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegerProviderTest {

    @Test
    void testIntegerProvider() {
        IntegerProvider provider = (ctx) -> 1;
        assertEquals(1, provider.getOne());
        assertEquals("1", provider.asString().getOne());
    }
}