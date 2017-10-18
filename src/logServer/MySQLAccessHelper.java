package logServer;

import java.sql.DriverManager;
import java.sql.ResultSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;

public class MySQLAccessHelper {
	private Connection connect=null;
	private Statement statement=null;
	private PreparedStatement preparedStatement=null;
	private ResultSet resultSet=null;
	
	public MySQLAccessHelper(){
		loadDrivers();
	}
	
	public void loadDrivers(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(Exception e){
			System.out.println("database drivers not found");
		}
	}
	public void readDataBase() throws Exception {
		try{
			connect=DriverManager.getConnection("jdbc:mysql//localhost/people?"+ "user=sqluser&password=sqluserpw");
			
			statement=connect.createStatement();
			
			resultSet=statement.executeQuery("select * from feedback.comments");
		}
		catch (Exception e){
			throw e;
		}
	}
	
	public void insert(String tableName, Map<String,String>values) throws Exception {
		connect=DriverManager.getConnection("jdbc:mysql//localhost/people?"+ "user=sqluser&password=sqluserpw");
		String query= "insert into "+tableName+" values (default, >
	}
}
