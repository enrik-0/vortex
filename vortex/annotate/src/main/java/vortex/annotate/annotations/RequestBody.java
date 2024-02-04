
package vortex.annotate.annotations;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 *  Gives the request body into the parameter.<br>
 *  <b>GET</b> doesn't have a body.
 *  @see RequestParam
 *  @author Enrique Javier Villar Cea
 */
@Target(PARAMETER)
@Retention(RUNTIME)
@Documented
public @interface RequestBody {

}
