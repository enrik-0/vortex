package kik.framework.vortex.initializer;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import kik.framework.vortex.database.mysql.storage.Manager;
import vortex.annotate.manager.AnnotationManager;
import vortex.annotate.manager.Storage;
import vortex.http.ServerHttp;
import vortex.properties.filemanager.FileReader;

public final class VortexRunner {

    public static void run(Class<?> clazz) {
	PrintStream originalErr = System.err;
	PrintStream originalOut = System.out;
	var originalIn = System.in;
	File file = new File("outputvortex" + System.currentTimeMillis());
	PrintStream  printer = null;
	    try {
		file.createNewFile();
		printer = new PrintStream(file);
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	try {

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
	    printer.close();
	    file.delete();
	    e.printStackTrace();
	}

    }
}
