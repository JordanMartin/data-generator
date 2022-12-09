package io.github.datagenerator.domain.providers.transform;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateAddTest {

    @Test
    void shouldAddDuration() {
        DateAdd provider = new DateAdd(ctx -> new Date(0), 10, "SECONDS");
        assertEquals(10_000, provider.get().getTime());
    }

    @Test
    void shouldMinusDuration() {
        DateAdd provider = new DateAdd(ctx -> new Date(20_000), -5, "SECONDS");
        assertEquals(15_000, provider.get().getTime());
    }

    @Test
    void shouldFailWithBadUnit() {
        assertThrows(DateAdd.InvalidUnit.class, () -> new DateAdd(ctx -> new Date(0), 10, "BAD_UNITS"));
    }
}