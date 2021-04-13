package fr.jordanmartin.datagenerator.provider.constant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstantTest {

    @Test
    void getOne() {
        Constant<String> provider = new Constant<>("a");
        assertEquals("a", provider.getOne());
        assertEquals("a", provider.getOne());
    }
}