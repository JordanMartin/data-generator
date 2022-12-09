package io.github.datagenerator.domain.providers.random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomDoubleTest {

    @Test
    void get() {
        double value = new RandomDouble(40, 45).get();
        assertTrue(value >= 40);
        assertTrue(value <= 45);
    }
}