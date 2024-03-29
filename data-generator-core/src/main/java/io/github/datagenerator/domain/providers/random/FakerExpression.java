package io.github.datagenerator.domain.providers.random;

import com.github.javafaker.Faker;
import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderArg;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import io.github.datagenerator.domain.core.StatelessValueProvider;
import io.github.datagenerator.domain.core.ValueProviderException;

import java.util.Locale;

/**
 * Générateur de donnée à partir d'une expression {@link Faker}
 * <a href="http://dius.github.io/java-faker/apidocs/index.html">http://dius.github.io/java-faker/apidocs/index.html</a>
 */
@Provider(
        name = "Faker",
        description = "Returns a string based on a faker expression (see https://dius.github.io/java-faker)",
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
                    description = "Faker expression",
                    examples = {"Name.firstName", "Internet.emailAddress"}) String expression,
            @ProviderArg(
                    name = "locale",
                    description = "Local to use for the result",
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
                    description = "Faker expression",
                    examples = {"Name.firstName", "Internet.emailAddress"}
            ) String expression) {
        this(expression, Locale.FRANCE);
    }

    @Override
    public String get() {
        try {
            return faker.expression(expression);
        } catch (Exception e) {
            throw new ValueProviderException(this, "Faker expression \"" + expression + "\" is not valid", e);
        }
    }
}
