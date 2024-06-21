
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
 * @see PostMapping 
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
public @interface PutMapping {

	/**
	 * Uri to handle with <b>{@linkplain PUT}
	 * <h1>example:</h1> <code> @PutMapping("/uri")</code>
	 */
	String value() default "";

	String[] uris() default {};

}
