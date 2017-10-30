package logServer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
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
	public Boolean createTable(JSONObject tableParametersJSON) throws JSONException{
		String tableName=tableParametersJSON.getString("tableName");
		tableParametersJSON.remove("tableName");
		Iterator<String>parameters=tableParametersJSON.keys();
		String sqlCreate="CREATE TABLE IF NOT EXISTS "+tableName+" (id INT AUTO_INCREMENT, date VARCHAR(200)";
		while(parameters.hasNext()){
			sqlCreate+=",";
			sqlCreate+=tableParametersJSON.getString(parameters.next());
			sqlCreate+=" VARCHAR(200)";
		}
		sqlCreate+=", PRIMARY KEY(id));";
		try {
			initConnection();
			if(!checkIfTableExists(tableName)){
			statement=connect.createStatement();
			statement.executeUpdate(sqlCreate);
			return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	
		
	}
	public Boolean checkIfTableExists(String tableName) throws SQLException{
		DatabaseMetaData dbm=connect.getMetaData();
		ResultSet rs=dbm.getTables(null, null, tableName,null);
		if(rs.next())
			return true;
		else return false;
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
	public PreparedStatement createInsertQuery(JSONObject dataJSON)throws Exception{
		//initConnection();
		String tableName=dataJSON.getString("tableName");
		dataJSON.remove("tableName");
		String SQL_INSERT="insert into "+tableName+" VALUES(NULL";
		for(int i=0; i<dataJSON.length(); i++)
			SQL_INSERT+=",?";
		SQL_INSERT+=")";
		Statement stmt=connect.createStatement();
		ResultSet set=stmt.executeQuery("SELECT * FROM "+tableName);
		ResultSetMetaData metadata=set.getMetaData();
		String columnName;
		String value;
		PreparedStatement statement=connect.prepareStatement(SQL_INSERT);
		int columnCount=metadata.getColumnCount();
		for(int i=1; i<columnCount; i++){
			columnName=metadata.getColumnLabel(i+1);
			value=dataJSON.getString(columnName);		
			statement.setString(i, value);
		}


			
		return statement;

		/*for(int i=0; i<columns.size(); i++)
			statement.setString(i+1, columns.get(i));
		return statement;
		*/
	}
	
	public Boolean insert(JSONObject dataJSON) throws Exception {
		initConnection();
		PreparedStatement statement=createInsertQuery(dataJSON);
		statement.executeUpdate();
		return true;
	}

	public JSONObject readTable(String tableName)throws Exception {
		String query= "SELECT * FROM "+tableName;
		PreparedStatement statement= connect.prepareStatement(query);
		ResultSet rs=statement.executeQuery();
		return convertResultSetTableToJSONObject(rs);
	}
	public JSONObject convertResultSetTableToJSONObject(ResultSet dataRS) throws SQLException, JSONException{
		JSONObject mainObject=new JSONObject();
		ResultSetMetaData rsmd=dataRS.getMetaData();
		JSONObject rowJSON;
		int columnCount=rsmd.getColumnCount();
		int counter=0;
		while(dataRS.next()){
			rowJSON=new JSONObject();			
			for(int i=1; i<=columnCount; i++){
				String columnName=rsmd.getColumnName(i);
				Object value= dataRS.getObject(i).toString();
				rowJSON.put(columnName, value);
			}
			mainObject.put("Row"+(++counter), rowJSON);
		}
		return mainObject;
	}
}
