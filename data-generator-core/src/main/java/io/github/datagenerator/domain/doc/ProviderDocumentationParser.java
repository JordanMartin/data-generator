package io.github.datagenerator.domain.doc;

import io.github.datagenerator.domain.annotation.Provider;
import io.github.datagenerator.domain.annotation.ProviderArg;
import io.github.datagenerator.domain.annotation.ProviderCtor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Parse la classe d'un générateur avec ses annotations pour générer la documentation associée
 */
@Slf4j
public class ProviderDocumentationParser {

    public static Optional<ProviderDoc> parse(Class<?> clazz) {
        Provider providerAnnotation = Optional.ofNullable(clazz.getAnnotation(Provider.class))
                .orElseThrow(() -> new IllegalArgumentException("The class " + clazz + " must have the annotation " + Provider.class));

        ProviderDoc providerDoc = new ProviderDoc();
        providerDoc.setName(clazz.getSimpleName());
        providerDoc.setDescription(providerAnnotation.description());
        providerDoc.setExamples(providerAnnotation.examples());
        providerDoc.setType(getReturnType(clazz));
        if (!providerAnnotation.group().isBlank()) {
            providerDoc.setGroupe(providerAnnotation.group());
        }

        if (!providerAnnotation.name().isBlank()) {
            providerDoc.setName(providerAnnotation.name());
        }

        if (providerAnnotation.returns().length > 0) {
            providerDoc.setType(providerAnnotation.returns()[0].getTypeName());
        }

        Arrays.stream(clazz.getConstructors())
                .filter(ctor -> ctor.isAnnotationPresent(ProviderCtor.class) || ctor.getParameters().length == 0)
                .map(ProviderDocumentationParser::getCtor)
                .forEach(providerDoc.constructors::add);

        if (providerDoc.constructors.isEmpty()) {
            log.warn("Ignoring provider " + clazz + " because it has no available constructor annotated with " + ProviderCtor.class);
            return Optional.empty();
        }

        // Trie les constructeurs par nombre de paramètres
        providerDoc.constructors.sort(Comparator.comparingInt(value -> value.getArgs().size()));
        return Optional.of(providerDoc);
    }

    /**
     * Retourne le type de données renvoyé par le générateur.
     * Cette méthode se base sur le type de retour de la méthode get() et get()
     */
    private static String getReturnType(Class<?> clazz) {
        Type type = Stream.of(clazz.getMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .filter(m -> "get".equals(m.getName()))
                .map(Method::getGenericReturnType)
                .filter(t -> Object.class != t)
                .filter(t -> !(t instanceof TypeVariable))
                .findFirst()
                .orElse(Object.class);
        return getTypeName(type);
    }

    /**
     * Génère la documentation constructeur
     */
    private static ProviderCtorDoc getCtor(Constructor<?> ctor) {
        ProviderCtorDoc providerCtorDoc = new ProviderCtorDoc();
        ProviderCtor ctorAnnotation = ctor.getAnnotation(ProviderCtor.class);
        if (ctorAnnotation != null) {
            providerCtorDoc.setDescription(ctorAnnotation.value());
        }

        // Pour chaque argument du constructeur
        Arrays.stream(ctor.getParameters())
                .map(ProviderDocumentationParser::getArgDoc)
                .forEach(providerCtorDoc.args::add);

        return providerCtorDoc;
    }

    /**
     * Génère la documentation d'un argument de constructeur
     */
    private static ProviderArgDoc getArgDoc(Parameter param) {
        // Récupère les informations via introspection
        ProviderArgDoc argDoc = new ProviderArgDoc();
        argDoc.setType(getTypeName(param));
        argDoc.setName(param.getName());
        argDoc.setExamples(new String[]{});


        // Si annotation présente, remplace les informations disponibles depuis celle-ci
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
    private static String getTypeName(Type type) {
        return type.getTypeName();
    }

    private static String getTypeName(Parameter param) {
        return getTypeName(param.getType());
    }
}
