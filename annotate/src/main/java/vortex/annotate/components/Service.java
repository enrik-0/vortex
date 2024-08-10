
package vortex.annotate.components;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Identifies classes used to implement bussiness logic
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Service {

}
