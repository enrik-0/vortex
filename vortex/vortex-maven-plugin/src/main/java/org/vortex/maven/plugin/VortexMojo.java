package org.vortex.maven.plugin;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import vortex.annotate.manager.AnnotationManager;
import vortex.annotate.manager.Storage;

@Mojo(name = "run")
public class VortexMojo extends AbstractMojo {
      
	@Parameter(property = "mainClass", required = true)
	private String mainClass;

	public void execute() throws MojoExecutionException {
		AnnotationManager.getInstance();
		for ( Class<?> e : Storage.getInstance().getRunnable()) {
			System.out.println(e.getName());
		}
		
	}

	

}