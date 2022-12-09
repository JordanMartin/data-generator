package io.github.datagenerator.domain.providers.transform;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ListByRepeatTest {

    @Test
    void get() {
        List<String> list = new Repeat<>((obectContext) -> "a", 5).get();
        assertEquals(5, list.size());
        for (String s : list) {
            assertEquals("a", s);
        }
    }

    @Test
    void getAsStream() {
    }
}