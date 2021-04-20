package fr.jordanmartin.datagenerator.provider.sequence;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SequenceFromListTest {

    @Test
    void getOneFromList() {
        var list = Arrays.asList("a", "b", "c");
        var provider = new SequenceFromList<>(list);

        for (String expected : list) {
            assertEquals(expected, provider.getOne());
        }
        for (String expected : list) {
            assertEquals(expected, provider.getOne());
        }
    }

    @Test
    void getOneFromVargs() {
        var expected = Arrays.asList("a", "b", "c");
        var provider = new SequenceFromList<>("a", "b", "c");

        for (String e : expected) {
            assertEquals(e, provider.getOne());
        }
        for (String e : expected) {
            assertEquals(e, provider.getOne());
        }
    }
}