package fr.jordanmartin.datagenerator.provider.sequence;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SequenceFromListTest {

    @Test
    void getOne() {
        var list = Arrays.asList("a", "b", "c");
        var provider = new SequenceFromList<>(list);

        for (String expected : list) {
            assertEquals(expected, provider.getOne());
        }
        for (String expected : list) {
            assertEquals(expected, provider.getOne());
        }
    }
}