package io.github.datagenerator.domain.providers.base;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstantTest {

    @Test
    void get() {
        Constant<String> provider = new Constant<>("a");
        assertEquals("a", provider.get());
        assertEquals("a", provider.get());
    }
}