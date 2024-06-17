package kik.framework.vortex.databasemanager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import vortex.properties.filemanager.FileReader;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
	FileReader.readPropertyFile("application-dev.properties");
	class A extends DatabaseConnection {
	    A(){
		
	    }
	    A(String... strings) {
		url = strings[0];
		username = strings[1];
		pwd = strings[2];

	    }

	    @Override
	    public

	    Connection getConnection() {
		Connection conexion = null;
		try {
		    conexion = DriverManager.getConnection(url, username, pwd);
		} catch (SQLException e) {
		    e.printStackTrace();
		}
		return conexion;
	    }

	    @Override
	    public
	    void closeConnection(Connection connection) {
		try {
		    connection.close();
		} catch (SQLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

	    }

	}
	System.out.println("Hello World!");
	//A a = new A("jdbc:mysql://localhost:3306/test", "root", "1234");
	A b = new A();
	Connection conexion = b.getConnection();
	var a = conexion.getSchema();
	Statement sentencia = null;
	try {
	    sentencia = conexion.createStatement();
	    String consultaSQL = "SELECT * FROM users";
	    ResultSet resultado = sentencia.executeQuery(consultaSQL);
	    while (resultado.next()) {
		System.out.println("año" + resultado.getInt("edad"));

	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if(sentencia != null) {
		    sentencia.close();
		}
		if (conexion != null) {
		    conexion.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
    }
}
