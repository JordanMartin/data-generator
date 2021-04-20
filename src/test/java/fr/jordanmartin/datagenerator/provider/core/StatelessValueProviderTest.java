package fr.jordanmartin.datagenerator.provider.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatelessValueProviderTest {

    @Test
    void shouldNotFailedWithoutContext() {
        StatelessValueProvider<Object> statelessProvider = new StatelessValueProvider<>() {
            @Override
            public Object getOne() {
                return "statelessvalue";
            }
        };

        assertEquals("statelessvalue", statelessProvider.getOne());
    }
}