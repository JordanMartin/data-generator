package fr.jordanmartin.datagenerator.provider.base;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstantTest {

    @Test
    void getOne() {
        Constant<String> provider = new Constant<>("a");
        assertEquals("a", provider.getOne());
        assertEquals("a", provider.getOne());
    }
}