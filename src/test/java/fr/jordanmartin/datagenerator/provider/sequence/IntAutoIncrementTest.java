package fr.jordanmartin.datagenerator.provider.sequence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntAutoIncrementTest {

    @Test
    void getOneDefault() {
        IntAutoIncrement provider = new IntAutoIncrement();
        for (int i = 0; i < 10; i++) {
            assertEquals(i, provider.getOne());
        }
    }

    @Test
    void getOneNegative() {
        IntAutoIncrement provider = new IntAutoIncrement(-10, 1, 0);
        for (int i = -10; i <= 0; i++) {
            assertEquals(i, provider.getOne());
        }
        for (int i = -10; i < 0; i++) {
            assertEquals(i, provider.getOne());
        }
    }

    @Test
    void getOneStepNegative() {
        IntAutoIncrement provider = new IntAutoIncrement(100, -1, 0);
        for (int i = 100; i >= 0; i--) {
            assertEquals(i, provider.getOne());
        }
        for (int i = 100; i >= 0; i--) {
            assertEquals(i, provider.getOne());
        }
    }
}