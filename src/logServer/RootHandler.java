package logServer;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RootHandler implements HttpHandler{

	@Override
	public void handle(HttpExchange he) throws IOException {
		String response="serwerek stoi";
		he.sendResponseHeaders(200, response.length());
		OutputStream os=he.getResponseBody();
		os.write(response.getBytes());
		os.close();
		
	}
	
}
