package vortex.annotate.method.parameter;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that this parameter have the full http request
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface HttpRequest {

}
