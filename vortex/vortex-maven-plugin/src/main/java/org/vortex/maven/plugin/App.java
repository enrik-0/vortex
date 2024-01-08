package org.vortex.maven.plugin;

import vortex.annotate.annotations.Launcher;
import vortex.annotate.annotations.VortexApplication;

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
        System.out.println( "Hello World!" );
    }
}
