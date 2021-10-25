package io.github.jordanmartin.datagenerator.provider.random;

import com.github.javafaker.Faker;
import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;
import io.github.jordanmartin.datagenerator.provider.core.ValueProviderException;

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
@Provider(
        name = "Date",
        description = "Retourne une date aléatoire",
        examples = {
                "Date() => 1616446668993",
                "FormatDate(Date(), \"yyyy-MM-dd\") => 2021-03-22"
        }
)
public class RandomDate implements StatelessValueProvider<Date> {

    private final Faker faker = new Faker();
    private Date from;
    private Date to;

    /**
     * Par défaut, une date random entre l'année dernière et l'année prochaine courante
     */
    @ProviderCtor("Date aléatoire entre J-365 et J+365 (J = date du jour)")
    public RandomDate() {
        from = Date.from(Instant.now().minus(365, ChronoUnit.DAYS));
        to = Date.from(Instant.now().plus(365, ChronoUnit.DAYS));
    }

    @ProviderCtor("Date aléatoire entre deux dates")
    public RandomDate(
            @ProviderArg(description = "Borne inférieur") Date from,
            @ProviderArg(description = "Borne supérieur") Date to
    ) {
        this.from = from;
        this.to = to;
    }

    @ProviderCtor("Date aléatoire entre deux dates")
    public RandomDate(
            @ProviderArg(description = "Borne inférieur (yyyy-MM-dd)") String from,
            @ProviderArg(description = "Borne supérieur (yyyy-MM-dd)") String to
    ) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.from = sdf.parse(from);
            this.to = sdf.parse(to);
        } catch (ParseException e) {
            throw new ValueProviderException(this, "Format de date incorrect", e);
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
