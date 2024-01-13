package vortex.annotate;

import vortex.annotate.annotations.Launcher;
import vortex.annotate.annotations.VortexApplication;
import vortex.annotate.manager.AnnotationManager;

/**
 * Hello world!
 *
 */
@VortexApplication
public class App 
{
	@Launcher
    public static void main( String[] args )
    {
		AnnotationManager.getInstance();
    }
}
