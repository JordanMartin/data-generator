package io.github.jordanmartin.datagenerator.provider.sequence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntIncrementTest {

    @Test
    void getOneDefault() {
        IntIncrement provider = new IntIncrement();
        for (int i = 0; i < 10; i++) {
            assertEquals(i, provider.getOne());
        }
    }

    @Test
    void getOneNegative() {
        IntIncrement provider = new IntIncrement(-10, 1, 0);
        for (int i = -10; i <= 0; i++) {
            assertEquals(i, provider.getOne());
        }
        for (int i = -10; i < 0; i++) {
            assertEquals(i, provider.getOne());
        }
    }

    @Test
    void getOneStepNegative() {
        IntIncrement provider = new IntIncrement(100, -1, 0);
        for (int i = 100; i >= 0; i--) {
            assertEquals(i, provider.getOne());
        }
        for (int i = 100; i >= 0; i--) {
            assertEquals(i, provider.getOne());
        }
    }
}