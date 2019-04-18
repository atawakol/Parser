package com.ef.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ef.domain.BlockedIP;
import com.ef.domain.LogEntry;
import com.ef.repository.BlockedIPRepository;
import com.ef.repository.LogEntryRepository;
import com.ef.util.DateUtil;
import com.ef.vo.UserInput;

@Service
public class LogParser {
	
	@Autowired
	private LogEntryRepository logEntryRepo;

	@Autowired
	private BlockedIPRepository blockedIPRepository;
	
	
	private static final char DELIMITER = '|';
	
	private List<LogEntry> logRecords;
	private List<BlockedIP> blockedIPs;

	private UserInput input;

	
	public List<String> parse(UserInput input) throws Exception {
		this.input = input;
		logRecords = new ArrayList<>();
		blockedIPs = new ArrayList<>();
		
		readLogFile();
		saveLogEntries();
		findBlockedIPs();
		saveBlockedIPs();
		
		return blockedIPs.stream().map(BlockedIP::getIp).collect(Collectors.toList());
	}

	

	private void findBlockedIPs() {
		
		Map<String, List<LogEntry>> ipsMap = logRecords.stream()
				.filter(record-> isInRange(record))
				.collect(Collectors.groupingBy(LogEntry::getIp));
				
		blockedIPs = ipsMap.entrySet().stream()
		.filter(entry -> entry.getValue().size() >= input.getThreshold())
		.map(entry -> createBlockedIp(entry.getKey(), input))
		.collect(Collectors.toList());
	}

	private BlockedIP createBlockedIp(String ip, UserInput input) {
		
		BlockedIP blockedIP = new BlockedIP();
		blockedIP.setIp(ip);
		blockedIP.setComment(createComment());
		return blockedIP;
	}

	private String createComment() {
		StringBuilder builder = new StringBuilder();
		builder.append("Blocked due to exceed the threshold limit of ( ");
		builder.append(input.getThreshold());
		builder.append(" ) during the period between ");
		builder.append(input.getStartDate());
		builder.append(" and ");
		builder.append(input.getEndDate());
		return builder.toString();
	}


	private boolean isInRange(LogEntry record) {
		Date recordDate = record.getDate();
		
		if (recordDate.compareTo(input.getStartDate()) >= 0 && recordDate.before(input.getEndDate())) {
			return true;
		}
		
		return false;
		
	}



	private void saveBlockedIPs() {
		blockedIPRepository.save(blockedIPs);
		
	}
	
	private void saveLogEntries() {
		logEntryRepo.save(logRecords);
	}

	private void readLogFile() throws FileNotFoundException, IOException {
		
		try (FileReader reader = new FileReader(input.getLogLocation())) {
			CSVFormat formatter = CSVFormat.DEFAULT.withDelimiter(DELIMITER);
			CSVParser parser = formatter.parse(reader);
			for (CSVRecord record : parser) {
				LogEntry entry = createLogEntry(record);
				logRecords.add(entry);
			}
		}
		
	}

	private LogEntry createLogEntry(CSVRecord record) {
		LogEntry entry = new LogEntry();
		entry.setDate(DateUtil.parseLogDate(record.get(0)));
		entry.setIp(record.get(1));
		entry.setMethod(record.get(2));
		entry.setHttpStatus(record.get(3));
		entry.setClient(record.get(4));
		
		return entry;
	}

}
