package io.github.datagenerator.domain.providers.base;


import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SequenceFromListTest {

    @Test
    void getFromList() {
        var list = Arrays.asList("a", "b", "c");
        var provider = new SequenceFromList<>(list);

        for (String expected : list) {
            assertEquals(expected, provider.get());
        }
        for (String expected : list) {
            assertEquals(expected, provider.get());
        }
    }

    @Test
    void getFromVargs() {
        var expected = Arrays.asList("a", "b", "c");
        var provider = new SequenceFromList<>("a", "b", "c");

        for (String e : expected) {
            assertEquals(e, provider.get());
        }
        for (String e : expected) {
            assertEquals(e, provider.get());
        }
    }
}