package io.github.jordanmartin.datagenerator.provider.doc;

import io.github.jordanmartin.datagenerator.provider.core.ValueProvider;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.Provider;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.ProviderArg;
import io.github.jordanmartin.datagenerator.provider.doc.annotation.ProviderCtor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

/**
 * Parse le classe d'un générateur avec ses annotations pour générer la documentation associée
 */
@Slf4j
public class ProviderDocParser {

    public static Optional<ProviderDoc> parse(Class<?> clazz) {
        Provider provider = Optional.ofNullable(clazz.getAnnotation(Provider.class))
                .orElseThrow(() -> new IllegalArgumentException("La classe " + clazz + " ne possède pas l'annotation " + Provider.class));

        ProviderDoc providerDoc = new ProviderDoc();
        providerDoc.setName(provider.name());
        providerDoc.setDescription(provider.description());
        providerDoc.setExamples(provider.examples());

        Arrays.stream(clazz.getConstructors())
                .filter(ctor -> ctor.isAnnotationPresent(ProviderCtor.class) || ctor.getParameters().length == 0)
                .map(ProviderDocParser::getCtor)
                .forEach(providerDoc.constructors::add);

        if (providerDoc.constructors.isEmpty()) {
            log.warn("Le generateur " + clazz + " est ignoré car il ne possède aucun constructeur accessible");
            return Optional.empty();
        }

        // Trie les constructeurs par nombre de paramètre
        providerDoc.constructors.sort(Comparator.comparingInt(value -> value.getArgs().size()));
        return Optional.of(providerDoc);
    }

    /**
     * Génère la documentation constructeur
     */
    private static ProviderCtorDoc getCtor(Constructor<?> ctor) {
        ProviderCtorDoc providerCtorDoc = new ProviderCtorDoc();
        ProviderCtor annotation = ctor.getAnnotation(ProviderCtor.class);
        if (annotation != null) {
            providerCtorDoc.setDescription(annotation.value());
        }

        // Pour chaque argument du constructeur
        Arrays.stream(ctor.getParameters())
                .map(ProviderDocParser::getArgDoc)
                .forEach(providerCtorDoc.args::add);

        return providerCtorDoc;
    }

    /**
     * Génère la documentation d'un argement de constructeur
     */
    private static ProviderArgDoc getArgDoc(Parameter param) {
        // Récupère les information via introspection
        ProviderArgDoc argDoc = new ProviderArgDoc();
        argDoc.setType(getArgType(param));
        argDoc.setName(param.getName());
        argDoc.setExamples(new String[]{});

        // Si annotation présente, remplace les information disponibles depuis celle-ci
        if (param.isAnnotationPresent(ProviderArg.class)) {
            ProviderArg argAnnotation = param.getAnnotation(ProviderArg.class);
            if (!argAnnotation.name().isBlank()) {
                argDoc.setName(argAnnotation.name());
            }
            if (!argAnnotation.description().isBlank()) {
                argDoc.setDescription(argAnnotation.description());
            }
            argDoc.setExamples(argAnnotation.examples());
        }

        return argDoc;
    }

    /**
     * Retourne le type du paramètre
     */
    private static String getArgType(Parameter param) {
        Class<?> type;
        String returnTypeFormat = "%s";
        String returnType;

        // Récupère le raw type d'une classe générique générique (ex ValueProvider<T> => ValueProvider)
        if (ParameterizedType.class.isAssignableFrom(param.getParameterizedType().getClass())) {
            ParameterizedType parameterizedType = (ParameterizedType) param.getParameterizedType();
            type = (Class<?>) parameterizedType.getRawType();
        }
        // Récupère le type d'un tableau. Ex: Integer[] => Integer
        else if (param.getType().isArray()) {
            returnTypeFormat = "%s[]";
            type = param.getType().getComponentType();
        } else {
            type = param.getType();
        }

        if (isIntegerArg(type)) {
            returnType = "integer";
        } else if (isDoubleType(type)) {
            returnType = "double";
        } else if (String.class.isAssignableFrom(type)) {
            returnType = "string";
        } else if (ValueProvider.class.isAssignableFrom(type)) {
            returnType = "generator";
        } else if (type == Object.class) {
            returnType = "any";
        } else {
            returnType = type.getSimpleName();
        }

        return String.format(returnTypeFormat, returnType);
    }

    private static boolean isDoubleType(Class<?> type) {
        return Double.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type);
    }

    private static boolean isIntegerArg(Class<?> type) {
        return type == int.class
                || type == long.class
                || Integer.class.isAssignableFrom(type)
                || Long.class.isAssignableFrom(type);
    }
}
