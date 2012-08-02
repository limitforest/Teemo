package com.ciotc.teemo.util;

import java.io.File;
import org.apache.commons.configuration.PropertiesConfiguration;

public class GeneralUtils {

	private static final PropertiesConfiguration prop = new PropertiesConfiguration( );

	public static void main( String[] args ) throws Exception {
		GeneralUtils.getProp( ).setProperty( Constant.CONFIG_KEY_PACS_AET, "DCM4CHEE" );
		GeneralUtils.getProp( ).setProperty( Constant.CONFIG_KEY_PACS_HOST, "192.168.110.56" );
		GeneralUtils.getProp( ).setProperty( Constant.CONFIG_KEY_PACS_PORT, "11112" );

		System.out.println( GeneralUtils.getProp( ).getInt( Constant.CONFIG_KEY_PACS_PORT ) );
	}

	static {
		try {
			prop.setFile( new File( "config/user.properties" ) );
			prop.load( );
			prop.setAutoSave( true );
		} catch ( Exception e ) {
			e.printStackTrace( );
		}
	}

	public static PropertiesConfiguration getProp( ) {
		return prop;
	}

}
