package io.github.datagenerator.domain.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Identifie un générateur de données
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface Provider {

    /**
     * Nom du générateur. Si non spécifié, le nom de la classe sera utilisé
     */
    String name() default "";

    /**
     * Description du générateur
     */
    String description();

    /**
     * Exemple d'utilisation du générateur
     */
    String[] examples() default {};

    /**
     * Type de retour du générateur
     */
    Class[] returns() default {};

    /**
     * Nom pour le regroupement des générateurs
     */
    String group() default "";
}
