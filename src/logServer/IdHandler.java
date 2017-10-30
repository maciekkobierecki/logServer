package logServer;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class IdHandler implements HttpHandler{

	@Override
	public void handle(HttpExchange he) throws IOException {
		String idAsString=Config.getProperty("id");
		int lastVotedId=Integer.parseInt(idAsString);
		Config.incrementLastClientId();
		JSONObject idJSON=new JSONObject();
		try {
			idJSON.put("id", lastVotedId);
			String id=idJSON.toString();
			he.sendResponseHeaders(200, id.length());
			OutputStream os=he.getResponseBody();
			os.write(id.getBytes());
			os.close();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
