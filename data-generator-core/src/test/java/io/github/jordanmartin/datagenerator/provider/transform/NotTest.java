package io.github.jordanmartin.datagenerator.provider.transform;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotTest {

    @Test
    void not() {
        assertEquals(true, new Not(false).getOne());
        assertEquals(false, new Not(true).getOne());
        assertEquals(true, new Not(ctx -> false).getOne());
        assertEquals(false, new Not(ctx -> true).getOne());
    }
}