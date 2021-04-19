package fr.jordanmartin.datagenerator.provider;

import fr.jordanmartin.datagenerator.provider.base.ValueProvider;
import fr.jordanmartin.datagenerator.provider.transform.Idempotent;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ValueProviderTest {

    @Test
    void getOne() {
        ValueProvider<String> provider = (objectContext) -> "a";
        assertEquals("a", provider.getOne());
    }

    @Test
    void testGetList() {
        ValueProvider<String> provider = (objectContext) -> "a";
        List<String> list = provider.getList(10);
        assertEquals(10, list.size());
    }

    @Test
    void testGetStream() {
        ValueProvider<String> provider = (objectContext) -> "a";
        assertEquals(10, provider.getStream(10).count());
        provider.getStream(10).forEach(value -> assertEquals("a", value));
    }

    @Test
    void multipleGet() {
        ValueProvider<String> provider = (objectContext) -> UUID.randomUUID().toString();
        String first = provider.getOne();
        String second = provider.getOne();
        assertNotEquals(first, second);
    }

    @Test
    void idempotent() {
        ValueProvider<String> provider = (objectContext) -> UUID.randomUUID().toString();
        Idempotent<String> idempotentProvider = provider.idempotent();
        String first = idempotentProvider.getOne();
        String second = idempotentProvider.getOne();
        assertEquals(first, second);
    }
}