package logServer;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class FetchInfoHandler implements HttpHandler{

	private MySQLAccessHelper sqlHelper;
	
	public FetchInfoHandler(MySQLAccessHelper accesshelper){
		sqlHelper=accesshelper;
	}
	@Override
	public void handle(HttpExchange he) throws IOException {
		JSONObject metadataJSON= new JSONObject();
		try {
			metadataJSON=sqlHelper.readDBinformation();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String response=metadataJSON.toString();

		he.sendResponseHeaders(200, response.length());
		OutputStream os=he.getResponseBody();
		os.write(response.getBytes());
		os.close();
		
		
	}

}
