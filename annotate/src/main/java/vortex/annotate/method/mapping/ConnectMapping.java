
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
 * @see OptionsMapping
 * @see PatchMapping
 * @see TraceMapping
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface ConnectMapping {

	/**
	 * Uri to handle with <b>CONNECT </b> method 
	 * <h4> Example </h4>
	 *  <code> @ConnectMapping("/uri")</code>
	 *  @return the uri
	 */
	String value() default "";

/**
	 * Uris to handle with <b>CONNECT </b> method
	 * <h4>Example</h4> <code> @ConnectMapping(uris = {"/uri1", "/uri2"})</code>
	 * @return uris defined
	 */
	String[] uris() default {};
}
