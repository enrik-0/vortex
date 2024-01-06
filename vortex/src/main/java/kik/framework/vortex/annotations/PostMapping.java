
package kik.framework.vortex.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Enrique Javier Villar Cea
 * @see DeleteMapping
 * @see GetMapping
 * @see PutMapping
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface PostMapping {

	/**
	 * Uri to handle with <b>POST</b> 
	 */
	String value();
}
