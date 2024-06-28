package kik.framework.vortex.databasemanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import vortex.properties.filemanager.FileReader;
import vortex.properties.kinds.Database;
import vortex.utils.MappingUtils;

public class ConnectorTest {

    @BeforeAll
    static void setUp() {
	try {
	    FileReader.readPropertyFile("test.properties");
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Test
    void testConnectorParams() {
	Connector connector = new Connector();
	assertEquals((String) Database.Credentials.URL.value(), connector.getUrl());
	assertEquals(((String) MappingUtils.map(Database.Credentials.PWD.value(), String.class)), connector.getPwd());
	assertEquals((String) Database.Credentials.USERNAME.value(), connector.getUsername());
	assertNull(connector.getSchema());
    }

    @Test
    void openConnection() {
	Connector connector = new Connector();
	Connection connection = null; 
	try {
	    connection = connector.getConnection();
	} catch (SQLException e) {
	assertNotNull(null);
	}
	assertNotNull(connection);
    }

    @Test
    void closeConnection() {
	Connector connector = new Connector();
	Connection connection = null;
	try {
	    connection = connector.getConnection();
	} catch (SQLException e) {
	    assertTrue(false);
	}

	assertNotNull(connection);
	try {
	    assertFalse(connection.isClosed());

	} catch (SQLException e) {
	    assertTrue(false);
	}
	connector.closeConnection(connection);
	try {
	    assertTrue(connection.isClosed());

	} catch (SQLException e) {
	    assertTrue(false);
	}
    }

    class Connector extends DatabaseConnection {

	Connector() {

	}

	@Override
	public

		Connection getConnection() throws SQLException {
	    return super.getConnection();
	}

	@Override
	public void closeConnection(Connection connection) {
	    try {
		super.closeConnection(connection);
	    } catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}

	public String getUrl() {
	    return url;
	}

	public String getUsername() {
	    return username;
	}

	public String getPwd() {
	    return pwd;
	}

	public String getSchema() {
	    return schema;
	}
    }
}
