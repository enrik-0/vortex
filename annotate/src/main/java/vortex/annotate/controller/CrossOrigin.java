
package vortex.annotate.controller;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**  
 * @Author: Enrique Javier Villar Cea
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface CrossOrigin {
	/**
	 *	 <big><bold>'*' </bold></big>means let all origin make request 
	 */
	String value() default "*";

}
