
package vortex.annotate.controller;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**  
 * {@link vortex.annotate.components.Controller} must deny connections if is not the origin
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface CrossOrigin {
	/**
	 *	 <b>*</b> means let all origin make request 
	 *@return the origin 
	 */
	String value() default "*";

}
