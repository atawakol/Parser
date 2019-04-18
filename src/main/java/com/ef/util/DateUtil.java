package com.ef.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Date parseInputDate(String date) {
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
			return format.parse(date);
			
		} catch (ParseException e) {
			throw new IllegalArgumentException("The 'startDate' should be in the 'yyyy-MM-dd.HH:mm:ss' format.  ");
		}
	}
	
	public static Date parseLogDate(String date) {

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			return format.parse(date);
			
		} catch (ParseException e) {
			throw new IllegalArgumentException("Error pasring Date/Time while parsing the log file ", e);
		}
	}
	
	public static Date addHour(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, 1);
		return cal.getTime();
	}
	
	public static Date addDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}
}
