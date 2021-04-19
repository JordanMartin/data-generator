package fr.jordanmartin.datagenerator.provider.transform;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoundTest {
    @Test
    void getOne() {
        assertEquals("11.0", new Round((obectContext) -> 10.123D, 0).getOne().toString());
        assertEquals("10.2", new Round((obectContext) -> 10.123D, 1).getOne().toString());
        assertEquals("10.13", new Round((obectContext) -> 10.123D, 2).getOne().toString());
        assertEquals("10.513", new Round((obectContext) -> 10.5129D, 3).getOne().toString());
    }
}