package logServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

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
		String query=requestedURI.getRawQuery();
		String response="create";
		
		he.sendResponseHeaders(333, response.length());
		OutputStream os=he.getResponseBody();
		os.write(response.getBytes());
		os.close();
		
		
		

	}

}
