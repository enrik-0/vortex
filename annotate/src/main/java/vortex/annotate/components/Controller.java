package vortex.annotate.components;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**  
 * Identifier of a class that have endpoints
 * @see vortex.annotate.controller.RequestMapping 
 * @see vortex.annotate.controller.CrossOrigin
 * @see vortex.annotate.method.mapping.GetMapping
 * @see vortex.annotate.method.mapping.PostMapping
 * @see vortex.annotate.method.mapping.PutMapping
 * @see vortex.annotate.method.mapping.DeleteMapping
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Controller{

}


