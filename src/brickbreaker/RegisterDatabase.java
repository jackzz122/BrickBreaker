package brickbreaker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RegisterDatabase {
	
	public static String DRIVERCLASS = "com.mysql.cj.jdbc.Driver";
	public static String DBURL = "jdbc:mysql://localhost:";
	
	public static Connection getConnection (String dbPort, String dbName, String User, String Pass)
	{
		Connection con = null;
		try {
			
			Class.forName(RegisterDatabase.DRIVERCLASS);
			
			try {
				con = DriverManager.getConnection(RegisterDatabase.DBURL + dbPort + "/" + dbName, User, Pass);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return con;
	}
	
	public static void closeConnection (Connection c)
	{
		if (c!=null)
		{
			try {
				c.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}