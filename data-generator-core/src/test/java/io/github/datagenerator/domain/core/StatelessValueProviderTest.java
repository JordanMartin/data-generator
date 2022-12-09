package io.github.datagenerator.domain.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatelessValueProviderTest {

    @Test
    void shouldNotFailedWithoutContext() {
        StatelessValueProvider<Object> statelessProvider = () -> "statelessvalue";
        assertEquals("statelessvalue", statelessProvider.get());
    }
}