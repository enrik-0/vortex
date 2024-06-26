package vortex.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import kik.framework.vortex.database.mysql.storage.Manager;
import kik.framework.vortex.databasemanager.exception.RelationTypeException;
import vortex.annotate.exceptions.InitiateServerException;
import vortex.annotate.exceptions.UriException;
import vortex.annotate.manager.AnnotationManager;
import vortex.http.ServerHttp;
import vortex.properties.filemanager.FileReader;



public final class Mock {

	
	public static void start() throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UriException, InitiateServerException, SQLException, RelationTypeException {
	    	FileReader.readPropertyFile("application-test.properties");
	    	AnnotationManager.getInstance();
	    	Manager manager = new Manager();
		ServerHttp.runServer();
	}


	
	public static void stop() {
		ServerHttp.stopServer();
	} 


}
