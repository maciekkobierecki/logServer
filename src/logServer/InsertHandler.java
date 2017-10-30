package logServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class InsertHandler implements HttpHandler{

	private MySQLAccessHelper sqlHelper;
	
	public InsertHandler(MySQLAccessHelper sqlHelper){
		this.sqlHelper=sqlHelper;
		
	}

	@Override
	public void handle(HttpExchange he) throws IOException {
		InputStreamReader isr=new InputStreamReader(he.getRequestBody(),"utf-8");
		BufferedReader br=new BufferedReader(isr);
		String line=br.readLine();
		JSONObject requestJSON;
		try {
			requestJSON=new JSONObject(line);
			if(sqlHelper.insert(requestJSON)){
				he.sendResponseHeaders(200, 0);
			}
			else {
				he.sendResponseHeaders(400, 0);
			}
			
		} catch (Exception e) {
			he.sendResponseHeaders(400, 0);
		}
		finally {
			he.close();
		}
				
	}
}
