
package vortex.annotate.method.mapping;

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
 * @see DeleteMapping
 * @see HeadMapping
 * @see PatchMapping
 * @see TraceMapping
 * @Author: Enrique Javier Villar Cea
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface OptionsMapping {

	/**
	 * Uri to handle with <b>{@linkplain OPTIONS} <br>
	 * <h1>example:</h1> <code> @OptionsMapping("/uri")</code>
	 */
	String value() default "";

	String[] uris() default {};
}
