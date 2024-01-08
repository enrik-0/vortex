package kik.framework.vortex.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

public class YesMojo extends AbstractMojo {

    @Parameter(defaultValue = "Hola mundo!")
    private String message;

    @Override
    public void execute() throws MojoExecutionException {
        System.out.println(message);
    }
}
