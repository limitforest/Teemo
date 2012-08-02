package com.ciotc.teemo.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class CheckLanguage {

	static String fileName = "config\\language.properties";
	private static String language;
	public static String getLanguage(){
		Properties prop  = new Properties();
		try {
			prop.load(new FileReader(fileName));
			language = prop.getProperty("language");
//			String country = prop.getProperty("country");
		}catch(IOException ex){
			ex.printStackTrace();
		}
		return language;
	}
}
