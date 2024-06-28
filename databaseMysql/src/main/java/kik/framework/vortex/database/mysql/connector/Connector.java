package kik.framework.vortex.database.mysql.connector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import kik.framework.vortex.databasemanager.DatabaseConnection;

public class Connector extends DatabaseConnection {

    private static Connector connector;

    private Connector() throws SQLException {

	sendRequest("SET GLOBAL max_connections = 5000");
    }

    public static Connector getInstance() throws SQLException {
	synchronized (Connector.class) {
	    if (connector == null) {
		connector = new Connector();
	    }

	}

	return connector;
    }

    @Override
    public Connection getConnection() throws SQLException {
	try {
	    Class.forName("com.mysql.cj.jdbc.Driver");
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
	return super.getConnection();
    }

    public boolean sendRequest(String sql) throws SQLException {

	return getConnection().createStatement().execute(sql);

    }

    public boolean sendRequest(Connection connection, String sql) throws SQLException {

	return connection.createStatement().execute(sql);

    }

    public Statement sendResultRequest(String sql) throws SQLException {

	var statement = getConnection().createStatement();
	return statement;
    }

    public String getSchema() {
	if (schema == null) {
	    try {
		getConnection().close();
		var temp  = url.split("\\?")[0].split("/");
		schema = temp[temp.length - 1];
	    } catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	return schema;
    }

    public ResultSet sendResultRequest(Connection connection, String sql) throws SQLException {

	return connection.createStatement().executeQuery(sql);

    }
}
