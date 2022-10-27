package io.github.jordanmartin.datagenerator.provider.object;

import io.github.jordanmartin.datagenerator.provider.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.annotation.ProviderCtor;
import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.core.ValueProviderException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Génère une valeur à partir d'une expression.
 * Permet d'utiliser la valeur d'un champ ou générateur faire de la concaténation
 * Ex: "${firstname} ${lastname}"
 */
@Provider(
        name = "Expression",
        description = "Generate a string from others fields",
        examples = {
                "Expression(\"Full name: ${firstName} ${lastName}\") => \"Full name: John Doe\""
        },
        returns = String.class,
        group = "reference"
)
public class Expression implements ValueProvider<String> {

    /**
     * L'expression à évaluer
     */
    private final String expression;

    @ProviderCtor
    public Expression(
            @ProviderArg(description = "Expression containing references to others field with ${fieldName}") String expression
    ) {
        this.expression = expression;
    }

    @Override
    public String getOneWithContext(IObjectProviderContext ctx) {
        // Recherche les variables de la forme "${nom}"
        Pattern pattern = Pattern.compile("(\\$?\\$)\\{([a-zA-Z0-9_-]+)}");
        Matcher matcher = pattern.matcher(expression);
        String result = expression;

        // Pour chaque variable
        while (matcher.find()) {
            String refType = matcher.group(1);
            boolean isFixedRef = "$$".equals(refType);
            String ref = matcher.group(2);

            // Récupère la valeur associé dans les champs existants de l'objet
            Object objectField = ctx.getFieldValue(ref);
            if (objectField == null) {
                // Sinon via les générateurs de references
                objectField = isFixedRef
                        ? ctx.getFixedRefValue(ref)
                        : ctx.getRefValue(ref);
            }

            if (objectField == null) {
                throw new ValueProviderException(this, "The reference \"" + refType + "{" + ref + "}\" doesn't exists");
            }

            // Remplace la variable par sa valeur dans l'expression
            String value = String.valueOf(objectField);
            result = result.replace(refType + "{" + ref + "}", value);
        }

        return result;
    }
}
