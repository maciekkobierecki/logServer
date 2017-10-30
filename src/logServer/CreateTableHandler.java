package logServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CreateTableHandler implements HttpHandler {

	private MySQLAccessHelper sqlHelper;
	public CreateTableHandler(MySQLAccessHelper helper){
		sqlHelper=helper;
	}
	@Override
	public void handle(HttpExchange he) throws IOException {
		URI requestedURI=he.getRequestURI();
		InputStreamReader isr=new InputStreamReader(he.getRequestBody(), "utf-8");
		BufferedReader br=new BufferedReader(isr);
		String line=br.readLine();
		JSONObject tableParametersJSON;
		try {
			tableParametersJSON = new JSONObject(line);
			Boolean created=sqlHelper.createTable(tableParametersJSON);
			if(created)
				he.sendResponseHeaders(200,0);
			else 
				he.sendResponseHeaders(400,0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		OutputStream os=he.getResponseBody();
	
		os.close();
		
		
		

	}

}
