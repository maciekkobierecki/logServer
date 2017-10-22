package logServer;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		LogServer logServer;
		Config.init();
		logServer=new LogServer(Integer.parseInt(Config.getProperty("port")));
		Scanner userInput=new Scanner(System.in);
		while(true){
			System.out.println(">");
			String input=userInput.nextLine();
			switch(input){
				case "exit":
					logServer.close();
					System.exit(0);
					break;
				
			}
		}

	}

}
