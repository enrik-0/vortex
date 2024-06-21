package kik.framework.vortex.initializer;

import java.io.File;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.reflections.Reflections;

import kik.framework.vortex.database.mysql.storage.Manager;
import kik.framework.vortex.databasemanager.storage.DatabaseStorage;
import vortex.annotate.manager.AnnotationManager;
import vortex.annotate.manager.PackageLoader;
import vortex.annotate.manager.Storage;
import vortex.http.ServerHttp;
import vortex.properties.filemanager.FileReader;

public final class VortexRunner {

    public static void run(Class<?> clazz) {
	PrintStream originalErr = System.err;
	PrintStream originalOut = System.out;
	var originalIn = System.in;
	try {

	    File file = new File("outputvortex" + System.currentTimeMillis());
	    if (!file.exists()) {
		file.createNewFile();
	    }
	    PrintStream printer = new PrintStream(file);
	    FileReader.readPropertyFile("application-dev.properties");
	    var storage = Storage.getInstance();
	   // System.setErr(null);
	    System.setIn(null);
	    //System.setOut(printer);
	    AnnotationManager.getInstance();
	    System.setOut(originalOut);
	    System.setErr(originalErr);
	    System.setIn(originalIn);
	    printer.close();
	    file.delete();
	    new Manager();
	    ServerHttp.runServer();
	} catch (Exception e) {
	    System.setOut(originalOut);
	    System.setErr(originalErr);
	    System.setIn(originalIn);
	    e.printStackTrace();
	}

    }
}
