
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
 * @see ConnectMapping
 * @see HeadMapping
 * @see OptionsMapping
 * @see PatchMapping
 * @see TraceMapping
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface DeleteMapping {

	/**
	 * Uri to handle with <b> DELETE</b> method
	 * <h4>example</h4> <code> @DeleteMapping("/uri")</code>
	 * @return uri to handle
	 */
	String value() default "";
	/**
	 * Uris to handle with <b> DELETE</b> method
	 * <h4>example</h4> <code> @DeleteMapping(uris = {"/uri1", "/uri2"})</code>
	 * @return uris to handle
	 */
	String[] uris() default {};
}
