package logServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {
	private static File configFile;
	private static Properties prop;
	public Config(){
		configFile=new File("config.properties");
		try {
			FileReader reader=new FileReader(configFile);
			prop=new Properties();
			prop.load(reader);
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Config file not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("err");
		}
		
	}
	public static String getProperty(String key, String defaultValue){
		return prop.getProperty(key, defaultValue);
	}

}
