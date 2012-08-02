package com.ciotc.teemo;

import java.io.IOException;
import java.io.Reader;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
// To get an instance of SqlMapClient which is the beginning of everything.
public class SqlMapClientUtil {

	private static SqlMapClient sqlMapper;

	static {
		Reader reader;
		try {
			reader = Resources.getResourceAsReader( "SqlMapConfig.xml" );
			sqlMapper = SqlMapClientBuilder.buildSqlMapClient( reader );
		} catch ( IOException e ) {
			e.printStackTrace( );
		}
	}

	public static SqlMapClient getSqlMapClient( ) {
		return sqlMapper;
	}

}
