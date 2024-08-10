
package vortex.annotate.method.parameter;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Handles params in the request, 
 * params in a request have this format:
 * 
 * <h2> 1 parameter</h2>
 * 
 * <code>&lt;rest of uri&gt;?&lt;param&gt;=&lt;value&gt; </code>
 * 
 *  <h2>2 or more parameters </h2>
 *  
 * <code>&lt;rest of uri&gt;?&lt;parameter1&gt;=&lt;value1&gt;&amp;&lt;parameter2&gt;=&lt;value2&gt;&amp;&lt;parameterN&gt;=&lt;valueN&gt;</code> <br>
 * The name of the parameter and the variable <b>MUST</b> be the same.
 * <h3>Example</h3>
 * if used in a int parameter called days<br>
 * <code><b>/example?days=2</b></code>
 * @see RequestBody
 */
@Target(PARAMETER)
@Retention(RUNTIME)
@Documented
public @interface RequestParam {

}
