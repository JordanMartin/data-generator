package io.github.jordanmartin.datagenerator.provider.transform;

import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FormatDateTest {

    @Test
    void getOne() {
        String dateFormat = "yyyy-MM-dd HH:mm";
        String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}";

        StatelessValueProvider<Date> dateProvider = Date::new;
        FormatDate formattedDateProvider = new FormatDate(dateProvider, dateFormat);
        Assertions.assertTrue(formattedDateProvider.getOne().matches(dateRegex));
    }
}