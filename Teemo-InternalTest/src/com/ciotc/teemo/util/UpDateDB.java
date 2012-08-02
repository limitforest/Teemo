package com.ciotc.teemo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UpDateDB {

private final static String 	CHECK_TABLE_SQL="select count(*) as rscount from sqlite_master where type ='table' and tbl_name ='short_comnts'";
private final static String CREATE_TABLE_SQL = "create table shortcomnts (\"id\" INTEGER PRIMARY KEY NOT NULL,\"records\" TEXT)";
private final static String QUERY_SQL = "select * from patients";
private final static String ADD_COLUMN_POWERA_SQL = "alter table patients add column powerA INTEGER default '10'";
private final static String ADD_COLUMN_GAIN_SQL = "alter table patients add column gain INTEGER default '0'";




public static void updateDB() {
	try {
		Class.forName("org.sqlite.JDBC");
	} catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	Connection connection = null;
	Statement statement = null;
	ResultSet resultSet = null;
	ResultSetMetaData metaData = null;
	try {
		// create a database connection
		connection = DriverManager.getConnection("jdbc:sqlite:teemo.db");
		
		statement = connection.createStatement();
		
		resultSet = statement.executeQuery(CHECK_TABLE_SQL);
		
//		System.out.println(resultSet.getInt("rscount"));
		if(resultSet.getInt("rscount") == 0){
//		create table.
			statement.executeUpdate(CREATE_TABLE_SQL);
//			System.out.println("Table 创建成功。");
		}
		
		resultSet = statement.executeQuery(QUERY_SQL);
		
		metaData = resultSet.getMetaData();
		ArrayList columnList = new ArrayList();
		for (int i = 1; i <= metaData.getColumnCount(); ++i) {
			columnList.add(metaData.getColumnLabel(i));
		}
			if(columnList.contains("powerA")){
			}
			else {
			// add column
			statement.executeUpdate(ADD_COLUMN_POWERA_SQL);
			
		}
			if(columnList.contains("gain")){
			}else{
				statement.executeUpdate(ADD_COLUMN_GAIN_SQL);
			}
	} catch (SQLException e) {
		// if the error message is "out of memory",
		// it probably means no database file is found
		System.err.println(e.getMessage());
	} catch(IllegalStateException e){
		System.err.println(e.getMessage());
	}finally {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			// connection close failed.
			System.err.println(e);
		}
	}
}
}
