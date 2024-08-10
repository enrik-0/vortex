
package vortex.annotate.method.mapping;

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
 * @see DeleteMapping
 * @see HeadMapping
 * @see OptionsMapping
 * @see PatchMapping
 * @see TraceMapping
 * @see ConnectMapping
 * 
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface GetMapping {

	/**
	 * Uri to handle with <b>GET </b> method
	 * <h4>example</h4> <code> @GetMapping("/uri")</code>
	 *  @return the uri to handle
	 */
	String value() default "/";
	/**
	 * Uri to handle with <b>GET</b> method
	 * <h4>example</h4> <code> @GetMapping(uris = {"/uri1", "/uri2"})</code>
	 * @return uris to handle
	 */
	String[] uris() default {};
}
