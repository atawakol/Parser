Parser
---------------

Parse log files for blocked IPS based on threshold. The input paramters are passed to the parser through the CMD.

------------------------------------------------------------------------------------------
Run the application from jar
----------------------------

- First create database Schema by executing the scripts in the DB/Sql_Schema.txt file.
- run the jar as one the following from command line
	* java -jar parser.jar --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100
	* java -jar parser.jar -e com.ef.parser --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100

- The database connection assume username=root and password=root.
- if your mysql use different username/password pairs. Then please update the application.properties file with your correct credentials and generate new jar.
- Please note that the jar is "fat" spring boot jar. So you may generate the jar using Maven (mvn package install)
	
