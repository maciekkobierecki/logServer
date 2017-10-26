package logServer;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpHandler;


public class LogServer {
	private MySQLAccessHelper sqlHelper;
	HttpServer server;
	
	public LogServer(int portNumber){
		try {
			sqlHelper=new MySQLAccessHelper();
			sqlHelper.loadDrivers();
			server= HttpServer.create(new InetSocketAddress(portNumber),0);
			server.createContext("/", new GetInfoHandler(sqlHelper));
			server.createContext("/create", new CreateTableHandler(sqlHelper));
			server.createContext("/fetchInfo",new FetchInfoHandler(sqlHelper));
			server.createContext("/data", new FetchTableHandler(sqlHelper));
			server.setExecutor(null);
			server.start();
			System.out.println("server started at "+portNumber);
		} catch (IOException e) {
			System.out.println("Can't create server. IOException: "+ e);
		}
		
	}
	public void close(){
		server.stop(0);
	}



}