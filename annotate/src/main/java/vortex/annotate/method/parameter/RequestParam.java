
package vortex.annotate.method.parameter;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Handles params in the request, 
 * params in a request have this format:
 * <h1>1 parameter</h1> <br>
 * <code>{rest of uri}?{param}={value} </code>
 * <h1> 2 or more parameters</h1> 
 * <code>{rest of uri}?{parameter1}={value1}&{parameter2}={value2}&{parameterN}={valueN} </code><br> <br>
 * The name of the parameter and the variable <b>MUST</b> be the same. <br>
 * if used in a int parameter called days<br>
 * <b>/example?days=2</b>
 * @see RequestBody
 * @author Enrique Javier Villar Cea
 */
@Target(PARAMETER)
@Retention(RUNTIME)
@Documented
public @interface RequestParam {

}
