package io.github.jordanmartin.datagenerator.provider.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Identifie et documente les constructeurs de générateur de données
 */
@Retention(RUNTIME)
@Target({CONSTRUCTOR})
public @interface ProviderCtor {
    /**
     * Description du constructeur
     */
    String value() default "";
}
