
package kik.framework.vortex.annotations;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Handles params in the request, 
 * params in a request have this format:
 * <h1>1 parameter</h1> <br>
 * {rest of uri}?{param}={value} 
 * <h1> 2 or more parameters</h1> <br>
 * {rest of uri}?{parameter1}={value1}&{parameter2}={value2}&{parameterN}={valueN} <br>
 * The name of the parameter and the variable <b>MUST</b> be the same. <br>
 * if used in a int parameter called days<br>
 * <b>uri?days=2</b>
 * @author Enrique Javier Villar Cea
 * @see RequestBody
 */
@Target(PARAMETER)
@Retention(RUNTIME)
@Documented
public @interface RequestParam {

}
