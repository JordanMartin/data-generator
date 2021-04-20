package fr.jordanmartin.datagenerator.provider.transform;

import org.junit.jupiter.api.Test;

import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoundTest {
    @Test
    void defaultRound() {
        assertEquals("11.0", new Round((ctx) -> 10.123D, 0).getOne().toString());
        assertEquals("10.2", new Round((ctx) -> 10.123D, 1).getOne().toString());
        assertEquals("10.13", new Round((ctx) -> 10.123D, 2).getOne().toString());
        assertEquals("10.513", new Round((ctx) -> 10.5129D, 3).getOne().toString());
    }

    @Test
    void roundWithMode() {
        assertEquals("10.1", new Round((ctx) -> 10.123D, 1, RoundingMode.DOWN).getOne().toString());
        assertEquals("10.12", new Round((ctx) -> 10.123D, 2, "down").getOne().toString());
        assertEquals("10.12", new Round((ctx) -> 10.123D, 2, "DOWN").getOne().toString());
        assertEquals("10.55", new Round((ctx) -> 10.546D, 2, RoundingMode.HALF_UP).getOne().toString());
    }
}