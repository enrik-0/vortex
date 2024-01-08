
package vortex.annotate.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**  
 * @Author: Enrique Javier Villar Cea
 * @see GetMapping
 * @see PostMapping
 * @see PutMapping
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface DeleteMapping {

	/**
	 * Uri to handle with <b>DELETE</b> 
	 */
	String value();
}
