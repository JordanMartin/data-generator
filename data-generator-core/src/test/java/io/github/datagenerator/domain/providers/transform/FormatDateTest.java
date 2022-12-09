package io.github.datagenerator.domain.providers.transform;

import io.github.datagenerator.domain.core.StatelessValueProvider;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FormatDateTest {

    @Test
    void get() {
        String dateFormat = "yyyy-MM-dd HH:mm";
        String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}";

        StatelessValueProvider<Date> dateProvider = Date::new;
        FormatDate formattedDateProvider = new FormatDate(dateProvider, dateFormat);
        assertTrue(formattedDateProvider.get().matches(dateRegex));
    }
}