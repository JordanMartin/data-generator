package io.github.datagenerator.domain.providers.transform;

import org.junit.jupiter.api.Test;

import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;


class RoundTest {
    @Test
    void defaultRound() {
        assertEquals("11.0", new Round((ctx) -> 10.123D, 0).get().toString());
        assertEquals("10.2", new Round((ctx) -> 10.123D, 1).get().toString());
        assertEquals("10.13", new Round((ctx) -> 10.123D, 2).get().toString());
        assertEquals("10.513", new Round((ctx) -> 10.5129D, 3).get().toString());
    }

    @Test
    void roundWithMode() {
        assertEquals("10.1", new Round((ctx) -> 10.123D, 1, RoundingMode.DOWN).get().toString());
        assertEquals("10.12", new Round((ctx) -> 10.123D, 2, "down").get().toString());
        assertEquals("10.12", new Round((ctx) -> 10.123D, 2, "DOWN").get().toString());
        assertEquals("10.55", new Round((ctx) -> 10.546D, 2, RoundingMode.HALF_UP).get().toString());
    }
}