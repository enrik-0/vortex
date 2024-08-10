package vortex.annotate.method.parameter;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import vortex.annotate.constants.HttpMethod;

/**
 * This get the requested header, this is always a string with the name of the header,
 * from the request, 
 * this can be a List of String or a single String depending 
 * on the number of values that header have
 */
@Retention(RUNTIME)
@Target(PARAMETER)
@Documented
public @interface Header {

    /**
     * name of the wanted header
     * @return  name of the wanted header
     */
    String value();
}