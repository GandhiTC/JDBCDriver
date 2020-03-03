package com.github.GandhiTC.java.JDBCDriver.examples;



import java.sql.SQLException;



/*
You can set up a free MySQL database at remotemysql.com
*/



public class PopulateSampleData extends DatabaseConnectionBase
{
	public static void main(String[] args) throws SQLException
	{
		/*
		 * POSITIVE TEST
		 * 
		 * Run this test first.
		 * 
		 * Simply checks if a table with the given name exists in the database.
		 * If it does not, script will populate the database with sample tables and data.
		 */
		System.out.println("POSITIVE TEST");
		if(!db.checkIfTableExists("offices"))
		{
			System.out.println("Populating database with sample data, it may take a while, depending on sample size.");
			
			db.parseSqlFile("src/test/resources/mysqltutorial.org_sample_database_slightly_edited.sql", false, true, false, false);
		}
		else
		{
			System.out.println("Table already exists in database, skipping auto-population of sample tables and data.");
		}
		
		
		/*
		 * NEGATIVE TEST
		 * 
		 * Run this test if database is already populated.
		 * Expect to see an error message similar to : (line 1): Table 'YourDatabaseName.xyz' doesn't exist
		 */
		System.out.println("\r\n\r\nNEGATIVE TEST");
		db.parseSqlFile("src/test/resources/negative_test.sql", false, true, false, false);
		
		
		//	close shop
		System.out.println("\r\nProcess completed, closing database connection.");
		db.closeConnection();
	}
}