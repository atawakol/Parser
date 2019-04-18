package com.ef;

import com.ef.service.ArgumentParser;
import com.ef.service.LogParser;
import com.ef.vo.UserInput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.System.exit;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Parser implements ApplicationRunner {

    @Autowired
    private LogParser logParser;
    
    @Autowired
    private ArgumentParser argumentParser;

    public static void main(String[] args) throws Exception {

        //disabled banner, don't want to see the spring logo
        SpringApplication app = new SpringApplication(Parser.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);

        //SpringApplication.run(SpringBootConsoleApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments arguments) throws Exception {
    	try {
    		UserInput input = argumentParser.parseUserInput(arguments);
    		List<String> ips = logParser.parse(input);
    		printResults(ips);
    		
    		
    	}catch (IllegalArgumentException ex) {
    		System.out.println(ex.getMessage());
    	}
    	
    	
        exit(0);
    }

	private void printResults(List<String> ips) {
		System.out.println("The following IPS were blocked due to exceeding the threshold. Results were updated to DB ");
		ips.forEach(System.out::println);
		
	}
}