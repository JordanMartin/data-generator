package io.github.jordanmartin.datagenerator.provider.random;

import com.github.javafaker.Faker;
import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.core.ValueProviderException;
import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;

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
                "Date() => \"2020-11-23T21:42:03.367Z\"",
                "Date(\"2020-01-01\", \"2020-12-31\") => \"2020-10-20T21:42:03.367Z\"",
                "FormatDate(Date(), \"yyyy-MM-dd\") => 2021-03-22"
        },
        groupe = "date"
)
public class RandomDate implements ValueProvider<Date> {

    private final Faker faker = new Faker();
    private ValueProvider<Date> fromProvider;
    private ValueProvider<Date> toProvider;

    /**
     * Par défaut, une date random entre l'année dernière et l'année prochaine courante
     */
    @ProviderCtor("Date aléatoire entre J-365 et J+365 (J = date du jour)")
    public RandomDate() {
        fromProvider = (ctx) -> Date.from(Instant.now().minus(365, ChronoUnit.DAYS));
        toProvider = (ctx) -> Date.from(Instant.now().plus(365, ChronoUnit.DAYS));
    }

    @ProviderCtor("Date aléatoire entre deux dates")
    public RandomDate(
            @ProviderArg(description = "Borne inférieur") Date from,
            @ProviderArg(description = "Borne supérieur") Date to
    ) {
        this.fromProvider = (ctx) -> from;
        this.toProvider = (ctx) -> to;
    }

    @ProviderCtor("Date aléatoire entre deux dates")
    public RandomDate(
            @ProviderArg(description = "Borne inférieur (yyyy-MM-dd)") String from,
            @ProviderArg(description = "Borne supérieur (yyyy-MM-dd)") String to
    ) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.fromProvider = (ctx) -> {
            try {
                return sdf.parse(from);
            } catch (ParseException e) {
                throw new ValueProviderException(this, "Format de date incorrect", e);
            }
        };
        this.toProvider = (ctx) -> {
            try {
                return sdf.parse(to);
            } catch (ParseException e) {
                throw new ValueProviderException(this, "Format de date incorrect", e);
            }
        };
    }

    @ProviderCtor("Date à partir d'une chaine de caratère")
    public RandomDate(
            @ProviderArg(description = "La date au format (yyyy-MM-dd)") String date
    ) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.fromProvider = (ctx) -> {
            try {
                return sdf.parse(date);
            } catch (ParseException e) {
                throw new ValueProviderException(this, "Format de date incorrect", e);
            }
        };
        this.toProvider = null;
    }

    @ProviderCtor("Date aléatoire entre deux dates obtenus d'un autre générateur")
    public RandomDate(
            @ProviderArg(description = "Borne inférieur") ValueProvider<Date> from,
            @ProviderArg(description = "Borne supérieur") ValueProvider<Date> to
    ) {
        this.fromProvider = from;
        this.toProvider = to;
    }

    public RandomDate from(int year, int month, int day) {
        fromProvider = (ctx) -> getDate(year, month, day);
        return this;
    }

    public RandomDate to(int year, int month, int day) {
        toProvider = (ctx) -> getDate(year, month, day);
        return this;
    }

    private Date getDate(int year, int month, int day) {
        Instant instant = LocalDate
                .of(year, month, day)
                .atStartOfDay(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    @Override
    public Date getOneWithContext(IObjectProviderContext ctx) {
        Date from = this.fromProvider.getOneWithContext(ctx);
        if (toProvider == null) {
            return from;
        }
        Date to = this.toProvider.getOneWithContext(ctx);
        return faker.date().between(from, to);
    }
}
