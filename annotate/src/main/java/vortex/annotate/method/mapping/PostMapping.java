
package vortex.annotate.method.mapping;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Identifies a method that handles the {@linkplain #value}
 * @see DeleteMapping
 * @see GetMapping
 * @see PutMapping 
 * @see HeadMapping
 * @see OptionsMapping
 * @see PatchMapping
 * @see TraceMapping
 * @see ConnectMapping
 * @author Enrique Javier Villar Cea
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface PostMapping {

	/**
	 * Uri to handle with <b>{@linkplain POST} 
	 * <h1>example:</h1> <code> @PostMapping("/uri")</code>
	 */
	String value();
}
