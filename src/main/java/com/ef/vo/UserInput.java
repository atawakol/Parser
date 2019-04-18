package com.ef.vo;

import java.io.File;
import java.util.Date;

public class UserInput {

	private long threshold;
	private String duration;
	private Date startDate;
	private Date endDate;
	
	private File logLocation;
	
	
	public long getThreshold() {
		return threshold;
	}
	public void setThreshold(long threshold) {
		this.threshold = threshold;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public File getLogLocation() {
		return logLocation;
	}
	public void setLogLocation(File logLocation) {
		this.logLocation = logLocation;
	}
	
}
