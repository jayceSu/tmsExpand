package com.anji.tmsexpand;

import java.io.Serializable;

public class ParamEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4925836976316284900L;

	private String dateSymbol;
	
	private String dateValue;
	
	private String projectName;
	
	private String num;
	
	private String route;

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getDateSymbol() {
		return dateSymbol;
	}

	public void setDateSymbol(String dateSymbol) {
		this.dateSymbol = dateSymbol;
	}

	public String getDateValue() {
		return dateValue;
	}

	public void setDateValue(String dateValue) {
		this.dateValue = dateValue;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
}
