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

	private static Mock instance;
	
	private Mock() throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UriException, InitiateServerException, SQLException, RelationTypeException {
	    	FileReader.readPropertyFile("application-test.properties");
	    	AnnotationManager.getInstance();
	    	Manager manager = new Manager();
		ServerHttp.runServer();
	}
	/**
	 * 
	 * @return {@link Mock}
	 * @throws IOException
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws UriException 
	 * @throws InitiateServerException 
	 * @throws RelationTypeException 
	 * @throws SQLException 
	 */
	public static Mock getInstance() throws IOException, InstantiationException, 
	IllegalAccessException, IllegalArgumentException, InvocationTargetException,
	NoSuchMethodException, SecurityException, UriException, InitiateServerException, SQLException, RelationTypeException{
		if(instance == null) {
			instance = new Mock();
		}
		return instance;
	}

	
	public static void stop() {
		ServerHttp.stopServer();
    instance = null;
	} 


}
