package io.github.datagenerator.domain.providers.random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomBooleanTest {

    @Test
    void get() {
        RandomBoolean provider = new RandomBoolean();
        assertNotNull(provider.get());
    }

    @Test
    void muchMoreTrue() {
        RandomBoolean provider = new RandomBoolean(0.75);
        int total = 10_000;
        int numberOfTrue = 0;
        for (int i = 0; i < 10_000; i++) {
            if (provider.get()) {
                numberOfTrue++;
            }
        }
        long numberOfFalse = total - numberOfTrue;
        assertTrue(numberOfTrue > numberOfFalse);
    }

    @Test
    void muchMoreFalse() {
        RandomBoolean provider = new RandomBoolean(0.25);
        int total = 10_000;
        int numberOfTrue = 0;
        for (int i = 0; i < 10_000; i++) {
            if (provider.get()) {
                numberOfTrue++;
            }
        }
        long numberOfFalse = total - numberOfTrue;
        assertTrue(numberOfTrue < numberOfFalse);
    }

    @Test
    void onlyTrue() {
        RandomBoolean provider = new RandomBoolean(1);
        for (int i = 0; i < 10_000; i++) {
            assertTrue(provider.get());
        }
    }


    @Test
    void onlyFlase() {
        RandomBoolean provider = new RandomBoolean(0);
        for (int i = 0; i < 10_000; i++) {
            assertFalse(provider.get());
        }
    }
}
