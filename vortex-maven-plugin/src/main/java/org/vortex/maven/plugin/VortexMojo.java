package org.vortex.maven.plugin;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import vortex.annotate.exceptions.InitiateServerException;
import vortex.annotate.exceptions.UriException;
import vortex.http.ServerHttp;
import vortex.properties.filemanager.FileReader;

@Mojo(name = "run", threadSafe = true, 
requiresDependencyCollection = ResolutionScope.RUNTIME_PLUS_SYSTEM, 
requiresDependencyResolution = ResolutionScope.RUNTIME_PLUS_SYSTEM)
public class VortexMojo extends AbstractMojo {
    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    private MavenSession session;

    public void execute() throws MojoExecutionException {
	List<String> classpathElements = null;
	MavenProject project = session.getCurrentProject();
	URLClassLoader loader = null;
	String pack = session.getProjects().get(0).getBasedir().getName();
	System.out.print(pack);
	try {
	    classpathElements = project.getCompileClasspathElements();
	    List<URL> projectClasspathList = new ArrayList<>();
	    for (String element : classpathElements) {
		if(element.contains(pack) || element.contains("vortex")) {
		    
		try {
		    projectClasspathList.add(new File(element).toURI().toURL());
		} catch (MalformedURLException e) {
		    throw new MojoExecutionException(element + " is an invalid classpath element", e);
		}
		loader = new URLClassLoader(projectClasspathList.toArray(new URL[0]));
		}

	    }

	    FileReader.readPropertyFile("application-dev.properties");
	    ServerHttp.runServer(loader);
	} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
		| NoSuchMethodException | SecurityException | IOException | UriException | InitiateServerException e) {
	    e.printStackTrace();
	} catch (DependencyResolutionRequiredException e1) {
	    e1.printStackTrace();
	}
	CountDownLatch latch = new CountDownLatch(1);
	Runtime.getRuntime().addShutdownHook(new Thread(() -> {
	    getLog().info("Shutdown hook triggered, stopping server.");
	    ServerHttp.stopServer();
	    latch.countDown();
	}));

	try {
	    latch.await();
	} catch (InterruptedException e) {
	    Thread.currentThread().interrupt();
	}

    }
}
