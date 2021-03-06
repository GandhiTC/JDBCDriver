package com.github.GandhiTC.java.JDBCDriver.core;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;



public enum JDBCDriverProxy
{
	INSTANCE;


	public static final JDBCDriver db = JDBCDriver.INSTANCE;


	public boolean isConnected()
	{
		return db.isConnected();
	}


	public Connection connection()
	{
		return db.connection();
	}


	public Statement statement()
	{
		return db.statement();
	}


	public ResultSet query(String sqlQuery)
	{
		return db.query(sqlQuery);
	}
	
	
	public boolean checkIfTableExists(String tableName)
	{
		return db.checkIfTableExists(tableName);
	}


	public void parseSqlFile(String filePath, boolean autoCommitEachLine, boolean stopScriptRunnerOnError, boolean closeConnectionOnError, boolean exitOnError)
	{
		db.parseSqlFile(filePath, autoCommitEachLine, stopScriptRunnerOnError, closeConnectionOnError, exitOnError);
	}


	public void closeConnection()
	{
		db.closeConnection();
	}
}