package org.vortex.maven.plugin;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "run")
public class VortexMojo extends AbstractMojo {

	public void execute() throws MojoExecutionException {
		System.out.println("mojo");
	}

	

}