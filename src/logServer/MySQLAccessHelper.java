package logServer;

import java.sql.DriverManager;
import java.sql.ResultSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class MySQLAccessHelper {
	private Connection connect=null;
	private Statement statement=null;
	private PreparedStatement preparedStatement=null;
	private ResultSet resultSet=null;
	
	public void readDataBase() throws Exception {
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			connect=DriverManager.getConnection("jdbc:mysql//localhost/people?"+ "user=sqluser&password=sqluserpw");
			
			statement=connect.createStatement();
			
			resultSet=statement.executeQuery("select * from feedback.comments");
		}
	}
}
