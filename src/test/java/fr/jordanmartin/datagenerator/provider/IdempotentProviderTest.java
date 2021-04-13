package fr.jordanmartin.datagenerator.provider;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IdempotentProviderTest {

    @Test
    void getOne() {
        ValueProvider<String> provider = () -> UUID.randomUUID().toString();
        IdempotentProvider<String> idempotentProvider = new IdempotentProvider<>(provider);

        String first = idempotentProvider.getOne();
        String second = idempotentProvider.getOne();

        assertEquals(first, second);
    }
}