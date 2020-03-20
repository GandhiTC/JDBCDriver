package com.github.GandhiTC.java.JDBCDriver.core;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;



enum JDBCDriver
{
	INSTANCE("src/main/resources/database.properties");
	
	
	private String 		host;
	private String 		port;
	private String 		database;
	private String 		username;
	private String 		password;
	private Connection	connection	= null;
	private Statement	statement	= null;
	private ResultSet	resultSet	= null;
	private String		prevQuery	= "";
	
	
	JDBCDriver(String propertiesFilePath)
	{
		Properties 		props 		= new Properties();
		FileInputStream fis;

		try
		{
			fis = new FileInputStream(propertiesFilePath);
			props.load(fis);
		}
		catch(IOException e)
		{
			e.printStackTrace();
//			System.err.println(e.getMessage());
		}

		this.host		= props.getProperty("host");
		this.port		= props.getProperty("port");
		this.database	= props.getProperty("database");
		this.username	= props.getProperty("username");
		this.password	= props.getProperty("password");

		this.connection	= connection();
		this.statement	= statement();
		this.resultSet	= query("SELECT 1");	//	https://stackoverflow.com/a/3670000
	}
	
	
	boolean isConnected()
	{
		try
		{
			return !connection.isValid(0) ? false : connection.isClosed() ? false : true;
		}
		catch(NullPointerException | SQLException npe)
		{
//			System.err.println(npe.getMessage());
//			npe.printStackTrace();
			return false;
		}
	}
	
	
	Connection connection()
	{
		if(isConnected() && (connection != null))
		{
			return connection;
		}
		else
		{
			String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
			
			try
			{
				connection = DriverManager.getConnection(url, username, password);
				return connection;
			}
			catch (SQLException e)
			{
				e.printStackTrace();
//				System.err.println(e.getMessage());
				return null;
			}
		}
	}
	
	
	Statement statement()
	{
		if(isConnected() && (statement != null))
		{
			return statement;
		}
		else
		{
			try
			{
				statement = connection.createStatement();
				return statement;
			}
			catch (SQLException e)
			{
				e.printStackTrace();
//				System.err.println(e.getMessage());
				return null;
			}
		}
	}
	
	
	ResultSet query(String sqlQuery)
	{
		if(isConnected() && (resultSet != null) && sqlQuery.equalsIgnoreCase(prevQuery))
		{
			return resultSet;
		}
		else
		{
			boolean succeeded = true;
			
			try
			{
				resultSet = statement.executeQuery(sqlQuery);
				return resultSet;
			}
			catch (SQLException e)
			{
				succeeded = false;
				e.printStackTrace();
//				System.err.println(e.getMessage());
				return null;
			}
			finally
			{
				if(succeeded == true)
				{
					prevQuery = sqlQuery;
				}
			}
		}
	}
	
	
	boolean checkIfTableExists(String tableName)
	{
		try
		{
			String 		sqlQuery 	= 	"SELECT * " +
										"FROM information_schema.tables " +
										"WHERE table_schema = '" + database + "' " +
										"AND table_name = '" + tableName + "' " +
										"LIMIT 1;";
			
			ResultSet 	rs 			= 	query(sqlQuery);
			
			if (rs.next() == false)
			{
				return false;
		    }
			else
			{
				return true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			return false;
		}
	}
	
	
	void parseSqlFile(String filePath, boolean autoCommitEachLine, boolean stopScriptRunnerOnError, boolean closeConnectionOnError, boolean exitOnError)
	{
		try
		{
			validateFile(filePath, "sql");
			
			//	Initialize object for ScripRunner
			ScriptRunner sr = new ScriptRunner(connection, autoCommitEachLine, stopScriptRunnerOnError);
			
			//	Give the input file to Reader
			Reader reader = new BufferedReader(new FileReader(filePath));
			
			//	Execute script
			sr.runScript(reader);
		}
		catch(Exception e)
		{
//			System.err.println("Failed to Execute \"" + filePath + "\"\r\nThe error is:\r\n" + e.getMessage());
//			System.out.println("Connected to database:  " + isConnected());
//			e.printStackTrace();
//			System.err.println(e.getMessage());
		}
		finally
		{
			if(closeConnectionOnError)
			{
				closeConnection();
				
				if(!isConnected())
				{
					System.err.println("Disconnected from database.");
				}
			}
			
			if(exitOnError)
			{
				System.err.println("Exiting program.");
				System.exit(1);
			}
		}
	}
	
	
	void closeConnection()
	{
		if(resultSet != null)
		{
			try
			{
				resultSet.close();
			}
			catch(SQLException e)
			{
//				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		if(statement != null)
		{
			try
			{
				statement.close();
			}
			catch(SQLException e)
			{
//				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		if(connection != null)
		{
			try
			{
				connection.close();
			}
			catch(SQLException e)
			{
//				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	
	private void validateFile(String filePath, String extensionRequired)
	{
		File file = new File(filePath);
		
		if(!file.isFile())
		{
			if(!file.exists())
			{
				String	fileName	= file.getName();
				int		dotIndex	= fileName.lastIndexOf('.');
				
				if(dotIndex == -1)
				{
					System.err.println("Please make sure you are pointing to a file, not a path.");
					closeConnection();
					System.exit(1);
				}
				
				String	extension	= fileName.substring(dotIndex + 1);
				
				if(!extensionRequired.equalsIgnoreCase(extension))
				{
					System.err.println("Please make sure the file extension is: ." + extensionRequired);
					closeConnection();
					System.exit(1);
				}
				
				System.err.println("The file does not exist.");
				closeConnection();
				System.exit(1);
			}
			else
			{
				System.err.println("Error in given file path.");
				closeConnection();
				System.exit(1);
			}
		}
	}
}