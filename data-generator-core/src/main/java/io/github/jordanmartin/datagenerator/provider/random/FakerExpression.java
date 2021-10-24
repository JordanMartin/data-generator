package io.github.jordanmartin.datagenerator.provider.random;

import com.github.javafaker.Faker;
import io.github.jordanmartin.datagenerator.provider.core.StatelessValueProvider;
import io.github.jordanmartin.datagenerator.provider.core.ValueProviderException;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.ProviderCtor;

import java.util.Locale;

/**
 * Générateur de donnée à partir d'une expression {@link Faker}
 * http://dius.github.io/java-faker/apidocs/index.html
 */
@Provider(
        name = "Faker",
        description = "Retourne une valeur à partir d'une expression Faker (https://dius.github.io/java-faker)",
        examples = {
                "Faker(\"Name.firstName\") => \"Pierre\"",
                "Faker(\"Address.city\") => \"Aulnay-sous-Bois\""
        }
)
public class FakerExpression implements StatelessValueProvider<String> {

    private final Faker faker;
    private final String expression;

    /**
     * @param expression Expression Faker
     * @param locale     La langue à utiliser
     */
    public FakerExpression(String expression, Locale locale) {
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
    @ProviderCtor
    public FakerExpression(
            @ProviderArg(
                    name = "expression",
                    description = "L'expression Faker",
                    examples = {"Name.firstName", "Internet.emailAddress"}) String expression,
            @ProviderArg(
                    name = "locale",
                    description = "La locale de la valeur générée",
                    examples = {"fr", "fr-CA", "en", "it"}
            ) String locale) {
        this(expression, new Locale(locale));
    }

    /**
     * Utilise la locale {@code Locale.FRANCE} pour Faker
     *
     * @param expression Expression Faker
     */
    @ProviderCtor
    public FakerExpression(
            @ProviderArg(
                    name = "expression",
                    description = "L'expression Faker",
                    examples = {"Name.firstName", "Internet.emailAddress"}
            ) String expression) {
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
