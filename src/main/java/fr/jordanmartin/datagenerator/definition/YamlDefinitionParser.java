package fr.jordanmartin.datagenerator.definition;

import fr.jordanmartin.datagenerator.provider.ValueProvider;
import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;
import lombok.SneakyThrows;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.yaml.snakeyaml.Yaml;

import java.lang.reflect.Constructor;
import java.util.*;

public class YamlDefinitionParser extends DefinitionParser {

    private final String definitionContent;

    public YamlDefinitionParser(String definitionContent) {
        this.definitionContent = definitionContent;
    }

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
            parseTemplate(definition.getTemplate())
                    .forEach(rootProvider::field);
        }

        parseTemplate(definition.getTemplate())
                .forEach(rootProvider::field);

        return rootProvider;
    }

    @SuppressWarnings("unchecked")
    private Map<String, ValueProvider<?>> parseTemplate(Map<String, Object> template) {
        Map<String, ValueProvider<?>> providers = new HashMap<>();

        for (Map.Entry<String, Object> field : template.entrySet()) {
            String fieldName = field.getKey();
            Object providerDefinition = field.getValue();
            ValueProvider<?> provider;

            // Création du générateur à partir de la definition
            if (providerDefinition instanceof String) {
                provider = newProviderFromDefinition((String) providerDefinition);
            }
            // Si c'est un objet => création d'un sous générateur
            else if (providerDefinition instanceof Map) {
                provider = new ObjectProvider();
                parseTemplate((Map<String, Object>) providerDefinition)
                        .forEach(((ObjectProvider) provider)::field);
            }
            // Ne devrait jamais arriver
            else {
                throw new DefinitionException("Definition incorrect du champ \"" + fieldName + "\" : " + providerDefinition);
            }

            providers.put(fieldName, provider);
        }

        return providers;
    }

    private ValueProvider<?> newProviderFromDefinition(String providerDefinition) {
        CharStream in = CharStreams.fromString(providerDefinition);
        ProviderDefintionLexer lexer = new ProviderDefintionLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ProviderDefintionParser parser = new ProviderDefintionParser(tokens);
        DefinitionVisitor visitor = new DefinitionVisitor();
        return (ValueProvider<?>) visitor.visitDefinition(parser.definition());
    }

    private class DefinitionVisitor extends ProviderDefintionBaseVisitor<Object> {
        @SneakyThrows
        @Override
        public Object visitFunc(ProviderDefintionParser.FuncContext ctx) {
            String provider = ctx.func_name().getText();
            Object[] providerParams = (Object[]) visitFunc_params(ctx.func_params());

            Class<?> classProvider = defaultProvider.get(provider);

            if (classProvider == null) {
                // TODO écrire la liste des générateurs dispo
                throw new DefinitionException("Le générateur \"" + provider + "\" n'existe pas");
            }

            Class<?>[] paramsClass = Arrays.stream(providerParams)
                    .map(Object::getClass)
                    .toArray(Class<?>[]::new);

            Constructor<?> constructor = Arrays.stream(classProvider.getConstructors())
                    .filter(c -> {
                        Class<?>[] types = c.getParameterTypes();
                        // Aucun argument
                        if (types.length == paramsClass.length && types.length == 0) {
                            return true;
                        }

                        // Nombre différent de paramètres
                        if (types.length != paramsClass.length) {
                            return false;
                        }

                        // Vérifie si le type des paramètres correspond
                        for (int i = 0; i < types.length; i++) {
                            if (types[i].isAssignableFrom(paramsClass[i])) {
                                return true;
                            } else if (types[i] == int.class && paramsClass[i] == Integer.class
                                    || types[i] == Integer.class && paramsClass[i] == int.class) {
                                return true;
                            } else if (types[i] == double.class && paramsClass[i] == Double.class
                                    || types[i] == Double.class && paramsClass[i] == double.class) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .findFirst().orElseThrow(() ->
                            new DefinitionException("Les paramètres du générateur \"" + provider + "\" sont incorrectes"));

            return constructor.newInstance(providerParams);
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
                // Supprime les quotes
                return ctx.getText().substring(1, ctx.getText().length() - 1);
            }
            return null;
        }

        @Override
        public Object visitNumber(ProviderDefintionParser.NumberContext ctx) {
            if (ctx.Integer() != null) {
                return Integer.parseInt(ctx.getText());
            } else if (ctx.Double() != null) {
                return Double.parseDouble(ctx.getText());
            }
            return null;
        }
    }

    private class DefinitionListener extends ProviderDefintionBaseListener {

        ValueProvider<?> rootProvider;
        String currentProvider;
        List<Object> currentProviderParams;

        @Override
        public void exitFunc_name(ProviderDefintionParser.Func_nameContext ctx) {
            currentProvider = ctx.getText();
            currentProviderParams = new ArrayList<>();
            System.out.println(currentProvider);
        }

        @Override
        public void exitFunc_param(ProviderDefintionParser.Func_paramContext ctx) {
            Object param = null;
            if (ctx.number() != null) {
                if (ctx.number().Integer() != null) {
                    param = Integer.parseInt(ctx.getText());
                } else if (ctx.number().Double() != null) {
                    param = Double.parseDouble(ctx.getText());
                }
            } else if (ctx.func() != null) {
                param = "function";
            } else if (ctx.string() != null) {
                // Supprime les quotes
                param = ctx.getText().substring(1, ctx.getText().length() - 1);
            }

            currentProviderParams.add(param);
            System.out.println(param);
        }

        public ValueProvider<?> buildProvider() {
            Class<? extends ValueProvider<?>> providerClass = defaultProvider.get(currentProvider);
            System.out.println(providerClass);
            return null;
        }

        public ValueProvider<?> getRootProvider() {
            return rootProvider;
        }
    }
}
