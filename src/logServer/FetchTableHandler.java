package logServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class FetchTableHandler implements HttpHandler{
	
	private MySQLAccessHelper sqlHelper;
	public FetchTableHandler(MySQLAccessHelper helper){
		sqlHelper=helper;
	}
	@Override
	public void handle(HttpExchange he) throws IOException {
		try {
			JSONObject tableDataJSON= new JSONObject();
			InputStreamReader in=new InputStreamReader(he.getRequestBody(), "UTF-8");
			BufferedReader br=new BufferedReader(in);
			JSONObject bodyJSON=new JSONObject(br.readLine());
			String tableName=bodyJSON.getString("tableName");
			tableDataJSON=sqlHelper.readTable(tableName);
			String data=tableDataJSON.toString();
			he.sendResponseHeaders(200, data.length());
			OutputStream os=he.getResponseBody();
			os.write(data.getBytes());
			os.close();
		} catch (Exception e) {
			String data="";
			he.sendResponseHeaders(400, 0);
		}
		
		
	}

}
