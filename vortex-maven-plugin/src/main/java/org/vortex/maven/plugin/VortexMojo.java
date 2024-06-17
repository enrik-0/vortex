package org.vortex.maven.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypesScanner;

import kik.framework.vortex.database.mysql.storage.Manager;
import kik.framework.vortex.databasemanager.exception.RelationTypeException;
import kik.framework.vortex.databasemanager.storage.StorageManager;
import vortex.annotate.annotations.VortexApplication;
import vortex.annotate.exceptions.InitiateServerException;
import vortex.annotate.exceptions.UriException;
import vortex.annotate.manager.AnnotationManager;
import vortex.annotate.manager.PackageLoader;
import vortex.annotate.manager.Storage;
import vortex.http.ServerHttp;
import vortex.properties.filemanager.FileReader;

@Mojo(name = "run", threadSafe = true, requiresDependencyCollection = ResolutionScope.RUNTIME_PLUS_SYSTEM, requiresDependencyResolution = ResolutionScope.RUNTIME_PLUS_SYSTEM)
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
		if (element.contains("jgrapht") || element.contains("jackson")) {
		    System.out.println("s");
		}
		// if (dependency(pack, element)) {

		try {
		    projectClasspathList.add(new File(element).toURI().toURL());
		} catch (MalformedURLException e) {
		    throw new MojoExecutionException(element + " is an invalid classpath element", e);
		}
		loader = new URLClassLoader(projectClasspathList.toArray(new URL[0]));
//		}

	    }
	    Thread.currentThread().setContextClassLoader(loader);
	    InputStream input = VortexMojo.class.getClassLoader()
		    .getResourceAsStream("META-INF/maven/kik.framework.vortex/vortex-maven-plugin/pom.properties");
	    Properties prop = new Properties();
	    prop.load(input);
	    String version = prop.getProperty("version");
	    System.out.println();
	    System.out.println("⠀⠀⠀⠀⠀⣠⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀              __     __         _            \r\n"
		    + "⠀⠀⠀⠀⠠⡏⠤⠀⠂⠂⣂⣵⣿⣿⣿⣿⣿⣿⣿⣶⣆⠂⠂⠈⠈⠀⠀⠀⠀⠀           \\ \\   / /__  _ __| |_ _____  __\r\n"
		    + "⠀⠀⠀⠀⠀⠀⠀⠀⢀⣶⣿⣿⡿⠛⠉⠉⠉⠛⢿⣿⣿⣷⣄⡀⠀⠀⠀⠀⠀⠀             \\ \\ / / _ \\| '__| __/ _ \\ \\/ /\r\n"
		    + "⠀⠀⠀⠀⠀⠀⢀⣴⣿⣿⣿⠋⠀⠀⠀⠀⠀⠀⠀⠙⣿⣿⣿⣿⣶⣤⣀⡀⠀⢀              \\   / (_) | |  | ||  __/>  < \r\n"
		    + "⠀⠀⣀⠰⣀⢿⣿⣿⣿⣿⣿⣀⣀⣀⣀⣀⣀⣰⣰⣶⣿⣿⣿⣿⣿⠿⠿⠁⠉⠁             \\_/ \\___/|_|   \\__\\___/_/\\_\\\r\n"
		    + "⠈⠀⠀⠀⠈⠉⠛⠛⠛⠛⣿⣿⡽⣏⠉⠉⠉⠉⠉⣽⣿⠉⠉⠀⠀⠀⠀⠀⠀⠀\r\n" + "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠻⣷⣦⣄⣀⣠⣴⣾⡿⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀\r\n"
		    + "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠛⠿⠿⠿⠟⠋⠀⠀⠀⠀⠀⠀⠀\r\n" + "");
	    System.out.println("Vortex version " + version);
	    /*
	     * FileReader.readPropertyFile("application-dev.properties");
	     * PackageLoader.getInstance().setLoader(loader);
	     * AnnotationManager.getInstance(); new Manager();
	     */ 
	    PrintStream originalErr = System.err;
	    PrintStream originalOut = System.out;
	    var originalIn = System.in;
	    File file =  new File("outputvortex" + System.currentTimeMillis());
	    if(!file.exists()) {
		file.createNewFile();
	    }
	    var printer = new PrintStream(file);
	    System.setOut(printer);
	    System.setErr(null);
	    System.setIn(null);
	    var reflections = new Reflections(loader, new TypeAnnotationsScanner(), new SubTypesScanner());
	    var s = reflections.getTypesAnnotatedWith(VortexApplication.class);
	    if(!s.isEmpty()) {
		
	    var r = (Class<?>) s.toArray()[0];
	    Method m = r.getMethod("main", String[].class);
	    Object a = r.getConstructor().newInstance();
	    System.setOut(originalOut);
	    System.setErr(originalErr);
	    System.setIn(originalIn);
	    printer.close();
	    file.delete();
	    m.invoke(a, new String[1]);
	    }else {
		FileReader.readPropertyFile("application-dev.properties");
		PackageLoader.getInstance().setLoader(loader);
		AnnotationManager.getInstance();
		new Manager();
		ServerHttp.runServer();

	    }
	    // ServerHttp.runServer(loader);
	} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
		| NoSuchMethodException | SecurityException | IOException e) {
	    e.printStackTrace();
	} catch (DependencyResolutionRequiredException e1) {
	    e1.printStackTrace();
	} catch (UriException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (InitiateServerException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (RelationTypeException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	System.out.println();
    }

}
