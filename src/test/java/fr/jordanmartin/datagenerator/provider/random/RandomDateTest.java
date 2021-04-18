package fr.jordanmartin.datagenerator.provider.random;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomDateTest {

    @Test
    void getOneDateConstructor() {
        Date min = getDate("2000-01-01");
        Date max = getDate("2000-01-02");

        RandomDate randomDateProvider = new RandomDate(min, max);

        for (int i = 0; i < 10; i++) {
            Date aDate = randomDateProvider.getOne();
            assertTrue(aDate.compareTo(min) >= 0);
            assertTrue(aDate.compareTo(max) <= 0);
        }
    }

    @Test
    void getOneIntConstructor() {
        Date min = getDate("2000-12-01");
        Date max = getDate("2000-12-31");

        RandomDate randomDateProvider = new RandomDate()
                .from(2000, 12, 1)
                .to(2000, 12, 31);

        for (int i = 0; i < 10; i++) {
            Date aDate = randomDateProvider.getOne();
            assertTrue(aDate.compareTo(min) >= 0);
            assertTrue(aDate.compareTo(max) <= 0);
        }
    }

    private Date getDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}