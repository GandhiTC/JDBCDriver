package com.github.GandhiTC.java.JDBCDriver.examples;



import java.sql.SQLException;
import java.sql.ResultSet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;



/*
	You can set up a free MySQL database at remotemysql.com
 */



public class UseRetrievedData extends DatabaseConnectionBase
{
	public static void main(String[] args)
	{
		//	prep ChromeDriver to have minimal output to console
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
		System.setProperty("webdriver.chrome.args", "--disable-logging");
		System.setProperty("webdriver.chrome.silentOutput", "true");
		WebDriver driver = new ChromeDriver();
		
		try
		{
			ResultSet resultSet = db.query("select * from offices where country ='USA'");
			
			if (resultSet.next() == false)
			{
		        System.err.println("Database query returned no results");
		    }
			else
			{
				do
				{
					//	extract values
					String addressLine1 = resultSet.getString("addressLine1");
					String postalCode 	= resultSet.getString("postalCode");
	
					//	pass values to WebDriver
					driver.get("https://tools.usps.com/zip-code-lookup.htm?byaddress");
					driver.findElement(By.xpath("//input[@id='tAddress']")).sendKeys(addressLine1);
					driver.findElement(By.xpath("//input[@id='tZip-byaddress']")).sendKeys(postalCode);
					driver.findElement(By.xpath("(//a[@id='zip-by-address'])")).click();
					
					Thread.sleep(2000);
			    }
				while (resultSet.next());
			}
		}
		catch(InterruptedException | SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			db.closeConnection();
			driver.quit();
			driver = null;
		}
	}
}