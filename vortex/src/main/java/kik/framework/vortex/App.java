
package kik.framework.vortex;

import kik.framework.vortex.annotations.Controller;
import kik.framework.vortex.manager.AnnotationManager;

/**
 * Hello world!
 *
 */
@Controller
public class App {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AnnotationManager.getInstance();
	}
}
