
package vortex.annotate.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * Identifies a method that handles the {@linkplain #value}
 * @see DeleteMapping
 * @see PostMapping
 * @see PutMapping
 * @author Enrique Javier Villar Cea
 * 
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface GetMapping {

	/**
	 * Uri to handle with <b>GET</b>  <br>
	 * Default value <core>"/"</core>
	 * <h1>example:</h1> <code> @GetMapping("/uri")</code>
	 */
	String value() default "/";
}
