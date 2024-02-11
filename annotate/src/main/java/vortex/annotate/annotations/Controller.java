
package vortex.annotate.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**  
 * Identifier of a class that have endpoints
 * @see RequestMapping
 * @see GetMapping
 * @see PostMapping
 * @see PutMapping
 * @see DeleteMapping
 * @Author: Enrique Javier Villar Cea
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Controller{

}
