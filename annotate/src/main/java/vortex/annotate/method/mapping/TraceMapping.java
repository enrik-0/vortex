
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
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface TraceMapping {

	/**
	 * Uris to handle with <b>TRACE</b> method
	 * <h4>example</h4> <code> @TraceMapping("/uri1")</code>
	 * @return uris to handle
	 */
	String value() default "";
	/**
	 * Uris to handle with <b>TRACE</b> method
	 * <h4>example</h4> <code> @TraceMapping(uris = {"/uri1", "/uri2"})</code>
	 * @return uris to handle
	 */
	String[] uris() default {};
}
