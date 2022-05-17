package io.github.jordanmartin.datagenerator.provider.random;

import io.github.jordanmartin.datagenerator.provider.core.ValueProviderException;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParseDateTest {

    @Test
    void shouldParseUTCDate() {
        ParseDate provider = new ParseDate("2020 10 12 13:12+00:00", "yyyy MM dd HH:mmXXX");
        OffsetDateTime date = provider.getOne().toInstant().atOffset(ZoneOffset.UTC);
        assertEquals(2020, date.getYear());
        assertEquals(10, date.getMonthValue());
        assertEquals(12, date.getDayOfMonth());
        assertEquals(13, date.getHour());
        assertEquals(12, date.getMinute());
    }
    @Test
    void shouldParseEuropeParisDate() {
        ParseDate provider = new ParseDate("2020 10 12 13:12+00:00", "yyyy MM dd HH:mmXXX", "Europe/Paris");
        ZonedDateTime date = provider.getOne().toInstant().atZone(ZoneId.of("Europe/Paris"));
        assertEquals(2020, date.getYear());
        assertEquals(10, date.getMonthValue());
        assertEquals(12, date.getDayOfMonth());
        assertEquals(13, date.getHour());
        assertEquals(12, date.getMinute());
    }

    @Test
    void    shouldFailedWhenDateIsInvalid() {
        ValueProviderException exception = assertThrows(ValueProviderException.class, () -> new ParseDate("bad date", "yyyy-MM-dd"));
        assertEquals(DateTimeParseException.class, exception.getCause().getClass());
    }
}