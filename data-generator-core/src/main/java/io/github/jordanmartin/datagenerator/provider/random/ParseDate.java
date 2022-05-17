package io.github.jordanmartin.datagenerator.provider.random;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.core.ValueProviderException;
import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.Date;

/**
 * Génère une date aléatoire dans un interval donnée
 */
@Provider(
        name = "ParseDate",
        description = "Parse une date à partir d'une chaine de caractère",
        examples = {
                "ParseDate(\"2022-10-10 21:30\", \"yyyy-MM-dd HH:mm\")"
        },
        group = "date"
)
public class ParseDate implements ValueProvider<Date> {

    private final Date date;

    @ProviderCtor("Date à partir d'une chaine de caractère au timezone UTC")
    public ParseDate(
            @ProviderArg(description = "La date au timezone UTC") String date,
            @ProviderArg(description = "Le format de la date",
                    examples = {"yyyy-MM-dd'T'HH:mm:ss.SSSZ", "Documentation complète : https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/format/DateTimeFormatter.html#patterns"}
            ) String format
    ) {
        this(date, format, "UTC");
    }

    @ProviderCtor("Date à partir d'une chaine de caractère et d'un timezone")
    public ParseDate(
            @ProviderArg(description = "La date au timezone définit") String date,
            @ProviderArg(description = "Le format de la date",
                    examples = {"yyyy-MM-dd'T'HH:mm:ss.SSSZ", "Documentation complète : https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/format/DateTimeFormatter.html#patterns"}
            ) String format,
            @ProviderArg(description = "Timezone de la date", examples = {"UTC", "CET", "Europe/Paris", "Europe/Berlin"}) String timezone
    ) {
      this(date, format, ZoneId.of(timezone));
    }

    public ParseDate(String date, String format, ZoneId timezone) {
        try {
            TemporalAccessor parsed = parseWithDefaults(format, date);
            ZonedDateTime zonedDateTime = LocalDateTime.from(parsed).atZone(timezone);
            this.date = new Date(zonedDateTime.toInstant().toEpochMilli());
        } catch (DateTimeException e) {
            throw new ValueProviderException(this, String.format("Impossible de parser la date '%s' avec " +
                    "le format '%s' au timezone '%s'", date, format, timezone), e);
        }
    }

    private static LocalDateTime parseWithDefaults(String pattern, String dateString) {
        TemporalAccessor parsed = DateTimeFormatter.ofPattern(pattern).parse(dateString);
        LocalDateTime result = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        for (TemporalField field : ChronoField.values()) {
            if (parsed.isSupported(field) && result.isSupported(field)) {
                result = result.with(field, parsed.getLong(field));
            }
        }
        return result;
    }

    @Override
    public Date getOneWithContext(IObjectProviderContext ctx) {
        return date;
    }
}
