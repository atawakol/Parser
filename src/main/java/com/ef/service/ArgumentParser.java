package com.ef.service;

import java.io.File;
import java.util.Date;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;

import com.ef.util.DateUtil;
import com.ef.vo.UserInput;

@Service
public class ArgumentParser {

	public  final String DURATION_DAILY = "daily";
	public  final String DURATION_HOURLY = "hourly";
	
	private  final String DURATION = "duration";
	private  final String START_DATE = "startDate";
	private  final String THRESHOLD = "threshold";
	private  final String LOG_FILE_PATH = "accesslog";
	

	public UserInput parseUserInput(ApplicationArguments arguments) {

		validateInput(arguments);
		return createUserInput(arguments);

	}

	private UserInput createUserInput(ApplicationArguments arguments) {
		
		UserInput input = new UserInput();
		
		input.setDuration(extractDuration(arguments));
		input.setThreshold(extractThreshold(arguments));
		input.setStartDate(extractStartDate(arguments));
		input.setEndDate(calculateEndDate(input.getDuration(), input.getStartDate()));
		
		
		input.setLogLocation(extractLogFilePath(arguments));
		
		return input;
	}

	private Date calculateEndDate(String duration, Date startDate) {
		
		if(DURATION_HOURLY.equals(duration)) {
			return DateUtil.addHour(startDate);
		}
		else {
			return DateUtil.addDay(startDate);
		}
	}

	private File extractLogFilePath(ApplicationArguments arguments) {
		
		if (arguments.containsOption(LOG_FILE_PATH)) {
			return extractFileFromInput(arguments);
		}
		
		return extractFileFromDefaulLocation();
	
	}

	private File extractFileFromDefaulLocation() {
		File file = new File("access.log");
		
		if (!file.exists()) {
			throw new IllegalArgumentException("please make sure that file 'access.log' exist beside the jar or provide full path using --accesslog option");
		}
		
		return file;
	}

	private File extractFileFromInput(ApplicationArguments arguments) {
		String filePath = arguments.getOptionValues(LOG_FILE_PATH).get(0).toLowerCase();	
		File file = new File(filePath);
		
		if (!file.exists()) {
			throw new IllegalArgumentException("Can't find the file specified by --accesslog: " + filePath);
		}
		
		return file;
	}

	private long extractThreshold(ApplicationArguments arguments) {
		
		try {
			String threshold = arguments.getOptionValues(THRESHOLD).get(0).toLowerCase();
			return Long.parseLong(threshold);
		}catch (NumberFormatException ex) {
			throw new IllegalArgumentException("The 'threshold' should be number.  ");
		}
		
	}

	private  Date extractStartDate(ApplicationArguments arguments) {
		
		return DateUtil.parseInputDate(arguments.getOptionValues(START_DATE).get(0).toLowerCase());
		
	}

	private String extractDuration(ApplicationArguments arguments) {
		// just use the first one. Better to validate it.
		String duration = arguments.getOptionValues(DURATION).get(0).toLowerCase();
		
		if(!DURATION_DAILY.equals(duration) && !DURATION_HOURLY.equals(duration)) {
			throw new IllegalArgumentException("The duration should be either 'daily' or 'hourly' ");
		}
		
		return duration;
				
	}

	private void validateInput(ApplicationArguments userInput) {
		
		if (userInput.getSourceArgs().length == 0) {
			
			throw new IllegalArgumentException(createHelpMessage());
		}

		if (userInput.containsOption("--h") || userInput.containsOption("--help")) {
			throw new IllegalArgumentException(createHelpMessage());
		}
		
		validateRequiredArgument(userInput);
	}

	private  void validateRequiredArgument(ApplicationArguments userInput) {
		
		if (!userInput.containsOption(DURATION)){
			throw new IllegalArgumentException("The --duration argument is required. E.g --duration=200");
		}
		
		if(!userInput.containsOption(START_DATE)) {
			throw new IllegalArgumentException("The --startDate argument is required. E.g --startDate=2017-01-01.13:00:00");
		}
		
		if (!userInput.containsOption(THRESHOLD)) {
			throw new IllegalArgumentException("The --duration argument is required. E.g --duration=200");
		}
	}
	
	private String createHelpMessage() {
		StringBuilder builder = new StringBuilder();
		builder.append("The parser application parses web server access log file, ");
		builder.append("loads the log to MySQL and checks if a given IP makes more than a ");
		builder.append("certain number of requests for the given duration.  "); 
		builder.append(System.lineSeparator());
		builder.append(System.lineSeparator());
		
		builder.append("The tool takes 'startDate', 'duration' and 'threshold' as command line arguments.");
		builder.append("'startDate' is of 'yyyy-MM-dd.HH:mm:ss' format, ");
		builder.append("duration' can take only 'hourly', 'daily' as inputs) ");
		builder.append(" and 'threshold' can be an integer");
		builder.append(System.lineSeparator());
		builder.append(System.lineSeparator());
		
		builder.append("Usage: This is how the tool works: "); 
		builder.append(System.lineSeparator());
		builder.append(System.lineSeparator());
		
		builder.append("java -cp \"parser.jar\" com.ef.Parser --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100 ");
		builder.append(System.lineSeparator());
		builder.append("java -cp \"parser.jar\" com.ef.Parser --startDate=2017-01-01.13:00:00 --duration=daily --threshold=250 ");
		builder.append(System.lineSeparator());
		builder.append(System.lineSeparator());
		
		builder.append("Note: the tool assumes to find the server log in the same path as the jar file with name 'access.log' ");
		builder.append(System.lineSeparator());
		builder.append("You can change this default by provide the input '/path/to/file' with full path to log location");
		builder.append(System.lineSeparator());
		builder.append("E.g java -cp \"parser.jar\" com.ef.Parser --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100  ");
		builder.append(System.lineSeparator());
		builder.append(System.lineSeparator());
		
		builder.append("Note: the tool will load the data to mysql using username=root and password=root. To override this, please ");
		builder.append("update application.properties file with your username/passowrd pairs and create new jar ");
		
		builder.append(System.lineSeparator());
		builder.append(System.lineSeparator());
		
		builder.append("Note: To print this help provide --h or --help as input");
		
		return builder.toString();
	}

}
