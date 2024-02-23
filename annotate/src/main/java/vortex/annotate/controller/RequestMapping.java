
package vortex.annotate.controller;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import vortex.annotate.components.Controller;
import vortex.annotate.method.mapping.DeleteMapping;
import vortex.annotate.method.mapping.GetMapping;
import vortex.annotate.method.mapping.PostMapping;
import vortex.annotate.method.mapping.PutMapping;

/**
 * uris to handle with a {@link Controller}
 * @see #value
 * @see #uris
 * @see DeleteMapping
 * @see GetMapping
 * @see PostMapping
 * @see PutMapping
 * @author Enrique Javier Villar Cea
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface RequestMapping{

	/**
	 * Uri to handle by a {@link Controller}
	 * <h1>example:</h1> <code> @RequestMapping("/uri")</code>
	 */
	String value() default "";
	/**
	 * Multiple Uris to handle by a {@link Controller}
	 * <h1>example:</h1> <code> @RequestMapping(uris = {"/uri", "/uri2", "uri3"})</code>
	 */
	String[] uris() default {};

}
