package io.github.jordanmartin.datagenerator.provider.transform;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Formatte une date
 */
@Provider(
        name = "FormatDate",
        description = "Formatte une date",
        examples = {"FormatDate(CurrentDate(), \"yyyy-MM-dd HH:mm:ss.SSS\")"},
        returns = String.class
)
public class FormatDate extends TransformerProvider<Date, String> {

    private final SimpleDateFormat sdf;

    /**
     * @param dateProvider Générateur de date
     * @param format       Format de la date {@link java.text.SimpleDateFormat}
     */
    @ProviderCtor
    public FormatDate(
            @ProviderArg(
                    name = "generateur",
                    description = "Un générateur retournant un type Date",
                    examples = "CurrentDate()"
            ) ValueProvider<Date> dateProvider,
            @ProviderArg(
                    name = "format",
                    description = "un format de date telle que définit par java.text.SimpleDateFormat",
                    examples = {"yyyy-MM-dd HH:mm:ss.SSS"}
            ) String format
    ) {
        super(dateProvider);
        this.sdf = new SimpleDateFormat(format);
    }

    @Override
    protected String map(Date input) {
        return sdf.format(input);
    }
}
