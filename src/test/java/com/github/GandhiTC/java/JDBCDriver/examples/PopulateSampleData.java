package com.github.GandhiTC.java.JDBCDriver.examples;



import java.sql.SQLException;



/*
You can set up a free MySQL database at remotemysql.com
*/



public class PopulateSampleData extends DatabaseConnectionBase
{
	public static void main(String[] args) throws SQLException
	{
		System.out.println("\r\nPopulating database with sample data, it may take a while, depending on sample size.\r\n");
		
		
		/*
		 * POSITIVE TEST
		 * 
		 * Run this test first.
		 * Populates the database with sample tables and data.
		 * Only need to run this once.
		 */
		db.parseSqlFile("src/test/resources/mysqltutorial.org_sample_database_slightly_edited.sql", false, true, false, false);
		
		
		/*
		 * NEGATIVE TEST
		 * 
		 * Run this test if database is already populated.
		 * Expect to see an error message similar to : (line 1): Table 'YourDatabaseName.xyz' doesn't exist
		 */
//		db.parseSqlFile("src/test/resources/negative_test.sql", false, true, false, false);
		
		
		System.out.println("Process completed.");
		db.closeConnection();
	}
}