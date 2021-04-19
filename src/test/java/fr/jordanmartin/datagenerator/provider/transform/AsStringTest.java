package fr.jordanmartin.datagenerator.provider.transform;

import fr.jordanmartin.datagenerator.provider.core.ValueProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AsStringTest {

    @Test
    void getOne() {
        ValueProvider<Integer> provider = (ctx) -> 1;
        AsString<Integer> asStringProvider = new AsString<>(provider);
        assertEquals("1", asStringProvider.getOneWithContext(null));
    }
}