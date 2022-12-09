package io.github.datagenerator.domain.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Documente les arguments de générateur
 */
@Retention(RUNTIME)
@Target({PARAMETER})
public @interface ProviderArg {
    /**
     * Nom de l'argument. Si non spécifié, le nom de la variable sera utilisé
     */
    String name() default "";

    /**
     * Description de l'argument
     */
    String description() default "";

    /**
     * Exemples d'argument
     */
    String[] examples() default {};
}
