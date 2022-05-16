package io.github.jordanmartin.datagenerator.provider.random;

import io.github.jordanmartin.datagenerator.provider.core.ValueProviderException;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParseDateTest {

    @Test
    void shouldParseUTCDate() {
        ParseDate provider = new ParseDate("2020 10 12 13:12+00:00", "yyyy MM dd HH:mmX");
        OffsetDateTime date = provider.getOne().toInstant().atOffset(ZoneOffset.UTC);
        assertEquals(2020, date.getYear());
        assertEquals(10, date.getMonthValue());
        assertEquals(12, date.getDayOfMonth());
        assertEquals(13, date.getHour());
        assertEquals(12, date.getMinute());
    }

    @Test
    void shouldFailedWhenDateIsInvalid() {
        ValueProviderException exception = assertThrows(ValueProviderException.class, () -> new ParseDate("bad date", "yyyy-MM-dd"));
        assertEquals(ParseException.class, exception.getCause().getClass());
    }
}