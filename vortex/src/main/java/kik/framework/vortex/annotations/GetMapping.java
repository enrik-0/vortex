
package kik.framework.vortex.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Enrique Javier Villar Cea
 * @see DeleteMapping
 * @see PostMapping
 * @see PutMapping
 * 
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface GetMapping {

	/**
	 * Uri to handle with <b>GET</b> 
	 * Default value <core>"/"</core>
	 */
	String value() default "/";
}
