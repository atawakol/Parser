Folder Content
---------------

-- The folder contains four files
	1- parser folder. This contains the source code for the application
	2- Sql_Schema.txt. This contains the mysql Schema.
	3- SQL_Queries_Answer.txt. This file contains the answer to SQL Questions
	4- parser.jar. Which is executable version of the application.
	
	
------------------------------------------------------------------------------------------
Run the application from jar
----------------------------

1- First create database Schema by executing the scripts in the Sql_Schema.txt file.
2- run the jar as one the following from command line
	a- java -jar parser.jar --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100
	b- java -jar parser.jar -e com.ef.parser --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100

3- The database connection assume username=root and password=root.
4- if your mysql use different username/password pairs. Then please update the application.properties file with your correct credentials and generate new jar.
5- Please note that the jar is "fat" spring boot jar. So you may generate the jar using Maven (mvn package install)
	