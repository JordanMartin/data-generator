package io.github.jordanmartin.datagenerator.provider.random;

import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class RandomBooleanTest {

    @Test
    void getOne() {
        RandomBoolean provider = new RandomBoolean();
        assertNotNull(provider.getOne());
    }

    @Test
    void muchMoreTrue() {
        RandomBoolean provider = new RandomBoolean(0.75);
        int total = 10_000;
        long trueCount = provider.getStream(total)
                .filter(b -> b)
                .count();
        long falseCount = total - trueCount;
        assertTrue(trueCount > falseCount);
    }

    @Test
    void muchMoreFalse() {
        RandomBoolean provider = new RandomBoolean(0.25);
        int total = 10_000;
        long trueCount = provider.getStream(total)
                .filter(b -> b)
                .count();
        long falseCount = total - trueCount;
        assertTrue(trueCount < falseCount);
    }

    @Test
    void onlyTrue() {
        RandomBoolean provider = new RandomBoolean(1);
        int total = 10_000;
        long trueCount = provider.getStream(total)
                .filter(b -> b)
                .count();
        assertEquals(total, trueCount);
    }


    @Test
    void onlyFlase() {
        RandomBoolean provider = new RandomBoolean(0);
        int total = 10_000;
        long falseCount = provider.getStream(total)
                .filter(b -> !b)
                .count();
        assertEquals(total, falseCount);
    }
}
