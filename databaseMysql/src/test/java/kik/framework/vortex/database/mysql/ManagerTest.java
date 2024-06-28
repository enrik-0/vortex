package kik.framework.vortex.database.mysql;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import kik.framework.vortex.database.mysql.connector.Connector;
import kik.framework.vortex.database.mysql.storage.Manager;
import kik.framework.vortex.databasemanager.exception.RelationTypeException;
import vortex.annotate.exceptions.InitiateServerException;
import vortex.annotate.exceptions.UriException;
import vortex.annotate.manager.AnnotationManager;
import vortex.properties.filemanager.FileReader;

public class ManagerTest {
    
    @BeforeAll
    static void init() throws SQLException, RelationTypeException, IOException, UriException, InitiateServerException {
	FileReader.readPropertyFile("application-test.properties");
	AnnotationManager.getInstance();
	new Manager();
    }
    
    @AfterAll
    static void finish() throws SQLException {
	try {
	    Connector.getInstance().sendRequest("DROP TABLE `test`.`books`, `test`.`compositekeyclasss`, `test`.`librarys`, `test`.`manytomanyclass1`, `test`.`manytomanyclass1_manytomanyclass2`, `test`.`manytomanyclass2`, `test`.`persons`, `test`.`relatedclasss`, `test`.`table1`, `test`.`table2`, `test`.`testclasss`, `test`.`trucks`, `test`.`users`, `test`.`vehicles`, `test`.`vehicles_users`;");
	    
	}catch(SQLException e) {
	    try {
	    
	   Connector.getInstance().sendRequest("DROP TABLE `test`.`books`, `test`.`compositekeyclasss`, `test`.`librarys`, `test`.`manytomanyclass1`, `test`.`manytomanyclass2_manytomanyclass1`, `test`.`manytomanyclass2`, `test`.`persons`, `test`.`relatedclasss`, `test`.`table1`, `test`.`table2`, `test`.`testclasss`, `test`.`trucks`, `test`.`users`, `test`.`vehicles`, `test`.`vehicles_users`;");
    }catch(SQLException e1) {
	
    }
	}
    }
    @Test
    void test() {
	assertTrue(true);
    }

}
