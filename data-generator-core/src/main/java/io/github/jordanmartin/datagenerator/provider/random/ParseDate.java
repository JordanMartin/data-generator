package io.github.jordanmartin.datagenerator.provider.random;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.core.ValueProviderException;
import io.github.jordanmartin.datagenerator.provider.object.IObjectProviderContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Génère une date aléatoire dans un interval donnée
 */
@Provider(
        name = "ParseDate",
        description = "Parse une date à partir d'une chaine de caractère de de son format",
        examples = {
                "ParseDate(\"2022-10-10 21:30\", \"yyyy-MM-dd HH:mm\")"
        },
        group = "date"
)
public class ParseDate implements ValueProvider<Date> {

    private final Date date;

    @ProviderCtor("Date à partir d'une chaine de caractère")
    public ParseDate(
            @ProviderArg(description = "La date") String date,
            @ProviderArg(description = "Le format de la date", examples = "yyyy-MM-dd HH:mm") String format
    ) {
        try {
            this.date = new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            throw new ValueProviderException(this, "Format de date incorrect", e);
        }
    }

    @Override
    public Date getOneWithContext(IObjectProviderContext ctx) {
        return date;
    }
}
