package io.github.jordanmartin.datagenerator.provider.core;

import io.github.jordanmartin.datagenerator.provider.transform.Round;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleProviderTest {

    @Test
    void asString() {
    }

    @Test
    void roundUp() {
    }

    @Test
    void roundDown() {
        DoubleProvider provider = (ctx) -> 1.234;
        Round roundDown = provider.roundDown(1);
        Round roundUp = provider.roundUp(2);

        assertEquals(1.2, roundDown.getOne());
        assertEquals(1.24, roundUp.getOne());
        assertEquals("1.24", roundUp.asString().getOne());
    }
}