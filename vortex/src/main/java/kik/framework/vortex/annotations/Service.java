
package kik.framework.vortex.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Enrique Javier Villar Cea
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Service {

}
