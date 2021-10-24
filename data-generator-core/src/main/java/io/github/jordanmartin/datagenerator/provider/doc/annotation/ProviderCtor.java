package io.github.jordanmartin.datagenerator.provider.doc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({CONSTRUCTOR})
public @interface ProviderCtor {
    String value() default "";
}
