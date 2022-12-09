package io.github.datagenerator.domain.providers.base;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntIncrementTest {

    @Test
    void getDefault() {
        IntIncrement provider = new IntIncrement();
        for (int i = 0; i < 10; i++) {
            assertEquals(i, provider.get());
        }
    }

    @Test
    void getNegative() {
        IntIncrement provider = new IntIncrement(-10, 1, 0);
        for (int i = -10; i <= 0; i++) {
            assertEquals(i, provider.get());
        }
        for (int i = -10; i < 0; i++) {
            assertEquals(i, provider.get());
        }
    }

    @Test
    void getStepNegative() {
        IntIncrement provider = new IntIncrement(100, -1, 0);
        for (int i = 100; i >= 0; i--) {
            assertEquals(i, provider.get());
        }
        for (int i = 100; i >= 0; i--) {
            assertEquals(i, provider.get());
        }
    }
}