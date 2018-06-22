package com.netpro.trinity.service.dto;

public class Querying {
	private String queryType;
	private String queryField;
	private String queryString;
	private Boolean ignoreCase;
	
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public String getQueryField() {
		return queryField;
	}
	public void setQueryField(String queryField) {
		this.queryField = queryField;
	}
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public Boolean getIgnoreCase() {
		return ignoreCase;
	}
	public void setIgnoreCase(Boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}
}
