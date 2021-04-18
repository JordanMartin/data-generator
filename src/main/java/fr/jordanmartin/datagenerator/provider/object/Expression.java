package fr.jordanmartin.datagenerator.provider.object;

import fr.jordanmartin.datagenerator.provider.base.ValueProvider;
import fr.jordanmartin.datagenerator.provider.base.ValueProviderException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Génère une valeur à partir d'une expression.
 * Permet d'utiliser la valeur d'un champ ou générateur faire de la concaténation
 * Ex: "${firstname} ${lastname}"
 */
public class Expression implements ValueProvider<ObjectContextHandler<String>> {

    /**
     * L'expression à évaluer
     */
    private final String expression;

    public Expression(String expression) {
        this.expression = expression;
    }

    @Override
    public ObjectContextHandler<String> getOne() {
        return ctx -> {
            // Recherche les variables de la forme "${nom}"
            Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z0-9_-]+)\\}");
            Matcher matcher = pattern.matcher(expression);
            String result = expression;

            // Pour chaque variable
            while (matcher.find()) {
                String ref = matcher.group(1);

                // Récupère la valeur associé dans les champs existant de l'objet
                Object objectField = ctx.getFieldValue(ref);
                if (objectField == null) {
                    // Sinon les génrateurs de references
                    objectField = ctx.getRefProviderValue(ref);
                }

                if (objectField == null) {
                    throw new ValueProviderException(this, "La référence \"${" + ref + "}\" n'existe pas");
                }

                // Remplace la variable par sa valeur dans l'expression
                String value = String.valueOf(objectField);
                result = result.replace("${" + ref + "}", value);
            }

            return result;
        };
    }

}
