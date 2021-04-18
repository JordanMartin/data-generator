package fr.jordanmartin.datagenerator.provider.random;

import com.github.javafaker.Faker;
import fr.jordanmartin.datagenerator.provider.base.ValueProvider;
import fr.jordanmartin.datagenerator.provider.base.ValueProviderException;

import java.util.Locale;

/**
 * Générateur de donnée à partir d'une expression {@link Faker}
 * http://dius.github.io/java-faker/apidocs/index.html
 */
public class Sample implements ValueProvider<String> {

    private final Faker faker;
    private final String expression;

    /**
     * @param expression Expression Faker
     * @param locale     La langue à utiliser
     */
    public Sample(String expression, Locale locale) {
        if (!expression.contains("#")) {
            this.expression = "#{" + expression.trim() + "}";
        } else {
            this.expression = expression;
        }
        this.faker = new Faker(locale);
    }

    /**
     * @param expression Expression Faker
     * @param locale     La langue à utiliser (ex: fr, fr-CA, en, it, de, ...)
     */
    public Sample(String expression, String locale) {
        this(expression, new Locale(locale));
    }

    /**
     * Utilise la locale {@code Locale.FRANCE} pour Faker
     *
     * @param expression Expression Faker
     */
    public Sample(String expression) {
        this(expression, Locale.FRANCE);
    }

    @Override
    public String getOne() {
        try {
            return faker.expression(expression);
        } catch (Exception e) {
            throw new ValueProviderException(this, "L'expression Faker \"" + expression + "\" est incorrecte", e);
        }
    }
}
