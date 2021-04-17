package fr.jordanmartin.datagenerator.provider.random;

import com.github.javafaker.Faker;
import fr.jordanmartin.datagenerator.provider.base.ValueProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Génère une date aléatoire dans un interval donnée
 */
public class RandomDate implements ValueProvider<Date> {

    private final Faker faker = new Faker();
    private Date from;
    private Date to;

    /**
     * Par défaut, une date random entre l'année dernière et l'année prochaine courante
     */
    public RandomDate() {
        from = Date.from(Instant.now().minus(365, ChronoUnit.DAYS));
        to = Date.from(Instant.now().plus(365, ChronoUnit.DAYS));
    }

    public RandomDate(Date from, Date to) {
        this.from = from;
        this.to = to;
    }

    public RandomDate(String from, String to) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.from = sdf.parse(from);
            this.to = sdf.parse(to);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public RandomDate from(int year, int month, int day) {
        from = getDate(year, month, day);
        return this;
    }

    public RandomDate to(int year, int month, int day) {
        to = getDate(year, month, day);
        return this;
    }

    private Date getDate(int year, int month, int day) {
        Instant instant = LocalDate
                .of(year, month, day)
                .atStartOfDay(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    @Override
    public Date getOne() {
        return faker.date().between(from, to);
    }
}
