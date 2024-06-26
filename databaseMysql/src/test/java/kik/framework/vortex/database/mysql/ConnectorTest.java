package kik.framework.vortex.database.mysql;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kik.framework.vortex.database.mysql.connector.Connector;
import vortex.properties.filemanager.FileReader;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectorTest {

    private static Connector connector;

    @BeforeAll
    static void setUpClass() throws IOException, SQLException {
	FileReader.readPropertyFile("application-test.properties");
        connector = Connector.getInstance();
    }

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection connection = connector.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS test (id INT PRIMARY KEY, name VARCHAR(255))");
            stmt.execute("INSERT INTO test (id, name) VALUES (1, 'John Doe')");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection connection = connector.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS test");
            connection.close();
        }
    }

    @Test
    void testGetInstance() throws SQLException {
        Connector instance1 = Connector.getInstance();
        Connector instance2 = Connector.getInstance();
        assertSame(instance1, instance2, "getInstance() should return the same instance");
    }

    @Test
    void testGetConnection() throws SQLException {
        Connection connection = connector.getConnection();
        assertNotNull(connection, "getConnection() should return a valid connection");
        connection.close();
    }

    @Test
    void testSendRequest() throws SQLException {
        connector.sendRequest("CREATE TABLE test2 (id INT PRIMARY KEY, name VARCHAR(255))");
        try (Connection connection = connector.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM test2")) {
            assertNotNull(rs, "Table test should exist and be queryable");
            connection.close();
        }

        connector.sendRequest("DROP TABLE test2");
    }

    @Test
    void testSendResultRequest() throws SQLException {
        try (Statement stmt = connector.sendResultRequest("SELECT * FROM test");
             ResultSet rs = stmt.executeQuery("SELECT * FROM test")) {
            assertNotNull(rs, "sendResultRequest() should return a Statement that can execute queries");

            assertTrue(rs.next(), "ResultSet should have at least one row");
            assertEquals(1, rs.getInt("id"), "The first column value should be 1");
            assertEquals("John Doe", rs.getString("name"), "The second column value should be 'John Doe'");
            stmt.close();
        }
    }

    @Test
    void testGetSchema() throws SQLException {
        String schema = connector.getSchema();
        assertNotNull(schema, "getSchema() should return a valid schema");
    }
}
