package logServer;

import java.sql.DriverManager;
import java.sql.ResultSet;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
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
			connect=DriverManager.getConnection("jdbc:mysql://localhost:3306/logserver?"+ "user=admin&password=admin");
			
			statement=connect.createStatement();
			DatabaseMetaData md=connect.getMetaData();
			ResultSet rs=md.getTables(null,null, "%", null);
			ResultSet columnsSet=md.getColumns(null, null, "%",null);
			while(rs.next())
				System.out.println(rs.getString(3));
			while(columnsSet.next()){
				String name=columnsSet.getString("COLUMN_NAME");
				String type=columnsSet.getString("TYPE_NAME");
				int size = columnsSet.getInt("COLUMN_SIZE");
			}
			//resultSet=statement.executeQuery("select * from feedback.comments");
		}
		catch (Exception e){
			throw e;
		}
	}
	public PreparedStatement createInsertQuery(String tableName, ArrayList<String> columns)throws Exception{
		String SQL_INSERT="insert into "+tableName+" VALUES(";
		for(String str: columns)
			SQL_INSERT+="?,";
		SQL_INSERT+=")";
		PreparedStatement statement=connect.prepareStatement(SQL_INSERT);
		for(int i=0; i<columns.size(); i++)
			statement.setString(i+1, columns.get(i));
		return statement;
	}
	
	public void insert(String tableName, ArrayList<String>columns) throws Exception {
		connect=DriverManager.getConnection("jdbc:mysql//localhost/people?"+ "user=sqluser&password=sqluserpw");
		PreparedStatement statement=createInsertQuery(tableName, columns);
		statement.executeUpdate();
	}
}
