
package vortex.annotate.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**  
 * Identifies a method that handles the {@linkplain #value}
 * @see GetMapping
 * @see PostMapping
 * @see PutMapping
 * @Author: Enrique Javier Villar Cea
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface DeleteMapping {

	/**
	 * Uri to handle with <b>DELETE</b> <br>
	 * <h1>example:</h1> <code> @DeleteMapping("/uri")</code>
	 */
	String value();
}
