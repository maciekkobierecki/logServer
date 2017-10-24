package logServer;

import java.sql.DriverManager;
import java.sql.ResultSet;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONObject;

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
	private void initConnection()throws Exception{
		String str= "user="+Config.getProperty("DBusername")+"&password="+Config.getProperty("DBpassword");
		connect=DriverManager.getConnection("jdbc:mysql://localhost:3306/logserver?"+str);
	}
	public void testMethod(){
		
	}
	//method returns data in JSON format 
	public JSONObject readDBinformation() throws Exception {
		try{
			ArrayList<String> DBinfo=new ArrayList<>();
			initConnection();
			statement=connect.createStatement();
			DatabaseMetaData md=connect.getMetaData();
			ResultSet rs=md.getTables(null,null, "%", null);
			String tableName="";
			String columnName="";
			String columnType="";
			JSONObject tablesJSON=new JSONObject();
			JSONObject tableJSON;
			int size;
			int tableNumber=1;
			while(rs.next()){
				tableJSON=new JSONObject();
				int columnNumber=1;
				tableName=rs.getString(3);
				tableJSON.put("tableName", tableName);
				JSONObject columnsJSON=new JSONObject();
				tableJSON.put("Columns", columnsJSON);
				DBinfo.add("TableName: "+tableName+" Columns:\n");
				ResultSet columnsSet=md.getColumns(null, null, tableName,null);
				while(columnsSet.next()){
					JSONObject columnJSON=new JSONObject();
					columnName=columnsSet.getString("COLUMN_NAME");
					columnType=columnsSet.getString("TYPE_NAME");
					size = columnsSet.getInt("COLUMN_SIZE");
					columnJSON.put("columnName", columnName);
					columnJSON.put("columnType", columnType);
					columnJSON.put("size", size);
					columnsJSON.put("Column"+columnNumber++, columnJSON);
					//DBinfo.add(columnName+" "+columnType+" "+size+ "\n");
				}
				tablesJSON.put("table"+tableNumber, tableJSON);
				tableNumber++;
				
			}
			return tablesJSON;
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
		initConnection();
		PreparedStatement statement=createInsertQuery(tableName, columns);
		statement.executeUpdate();
	}
}
