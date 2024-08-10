
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
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface PostMapping {

	/**
	 * Uris to handle with <b>POST</b> method
	 * <h4>example</h4> <code> @PostMapping("/uri")</code>
	 * @return uris to handle
	 */
	String value();
	/**
	 * Uris to handle with <b>POST</b> method
	 * <h4>example</h4> <code> @PostMapping(uris = {"/uri1", "/uri2"})</code>
	 * @return uris to handle
	 */
	String[] uris() default {};
}
