JDBCDriver is a wrapper/driver used for simplifying interacting with a MySQL database via JDBC.


It has 4 primary purposes:
	- Prevent hammering of the MySQL database/server by using a controllable persistent instance.
	- Add protection layers between database code and any code that calls on it.
	- Create shortcuts for commonly used code.
	- Cleaner, easier to read test files.


Basic benefits:
	- Dev    - If you absolutely must share existing DB credentials with a tester and you want to limit their permissions, 
				you can modify JDBCDriverProxy.java to limit client-side permissions while maintaining your server-side permissions.
	- Tester - DatabaseConnectionBase.java acts as a base file that works specifically for a database connection.
				This will replace the need to repeat the same DB-related code in each test file.



---------------------------------------------------------------------------------------------------------------------------------------
JDBCDriver.java
---------------------------------------------------------------------------------------------------------------------------------------
This file holds the actual code for interacting with a database/server.

It is set-up as an enum so that it can be used as a true singleton.

It's access modifier is set to default (restricted to its package only), it is then accessed via an interface/proxy.



---------------------------------------------------------------------------------------------------------------------------------------
JDBCDriverProxy.java
---------------------------------------------------------------------------------------------------------------------------------------
This file acts as a public interface/proxy for the JDBCDriver enum.

it is comprised of getter-type methods for the only JDBCDriver properties that you want to allow access to.

It also is an enum to be used as a true singleton.



---------------------------------------------------------------------------------------------------------------------------------------
DatabaseConnectionBase.java
---------------------------------------------------------------------------------------------------------------------------------------
This class holds a static final instance of the JDBCDriverProxy interface/proxy, this instance is to be used by test/other files.

By default, this file resides in the same package as the test files (only to avoid having to import), 
but it can be called on from anywhere.

Test classes can extend this class and then call the getter-type methods in JDBCDriverProxy.java.



---------------------------------------------------------------------------------------------------------------------------------------
Run Sample Tests
---------------------------------------------------------------------------------------------------------------------------------------
To see examples in action
	- Place a copy of chromedriver.exe file in the test resources directory:  ./src/test/resources/
	- Create an account and a new database at remotemysql.com
	- Update database credentials in properties file:  ./src/main/resources/database.properties
	- In PopulateSampleData.java, make sure the negative test is commented out and the positive test is uncommented, 
		run this file as a java application
	- Once database is populated with sample data, comment out the positive test and uncomment the negative test, 
		and run the file again.
	- Run UserRetrievedData.java as a java application to see info being pulled from the database then passed to Selenium 
		to fill out a form.



---------------------------------------------------------------------------------------------------------------------------------------
Credits
---------------------------------------------------------------------------------------------------------------------------------------
The ScriptRunner class file is an edited version of the original file from:  https://github.com/BenoitDuffez/ScriptRunner



---------------------------------------------------------------------------------------------------------------------------------------
Additional Info
---------------------------------------------------------------------------------------------------------------------------------------
Download the version of ChromeDriver that corresponds with the version of Chrome Browser installed on your computer.
	https://chromedriver.chromium.org/downloads

Place the chromedriver.exe file in the test resources directory
	./src/test/resources/

Database connection info is retrieved from a local properties file, which is done only for the sake of providing an example.
It is highly recommended to use a more secure method in real-world apps.



