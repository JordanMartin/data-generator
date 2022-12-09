package io.github.datagenerator.domain.providers.transform;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderArg;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.ValueProvider;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Formate une date
 */
@Provider(
        name = "FormatDate",
        description = "Format a date",
        examples = {
                "FormatDate(Now(), \"yyyy-MM-dd HH:mm:ss.SSSXXX\") => \"2022-05-17 18:22:34.253Z\"",
                "FormatDate(Now(), \"yyyy-MM-dd HH:mm:ss.SSSXXX\", \"Europe/Paris\") => \"2022-05-17 20:22:34.253+02:00\""
        },
        returns = String.class,
        group = "date"
)
public class FormatDate extends TransformerProvider<Date, String> {

    private final DateTimeFormatter formatter;
    private final ZoneId timezone;

    /**
     * @param dateProvider Générateur de date
     * @param format       Format de la date {@link DateTimeFormatter}
     */
    @ProviderCtor("Format a date at the specified timezone")
    public FormatDate(
            @ProviderArg(
                    description = "Provider of Date",
                    examples = "Now()"
            ) ValueProvider<Date> dateProvider,
            @ProviderArg(
                    name = "format",
                    description = "Date format as defined by java.time.DateTimeFormatter",
                    examples = {"yyyy-MM-dd'T'HH:mm:ss.SSSZ", "Full documentation : https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/format/DateTimeFormatter.html#patterns"}
            ) String format
    ) {
        this(dateProvider, format, "UTC");
    }

    @ProviderCtor("Format a date in the specified timezone")
    public FormatDate(
            @ProviderArg(
                    description = "Provider of Date",
                    examples = "Now()"
            ) ValueProvider<Date> dateProvider,
            @ProviderArg(
                    name = "format",
                    description = "Date format as defined by java.time.DateTimeFormatter",
                    examples = {"yyyy-MM-dd'T'HH:mm:ss.SSSZ", "Full documentation : https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/format/DateTimeFormatter.html#patterns"}
            ) String format,
            @ProviderArg(description = "Timezone de la date", examples = {"UTC", "CET", "Europe/Paris", "Europe/Berlin"}) String timezone) {
        super(dateProvider);
        this.formatter = DateTimeFormatter.ofPattern(format);
        this.timezone = ZoneId.of(timezone);
    }

    @Override
    protected String map(Date input) {
        ZonedDateTime date = input.toInstant().atZone(timezone);
        return formatter.format(date);
    }
}
