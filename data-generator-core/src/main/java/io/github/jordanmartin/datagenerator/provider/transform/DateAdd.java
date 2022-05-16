package io.github.jordanmartin.datagenerator.provider.transform;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.List;

import static java.time.temporal.ChronoUnit.*;

/**
 * Ajout une durée à une date
 */
@Provider(
        name = "DateAdd",
        description = "Ajoute une durée à une date",
        examples = {"DateAdd(Now(), 1, \"HOURS\")"},
        returns = Date.class,
        group = "date"
)
public class DateAdd extends TransformerProvider<Date, Date> {

    public static final List<TemporalUnit> ALLOWED_UNITS = List.of(
            NANOS, MICROS, MILLIS, SECONDS, MINUTES, HOURS, DAYS, WEEKS, MONTHS, YEARS
    );

    private final long duration;
    private final ChronoUnit unit;

    @ProviderCtor
    public DateAdd(
            @ProviderArg(
                    name = "generateur",
                    description = "Un générateur retournant un type Date",
                    examples = "Now()"
            ) ValueProvider<Date> dateProvider,
            @ProviderArg(
                    name = "durée",
                    description = "Durée à ajouter/retrancher"
            ) long duration,
            @ProviderArg(
                    name = "unité",
                    description = "Unité de la durée : NANOS, MICROS, MILLIS, SECONDS, MINUTES, HOURS, DAYS, WEEKS, MONTHS, YEARS"
            ) String unit
    ) {
        this(dateProvider, duration, getUnit(unit));
    }

    private static ChronoUnit getUnit(String unit) {
        try {
            return ChronoUnit.valueOf(unit);
        } catch (IllegalArgumentException e) {
            throw new InvalidUnit(unit);
        }
    }

    public DateAdd(ValueProvider<Date> dateProvider, long duration, ChronoUnit unit) {
        super(dateProvider);
        this.duration = duration;
        this.unit = unit;
    }

    @Override
    protected Date map(Date input) {
        Instant result = input.toInstant().plus(duration, unit);
        return new Date(result.toEpochMilli());
    }

    @AllArgsConstructor
    public static class InvalidUnit extends RuntimeException {
        private String unit;

        @Override
        public String getMessage() {
            return String.format("L'unité \"%s\" est invalide. Les unités supportées sont : NANOS, MICROS, MILLIS, " +
                    "SECONDS, MINUTES, HOURS, DAYS, WEEKS, MONTHS, YEARS", unit);
        }
    }
}
