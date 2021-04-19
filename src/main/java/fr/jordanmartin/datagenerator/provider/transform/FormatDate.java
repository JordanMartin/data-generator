package fr.jordanmartin.datagenerator.provider.transform;

import fr.jordanmartin.datagenerator.provider.core.ValueProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Formatte une date
 */
public class FormatDate extends TransformerProvider<Date, String> {

    private final SimpleDateFormat sdf;

    /**
     * @param dateProvider Générateur de date
     * @param format       Format de la date {@link java.text.SimpleDateFormat}
     */
    public FormatDate(ValueProvider<Date> dateProvider, String format) {
        super(dateProvider);
        this.sdf = new SimpleDateFormat(format);
    }

    @Override
    protected String map(Date input) {
        return sdf.format(input);
    }
}
