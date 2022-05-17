package io.github.jordanmartin.datagenerator.provider.transform;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Formate une date
 */
@Provider(
        name = "FormatDate",
        description = "Formate une date",
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
    @ProviderCtor("Formate une date. La date sera interprétée au timezone UTC")
    public FormatDate(
            @ProviderArg(
                    name = "generateur",
                    description = "Un générateur retournant un type Date",
                    examples = "Now()"
            ) ValueProvider<Date> dateProvider,
            @ProviderArg(
                    name = "format",
                    description = "Un format de date tel que définit par java.time.DateTimeFormatter",
                    examples = {"yyyy-MM-dd'T'HH:mm:ss.SSSZ", "Documentation complète : https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/format/DateTimeFormatter.html#patterns"}
            ) String format
    ) {
        this(dateProvider, format, "UTC");
    }

    @ProviderCtor("Formate une date dans un timezone spécifié")
    public FormatDate(
            @ProviderArg(
                    name = "generateur",
                    description = "Un générateur retournant un type Date",
                    examples = "Now()"
            ) ValueProvider<Date> dateProvider,
            @ProviderArg(
                    name = "format",
                    description = "Un format de date tel que définit par java.time.DateTimeFormatter",
                    examples = {"yyyy-MM-dd'T'HH:mm:ss.SSSZ", "Documentation complète : https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/format/DateTimeFormatter.html#patterns"}
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
