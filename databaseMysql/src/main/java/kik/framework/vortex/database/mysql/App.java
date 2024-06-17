package kik.framework.vortex.database.mysql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.reflections.Reflections;

import kik.framework.vortex.database.mysql.storage.Manager;
import kik.framework.vortex.databasemanager.exception.DataTypeException;
import kik.framework.vortex.databasemanager.exception.RelationTypeException;
import vortex.annotate.components.Entity;
import vortex.annotate.exceptions.InitiateServerException;
import vortex.annotate.exceptions.UriException;
import vortex.annotate.manager.AnnotationManager;
import vortex.properties.filemanager.FileReader;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws SQLException, IOException, UriException, InitiateServerException, DataTypeException, RelationTypeException
    {
	FileReader.readPropertyFile("application-dev.properties");
	AnnotationManager.getInstance();
	new Manager(null);
	AnnotationManager.getInstance();
	UserRepository repository = new UserRepository();
	List<User> w = new ArrayList<>();
	for(int i = 0; i < 3; i++) {
	    w.add(new User("" + i,i));
	   
	}
	repository.save(new User("w", 2));
	repository.saveAll(w);
        System.out.println( "Hello World!" );
    }
}
