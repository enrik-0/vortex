
package kik.framework.vortex;

import vortex.annotate.annotations.Controller;
import vortex.annotate.annotations.Launcher;
import vortex.annotate.annotations.VortexApplication;
import vortex.annotate.manager.AnnotationManager;

/**
 * Hello world!
 *
 */
@VortexApplication
@Controller
public class App {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AnnotationManager.getInstance();
	}
}
