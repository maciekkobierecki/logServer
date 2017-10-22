package logServer;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GetInfoHandler implements HttpHandler{

	private MySQLAccessHelper sqlHelper;
	public GetInfoHandler(MySQLAccessHelper helper){
		sqlHelper=helper;
	}
	@Override
	public void handle(HttpExchange he) throws IOException {
		String response="serwerek stoi";
		he.sendResponseHeaders(200, response.length());
		OutputStream os=he.getResponseBody();
		try{
		sqlHelper.readDBinformation();
		}
		catch(Exception e){
			System.out.println("Unable to read database");
		}
		String method=he.getRequestMethod();
		os.write(response.getBytes());
		os.close();
		
	}
	
}
