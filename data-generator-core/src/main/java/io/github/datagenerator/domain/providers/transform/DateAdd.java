package io.github.datagenerator.domain.providers.transform;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderArg;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.ValueProvider;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Ajout une durée à une date
 */
@Provider(
        name = "DateAdd",
        description = "Add a duration to a date",
        examples = {"DateAdd(Now(), 1, \"HOURS\")"},
        returns = Date.class,
        group = "date"
)
public class DateAdd extends TransformerProvider<Date, Date> {

    private final long duration;
    private final ChronoUnit unit;

    @ProviderCtor
    public DateAdd(
            @ProviderArg(
                    description = "Provider of Date",
                    examples = "Now()"
            ) ValueProvider<Date> dateProvider,
            @ProviderArg(description = "Amount to add or subtract (if negative)") long amount,
            @ProviderArg(
                    description = "Amount unit : NANOS, MICROS, MILLIS, SECONDS, MINUTES, HOURS, HALF_DAYS, DAYS"
            ) String unit
    ) {
        this(dateProvider, amount, getUnit(unit));
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
            return String.format("Unit \"%s\" is not valid. Available units are %s",
                    unit, "NANOS, MICROS, MILLIS, SECONDS, MINUTES, HOURS, HALF_DAYS, DAYS");
        }
    }
}
