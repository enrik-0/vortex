
package kik.framework.vortex.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @Author: Enrique Javier Villar Cea
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface Autowired {

}
