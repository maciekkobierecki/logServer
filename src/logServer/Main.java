package logServer;

public class Main {

	public static void main(String[] args) {
		Config.init();
		new LogServer(Integer.parseInt(Config.getProperty("port")));

	}

}
