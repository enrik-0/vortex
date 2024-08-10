
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
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface PutMapping {

	/**
	 * Uris to handle with <b>PUT</b> method
	 * <h4>example</h4> <code> @PutMapping("/uri1")</code>
	 * @return uris to handle
	 */
	String value() default "";
	/**
	 * Uris to handle with <b>PUT</b> method
	 * <h4>example</h4> <code> @PutMapping(uris = {"/uri1", "/uri2"})</code>
	 * @return uris to handle
	 */
	String[] uris() default {};

}
