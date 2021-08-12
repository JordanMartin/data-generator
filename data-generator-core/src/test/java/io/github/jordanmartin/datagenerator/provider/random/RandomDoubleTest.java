package io.github.jordanmartin.datagenerator.provider.random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomDoubleTest {

    @Test
    void getOne() {
        double value = new RandomDouble(40, 45).getOne();
        assertTrue(value >= 40);
        assertTrue(value <= 45);
    }
}