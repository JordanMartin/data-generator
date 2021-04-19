package fr.jordanmartin.datagenerator.provider.transform;

import fr.jordanmartin.datagenerator.provider.core.ValueProvider;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IdempotentTest {

    @Test
    void getOne() {
        ValueProvider<String> provider = (obectContext) -> UUID.randomUUID().toString();
        Idempotent<String> idempotent = new Idempotent<>(provider);

        String first = idempotent.getOne();
        String second = idempotent.getOne();

        assertEquals(first, second);
    }
}