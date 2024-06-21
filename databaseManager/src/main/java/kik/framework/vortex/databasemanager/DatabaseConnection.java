package kik.framework.vortex.databasemanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static vortex.properties.kinds.Database.Credentials.URL;
import static vortex.properties.kinds.Database.Credentials.PWD;
import static vortex.properties.kinds.Database.Credentials.USERNAME;
import vortex.utils.MappingUtils;

public abstract class DatabaseConnection {

    protected String url = (String) MappingUtils.map(URL.value(), String.class);
    protected String username = (String) MappingUtils.map(USERNAME.value(), String.class);
    protected String pwd = (String) MappingUtils.map(PWD.value(), String.class);
    protected String schema;

    public Connection getConnection() throws SQLException {
	Connection connection = null;
	try {
	    connection = DriverManager.getConnection(url, username, pwd);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return connection;

    }

    public void closeConnection(Connection connection) throws SQLException{
	connection.close();
    }
    public abstract String getSchema();

}
