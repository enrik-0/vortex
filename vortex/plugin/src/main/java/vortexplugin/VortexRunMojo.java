package vortexplugin;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "run")
public class VortexRunMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.finalName}.jar")
    private String jarName;

    public void execute() throws MojoExecutionException {
        getLog().info("Ejecutando la aplicación Vortex...");

        // Aquí puedes agregar lógica adicional según sea necesario
        // Por ejemplo, puedes usar la clase org.codehaus.plexus.util.cli.CommandLineUtils para ejecutar tu JAR.

        // Ejemplo:
        // CommandLineUtils.executeCommandLine("java", "-jar", jarName);
    }
}
