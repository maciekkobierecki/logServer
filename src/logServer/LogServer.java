package logServer;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpHandler;


public class LogServer {
	
	public LogServer(int portNumber){
		try {
			HttpServer server= HttpServer.create(new InetSocketAddress(portNumber),0);
			server.createContext("/", new GetInfoHandler());
			server.createContext("/create", new CreateTableHandler());
			server.setExecutor(null);
			server.start();
			System.out.println("server started at "+portNumber);
		} catch (IOException e) {
			System.out.println("Can't create server. IOException: "+ e);
		}
		
	}



}