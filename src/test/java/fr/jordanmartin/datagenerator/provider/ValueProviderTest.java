package fr.jordanmartin.datagenerator.provider;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ValueProviderTest {

    @Test
    void getOne() {
        ValueProvider<String> provider = () -> "a";
        assertEquals("a", provider.getOne());
    }

    @Test
    void testGetList() {
        ValueProvider<String> provider = () -> "a";
        List<String> list = provider.getList(10);
        assertEquals(10, list.size());
    }

    @Test
    void testGetStream() {
        ValueProvider<String> provider = () -> "a";
        assertEquals(10, provider.getStream(10).count());
        provider.getStream(10).forEach(value -> assertEquals("a", value));
    }

    @Test
    void multipleGet() {
        ValueProvider<String> provider = () -> UUID.randomUUID().toString();
        String first = provider.getOne();
        String second = provider.getOne();
        assertNotEquals(first, second);
    }

    @Test
    void idempotent() {
        ValueProvider<String> provider = () -> UUID.randomUUID().toString();
        ValueProvider<String> idempotentProvider = provider.idempotent();
        String first = idempotentProvider.getOne();
        String second = idempotentProvider.getOne();
        assertEquals(first, second);
    }
}