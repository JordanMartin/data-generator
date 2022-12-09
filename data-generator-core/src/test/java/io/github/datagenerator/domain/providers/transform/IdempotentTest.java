package io.github.datagenerator.domain.providers.transform;

import io.github.datagenerator.domain.core.ValueProvider;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IdempotentTest {

    @Test
    void get() {
        ValueProvider<String> provider = ctx -> UUID.randomUUID().toString();
        Idempotent<String> idempotent = new Idempotent<>(provider);

        String first = idempotent.get();
        String second = idempotent.get();

        assertEquals(first, second);
    }
}