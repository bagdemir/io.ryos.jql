package io.ryos.json.jql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Jql annotation used to annotate the class members to which the Json fields are mapped.
 * @author Erhan Bagdemir
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Jql {

  String expression() default "";
}
