package brickbreaker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class StatementHandle {
	
	public static void closeStatement (Statement st)
	{
		if (st!=null)
		{
			try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static boolean add (PlayerHistory ph,String dbPort, String dbName, String root, String pass)
	{
		Connection conn = RegisterDatabase.getConnection(dbPort ,dbName,root, pass);
		Statement stm = null;
		int check = 0;
		try {
			stm = conn.createStatement();
			
			String addStatement = "insert into player (Name,Score) values (\"" + ph.getName() + "\"," + ph.getScore() + ")";
			
			check = stm.executeUpdate(addStatement);
			int idSetBackToPlayer = 0;
			ResultSet rs = stm.executeQuery("select * from player where Id = (select max(Id) FROM player)");
			while (rs.next())
			{
				idSetBackToPlayer = rs.getInt("Id");
			}
			rs.close();
			ph.setId(idSetBackToPlayer);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally 
		{
			StatementHandle.closeStatement(stm);
			RegisterDatabase.closeConnection(conn);
		}
		
		return check != 0;
	}
	
	
	public static boolean update (PlayerHistory ph, int diemmoi,String dbPort, String dbName, String root, String pass)
	{
		Connection conn = RegisterDatabase.getConnection(dbPort ,dbName,root, pass);
		Statement stm = null;
		int check = 0;
		try {
			stm = conn.createStatement();
			
			String updateStatement = "update player set Score = " + diemmoi + " where Id = " + ph.getId() ;
			
			check = stm.executeUpdate(updateStatement);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally 
		{
			StatementHandle.closeStatement(stm);
			RegisterDatabase.closeConnection(conn);
		}
		
		return check != 0;
	}
	
	public static boolean delete (int PlayerId,String dbPort, String dbName, String root, String pass)
	{
		Connection conn = RegisterDatabase.getConnection(dbPort ,dbName,root, pass);
		Statement stm = null;
		int check = 0;
		try {
			stm = conn.createStatement();
			
			String deleteStatement = "delete from player where Id = " + PlayerId;
			check = stm.executeUpdate(deleteStatement);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally 
		{
			StatementHandle.closeStatement(stm);
			RegisterDatabase.closeConnection(conn);
		}
		
		return check !=0;	
	}
	
	public static ArrayList <PlayerHistory> readAll (int top,String dbPort, String dbName, String root, String pass)
	{
		ArrayList <PlayerHistory> res = new ArrayList <PlayerHistory> ();
		
		Connection conn = RegisterDatabase.getConnection(dbPort ,dbName,root, pass);
		Statement stm = null;
		try {
			stm = conn.createStatement();
			
			String select = "SELECT * FROM player order by Score DESC limit " + top;
			ResultSet rs = stm.executeQuery(select);
			
			while (rs.next())
			{
				int id = rs.getInt("Id");
				String name = rs.getString("Name");
				int score = rs.getInt("Score");
				res.add(new PlayerHistory(id, name,score));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally 
		{
			StatementHandle.closeStatement(stm);
			RegisterDatabase.closeConnection(conn);
		}
		
		return res;
	}
}