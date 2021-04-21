package fr.jordanmartin.datagenerator.definition;

import fr.jordanmartin.datagenerator.provider.core.ValueProvider;
import fr.jordanmartin.datagenerator.provider.object.Expression;
import fr.jordanmartin.datagenerator.provider.object.FixedReference;
import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;
import fr.jordanmartin.datagenerator.provider.object.Reference;
import fr.jordanmartin.datagenerator.provider.transform.ListOf;
import org.antlr.v4.runtime.*;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Un parseur de definition au format YAML.
 * <p>
 * Le fichier de définition doit avoir la structure :
 * <pre>
 *  references:
 *    ref1: <provider_definition>
 *    ...
 *  template:
 *    field1: <provider_definition>
 *    ...
 * </pre>
 */
public class YamlDefinitionParser extends DefinitionParser {

    /**
     * Contenu de la definition
     */
    private final String definitionContent;

    public YamlDefinitionParser(String definitionContent) {
        this.definitionContent = definitionContent;
    }

    @Override
    public ObjectProvider parse() {
        Definition definition = new Yaml().loadAs(definitionContent, Definition.class);
        return parseDefinition(definition);
    }

    private ObjectProvider parseDefinition(Definition definition) {
        ObjectProvider rootProvider = new ObjectProvider();

        if (definition.getTemplate() == null || definition.getTemplate().isEmpty()) {
            throw new DefinitionException("La définition doit déclarer \"template\" avec au moins un champ");
        }

        if (definition.getReferences() != null && !definition.getReferences().isEmpty()) {
            parseTemplate(definition.getReferences())
                    .forEach(rootProvider::providerRef);
        }

        parseTemplate(definition.getTemplate())
                .forEach(rootProvider::field);

        return rootProvider;
    }

    private Map<String, ValueProvider<?>> parseTemplate(Map<String, Object> fields) {
        // LinkedHashMap pour préserver l'ordre des champs
        Map<String, ValueProvider<?>> providers = new LinkedHashMap<>();

        for (Map.Entry<String, Object> field : fields.entrySet()) {
            String fieldName = field.getKey();
            Object providerDefinition = field.getValue();
            ValueProvider<?> provider = parseProviderDefinition(fieldName, providerDefinition);
            providers.put(fieldName, provider);
        }

        return providers;
    }

    @SuppressWarnings("unchecked")
    private ValueProvider<?> parseProviderDefinition(String fieldName, Object definition) {
        ValueProvider<?> provider;

        if (definition instanceof String) {
            provider = newProviderFromDefinition(fieldName, (String) definition);
        }
        // Si c'est un tableau
        else if (definition instanceof List) {
            List<ValueProvider<?>> providers = ((List<?>) definition).stream()
                    .map(def -> parseProviderDefinition(fieldName, def))
                    .collect(Collectors.toList());
            provider = new ListOf(providers);
        }
        // Si c'est un objet => création d'un sous générateur
        else if (definition instanceof Map) {
            provider = new ObjectProvider();
            parseTemplate((Map<String, Object>) definition)
                    .forEach(((ObjectProvider) provider)::field);
        }
        // Ne devrait jamais arriver
        else {
            throw new DefinitionException("Definition incorrect du champ \"" + fieldName + "\" : " + definition);
        }

        return provider;
    }

    /**
     * Parse la définition d'un générateur avec ANTLR4
     *
     * @param fieldName          Nom du champ
     * @param providerDefinition Le texte de definition du générateur
     */
    private ValueProvider<?> newProviderFromDefinition(String fieldName, String providerDefinition) {
        CharStream in = CharStreams.fromString(providerDefinition);
        ProviderDefintionLexer lexer = new ProviderDefintionLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ProviderDefintionParser parser = new ProviderDefintionParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new DefinitionException("\"" + fieldName + ": " + providerDefinition + "\" at position " + charPositionInLine + " => " + msg);
            }
        });
        DefinitionVisitor visitor = new DefinitionVisitor();
        return (ValueProvider<?>) visitor.visitDefinition(parser.definition());
    }

    /**
     * Visiteur pour créer le générateur à partie du fichier de définition
     */
    private class DefinitionVisitor extends ProviderDefintionBaseVisitor<Object> {
        @Override
        public Object visitFunc(ProviderDefintionParser.FuncContext ctx) {
            String providerName = ctx.func_name().getText();
            Object[] providerParams = (Object[]) visitFunc_params(ctx.func_params());
            return createNewProvider(providerName, providerParams);
        }

        @Override
        public Object visitFunc_params(ProviderDefintionParser.Func_paramsContext ctx) {
            return ctx.func_param().stream()
                    .map(this::visitFunc_param)
                    .toArray();
        }

        @Override
        public Object visitFunc_param(ProviderDefintionParser.Func_paramContext ctx) {
            if (ctx.number() != null) {
                return visitNumber(ctx.number());
            } else if (ctx.func() != null) {
                return visitFunc(ctx.func());
            } else if (ctx.string() != null) {
                return visitString(ctx.string());
            } else if (ctx.list() != null) {
                return visitList(ctx.list());
            } else if (ctx.reference() != null) {
                return visitReference(ctx.reference());
            }

            // Ne devrait jamais arriver
            return notSupportedDefinition(ctx);
        }

        @Override
        public Object visitList(ProviderDefintionParser.ListContext ctx) {
            return ctx.list_element().stream()
                    .map(this::visitList_element)
                    .collect(Collectors.toList());
        }

        @Override
        public Object visitList_element(ProviderDefintionParser.List_elementContext ctx) {
            if (ctx.number() != null) {
                return visitNumber(ctx.number());
            } else if (ctx.func() != null) {
                return visitFunc(ctx.func());
            } else if (ctx.string() != null) {
                return visitString(ctx.string());
            }

            // Ne devrait jamais arriver
            return notSupportedDefinition(ctx);
        }

        @Override
        public Object visitString(ProviderDefintionParser.StringContext ctx) {
            // Supprime les quotes pour ne garder que la chaine de caracètres
            return ctx.getText().substring(1, ctx.getText().length() - 1);
        }

        @Override
        public Object visitNumber(ProviderDefintionParser.NumberContext ctx) {
            if (ctx.Integer() != null) {
                return Integer.parseInt(ctx.getText());
            } else if (ctx.Double() != null) {
                return Double.parseDouble(ctx.getText());
            }
            // Ne devrait jamais arriver
            return notSupportedDefinition(ctx);
        }

        @Override
        public Object visitReference(ProviderDefintionParser.ReferenceContext ctx) {
            if (ctx.standardRef() != null) {
                return visitStandardRef(ctx.standardRef());
            } else if (ctx.fixedRef() != null) {
                return visitFixedRef(ctx.fixedRef());
            } else if (ctx.expression() != null) {
                return visitExpression(ctx.expression());
            }

            // Ne devrait jamais arriver
            return notSupportedDefinition(ctx);
        }

        @Override
        public Object visitFixedRef(ProviderDefintionParser.FixedRefContext ctx) {
            String ref = ctx.Ident().getText();
            return new FixedReference<>(ref);
        }

        @Override
        public Object visitStandardRef(ProviderDefintionParser.StandardRefContext ctx) {
            String ref = ctx.Ident().getText();
            return new Reference<>(ref);
        }

        @Override
        public Object visitExpression(ProviderDefintionParser.ExpressionContext ctx) {
            String expression = (String) visitString(ctx.string());
            return new Expression(expression);
        }

        private Void notSupportedDefinition(ParserRuleContext ctx) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("La definition \"" + ctx.getText() + "\" n'est pas implémenté");
        }
    }
}
