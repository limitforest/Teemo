package com.ciotc.teemo.resource;

import java.awt.Dimension;
import java.io.FileReader;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.pushingpixels.flamingo.api.common.icon.ImageWrapperResizableIcon;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;

import com.ciotc.teemo.frame.MainFrame;

public class MyResources {

	private static ResourceBundle resources;
	static {
		try {
			//PropertiesConfiguration pc = GeneralUtils.getProp();

			//String language = (String) pc.getProperty("language");
			//String country = (String) pc.getProperty("country");
			String fileName = "config\\language.properties";
			Properties prop = new Properties();
			Locale locale = null;
			try {
				prop.load(new FileReader(fileName));
				String language = prop.getProperty("language");
				String country = prop.getProperty("country");
				//System.out.println(language+"_"+country);
				locale = new Locale(language, country);
				// System.out.println(Locale.getDefault());

			} catch (Exception e) {
				locale = null;
			}
			if (locale != null)
				//如果找不到语言的话 它会以本机默认的语言显示
				resources = ResourceBundle.getBundle("teemo", locale);
			else
				resources = ResourceBundle.getBundle("teemo");

		} catch (MissingResourceException mre) {
			//System.err.println("res/teemo.properties not found");
			//JOptionPane.showMessageDialog(null, "未找到语言包，请在安装目录下的程序中选择", "Teemo",JOptionPane.WARNING_MESSAGE); 
			System.exit(1);
		}
	}

	public static URL getResourceURL(String key) {
		String name = getResourceString(key);
		if (name != null) {
			URL url = ClassLoader.getSystemClassLoader().getResource(name);
			return url;
		}
		return null;
	}

	public static URL getResourceURL(String key, Locale locale) {
		String name = getResourceString(key, locale);
		if (name != null) {
			URL url = ClassLoader.getSystemClassLoader().getResource(name);
			return url;
		}
		return null;
	}

	public static String getResourceString(String nm) {
		String str;
		try {
			str = resources.getString(nm);
		} catch (MissingResourceException mre) {
			str = null;
		}
		return str;
	}

	public static String getResourceString(String nm, Locale locale) {
		String str;
		try {
			str = resources.getString(nm);
		} catch (MissingResourceException mre) {
			str = null;
		}
		return str;
	}

	public static ResizableIcon getResizableIconFromResource(String resource) {
		return ImageWrapperResizableIcon.getIcon(MyResources.class.getClassLoader().getResource(resource), new Dimension(24, 24));
	}

	public static void main(String[] args) {
		System.out.println(getResourceURL("firstContactImage"));
		System.out.println(getResourceString("movie"));

	}
}
