package com.anji.tmsexpand.entity;

import java.io.Serializable;

public class DateEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String projectName;

	private String timeBegin;
	
	private String timeEnd;
	
	private String dateSymbol;
	
	public String getDateSymbol() {
		return dateSymbol;
	}

	public void setDateSymbol(String dateSymbol) {
		this.dateSymbol = dateSymbol;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getTimeBegin() {
		return timeBegin;
	}

	public void setTimeBegin(String timeBegin) {
		this.timeBegin = timeBegin;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	
}
